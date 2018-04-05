package pers.msidolphin.mblog.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.msidolphin.mblog.object.dto.ReplyDto;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/29.
 */
public interface RepliesMapper {

	List<ReplyDto> findRepliesByCommentId(String id);

	int updateStatusById(@Param("status") String status,@Param("id") String id);
}
