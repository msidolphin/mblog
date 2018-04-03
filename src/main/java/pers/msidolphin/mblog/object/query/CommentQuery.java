package pers.msidolphin.mblog.object.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/4.
 */
@Getter
@Setter
public class CommentQuery extends BaseQuery {
	private String title;
	private String username;
	private String content;
	private String startTime;
	private String endTime;
}
