package pers.msidolphin.mblog.service;

import com.google.common.collect.Maps;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.UserMapper;
import pers.msidolphin.mblog.model.repository.UserRepository;
import pers.msidolphin.mblog.object.dto.AdminUserDto;
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
import java.util.Optional;
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

	@Transactional
	public ServerResponse<?> save(AdminUserDto adminUserDto) {
		//更新到数据库
		Map<String, String> params = Maps.newHashMap();
		params.put("id", adminUserDto.getId());
		if(Util.isNotEmpty(adminUserDto.getNickname())) params.put("nickname",adminUserDto.getNickname());
		if(Util.isNotEmpty(adminUserDto.getAvatar())) 	params.put("avatar", adminUserDto.getAvatar());
		if(Util.isNotEmpty(adminUserDto.getEmail())) 	params.put("email", adminUserDto.getEmail());
		if(Util.isNotEmpty(adminUserDto.getPhone())) 	params.put("phone", adminUserDto.getPhone());
		if(Util.isNotEmpty(adminUserDto.getSummary())) 	params.put("summary", adminUserDto.getSummary());
		userMapper.updateUserById(params);
		User user = userRepository.findByUsername(adminUserDto.getUsername());
		//更新缓存
		redisHelper.setValue(RequestHolder.getRequestToken(), JsonHelper.object2String(user));
		return ServerResponse.success(adminUserDto);
	}

	public ServerResponse<?> auth(AdminUserDto userDto, HttpServletResponse response) {
		if(Util.isNotEmpty(userDto.getAccessToken()))
			return checkAccessToken(userDto.getAccessToken());
		return login(userDto.getUsername(), userDto.getPassword(), response);
	}

	/**
	 * 用于后台用户登录
	 * @param username		用户名
	 * @param password		密码
	 * @return
	 */
	public ServerResponse<?> login(String username, String password, HttpServletResponse response) {
		//判断当前是否已经登录过了
		if(RequestHolder.getCurrentAdmin() != null) return ServerResponse.success("登录成功");
		if(Util.isEmpty(username) || Util.isEmpty(password))
			throw new InvalidParameterException("用户名或密码不能为空");
		User user = userRepository.findByUsername(username);
		if(user == null)
			return ServerResponse.response(ResponseCode.NOT_FOUND, "用户不存在");
		if(user.getSalt() == null)
			user.setSalt("");
		String encry = MD5Helper.md5EncodeWithUtf8(password + user.getSalt());
		user = userRepository.findByUsernameAndPassword(username, encry);
		if(user == null)
			return ServerResponse.badRequest("密码错误");
		if(user.getIsAdmin() != 1) return ServerResponse.forbidden();

		//用户登录成功，生成token存入到cookie
		String token = UUID.randomUUID().toString();
		//存入到缓存
		redisHelper.setValue(token, JsonHelper.object2String(user), propertiesHelper.getLong("blog.admin.user.token.timeout"));
		Cookie cookie = new Cookie(propertiesHelper.getValue("blog.admin.user.token.key"), token);
		cookie.setMaxAge(propertiesHelper.getInt("blog.admin.user.token.timeout"));
		cookie.setPath("/");
		cookie.setDomain("localhost");
		response.addCookie(cookie);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", RequestHolder.getCurrentRequest().getHeader("Origin"));

		return ServerResponse.success("登录成功");
	}

	public ServerResponse<?> checkAccessToken(String token) {
		if(token != null) {
			String userString = redisHelper.getValue(token);
			try {
				if(userString != null) {
					AdminUserDto user = JsonHelper.string2Object(userString, AdminUserDto.class);
					if(Util.isNotEmpty(user.getAvatar()))
						user.setAvatar(propertiesHelper.getValue("blog.image.server") + user.getAvatar());
					return ServerResponse.success(user);
				}
				return ServerResponse.unauthorized("令牌失效");
			} catch (IOException e) {
				throw new ServiceException(e);
			}
		}
		return ServerResponse.unauthorized("未携带令牌");
	}

	public AdminUserDto getCurrentUser() {
		User user = RequestHolder.getCurrentAdmin();
		if(user == null) throw new AuthorizedException();
		return copy2AdminDto(user);
	}

	public ServerResponse<?> getAdminUser(String id) {
		AdminUserDto userDto = getCurrentUser();
		if(userDto != null) return ServerResponse.success(userDto);
		if(id == null) throw new InvalidParameterException("用户id不能为空");
		Optional<User> userOptional = userRepository.findById(Long.parseLong(id));
		User user = null;
		if(userOptional.isPresent()) user = userOptional.get();
		if(user == null) return ServerResponse.response(ResponseCode.NOT_FOUND);
		return ServerResponse.success(copy2AdminDto(user));
	}

	private AdminUserDto copy2AdminDto(User user) {
		AdminUserDto adminUserDto = new AdminUserDto();
		adminUserDto.setId(user.getId().toString());
		adminUserDto.setUsername(user.getUsername());
		adminUserDto.setNickname(user.getNickname());
		adminUserDto.setEmail(user.getEmail());
		adminUserDto.setAvatar(user.getAvatar());
		adminUserDto.setPhone(user.getPhone());
		return adminUserDto;
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

	/**
	 * 新增用户 前台
	 * @param user
	 * @param response
	 * @return
	 */
	@Transactional
	public ServerResponse<?> addUser(User user, HttpServletResponse response) {
		Map<String, String> validateRes = BeanValidatorHelper.validate(user);
		if (!validateRes.isEmpty()) return ServerResponse.badRequest(ResponseCode.BAD_REQUEST.getDescription(), validateRes);

		HttpServletRequest request = RequestHolder.getCurrentRequest();

		//验证用户是否存在
		User dbUser = userRepository.findByUsernameAndEmail(user.getUsername(), user.getEmail());
		if (dbUser != null) {
			//如果用户改变自己的个人网址，则更新用户网址和刷新缓存
			if((dbUser.getWebsite() != null && dbUser.getWebsite().equals(user.getWebsite())) || Util.isEmpty(dbUser.getWebsite()) && Util.isNotEmpty(user.getWebsite())) {
				userRepository.updateWebsiteById(user.getWebsite(), dbUser.getId().toString());
				dbUser.setWebsite(user.getWebsite());
				//存在该用户 直接登录
				addCookie(response, request.getSession(), dbUser);
				//更改redis缓存
				String key = RequestHolder.getPortalRequestToken();
				if (key == null)
					key = RequestHolder.getCurrentRequest().getSession().getId();
				redisHelper.setValue(key, JsonHelper.object2String(dbUser));
			}
			PortalUserDto userDto = new PortalUserDto();
			userDto.setId(dbUser.getId().toString());
			userDto.setAvatar(dbUser.getAvatar());
			userDto.setWebsite(dbUser.getWebsite());
			return ServerResponse.success(userDto);
		}

		//新增用户
		user.setId(AutoIdHelper.getId());
		user.setNickname(user.getUsername());
		user.setIsAdmin(0);
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
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setMaxAge(propertiesHelper.getInt("blog.admin.user.token.timeout"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", RequestHolder.getCurrentRequest().getHeader("Origin"));
		//更新当前用户
		RequestHolder.removeCurrentUser();
		RequestHolder.add(user);
		response.addCookie(cookie);
	}

	/**
	 * 获取用户数量(非后台用户)
	 * @return
	 */
	public int getUserCount() {
		return userMapper.selectUserCount();
	}
}
