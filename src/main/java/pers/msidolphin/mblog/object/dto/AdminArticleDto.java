package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.object.po.Article;

/**
 * 改变数据库表结构 新增文章标签关系表
 * Created by msidolphin on 2018/4/5.
 */
@Getter
@Setter
public class AdminArticleDto extends Article {
	private String tags;		 //标签字符串
	private String tagsId;
}
