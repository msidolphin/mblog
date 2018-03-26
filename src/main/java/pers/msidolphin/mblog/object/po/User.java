package pers.msidolphin.mblog.object.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户实体类
 * Created by msidolphin on 2018/3/26.
 */
@Entity @Getter @Setter
@Table(name = "user", schema = "mblog")
public class User extends BasePo{

	@Id
	private Integer id;			//主键

	@Column(name = "username")
	private String username;	//用户名

	@Column(name = "password")
	private String password;	//密码

	@Column(name = "nickname")
	private String nickname;	//昵称

	@Column(name = "salt")
	private String salt;		//盐值

	@Column(name = "summary")
	private String summary;		//个人摘要/简介

	@Column(name = "avatar")
	private String avatar;		//头像

	@Column(name = "email")
	private String email;		//电子邮箱

	@Column(name = "phone")
	private String phone;		//联系电话

	@Column(name = "website")
	private String website;		//个人网址

	@Column(name = "login_time")
	private Date loginTime;		//登录时间

	@Column(name = "login_ip")
	private String loginIp;		//登录IP

	@Column(name = "status")
	private Integer status;		//状态 0-启用 1-禁用

	@Column(name = "is_admin")
	private Integer isAdmin;	//是否为博主 0-否 1-是

}
