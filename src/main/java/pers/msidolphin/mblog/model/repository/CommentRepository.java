package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pers.msidolphin.mblog.object.po.Comment;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
}
