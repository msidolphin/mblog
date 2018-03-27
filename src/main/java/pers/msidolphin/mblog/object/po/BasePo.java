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
	protected Date createTime;	 //创建时间

	@Column(name = "update_time")
	protected Date updateTime;	 //更新时间

	@Column(name = "creator")
	protected Long creator;		 //创建者

	@Column(name = "updator")
	protected Long updator;		 //更新者
}
