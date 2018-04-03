package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.query.CommentQuery;
import pers.msidolphin.mblog.service.CommentService;

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
}
