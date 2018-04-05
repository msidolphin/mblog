package pers.msidolphin.mblog.object.query;

import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Setter
public class TagQuery extends BaseQuery {

	public String name;
	public String frequency;
	public String order;

	public String getOrder() {
		return order;
	}

	public String getName() {
		if(name == null) return name;
		if("".equals(name.trim())) return null;
		return name + "%";
	}

	public String getFrequency() {
		if(frequency == null) return frequency;
		if("".equals(frequency.trim())) return null;
		return frequency;
	}
}
