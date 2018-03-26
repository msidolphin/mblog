package pers.msidolphin.mblog.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/articles")
public class ArticlesController {

	@GetMapping("")
	public ServerResponse<?> index() {
		return ServerResponse.success();
	}
}
