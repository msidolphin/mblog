package pers.msidolphin.mblog.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.object.po.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String> {

	Article findByArticleIdAndIsDelete(String article, Integer isDelete);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update article a set a.is_delete = ?1 where a.id = ?2", nativeQuery = true)
	int updateStatusById(int status, String id);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update article a set a.views = a.views + 1 where a.id = ?1", nativeQuery = true)
	int incrementsViewsById(String id);

}
