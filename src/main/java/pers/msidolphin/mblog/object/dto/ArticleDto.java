package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

	private Integer comments;

	protected String createTime;	 //创建时间

	protected String updateTime;	 //更新时间
}
