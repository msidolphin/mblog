package pers.msidolphin.mblog.helper;

import java.util.Map;

/**
 * Created by msidolphin on 2017/12/31.
 */
@SuppressWarnings("ALL")
public class MapsHelper {

	public static boolean isEmpty(Map target) {
		return target == null || target.isEmpty();
	}

	public static boolean isNotEmpty(Map target) {
		return !isEmpty(target);
	}

}
