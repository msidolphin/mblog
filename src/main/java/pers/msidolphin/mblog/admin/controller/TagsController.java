package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.query.TagQuery;
import pers.msidolphin.mblog.service.TagService;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/tags")
public class TagsController {

	@Autowired
	private TagService tagService;

	@GetMapping
	public ServerResponse<?> list(TagQuery query) {
		return tagService.getTags(query);
	}

}
