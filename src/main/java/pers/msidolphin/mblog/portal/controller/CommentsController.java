package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.po.Comment;
import pers.msidolphin.mblog.service.CommentService;

import javax.servlet.http.HttpSession;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalCommentsController")
@RequestMapping("/comments")
public class CommentsController {

	@Autowired
	private CommentService commentService;

	@PostMapping("")
	public ServerResponse<?> save(HttpSession session, Comment comment) {
		return ServerResponse.success(commentService.addComment(comment));
	}


}
