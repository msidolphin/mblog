package pers.msidolphin.mblog.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 上下文工具类 由Spring管理
 * 实现ApplicationContextAware接口
 * 通过它Spring容器会自动把上下文环境对象调用ApplicationContextAware接口中的setApplicationContext方法
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */

@SuppressWarnings("ALL")
public class SpringApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(String beanId, Class<T> requiredType) {
		if(applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanId, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) {
		if(applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(requiredType);
	}
}
