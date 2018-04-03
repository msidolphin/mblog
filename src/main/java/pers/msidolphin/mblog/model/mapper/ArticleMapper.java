package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/27.
 */
public interface ArticleMapper {

	List<ArticleDto> findArticles(ArticleQuery query);

	int updateById(Article article);
}
