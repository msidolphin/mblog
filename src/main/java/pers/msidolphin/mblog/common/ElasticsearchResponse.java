package pers.msidolphin.mblog.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by msidolphin on 2018/5/3.
 */
@Getter
@Setter
public class ElasticsearchResponse {

	private float score;
	private String id;
	private String index;
	private String type;
	private Map<String, Object> source;
	private Map<String, Object> highlight;

}
