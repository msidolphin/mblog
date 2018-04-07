package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.PortalUserDto;

import java.util.Map;

/**
 * Created by msidolphin on 2018/3/31.
 */
public interface UserMapper {

	PortalUserDto findUserById(String id);

	int updateUserById(Map<String, String> params);

	int selectUserCount();
}
