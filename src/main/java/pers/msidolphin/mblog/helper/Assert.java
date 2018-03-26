package pers.msidolphin.mblog.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 * Created by msidolphin on 2018/1/1.
 */
@SuppressWarnings("ALL")
public abstract class Assert {

	private static Logger log = LoggerFactory.getLogger(Assert.class);

	public static final String DEFAULT_NOT_NULL_MESSAGE = "target must not be null";

	public static final String DEFAULT_NOT_EMPTY_MESSAGE = "target must not be empty";

	public static void notNull(Object target) {
		notNull(target, DEFAULT_NOT_NULL_MESSAGE);
	}

	public static void notNull(Object target, String message) {
		if(target == null) {
			throw new AssertionError(message);
		}
	}

	public static void notEmpty(Object[] targets, String message) {
		Assert.notNull(targets);
		if(targets.length == 0) {
			throw new AssertionError(message);
		}
	}

	public static void notEmpty(Collection collection, String message) {
		Assert.notNull(collection);
		if(collection.isEmpty()) {
			throw new AssertionError(message);
		}
	}

	public static void notEmpty(Map<?, ?> map, String message) {
		Assert.notNull(map);
		if(map.isEmpty()) {
			throw new AssertionError(message);
		}
	}

}
