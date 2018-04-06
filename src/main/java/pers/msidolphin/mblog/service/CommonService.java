package pers.msidolphin.mblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.helper.RedisHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.object.po.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by msidolphin on 2018/4/6.
 */
@Service
public class CommonService {

	private static final Logger log = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	public RedisHelper redisHelper;

	@Autowired
	public PropertiesHelper propertiesHelper;

	/**
	 * 注销
	 * @param response {HttpServletResponse}
	 * @return
	 */
	public ServerResponse<?> logout(HttpServletResponse response) {
		HttpServletRequest request = RequestHolder.getCurrentRequest();
		User user = RequestHolder.getCurrentAdmin();
		if (user == null) return ServerResponse.success(); //用户从未登录过
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if (cookie.getName().equals(propertiesHelper.getValue("blog.admin.user.token.key"))) {
					//清除cookie
					cookie.setPath(propertiesHelper.getValue("blog.cookie.path"));
					cookie.setDomain(propertiesHelper.getValue("blog.cookie.domain"));
					//移除redis中用户缓存
					if(!redisHelper.remove(cookie.getValue())) {
						//不太可能哈
						log.warn("删除cookie {}失败", cookie.getValue());
					}
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					response.setHeader("Access-Control-Allow-Credentials", "true");
					response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

					return ServerResponse.success();
				}
			}
		}
		return ServerResponse.success();
	}

}
