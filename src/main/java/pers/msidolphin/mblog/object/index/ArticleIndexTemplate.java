package pers.msidolphin.mblog.object.index;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 文章索引模板
 * Created by msidolphin on 2018/4/20.
 */
@Getter
@Setter
public class ArticleIndexTemplate {

	public static final String INDEX_NAME = "article";
	public static final String TYPE = "doc";

	public static final String ARTICLE_ID = "articleId";
	public static final String SUMMARY = "summary";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String TAG = "tag";
	public static final String VIEWS = "views";
	public static final String CREATE_TIME = "createTime";
	public static final String UPDATE_TIME = "updateTime";

	private String articleId;

	private String summary;

	private String title;

	private String content;

	private List<String> tag;

	private Integer views;

	private Date createTime;

	private Date updateTime;

	@Override
	public String toString() {
		return "ArticleIndexTemplate{" +
				"articleId='" + articleId + '\'' +
				", summary='" + summary + '\'' +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", tag=" + tag +
				", views=" + views +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
