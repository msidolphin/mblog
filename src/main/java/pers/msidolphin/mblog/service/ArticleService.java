package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.repository.ArticleRepository;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.po.Article;

import java.util.*;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagService tagService;

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
            tagService.saveTags(article.getTags().split(","));
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
}
