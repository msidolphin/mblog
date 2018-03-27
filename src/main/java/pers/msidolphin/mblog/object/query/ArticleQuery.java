package pers.msidolphin.mblog.object.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by msidolphin on 2018/3/27.
 */
public class ArticleQuery extends BaseQuery {

	private String title;

	private String tags;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	public String getTitle() {
		return title == null ? null : title.trim();
	}

	public ArticleQuery setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTags() {
		return tags;
	}

	public ArticleQuery setTags(String tags) {
		this.tags = tags;
		return this;
	}

	public Date getStartTime() {
		return startTime;
	}

	public ArticleQuery setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public ArticleQuery setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}
}
