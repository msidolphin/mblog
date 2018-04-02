package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.object.dto.PortalUserDto;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalUsersController")
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public ServerResponse<?> get() {
		User user = RequestHolder.getCurrentUser();
		if(user == null) return ServerResponse.unauthorized();
		PortalUserDto userDto = new PortalUserDto();
		userDto.setUsername(user.getUsername());
		userDto.setWebsite(user.getWebsite());
		userDto.setId(user.getId().toString());
		userDto.setAvatar(user.getAvatar());
		userDto.setEmail(user.getEmail());
		userDto.setNickname(user.getUsername());
		return ServerResponse.success(userDto);
	}

	@PostMapping("")
	public ServerResponse<?> save(HttpServletResponse response, User user) {
		return userService.addUser(user, response);
	}
}
