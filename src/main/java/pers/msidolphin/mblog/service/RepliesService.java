package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.model.repository.RepliesRepository;
import pers.msidolphin.mblog.object.po.Reply;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Service
public class RepliesService {

	@Autowired
	private RepliesRepository repliesRepository;


	public ServerResponse<?> addReplies(Reply reply) {
		return ServerResponse.success(reply);
	}

}
