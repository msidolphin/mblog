package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Getter
@Setter
public class AdminLinkDto {
	private String id;
	private String name;
	private String url;
	private String sort;
	private String createTime;

	@Override
	public String toString() {
		return "AdminLinkDto{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", url='" + url + '\'' +
				", sort='" + sort + '\'' +
				", createTime='" + createTime + '\'' +
				'}';
	}
}
