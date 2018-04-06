package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.object.dto.AdminUserDto;
import pers.msidolphin.mblog.service.CommonService;
import pers.msidolphin.mblog.service.UserService;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private CommonService commonService;

	@PostMapping(value = "")
	public ServerResponse<?> auth (@RequestBody AdminUserDto userDto, HttpServletResponse response) {
		return userService.auth(userDto, response);
	}

	@GetMapping("")
	public ServerResponse<?> get() {
		AdminUserDto userDto = userService.getCurrentUser();
		if (userDto == null) return ServerResponse.unauthorized();
		return ServerResponse.success(userDto);
	}

	@GetMapping("/logout")
	public ServerResponse<?> logout(HttpServletResponse response) {
		return commonService.logout(response);
	}

	@PostMapping("/{id}")
	public ServerResponse<?> save(@RequestBody AdminUserDto adminUserDto) {
		return userService.save(adminUserDto);
	}
}
