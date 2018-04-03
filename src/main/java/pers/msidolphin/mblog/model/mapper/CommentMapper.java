package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.AdminCommentDto;
import pers.msidolphin.mblog.object.dto.CommentDto;
import pers.msidolphin.mblog.object.query.CommentQuery;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/29.
 */
public interface CommentMapper {

	List<CommentDto> findCommentsByArticleId(String id);

	List<AdminCommentDto> findComments(CommentQuery query);
}
