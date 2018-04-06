package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 链接实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity
@Setter @Getter
@Table(name = "links", schema = "mblog")
public class Link extends BasePo{

	@Id
	private String id;			//主键

	@Column(name = "name")
	private String name;		//连接名称

	@Column(name = "url")
	private String url;			//url

	@Column(name = "sort")
	private String sort;		//排序号
}
