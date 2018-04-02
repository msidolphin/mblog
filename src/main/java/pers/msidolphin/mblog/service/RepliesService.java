package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.model.repository.RepliesRepository;
import pers.msidolphin.mblog.object.po.Reply;
import pers.msidolphin.mblog.object.po.User;

import java.util.Date;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class RepliesService {

	@Autowired
	private RepliesRepository repliesRepository;

	public ServerResponse<?> addReplies(Reply reply) {
		//评论人
		User user = RequestHolder.getCurrentUser();
		if (user == null || user.getId() == null) {
			return ServerResponse.unauthorized();
		}
		//主键
		reply.setId(AutoIdHelper.getId());
		//回复人id
		reply.setUserId(user.getId());
		//回复时间
		reply.setCreateTime(new Date());
		//更新时间
		reply.setUpdateTime(new Date());
		//点赞数
		reply.setVote(0);
		repliesRepository.save(reply);
		return ServerResponse.success(reply);
	}

}
