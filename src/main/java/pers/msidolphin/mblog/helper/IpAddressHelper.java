package pers.msidolphin.mblog.helper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * IP地址帮助类
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@Slf4j
@SuppressWarnings("ALL")
public class IpAddressHelper {

	/**
	 * 获取客户端IP地址
	 * 在大部分情况中，通过<code>request.getRemoteAddr()</code>就能够获取到客户端真实IP地址
	 * 但是如果通过了Apache、Squid等反向代理软件不能够获取到客户端真实IP地址了
	 * 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，
	 * 服务器端应用也无法直接通过转发请求的地址返回给客户端。但是在转发请求的HTTP头信息中，
	 * 增加了X－FORWARDED－FOR信息。用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址。
	 * 但是forwarded_for选项在某些代理中可以被配置为off的，此时获取的x-forwarded-for值为
	 * unknown，所以要对这种情况进行过滤
	 * @param request	客户端请求
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (log.isInfoEnabled()) {
			log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (log.isInfoEnabled()) {
					log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

}
