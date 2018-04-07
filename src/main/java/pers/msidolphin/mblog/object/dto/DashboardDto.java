package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by msidolphin on 2018/4/7.
 */
@Getter
@Setter
public class DashboardDto {

	private Integer articleCount;

	private Integer userCount;

	private Integer commentCount;

	private Integer tagCount;

	private Map<String, Object> echartsData;

	@Override
	public String toString() {
		return "DashboardDto{" +
				"articleCount=" + articleCount +
				", userCount=" + userCount +
				", commentCount=" + commentCount +
				", tagCount=" + tagCount +
				", echartsData=" + echartsData +
				'}';
	}
}
