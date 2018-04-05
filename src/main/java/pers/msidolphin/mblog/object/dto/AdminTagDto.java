package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.object.query.BaseQuery;

/**
 * Created by msidolphin on 2018/4/5.
 */
@Setter
@Getter
public class AdminTagDto extends BaseQuery {

	private String id;
	private String name;
	private String frequency;
	private String createTime;

}
