package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.PortalUserDto;

/**
 * Created by msidolphin on 2018/3/31.
 */
public interface UserMapper {

	PortalUserDto findUserById(String id);
}
