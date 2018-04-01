package pers.msidolphin.mblog.helper;

import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 缓存当前用户和请求
 * Created by msidolphin on 2018/3/31.
 */
public class RequestHolder {

	// ThreadLocal保证了各自进程数据的独立性
	private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

	private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

	public static void add(User user) {
		userHolder.set(user);
	}

	public static void add(HttpServletRequest request) {
		requestHolder.set(request);
	}

	public static User getCurrentUser() {
		return userHolder.get();
	}

	public static HttpServletRequest getCurrentRequest() {
		return requestHolder.get();
	}

	public static void removeCurrentRequest() {
		requestHolder.remove();
	}

	public static void removeCurrentUser() {
		userHolder.remove();
	}

	public static void removeAll() {
		removeCurrentRequest();
		removeCurrentUser();
	}
}
