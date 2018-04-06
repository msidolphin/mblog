package pers.msidolphin.mblog.object.query;

import lombok.Setter;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Setter
public class LinkQuery extends BaseQuery {

	private String name;

	public String getName() {
		if(name == null) return null;
		if("".equals(name.trim())) return null;
		return name + "%";
	}
}
