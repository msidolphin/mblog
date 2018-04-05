package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Getter
@Setter
public class AdminSettingDto {
	private String id;
	private String key;		//不显示KEY值
	private String value;
	private String name;	//显示名称
	private String type;
}
