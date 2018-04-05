package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.service.RepliesService;

import java.util.Map;

/**
 * Created by msidolphin on 2018/4/5.
 */
@RestController
@RequestMapping("/admin/replies")
public class RepliesController {

	@Autowired
	private RepliesService repliesService;

	@PutMapping
	public ServerResponse<?> put(@RequestBody Map<String, String> params) {
		return ServerResponse.success(repliesService.changeStatus(params.get("id"), params.get("status")));
	}

}
