package pers.msidolphin.mblog.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.msidolphin.mblog.exception.AbstractApplicationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by msidolphin on 2017/12/31.
 */
@SuppressWarnings("ALL")
public class PropertiesHelper {

	private static Logger log 					= 	LoggerFactory.getLogger(PropertiesHelper.class);

	private static Properties properties 		= 	new Properties();

	//private static Map<String, Properties> propertiesMap = new HashMap<>();

	//默认资源路径
	private static String resourceLocation 		= 	null;

	private static String defaultLocation 		= 	StringHelper.getResourcePathInClasspath("system.properties");

	/**
	 * 设置资源文件路径，重新加载资源文件
	 * @param resourceLocation 资源文件路径
	 * @return
	 */
	public PropertiesHelper setResourceLocation(String resourceLocation) {
		this.resourceLocation = StringHelper.getResourcePath(resourceLocation);
		try {
			log.info("加载资源文件: {}", resourceLocation);
			if(StringHelper.isBlank(resourceLocation)) {
				this.resourceLocation = defaultLocation;
			}
			properties.load(new FileInputStream(new File(this.resourceLocation)));
		}catch (IOException e) {
			if(log.isErrorEnabled()) {
				log.error("加载资源文件出错...\n{}", e.getMessage());
			}
			throw new AbstractApplicationException("加载资源文件"+ resourceLocation +"出错", e);
		}
		return this;
	}

	public static String getValue(String key) {
		if(StringHelper.isBlank(key)) {
			return null;
		}
		return properties.getProperty(key);
	}
}
