package pers.msidolphin.mblog.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.object.po.User;

/**
 * Created by msidolphin on 2018/3/31.
 */
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsernameAndEmail(String username, String email);

	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update user u set u.website = ?1 where u.id = ?2", nativeQuery = true)
	int updateWebsiteById(String website, String id);
}
