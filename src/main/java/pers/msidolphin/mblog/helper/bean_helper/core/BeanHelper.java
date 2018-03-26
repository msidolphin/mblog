package pers.msidolphin.mblog.helper.bean_helper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.msidolphin.mblog.exception.AbstractApplicationException;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.helper.StringHelper;
import pers.msidolphin.mblog.helper.bean_helper.reflect.ReflectHelper;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * JavaBean工具类 
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 * @createTime 2017-12-1 下午05:21:23
 */
@SuppressWarnings("ALL")
public final class BeanHelper {

	private static Logger log 						= 	LoggerFactory.getLogger(BeanHelper.class);

	/**
	 * 默认的字符串分隔符
	 */
	private static final String DEFAULT_SPLITOR 	= 	",";

	/**
	 * 字符串分隔符
	 */
	private static String stringSplitor 			= 	DEFAULT_SPLITOR;

	/**
	 * 是否从资源文件中读取
	 */
	private static boolean isReadResource 			= 	false;

	/**
	 * Map转换为Java Bean
	 * @param map			数据源map
	 * @param requiredType	待转换的类型
	 * @param <T>
	 * @return T
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> requiredType) {
		Object bean = null;
		try {
			bean = requiredType.newInstance();
			mapToBean(map, bean);
			return (T) bean;
		} catch (InstantiationException e) {
			if(log.isErrorEnabled()) {
				log.error("实例化{}出错，该类是否为抽象类、接口？", requiredType.getName());
			}
			throw new AbstractApplicationException(e);
		} catch (IllegalAccessException e) {
			if(log.isErrorEnabled()) {
				log.error("没有{}构造函数的访问权限，请检查该类的构造方法访问修饰符是否为public",
						requiredType.getName());
			}
			throw new AbstractApplicationException(e);
		}

	}

	/**
	 * Map转换为Java Bean
	 * @param map 		数据源map
	 * @param bean		目标java bean
	 * @return bean
	 */
	public static Object mapToBean(Map<?, ?> map, Object bean) {
		//获取setter方法
		Map<String, Method> methdos = ReflectHelper.getSetMethods(bean.getClass());
		if(map != null && bean != null) {
			Class<?> clazz = bean.getClass();
			Map<String, Field> fieldMaps = ReflectHelper.getAllFields(clazz);
			for(Map.Entry<String, Field> entry : fieldMaps.entrySet()) {
				String name = entry.getKey();
				//以属性名到map中获取值
				Object value = map.get(name);
				if(value != null) {
					Field field = entry.getValue();
					//类型转化
					value = ConverterHelper.convertToNewValue(field.getType(), value);
					field.setAccessible(true);
					//获取setter方法
					String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
					Method method = methdos.get(methodName);
					if(method == null) {
						if(log.isWarnEnabled()) {
							log.warn("没有{}属性对应的setter方法", field.getName());
						}
						continue;
					}
					try {
						method.invoke(bean, value);
					} catch (IllegalArgumentException e) {
						if(log.isErrorEnabled()) {
							log.error("非法参数异常:{}", e.getMessage());
						}
						throw new AbstractApplicationException(e);
					} catch (IllegalAccessException e) {
						if(log.isErrorEnabled()) {
							log.error("没有访问{}属性的权限", field.getName());
						}
						throw new AbstractApplicationException(e);
					} catch (InvocationTargetException e) {
						if(log.isErrorEnabled()) {
							log.error("调用{}属性的setter方法出错", field.getName());
						}
						throw new AbstractApplicationException(e);
					}
				}
			}
		}
		return bean;
	}
	
	public static Object setProperty(Object bean, String propertyName, Object... value) {
		//获取setter方法
		Method setter = ReflectHelper.getSetterByName(bean.getClass(), propertyName);
		if(setter != null) {
			try {
				setter.setAccessible(true);
				setter.invoke(bean, value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		return bean;
	}
	
	public static Object copyProperties(Object dest, Object orig) {
		return dest;
	}

	/**
	 * 将HttpServletRequest请求参数转换为JavaBean
	 * @param request		HttpServletRequest
	 * @param requiredType	待转换的JavaBean类型
	 * @param <T>
	 * @return T
	 */
	public static <T> T requestToBean(HttpServletRequest request, Class<T> requiredType) {
		if(request == null && requiredType == null) {
			return null;
		}
		//<String, String[]> 因为checkbox可能会选择多个值，从
		Map<String, String[]> parameterMaps = request.getParameterMap();
		if(isReadResource) {
			//从资源文件中读取分隔符
			stringSplitor = PropertiesHelper.getValue("beanhelper.string.splitor");
			if(stringSplitor == null) {
				stringSplitor = DEFAULT_SPLITOR;
			}
		}
		Map<String, String> newMaps = new HashMap<>();
		for(String key : parameterMaps.keySet()) {
			String[] value = parameterMaps.get(key);
			String newValue;
			if(value.length > 1) {
				//使用分隔符将字符串连接起来
				newValue = StringHelper.concatStringArray(value, stringSplitor);
			}else {
				newValue = value[0];
			}
			newMaps.put(key, newValue);
		}
		return mapToBean(newMaps, requiredType);
	}

	public static void setIsReadResource(boolean isReadResource) {
		BeanHelper.isReadResource = isReadResource;
	}

	public static void noting(){}
}