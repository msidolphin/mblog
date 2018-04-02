package pers.msidolphin.mblog.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.object.po.Comment;
import pers.msidolphin.mblog.object.po.Reply;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.service.CommentService;
import pers.msidolphin.mblog.service.RepliesService;
import pers.msidolphin.mblog.service.UserService;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalCommentsController")
@RequestMapping("/comments")
@SuppressWarnings("ALL")
public class CommentsController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private RepliesService repliesService;

	@Autowired
	private UserService userService;

	/**
	 * 获取文章评论
	 * @param articleId 文章id
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@GetMapping("/{id}")
	public ServerResponse<?> get(@PathVariable("id") String articleId,
								 @RequestParam(defaultValue = "10", required = false) Integer pageSize,
								 @RequestParam(defaultValue = "1", required = false) Integer pageNum) {
		if(Util.isEmpty(articleId)) return ServerResponse.badRequest();
		return ServerResponse.success(commentService.getComments(articleId, pageSize, pageNum));
	}

	/**
	 * 获取评论回复
	 * @param commentId 评论id
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@GetMapping("/replies/{id}")
	public ServerResponse<?> getReplies(@PathVariable("id") String commentId,
										@RequestParam(defaultValue = "5", required = false) Integer pageSize,
										@RequestParam(defaultValue = "1", required = false) Integer pageNum) {
		if(Util.isEmpty(commentId)) return ServerResponse.badRequest();
		return ServerResponse.success(repliesService.getReplies(commentId, pageSize, pageNum));
	}

	/**
	 * 新增评论
	 * @param response
	 * @param comment
	 * @param user
	 * @return
	 */
	@PostMapping("")
	public ServerResponse<?> save(HttpServletResponse response, Comment comment, User user) {
		userService.addUser(user, response);
		return ServerResponse.success(commentService.addComment(comment));
	}

	/**
	 * 新增回复
	 * @param response
	 * @param reply
	 * @param user
	 * @return
	 */
	@PostMapping("/replies")
	public ServerResponse<?> reply(HttpServletResponse response, Reply reply, User user) {
		userService.addUser(user, response);
		return repliesService.addReplies(reply);
	}


}
