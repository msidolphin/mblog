package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.common.annotation.Validation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * Created by msidolphin on 2018/3/26.
 */
@Entity @Getter @Setter
@Table(name = "article", schema = "mblog")
@Validation
public class Article {

	@Id
	@Column(name = "id")
	private String id;			 //主键

	@Column(name = "title")
	@NotEmpty(message = "文章标题不能为空")
	private String title;		 //文章标题

	@Column(name = "thumbnail")
	private String thumbnail;    //缩略图路径

	@Column(name = "summary")
	private String summary;      //文章摘要

	@Column(name = "type")
	private Integer type;		 //文章类型 0-原创 1-转载 2-翻译

	@Column(name = "content")
	@NotEmpty(message = "文章内容不能为空")
	private String content;      //文章内容

	@Column(name = "is_delete")
	private Integer isDelete;	 //是否删除 0-否 1-是

	@Column(name = "views")
	private Integer views;		 //文章阅览数

	@Column(name = "editor")
	private Integer editor;		 //编辑器类型 0-富文本 1-Markdown

	@Column(name = "vote")
	private Integer vote;		 //点赞数

	@Column(name = "cid")
	private Integer cid;		 //分类名称

	@Column(name = "create_time")
	protected Date createTime;	 //创建时间

	@Column(name = "update_time")
	protected Date updateTime;	 //更新时间

	@Column(name = "creator")
	protected Long creator;		 //创建者

	@Column(name = "updator")
	protected Long updator;		 //更新者

	@Override
	public String toString() {
		return "文章：{" +
				"id=" + id +
				", title='" + title + '\'' +
				", thumbnail='" + thumbnail + '\'' +
				", summary='" + summary + '\'' +
				", type=" + type +
				", content='" + content + '\'' +
				", isDelete=" + isDelete +
				", views=" + views +
				", editor=" + editor +
				", vote=" + vote +
				", cid=" + cid +
				'}';
	}
}

