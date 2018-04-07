package pers.msidolphin.mblog.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/27.
 */
public interface ArticleMapper {

	List<ArticleDto> findArticles(ArticleQuery query);

	int updateById(Article article);

	List<Map<String, Integer>> monthlyReport(@Param("year") String year, @Param("maxMonth") Integer max, @Param("list") List<String> list);

	List<Map<String, Integer>> yearReport(@Param("start") Integer start, @Param("end") Integer end, @Param("list") List<String> list);

	List<Map<String, Integer>> pieReport();

	int selectArticleCount();
}
