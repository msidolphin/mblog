package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/6.
 */
@Getter
@Setter
public class ReportDto {
	private Integer start;	  //开始
	private Integer end;		  //结束
	private String specific;  //特定的 月份或年份
	private Integer type;	  //统计类型 按年 按月

	private Integer chartType; //0-line 1-bar
	private boolean vertical;   //图表水平还是垂直


}
