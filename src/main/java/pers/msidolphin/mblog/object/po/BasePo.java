package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 实体类基类
 * Created by msidolphin on 2018/3/26.
 */
@Setter
@Getter
public class BasePo implements Serializable {


	@Column(name = "create_time")
	private Date createTime;	 //创建时间

	@Column(name = "update_time")
	private Date updateTime;	 //更新时间

	@Column(name = "creator")
	private Long creator;		 //创建者

	@Column(name = "updator")
	private Long updator;		 //更新者
}
