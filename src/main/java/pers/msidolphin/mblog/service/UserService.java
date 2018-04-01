package pers.msidolphin.mblog.service;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.UserMapper;
import pers.msidolphin.mblog.model.repository.UserRepository;
import pers.msidolphin.mblog.object.dto.PortalUserDto;
import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
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

	@Autowired
	private RedisHelper redisHelper;

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

	public PortalUserDto checkLogin() {
		User currentUser = RequestHolder.getCurrentUser();
		if (currentUser != null) {
			PortalUserDto userDto = new PortalUserDto();
			userDto.setId(currentUser.getId().toString());
			userDto.setAvatar(currentUser.getAvatar());
			userDto.setWebsite(currentUser.getWebsite());
			return userDto;
		}
		return null;
	}

	@Transactional
	public ServerResponse<?> addUser(User user, HttpServletResponse response) {
		Map<String, String> validateRes = BeanValidatorHelper.validate(user);
		if (!validateRes.isEmpty()) return ServerResponse.badRequest(ResponseCode.BAD_REQUEST.getDescription(), validateRes);

		HttpServletRequest request = RequestHolder.getCurrentRequest();

		//验证用户是否存在
		User dbUser = userRepository.findByUsernameAndEmail(user.getUsername(), user.getEmail());
		if (dbUser != null) {
			//存在该用户 直接登录
			addCookie(response, request.getSession(), dbUser);
			PortalUserDto userDto = new PortalUserDto();
			userDto.setId(dbUser.getId().toString());
			userDto.setAvatar(dbUser.getAvatar());
			userDto.setWebsite(dbUser.getWebsite());
			return ServerResponse.success(userDto);
		}

		//新增用户
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

		addCookie(response, request.getSession(), user);
		return ServerResponse.success(userDto);
	}

	private void addCookie(HttpServletResponse response, HttpSession session, User user) {
		//存入到Redis
		redisHelper.setValue(session.getId(), JsonHelper.object2String(user),
				propertiesHelper.getLong("blog.user.session,timeout"));
		//将session id写回到cookie
		Cookie cookie = new Cookie(propertiesHelper.getValue("blog.user.session.key"), session.getId());
		response.addCookie(cookie);
	}
}
