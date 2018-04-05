package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.query.CommentQuery;
import pers.msidolphin.mblog.service.CommentService;

import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/comments")
public class CommentsController {

	@Autowired
	private CommentService commentService;

	@GetMapping("")
	public ServerResponse<?> list(CommentQuery commentQuery) {
		return ServerResponse.success(commentService.findComments(commentQuery));
	}

	@PutMapping("")
	public ServerResponse<?> put(@RequestBody Map<String, String> params) {
		return ServerResponse.success(commentService.changeStatus(params.get("id"), params.get("status")));
	}
}
