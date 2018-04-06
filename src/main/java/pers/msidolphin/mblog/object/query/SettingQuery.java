package pers.msidolphin.mblog.object.query;

import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Setter
public class SettingQuery extends BaseQuery {

	private String name;
	private String key;
	private String type;

	public String getName() {
		if(name == null) return null;
		if("".equals(name.trim())) return null;
		return name + "%";
	}

	public String getKey() {
		if(key == null) return null;
		if("".equals(key.trim())) return null;
		return key + "%";
	}

	public String getType() {
		return type;
	}
}
