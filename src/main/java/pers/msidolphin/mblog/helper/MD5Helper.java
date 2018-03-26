package pers.msidolphin.mblog.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Created by msidolphin on 2017/12/31.
 */
@SuppressWarnings("ALL")
public class MD5Helper {

	//日志
	private static Logger log 				= 	LoggerFactory.getLogger(MD5Helper.class);

	//十六进制
	private static final String[] hexDigits = 	{
			"0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
	};

	private MD5Helper() {}

	private static String byteArrayToHexString(byte[] b) {
		StringBuilder buffer = new StringBuilder();
		for (int index = 0; index < b.length; index++) {
			buffer.append(byteToHexString(b[index]));
		}
		return buffer.toString();
	}

	private static String byteToHexString(byte b) {
		short n = b;
		//如果是负数，则取反
		if (n < 0) {
			n += 256;
		}

		//byte高四位
		int d1 = (n & 0x00f0) >> 4;
		//byte低四位
		int d2 = n & 0x000f;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * @Description 返回大写MD5
	 * @param origin
	 * @return
	 */
	private static String md5Encode(String origin, String charset) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			//是否存在字符集
			if (charset == null || "".equals(charset)) {
				//如果不存在则使用系统默认
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			}else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
			}
		}catch (Exception exception) {
			log.error("md5 encoding error",exception);
		}
		return (resultString == null) ? null : resultString.toUpperCase();
	}

	public static String md5EncodeWithUtf8(String origin) {
		return md5Encode(origin + PropertiesHelper.getValue("salt"), "utf-8");
	}

}