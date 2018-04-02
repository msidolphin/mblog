package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.common.annotation.Validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 评论实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity @Getter @Setter
@Table(name = "comment", schema = "mblog")
@Validation
public class Comment extends BasePo{

	@Id
	@Column(name = "id")
	private Long id;				//主键

	@Column(name = "article_id")
	@NotEmpty(message = "文章id不能为空")
	private String articleId;		//所属文章id

	@Column(name = "user_id")
	private Long userId;			//用户id

	@Column(name = "content")
	@NotEmpty(message = "评论内容不能为空")
	private String content;			//评论内容

	@Column(name = "status")
	private Integer status;			//状态 0-正常 1-删除

	@Column(name = "vote")
	private Integer vote;			//点赞数

	@Override
	public String toString() {
		return "Comment{" +
				"id=" + id +
				", articleId='" + articleId + '\'' +
				", userId=" + userId +
				", content='" + content + '\'' +
				", status=" + status +
				", vote=" + vote +
				'}';
	}
}
