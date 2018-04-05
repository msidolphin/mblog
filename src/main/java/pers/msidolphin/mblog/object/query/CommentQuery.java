package pers.msidolphin.mblog.object.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/4.
 */
@Setter
public class CommentQuery extends BaseQuery {
	private String title;
	private String username;
	private String content;
	private String startTime;
	private String endTime;

	public String getTitle() {
		if (title == null) return null;
		if("".equals(title.trim())) return null;
		return title + "%";
	}

	public String getUsername() {
		if(username == null) return null;
		if("".equals(username.trim())) return null;
		return username + "%";
	}

	public String getContent() {
		if(content == null) return null;
		if("".equals(content.trim())) return null;
		return "%" + content + "%";
	}

	public String getStartTime() {
		if(startTime == null) return null;
		if("".equals(startTime.trim())) return null;
		return startTime;
	}

	public String getEndTime() {
		if(endTime == null) return null;
		if("".equals(endTime.trim())) return null;
		return endTime + " 23:59:59";
	}

	@Override
	public String toString() {
		return "CommentQuery{" +
				"title='" + title + '\'' +
				", username='" + username + '\'' +
				", content='" + content + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				'}';
	}
}
