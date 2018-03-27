package pers.msidolphin.mblog.object.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by msidolphin on 2018/3/27.
 */
public abstract class BaseQuery {

	protected Integer pageNum;
	protected Integer pageSize;

	public Integer getPageNum() {
		return pageNum == null ? 1 : pageNum;
	}

	public BaseQuery setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return this;
	}

	public Integer getPageSize() {
		return pageSize == null ? 10 : pageSize;
	}

	public BaseQuery setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
