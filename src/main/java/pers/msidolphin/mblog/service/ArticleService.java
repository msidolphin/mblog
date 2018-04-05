package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ArticleType;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.ArticleMapper;
import pers.msidolphin.mblog.model.repository.ArticleRepository;
import pers.msidolphin.mblog.object.dto.AdminArticleDto;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private RedisHelper redis;

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    private static final String DEFAULT_VIEWS_PREFIX = "articleViewsCache:";
    private static final Long   DEFAULT_VIEWS_TIMEOUT = 900000L;

    public PageInfo<ArticleDto> getArticles(ArticleQuery query) throws ParseException {
        Assert.notNull(query);
        //处理查询对象中的结束时间
        if(Util.isNotEmpty(query.getEndTime())) {
            query.setEndTime(query.getEndTime() + " 23:59:59");
        }
        if(Util.isNotEmpty(query.getTags())) {
            //将标签字符串转换为List
//            query.setTagList(Util.asList(query.getTags().split(",")));
            String[] tags = query.getTags().split(",");

            //获取效应标签的id
            List<String> ids = Lists.newArrayList();
            for(String tag : tags) {
                String id = tagService.getTagIdByName(tag);
                //该标签不存在，已经不需要继续查询了
                if (id == null) return new PageInfo<>(Lists.newArrayList());
                ids.add(id);
            }
            query.setTagIdList(ids);
        }else {
            query.setTags(null);
        }
        //设置分页参数
        PageHelper.startPage(query.getPageNum(), query.getPageSize(), "a.create_time desc");
        List<ArticleDto> lists = articleMapper.findArticles(query);
        //获取标签
        for(ArticleDto articleDto : lists) {
            List<Map<String, Object>> tags = tagService.getTags(articleDto.getId());
            StringBuffer tagsName = new StringBuffer();
            for(Map<String, Object> tag : tags) {
                tagsName.append(tag.get("name").toString()).append(",");
            }
            articleDto.setTags(tagsName.substring(0, tagsName.lastIndexOf(",")));
        }
        return new PageInfo<>(lists);
    }

    @Transactional
    public Article saveArticle(AdminArticleDto articleDto, String originTagStr, String originTagId) {
        //判断用户是否登录
        User user = RequestHolder.getCurrentAdmin();
        if(user == null) throw new AuthorizedException();
        Article article = new Article();
        BeanUtils.copyProperties(articleDto,article);
        //判断id是否为空，若id
        String id = article.getId();

        List<String> dels;
        List<String> adds;

        //维护文章表与标签表的关系
        List<String> delIds;
        List<String> addIds;

        if(Util.isEmpty(id)) {
            //新增操作
            article.setId(AutoIdHelper.getId().toString());
            //判断是否新增标签
            if(Util.isNotEmpty(articleDto.getTags())) {
                //tags字段现在不存入数据库
                List<String> ids = tagService.saveTags4newArticle(Util.asList(articleDto.getTags().split(",")), user.getId());
                for(String nid : ids) {
                    //建立关联
                    tagService.createRelationship(article.getId().toString(), nid);
                }
            }
            article.setCreator(user.getId());
            article.setUpdator(user.getId());
            article.setUpdateTime(new Date());
            article.setCreateTime(new Date());
            article.setCid(null);
            article.setIsDelete(0);
            article.setViews(0);
            article.setVote(0);
            article.setSummary("");
            //====
            //保存文章
            articleRepository.save(article);

        }else {
            //判断当前标签和原标签的差异
            List<String> originTags = Util.asList(originTagStr.split(","));
            List<String> currentTags = Util.asList(articleDto.getTags().split(","));
            //删除的
            dels = Util.diff(originTags, currentTags);
            //新增的
            adds = Util.diff(currentTags, originTags);

            //add
            if(originTagId == null) originTagId = "";
            if(articleDto.getTagsId() == null) articleDto.setTags("");
            List<String> originTagIds = Util.asList(originTagId.split(","));
            List<String> currentTagIds = Util.asList(articleDto.getTagsId().split(","));
            //===========

            delIds = Util.diff(originTagIds, currentTagIds);
            addIds = Util.diff(currentTagIds, originTagIds);

            if(dels.size() > 0) {
                //删除标签
                tagService.delTag(dels, user.getId());

                //打破文章与标签的原有关系
                for(int i = 0 ; i < delIds.size() ; ++i)
                    tagService.brokenRelationship(article.getId().toString(), delIds.get(i));

            }
            if(adds.size() > 0) {
                //新增的
                List<String> newIds = tagService.saveTags(adds, user.getId());
                for(String newId : newIds) {
                    addIds.add(newId);
                }
                //建立关联
                for(int i = 0 ; i < addIds.size() ; ++i)
                    tagService.createRelationship(article.getId().toString(), addIds.get(i));

            }
            article.setUpdator(user.getId());
            articleMapper.updateById(article);
        }
        return article;
    }

    /**
     * 根据文章id获取文章
     * @param id
     * @return
     */
    public AdminArticleDto getArticle(Long id) {
        if(Util.isEmpty(id)) {
            throw new InvalidParameterException("文章id不能为为空");
        }
        Optional<Article> optionalArticle = articleRepository.findById(id.toString());
        Article article = optionalArticle.get();
        //获取文章标签
        AdminArticleDto adminArticleDto = new AdminArticleDto();
        BeanUtils.copyProperties(article, adminArticleDto);
        //查询文章标签
        List<Map<String, Object>> tags = tagService.getTags(article.getId());
        System.out.println("tags:"+ tags);
        StringBuffer tagsName = new StringBuffer();
        StringBuffer tagsId = new StringBuffer();
        for(Map<String, Object> tag : tags) {
            tagsName.append(tag.get("name").toString()).append(",");
            tagsId.append(tag.get("id").toString()).append(",");
        }
        adminArticleDto.setTagsId(tagsId.substring(0, tagsId.lastIndexOf(",")));
        adminArticleDto.setTags(tagsName.substring(0, tagsName.lastIndexOf(",")));
        return adminArticleDto;
    }

    /**
     * 逻辑删除文章
     * @param id 文章id
     * @return {int} 影响行数
     */
    public int logicDelete(String id) {
        return articleRepository.updateStatusById(1, id);
    }


    /**
     * 物理删除文章
     * @param id 文章id
     */
    public void delete(String id) {
        if(Util.isEmpty(id)) throw new InvalidParameterException("文章id不能为空");
        articleRepository.deleteById(id);
    }


    /**
     * 获取文章详情 包含评论信息
     * @param id 文章id
     * @return ArticleDto
     */
    public ArticleDto getArticleDetail(String id) {
        Assert.notNull(id);
        List<ArticleDto> articleDtos = articleMapper.findArticles(new ArticleQuery().setId(id));
        ArticleDto articleDto = null;
        System.out.println(articleDtos.size());
        if(Util.isNotEmpty(articleDtos) && articleDtos.size() == 1) {

            articleDto = articleDtos.get(0);
            //文章类型
            articleDto.setTypeName(ArticleType.get(articleDto.getTypeCode()).getValue());
            //获取评论信息
            PageInfo<CommentDto> comments = commentService.getComments(id, 10, 1);
            //统计总回复数
            int sum = 0;
            for(CommentDto comment : comments.getList())  {
                sum += comment.getReplyCount();
            }
            articleDto.setReplies(sum + articleDto.getCommentCount());
            articleDto.setCommentList(comments);
        }else {
            //文章不存在
            throw new ServiceException("文章不存在或返回多个文章记录");
        }
        return articleDto;
    }

    public boolean articleViewsHandle(String id, HttpServletRequest request) {

        String viewsPrefix = propertiesHelper.getValue("blog.article.ip.cache.prefix");

        Long viewsTimeout = propertiesHelper.getLong("blog.article.ip.cache.timeout");

        if(viewsPrefix == null) viewsPrefix = DEFAULT_VIEWS_PREFIX;
        if(viewsTimeout == null) viewsTimeout = DEFAULT_VIEWS_TIMEOUT;

        //获取ip地址
        String ipAddr = IpAddressHelper.getIpAddress(request);
        log.info("ip地址为{}的游客访问了文章{}", ipAddr, id);
        //从redis中获取ip地址对应的访问列表
        if(redis.getValue(viewsPrefix + ipAddr + id) == null) {
            //文章阅览数自增1
            articleRepository.incrementsViewsById(id);
            //文章阅览数新增一条
            redis.setValue(viewsPrefix + ipAddr + id,
                    JsonHelper.object2String(true), viewsTimeout);
        }else {
            return false;
        }
        return true;
    }

    @Deprecated
    private Specification<Article> where(ArticleQuery query) {
        return new Specification<Article>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                if(Util.isNotEmpty(query)) {

                    if(Util.isNotEmpty(query.getTitle())) {
                        //查询条件标题不为空
                        predicates.add(cb.like(root.get("title").as(String.class), "%" + query.getTitle() + "%"));
                    }

                    if(Util.isNotEmpty(query.getStartTime())) {
                        //发布时间 开始
                        //predicates.add(cb.greaterThan(root.get("createTime").as(Date.class), query.getStartTime()));
                    }

                    if(Util.isNotEmpty(query.getEndTime())) {
                        //发布时间 结束
//                        Date endTime = query.getEndTime();
//                        predicates.add(cb.lessThan(root.get("createTime").as(Date.class), query.getEndTime()));
                    }

                    if(Util.isNotEmpty(query.getTags())) {
                        //标签
                        String[] tags = query.getTags().split(",");
                        for(String tag : tags) {
                            predicates.add(cb.like(root.get("tags").as(String.class), "%" + tag + "%"));
                        }
                    }
                }
                Predicate[] pres = new Predicate[predicates.size()];
                return criteriaQuery.where(predicates.toArray(pres)).getGroupRestriction();
            }
        };
    }
}
