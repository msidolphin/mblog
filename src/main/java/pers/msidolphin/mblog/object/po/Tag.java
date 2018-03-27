package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 标签实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity
@Getter @Setter
@Table(name = "tags", schema = "mblog")
public class Tag {

	@Id
	@Column(name = "id")
	private Long id;				//主键

	@Column(name = "name")
	private String name;			//标签名称

	@Column(name = "frequency")
	private Integer frequency;		//引用频率

	@Column(name = "create_time")
	protected Date createTime;	 //创建时间

	@Column(name = "update_time")
	protected Date updateTime;	 //更新时间

	@Column(name = "creator")
	protected Long creator;		 //创建者

	@Column(name = "updator")
	protected Long updator;		 //更新者

}
