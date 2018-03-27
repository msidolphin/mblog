package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pers.msidolphin.mblog.object.po.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long>{

    Tag findByName(String name);

    void deleteByName(String name);
}
