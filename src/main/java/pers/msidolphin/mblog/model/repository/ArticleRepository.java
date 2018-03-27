package pers.msidolphin.mblog.model.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pers.msidolphin.mblog.object.po.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long>, JpaSpecificationExecutor<Article> {
}
