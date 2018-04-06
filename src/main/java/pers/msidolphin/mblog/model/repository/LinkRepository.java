package pers.msidolphin.mblog.model.repository;

import org.springframework.data.repository.CrudRepository;
import pers.msidolphin.mblog.object.po.Link;

/**
 * Created by msidolphin on 2018/4/6.
 */
public interface LinkRepository extends CrudRepository<Link, String> {
}
