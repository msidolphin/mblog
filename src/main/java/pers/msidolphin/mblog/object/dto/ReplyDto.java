package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/3/29.
 */
@Getter
@Setter
public class ReplyDto {

	private String id;

	private String commentId;

	private String content;

	private String createTime;

	private PortalUserDto user;

	private PortalUserDto toUser;
}
