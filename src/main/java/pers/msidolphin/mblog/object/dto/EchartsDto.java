package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 基础echarts 对象
 * Created by msidolphin on 2018/4/6.
 */
@Getter
@Setter
public class EchartsDto implements Serializable {
	private Title title;
	private Legend legend;
	private List<xAxis> xAxis;
	private List<yAxis> yAxis;
	private List<Series> series;

	@Getter
	@Setter
	public class Title {
		private String text;
	}

	/**
	 * 图例
	 */
	@Getter
	@Setter
	public class Legend {
		private String Orient;
		private String top;
		private String left;
		private List<Object> data;
	}

	@Getter
	@Setter
	public class xAxis {
		private String type;
		private List<Object> data;
	}

	@Getter
	@Setter
	public class yAxis {
		private String type;
		private Integer min;
		private Integer max;
	}

	@Getter
	@Setter
	public class Series <T> {
		private String name;
		private String type;
		private String stack;
		private List<T> data;
	}
}
