package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by msidolphin on 2018/3/26.
 */
@Entity @Getter @Setter
@Table(name = "article", schema = "mblog")
public class Article extends BasePo{

	@Id
	@Column(name = "id")
	private Long id;			 //主键

	@Column(name = "title")
	private String title;		 //文章标题

	@Column(name = "thumbnail")
	private String thumbnail;    //缩略图路径

	@Column(name = "summary")
	private String summary;      //文章摘要

	@Column(name = "type")
	private Integer type;		 //文章类型 0-原创 1-转载 2-翻译

	@Column(name = "content")
	private String content;      //文章内容

	@Column(name = "tags")
	private String tags;		 //标签字符串

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

}

