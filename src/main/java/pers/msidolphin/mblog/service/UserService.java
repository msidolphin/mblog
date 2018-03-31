package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.helper.Assert;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.model.mapper.UserMapper;
import pers.msidolphin.mblog.model.repository.UserRepository;
import pers.msidolphin.mblog.object.dto.PortalUserDto;
import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;

	/*
	 * portal start
	 */
	public void addUser() {

	}

	public PortalUserDto getCurrentUser(HttpSession session) {
		return (PortalUserDto) session.getAttribute("currentUser");
	}

	public PortalUserDto getUser(String id) {
		Assert.notNull(id);
		return userMapper.findUserById(id);
	}

	@Transactional
	public PortalUserDto addUser(User user) {
		Assert.notNull(user);
		user.setId(AutoIdHelper.getId());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		//随机生成头像并上传头像

		userRepository.save(user);
		PortalUserDto userDto = new PortalUserDto();
		userDto.setId(user.getId().toString());
		userDto.setAvatar(user.getAvatar());
		return userDto;
	}
}
