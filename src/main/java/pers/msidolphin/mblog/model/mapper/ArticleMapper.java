package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/27.
 */
public interface ArticleMapper {

	List<ArticleDto> findAll(ArticleQuery query);
}
