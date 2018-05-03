package pers.msidolphin.mblog.object.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/5/1.
 */
@Getter
@Setter
public class ArticleSearch extends BaseQuery{

	private String title;

	private String summary;
}
