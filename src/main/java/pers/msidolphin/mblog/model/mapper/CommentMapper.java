package pers.msidolphin.mblog.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.msidolphin.mblog.object.dto.AdminCommentDto;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.query.CommentQuery;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/29.
 */
public interface CommentMapper {

	List<CommentDto> findCommentsByArticleId(@Param("id") String id,@Param("status") Integer status);

	List<AdminCommentDto> findComments(CommentQuery query);

	int updateStatusById(@Param("status") String status,@Param("id") String id);

	int selectCommentCount();

	int deleteRepliesByCommentId(List<String> list);

	int deleteByCommentIds(List<String> list);
}
