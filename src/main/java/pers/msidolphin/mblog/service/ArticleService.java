package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.Assert;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.ArticleMapper;
import pers.msidolphin.mblog.model.repository.ArticleRepository;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.*;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

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
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ArticleDto> lists = articleMapper.findAll(query);
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
        }
        //TODO
        article.setCreator(1L);
        article.setUpdator(1L);
        article.setUpdateTime(new Date());
        article.setCreateTime(new Date());
        article.setViews(0);
        article.setVote(0);
        article.setCid(null);
        article.setSummary("");
        System.out.println(article);
        //====
        //保存文章
        articleRepository.save(article);
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
     * @return
     */
    public int logicDelete(String id) {
        return articleRepository.updateStatusById(1, id);
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
