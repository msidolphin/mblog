package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.po.Comment;
import pers.msidolphin.mblog.object.po.Reply;
import pers.msidolphin.mblog.service.CommentService;
import pers.msidolphin.mblog.service.RepliesService;

import javax.servlet.http.HttpSession;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalCommentsController")
@RequestMapping("/comments")
public class CommentsController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private RepliesService repliesService;

	@PostMapping("")
	public ServerResponse<?> save(Comment comment) {
		return ServerResponse.success(commentService.addComment(comment));
	}

	@PostMapping("/replies")
	public ServerResponse<?> reply(Reply reply) {
		return repliesService.addReplies(reply);
	}


}
