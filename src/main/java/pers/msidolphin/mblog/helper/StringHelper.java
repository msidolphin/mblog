package pers.msidolphin.mblog.helper;

/**
 * 字符串工具类
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public final class StringHelper {

	private static final String DEFAULT_SPLITOR = ",";

	/**
	 * 判断字符串是否为空串
	 * 空串的定义：
	 * 1、该字符串为NULL
	 * 2、该字符串为空字符串
	 * 3、该字符串都为空白字符
	 * @param target	目标字符串
	 * @return 布尔类型
	 */
	public static boolean isBlank(String target) {
		if(target == null || target.length() == 0) {
			return true;
		}
		if("".equals(target.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为空串
	 * 空串的定义：
	 * 1、该字符串为NULL
	 * 2、该字符串为空字符串
	 * 3、该字符串都为空白字符
	 * @param target	目标字符串
	 * @return 布尔类型
	 */
	public static boolean isNotBlank(String target) {
		return !isBlank(target);
	}

	/**
	 * 判断字符串是否为空串
	 * 空串的定义：
	 * 1、该字符串为NULL
	 * 2、该字符串为空字符串
	 * 3、该字符串都为空白字符
	 * @param target	目标字符串
	 * @return 布尔类型
	 */
	public static boolean isEmpty(String target) {
		return isBlank(target);
	}

	/**
	 * 判断字符串是否为空串
	 * 空串的定义：
	 * 1、该字符串为NULL
	 * 2、该字符串为空字符串
	 * 3、该字符串都为空白字符
	 * @param target	目标字符串
	 * @return 布尔类型
	 */
	public static boolean isNotEmpty(String target) {
		return isNotBlank(target);
	}

	/**
	 * 获取文件路径
	 * 如果文件在类路径下可以加上classpath:前缀
	 * @param fileName	文件名
	 * @return 文件路径字符串
	 */
	public static String getResourcePath(String fileName) {
		if(isBlank(fileName)) {
			return null;
		}
		if(fileName.startsWith("classpath:")) {
			//类路径下
			int index = -1;
			for (int i = 10; i < fileName.length(); i++) {
				if(fileName.charAt(i) != ' ') {
					index = i;
					break;
				}
			}
			return getResourcePathInClasspath(fileName.substring(index));
		}
		return fileName;
	}

	/**
	 * 在类路径下获取该文件路径
	 * @param fileName 文件名
	 * @return 文件路径字符串
	 */
	public static String getResourcePathInClasspath(String fileName) {
		if (isBlank(fileName)) {
			return null;
		}
		String path = StringHelper.class.getClassLoader().getResource(fileName).toString();
		return path.length() > 6 ? path.substring(6) : null;
	}

	/**
	 * 使用指定的分隔符连接字符串数组，如果分隔符为空，则使用默认的逗号分隔符
	 * @param strings	目标字符串数组
	 * @param splitor	分隔字符串
	 * @return 字符串
	 */
	public static String concatStringArray(String[] strings, String splitor) {
		if(strings == null) {
			return null;
		}
		//如果分隔符为空，则使用默认的分隔符
		if(isBlank(splitor)) {
			splitor = DEFAULT_SPLITOR;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			if(i != strings.length - 1) {
				stringBuilder.append(strings[i]).append(splitor);
			}else {
				stringBuilder.append(strings[i]);
			}
		}
		return stringBuilder.toString();
	}

}
