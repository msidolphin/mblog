package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 回复实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity
@Setter @Getter
@Table(name = "replies", schema = "mblog")
public class Reply extends BasePo{

	@Id
	@Column(name = "id")
	private Integer id;				//主键

	@Column(name = "comment_id")
	private Integer commentId;		//所属文章id

	@Column(name = "user_id")
	private Integer userId;			//用户id

	@Column(name = "content")
	private String content;			//评论内容

	@Column(name = "status")
	private Integer status;			//状态 0-正常 1-删除

	@Column(name = "vote")
	private Integer vote;			//点赞数
}
