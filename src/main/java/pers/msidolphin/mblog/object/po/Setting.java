package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设置实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity
@Setter @Getter
@Table(name = "setting", schema = "mblog")
public class Setting extends BasePo{

	@Id
	@Column(name = "id")
	private Integer id;				//主键

	@Column(name = "key_value")
	private String key;				//key值

	@Column(name = "value")
	private String value;			//value值

	@Column(name = "type")
	private Integer type;			//类型 0-门户 1-后台 2-公共
}
