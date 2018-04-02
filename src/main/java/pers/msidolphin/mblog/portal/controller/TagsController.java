package pers.msidolphin.mblog.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalTagsController")
@RequestMapping("/tags")
public class TagsController {

	@GetMapping("")
	public ServerResponse<?> get() {
		return null;
	}
}
