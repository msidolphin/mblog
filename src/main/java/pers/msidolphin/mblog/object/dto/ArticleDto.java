package pers.msidolphin.mblog.object.dto;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/3/27.
 */
@Getter
@Setter
public class ArticleDto{

	//文章id
	private String id;

	//文章标题
	private String title;

	//文章作者
	private String author;

	//文章标签
	private String tags;

	//文章摘要
	private String summary;

	//文章缩略图
	private String thumbnail;

	//文章内容
	private String content;

	//文章类型编码
	private Integer typeCode;

	//文章类型名称
	private String typeName;

	//评论数
	private Integer commentCount = 0;

	//总回复数 = 评论数 + 回复数
	private Integer replies = 0;

	//文章阅览数
	private Integer views;

	//文章评论列表
	private PageInfo<CommentDto> commentList;

	//创建时间
	protected String createTime;

	//更新时间
	protected String updateTime;

}
