package pers.msidolphin.mblog.object.dto;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Getter
@Setter
public class CommentDto {

	private PortalUserDto user;

	private String id;

	private String articleId;

	private String content;

	private String createTime;

	private PageInfo<ReplyDto> replies;

	private Integer replyCount = 0;

}
