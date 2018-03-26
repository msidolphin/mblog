package pers.msidolphin.mblog.object.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标签实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity
@Table(name = "tags", schema = "mblog")
public class Tag extends BasePo{

	@Id
	@Column(name = "id")
	private Integer id;				//主键

	@Column(name = "name")
	private String name;			//标签名称

	@Column(name = "frequency")
	private Integer frequency;		//引用频率

}
