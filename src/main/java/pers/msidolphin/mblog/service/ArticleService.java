package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.enums.ArticleType;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.ArticleMapper;
import pers.msidolphin.mblog.model.repository.ArticleRepository;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.po.Article;
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
            query.setTagList(Util.asList(query.getTags().split(",")));
        }else {
            query.setTags(null);
        }
        //设置分页参数
        PageHelper.startPage(query.getPageNum(), query.getPageSize(), "a.create_time desc");
        List<ArticleDto> lists = articleMapper.findArticles(query);
        return new PageInfo<>(lists);
    }

    @Transactional
    public Article saveArticle(Article article, String originTagStr) {
        //入参判断

        //判断id是否为空，若id
        Long id = article.getId();

        List<String> dels;
        List<String> adds;

        if(Util.isEmpty(id)) {
            //新增操作
            article.setId(AutoIdHelper.getId());
            //判断是否新增标签
            if(Util.isNotEmpty(article.getTags())) {
                tagService.saveTags(article.getTags().split(","));
            }
            //TODO
            article.setCreator(1L);
            article.setUpdator(1L);
            article.setUpdateTime(new Date());
            article.setCreateTime(new Date());
            article.setCid(null);
            article.setIsDelete(0);
            article.setViews(0);
            article.setVote(0);
            article.setSummary("");
            System.out.println(article);
            //====
            //保存文章
            articleRepository.save(article);
        }else {
            //判断当前标签和原标签的差异
            List<String> originTags = Util.asList(originTagStr.split(","));
            List<String> currentTags = Util.asList(article.getTags().split(","));
            //删除的
            dels = Util.diff(originTags, currentTags);
            //新增的
            adds = Util.diff(currentTags, originTags);

            if(dels.size() > 0) {
                //删除标签
                tagService.delTag(dels);
            }
            if(adds.size() > 0) {
                //新增的
                tagService.saveTags(adds);
            }
            articleMapper.updateById(article);
        }
        return article;
    }

    /**
     * 根据文章id获取文章
     * @param id
     * @return
     */
    public Article getArticle(Long id) {
        if(Util.isEmpty(id)) {
            throw new InvalidParameterException("文章id不能为为空");
        }
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(optionalArticle.isPresent()) {
            return optionalArticle.get();
        }
        return null;
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
        articleRepository.deleteById(Long.parseLong(id));
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
