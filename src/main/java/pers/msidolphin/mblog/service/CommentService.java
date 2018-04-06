package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.CommentMapper;
import pers.msidolphin.mblog.model.mapper.RepliesMapper;
import pers.msidolphin.mblog.model.repository.CommentRepository;
import pers.msidolphin.mblog.object.dto.AdminCommentDto;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.dto.ReplyDto;
import pers.msidolphin.mblog.object.po.Comment;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.CommentQuery;

import javax.servlet.http.HttpSession;
import java.util.Date;
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
	private CommentRepository commentRepository;

	@Autowired
	private RepliesMapper repliesMapper;

	@Autowired
	private PropertiesHelper propertiesHelper;

	@Autowired
	private RepliesService repliesService;

	/**
	 * 根据文章id获取评论列表
	 * @param articleId 文章id
	 * @return {List} 评论列表
	 */
	public PageInfo<CommentDto> getComments(String articleId, Integer pageSize, Integer pageNum) {
		Assert.notNull(articleId);
		//评论分页
		PageHelper.startPage(pageNum, pageSize, "c.create_time desc");
		List<CommentDto> comments = commentMapper.findCommentsByArticleId(articleId);
		PageInfo<CommentDto> pageInfo = new PageInfo<>(comments);
		//获取评论回复
		for(CommentDto comment : pageInfo.getList()) {
			//设置用户头像地址 图片服务器+路径
			comment.getUser().setAvatar(
					propertiesHelper.getValue("blog.image.server") + comment.getUser().getAvatar());
			PageInfo<ReplyDto> replies = repliesService.getReplies(comment.getId(), 5, 1);
			comment.setReplies(replies);
		}
		return pageInfo;
	}

	public PageInfo<AdminCommentDto> findComments(CommentQuery query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		PageInfo<AdminCommentDto> pageInfo = new PageInfo<>(commentMapper.findComments(query));
		return pageInfo;
	}

	@Transactional
	public String changeStatus(String id, String status) {
		if(Util.isEmpty(id)) throw new InvalidParameterException("评论id不能为空");
		commentMapper.updateStatusById(status, id);
		return status;
	}

	/**
	 * 新增评论
	 * @param comment
	 * @return
	 */
	@Transactional
	public ServerResponse<?> addComment(Comment comment) {
		//评论人
		User user = RequestHolder.getCurrentUser();
		if (user == null) {
			return ServerResponse.unauthorized();
		}
		//主键
		comment.setId(AutoIdHelper.getId());
		//评论人id
		comment.setUserId(user.getId());
		//创建时间
		comment.setCreateTime(new Date());
		//修改时间
		comment.setUpdateTime(new Date());
		//点赞数
		comment.setVote(0);
		//状态
		comment.setStatus(0);

		//保存
		commentRepository.save(comment);

		return ServerResponse.success(comment);
	}

}
