package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.AdminUserDto;
import pers.msidolphin.mblog.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "")
	public ServerResponse<?> auth (@RequestBody AdminUserDto userDto, HttpServletResponse response) {
		return userService.auth(userDto, response);
	}

	@GetMapping("/{id}")
	public ServerResponse<?> get(@PathVariable String id) {
		return null;
	}
}
