package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.CommentDto;

import java.util.List;

/**
 * Created by msidolphin on 2018/3/29.
 */
public interface CommentMapper {

	List<CommentDto> findCommentsByArticleId(String id);
}
