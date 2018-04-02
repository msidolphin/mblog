package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import pers.msidolphin.mblog.object.po.Reply;

/**
 * Created by msidolphin on 2018/4/2.
 */
public interface RepliesRepository extends CrudRepository<Reply, Long>{
}
