package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import pers.msidolphin.mblog.object.po.User;

/**
 * Created by msidolphin on 2018/3/31.
 */
public interface UserRepository extends CrudRepository<User, Long>{
}
