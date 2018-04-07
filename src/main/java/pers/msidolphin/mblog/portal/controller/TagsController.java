package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.service.TagService;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalTagsController")
@RequestMapping("/tags")
public class TagsController {

	@Autowired
	private TagService tagService;

	@GetMapping("")
	public ServerResponse<?> get() {
		return null;
	}

	@GetMapping("/reports/bar")
	public ServerResponse<?> report(@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
		return tagService.barReport(limit);
	}
}
