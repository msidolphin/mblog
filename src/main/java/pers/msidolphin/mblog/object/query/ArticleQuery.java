package pers.msidolphin.mblog.object.query;

import org.springframework.format.annotation.DateTimeFormat;
import pers.msidolphin.mblog.helper.Util;

import java.util.Date;
import java.util.List;

/**
 * Created by msidolphin on 2018/3/27.
 */
public class ArticleQuery extends BaseQuery {

	private String title;

	private String tags;

	private List<String> tagList;

	private String startTime;

	private String endTime;

	/**
	 * 文章右模糊查询
	 * @return
	 */
	public String getTitle() {
		if(title == null) {
			return null;
		}
		title = title.trim();
 		if(Util.isNotEmpty(title)) {
			return title + "%";
		}
		return null;
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

	public String getStartTime() {
		return startTime;
	}

	public ArticleQuery setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public ArticleQuery setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public ArticleQuery setTagList(List<String> tagList) {
		this.tagList = tagList;
		return this;
	}

	@Override
	public String toString() {
		return "ArticleQuery{" +
				"title='" + title + '\'' +
				", tags='" + tags + '\'' +
				", tagList=" + tagList +
				", startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}
}
