package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalUsersController")
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@PostMapping("")
	public ServerResponse<?> save(HttpServletRequest request, User user) {
		return ServerResponse.success(userService.addUser(user, request));
	}
}
