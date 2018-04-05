package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.RepliesMapper;
import pers.msidolphin.mblog.model.repository.RepliesRepository;
import pers.msidolphin.mblog.object.dto.ReplyDto;
import pers.msidolphin.mblog.object.po.Reply;
import pers.msidolphin.mblog.object.po.User;

import java.util.Date;
import java.util.List;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class RepliesService {

	@Autowired
	private RepliesRepository repliesRepository;

	@Autowired
	private RepliesMapper repliesMapper;

	@Autowired
	private PropertiesHelper propertiesHelper;

	public PageInfo<ReplyDto> getReplies(String commentId, Integer pageSize, Integer pageNum) {
		PageHelper.startPage(pageNum, pageSize, "r.create_time desc");
		List<ReplyDto> replies = repliesMapper.findRepliesByCommentId(commentId);
		PageInfo<ReplyDto> pageInfo = new PageInfo<>(replies);
		//凭借
		for(ReplyDto replyDto : pageInfo.getList()) {
			replyDto.getUser().setAvatar(propertiesHelper.getValue("blog.image.server") + replyDto.getUser().getAvatar());
		}
		return pageInfo;
	}

	public ServerResponse<?> addReplies(Reply reply) {
		//评论人
		User user = RequestHolder.getCurrentUser();
		if (user == null || user.getId() == null) {
			return ServerResponse.unauthorized();
		}
		//主键
		reply.setId(AutoIdHelper.getId());
		//回复人id
		reply.setUserId(user.getId().toString());
		//回复时间
		reply.setCreateTime(new Date());
		//更新时间
		reply.setUpdateTime(new Date());
		reply.setStatus(0);
		//点赞数
		reply.setVote(0);
		repliesRepository.save(reply);
		return ServerResponse.success(reply);
	}

	public String changeStatus(String id, String status) {
		if(Util.isEmpty(id) || Util.isEmpty(status))
			throw new InvalidParameterException("评论id或状态不能为空");
		repliesMapper.updateStatusById(status, id);
		return status;
	}

}
