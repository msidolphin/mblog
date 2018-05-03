package pers.msidolphin.mblog.service.inter;

import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.query.ArticleQuery;
import pers.msidolphin.mblog.object.query.ArticleSearch;
import pers.msidolphin.mblog.object.query.BaseQuery;

/**
 * 检索接口
 * Created by msidolphin on 2018/4/20.
 */
public interface ISearchService {

	/**
	 * 创建文档
	 * @param id
	 */
	boolean index(String id);

	/**
	 * 移除索引
	 * @param id
	 */
	boolean remove(String id);

	ServerResponse<?> search(ArticleSearch search);
}
