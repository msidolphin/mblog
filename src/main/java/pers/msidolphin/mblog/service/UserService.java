package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.Assert;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.AvatarHelper;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.model.mapper.UserMapper;
import pers.msidolphin.mblog.model.repository.UserRepository;
import pers.msidolphin.mblog.object.dto.PortalUserDto;
import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PropertiesHelper propertiesHelper;

	@Autowired
	private FileService fileService;

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
	public PortalUserDto addUser(User user, HttpServletRequest request) {
		Assert.notNull(user);
		user.setId(AutoIdHelper.getId());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		//随机生成头像并上传头像
		try {
			String tmpdirStr = request.getServletContext().getRealPath("upload/");
			File tmpdir = new File(tmpdirStr);
			//判断临时目录是否存在
			if(!tmpdir.exists()) {
				tmpdir.setWritable(true);
				tmpdir.mkdirs();
			}
			String path = AvatarHelper.generateAvatar(user.getUsername(), tmpdirStr,
					UUID.randomUUID().toString());
			File file = new File(path);
			String avatar = fileService.uploadImage(file);
			user.setAvatar(avatar);
		} catch (IOException e) {
			throw new ServiceException("上传头像出错", e);
		}
		userRepository.save(user);
		PortalUserDto userDto = new PortalUserDto();
		userDto.setId(user.getId().toString());
		userDto.setAvatar(user.getAvatar());
		userDto.setWebsite(user.getWebsite());
		return userDto;
	}
}
