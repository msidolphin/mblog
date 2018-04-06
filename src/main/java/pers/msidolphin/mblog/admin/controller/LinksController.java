package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.AdminLinkDto;
import pers.msidolphin.mblog.object.query.LinkQuery;
import pers.msidolphin.mblog.service.LinkService;

import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/links")
public class LinksController {

	@Autowired
	private LinkService linkService;

	@GetMapping
	public ServerResponse<?> list(LinkQuery linkQuery) {
		return linkService.getLinks(linkQuery);
	}

	@PostMapping
	public ServerResponse<?> save(@RequestBody AdminLinkDto linkDto) {
		return linkService.save(linkDto);
	}

	@DeleteMapping("")
	public ServerResponse<?> delete(@RequestBody Map<String, String> requestParam) {
		return linkService.deletelink(requestParam.get("id"));
	}
}
