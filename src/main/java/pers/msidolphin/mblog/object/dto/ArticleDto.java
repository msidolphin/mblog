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

	private String id;

	private String title;

	private String userName;

	private String tags;

	private String summary;

	private String thumbnail;

	//评论数
	private Integer commentCount = 0;

	//总回复数 = 评论数 + 回复数
	private Integer replies = 0;

	private Integer views;

	private PageInfo<CommentDto> commentList;

	protected String createTime;	 //创建时间

	protected String updateTime;	 //更新时间

}
