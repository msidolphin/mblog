package pers.msidolphin.mblog.object.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/4.
 */
@Getter
@Setter
public class AdminCommentDto {

	private String type;
	private String id;
	private String title;
	private String username;
	private String email;
	private String website;
	private String content;
	private String create_time;
	private Boolean status;
}
