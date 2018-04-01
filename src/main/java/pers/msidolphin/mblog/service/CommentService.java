package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.helper.Assert;
import pers.msidolphin.mblog.helper.BeanValidatorHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.model.mapper.CommentMapper;
import pers.msidolphin.mblog.model.mapper.RepliesMapper;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.dto.ReplyDto;
import pers.msidolphin.mblog.object.po.Comment;
import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class CommentService extends BaseService{

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private RepliesMapper repliesMapper;

	/**
	 * 根据文章id获取评论列表
	 * @param articleId 文章id
	 * @return {List} 评论列表
	 */
	public PageInfo<CommentDto> getComments(String articleId) {
		Assert.notNull(articleId);
		//评论分页
		PageHelper.startPage(1, 10, "c.create_time desc");
		List<CommentDto> comments = commentMapper.findCommentsByArticleId(articleId);
		PageInfo<CommentDto> pageInfo = new PageInfo<>(comments);
		//获取评论回复
		for(CommentDto comment : pageInfo.getList()) {
			//回复分页
			PageHelper.startPage(1, 5, "r.create_time desc");
			List<ReplyDto> repies = repliesMapper.findRepliesByCommentId(comment.getId());
			PageInfo<ReplyDto> repiesInfo = new PageInfo<>(repies);
			comment.setReplies(repiesInfo);
		}
		return pageInfo;
	}

	public ServerResponse<?> addComment(Comment comment) {
		Map<String, String> validateRes = BeanValidatorHelper.validate(comment);
		if (!validateRes.isEmpty()) {
			return ServerResponse.badRequest(ResponseCode.BAD_REQUEST.getDescription(), validateRes);
		}
		User user = RequestHolder.getCurrentUser();
		if (user == null) {
			return ServerResponse.unauthorized();
		}


		return null;
	}

}
