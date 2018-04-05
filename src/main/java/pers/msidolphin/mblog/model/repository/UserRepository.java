package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import pers.msidolphin.mblog.object.po.User;

/**
 * Created by msidolphin on 2018/3/31.
 */
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsernameAndEmail(String username, String email);

	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);
}
