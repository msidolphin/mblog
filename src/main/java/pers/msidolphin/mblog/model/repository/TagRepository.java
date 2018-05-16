package pers.msidolphin.mblog.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.object.po.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long>{

    Tag findByName(String name);

    void deleteByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from article_tag where aid = ?1", nativeQuery = true)
    void deleteByArticleId(String id);
}
