package pers.msidolphin.mblog.helper;

import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 缓存当前用户和请求
 * Created by msidolphin on 2018/3/31.
 */
public class RequestHolder {

	/* ThreadLocal保证了各自进程数据的独立性*/

	//保存前台用户信息
	private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

	//保存后台用户信息
	private static final ThreadLocal<User> adminHolder = new ThreadLocal<>();

	//保存后台请求token信息 即cookie
	private static final ThreadLocal<String> adminRequestToken = new ThreadLocal<>();

	private static final ThreadLocal<String> portalRequestToken = new ThreadLocal<>();

	//本次请求
	private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

	public static void add(User user) {
		userHolder.set(user);
	}

	public static void addAdminUser(User user) {adminHolder.set(user);}

	public static void add(HttpServletRequest request) {
		requestHolder.set(request);
	}

	public static void add(String token) {adminRequestToken.set(token);}

	public static void addPortalRequestToken(String token) {portalRequestToken.set(token);}

	public static User getCurrentUser() {
		return userHolder.get();
	}

	public static User getCurrentAdmin() {return adminHolder.get();}

	public static HttpServletRequest getCurrentRequest() {
		return requestHolder.get();
	}

	public static String getRequestToken() {return adminRequestToken.get();}

	public static String getPortalRequestToken() {return portalRequestToken.get();}

	public static void removeCurrentRequest() {
		requestHolder.remove();
	}

	public static void removeCurrentUser() {
		userHolder.remove();
	}

	public static void removeCurrentAdmini() {adminHolder.remove();}

	public static void removeAdminRequestToken() {adminRequestToken.remove();}

	public static void removePortalRequestToken() {portalRequestToken.remove();}

	public static void removeAll() {
		removeCurrentRequest();
		removeCurrentUser();
		removeCurrentAdmini();
		removeAdminRequestToken();
		removePortalRequestToken();
	}
}
