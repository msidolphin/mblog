package pers.msidolphin.mblog.admin.controller;

import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/articles")
public class ArticlesController {

	@GetMapping("")
	public ServerResponse<?> list() {
		return ServerResponse.success();
	}

	@PostMapping("")
	public ServerResponse<?> add() {
		return null;
	}

	@PutMapping("")
	public ServerResponse<?> save() {
		return null;
	}

	@GetMapping("/{id}")
	public ServerResponse<?> get(@PathVariable  Long id) {
		return null;
	}

	@DeleteMapping("/{id}")
	public ServerResponse<?> delete(@PathVariable Long id) {
		return null;
	}

}
