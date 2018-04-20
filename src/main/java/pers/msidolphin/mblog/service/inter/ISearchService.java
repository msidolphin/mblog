package pers.msidolphin.mblog.service.inter;

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

}
