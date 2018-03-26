package pers.msidolphin.mblog.helper.bean_helper.reflect;

import pers.msidolphin.mblog.helper.Assert;
import pers.msidolphin.mblog.helper.Cache;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 反射工具类
 * <p>使用了ConcurrentMap作为缓存,单纯使用并发容器虽然拥有更好的并发行为，可以避免客户端加锁带来的串行性<p>
 * <p>但是仍然存在不足之处：当多个线程同时调用本方法时，可能出现计算得到相同的值
 * 如果一个线程已经正在进行计算，其他的线程并不知道，那么很可能就会重复这个计算</p>
 * <p>期望达到的目标：当线程A进行计算f(x)的时候，这样当另一个线程在查询f(x)的时候，能够达到最高效的方式是：
 * 等待线程A计算f(x)完毕，再去查询f(x)，而不是重复进行计算</p>
 * <p>FutureTask类基本能够实现这个功能。FutureTask表示一个计算的过程，这个过程可能已经计算完成，也可能正在
 * 进行。如果结果可用，那么FutureTask.get将立即返回结果，否则它会一直阻塞，直到结果计算出来再将其返回</p>
 * @author yangyu91575
 * @createTime 2017-11-27 下午01:20:13
 * @modifier 
 * @modifyDescription 描述本次修改内容
 */
@SuppressWarnings("ALL")
public class ReflectHelper implements Serializable{

	private static final Cache<String, Map<String, Field>> 	FIELD_CACHE 	= 	new Cache<>();
	private static final Cache<String, Map<String, Method>> METHOD_CACHE 	= 	new Cache<>();
	private static final Cache<String, Map<String, Method>> SETTER_CACHE	= 	new Cache<>();


	public static Map<String, Method> getSetMethods(final Class<?> clazz) {
		return SETTER_CACHE.get(clazz.getName(), ()->{
			double startTime = System.currentTimeMillis();

			//获取所有的方法
			Map<String, Method> methods = getAllMethods(clazz);
			Map<String, Method> methodMaps = new HashMap<String, Method>();
			for (Map.Entry<String, Method> entry : methods.entrySet()) {
				String name = entry.getKey();
				if(name.startsWith("set")) {
					//只获取setter方法
					methodMaps.put(name, entry.getValue());
				}
			}
			double endTime = System.currentTimeMillis();
			System.out.println("获取setter耗时: " + (endTime - startTime) + "ms");
			return methodMaps.size() > 1 ? methodMaps : null;
		});
	}
	
	public static Map<String, Method> getAllMethods(final Class<?> clazz) {
		return  METHOD_CACHE.get(clazz.getName(), ()->{
			double startTime = System.currentTimeMillis();
			Set<Method> methodSet = new HashSet<>();
			Class<?> cls = clazz;
			String className = cls.getName();
			for(; cls != null; cls = cls.getSuperclass()) {
				Method[] methods = cls.getDeclaredMethods();
				for(Method method : methods) {
					if(!methodSet.contains(method)) {
						//如果派生类覆盖了方法，以派生类为准
						methodSet.add(method);
					}
				}
			}
			Map<String, Method> methodMap = new HashMap<String, Method>();
			for(Method method : methodSet) {
				methodMap.put(method.getName(), method);
			}

			double endTime = System.currentTimeMillis();
			System.out.println("获取methods耗时: " + (endTime - startTime) + "ms");
			return methodMap.size() > 1 ? methodMap : null;
		});
	}
	
	public static Map<String, Field> getAllFields(final Class<?> clazz) {
		final String className = clazz.getName();
		return FIELD_CACHE.get(className, ()->{
			double startTime = System.currentTimeMillis();

			Set<Field> fieldSet = new HashSet<Field>();
			for(Class<?> clz = clazz ;clz != null ; clz = clz.getSuperclass()) {
				Field[] fields = clz.getDeclaredFields();
				for(Field field : fields) {
					fieldSet.add(field);
				}
			}
			Map<String, Field> fieldMap = new HashMap<String, Field>();
			for(Field field : fieldSet) {
				fieldMap.put(field.getName(), field);
			}
			double endTime = System.currentTimeMillis();
			System.out.println("获取field耗时: " + (endTime - startTime) + "ms");
			return fieldMap.size() > 1 ? fieldMap : null;
		});

	}

	public static Method getSetterByName(Class<?> type, String fieldName) {
		Map<String, Method> setterMap = getSetMethods(type);
		String setterName = getSetterMethodName(fieldName);
		if(setterMap != null) {
			return setterMap.get(setterName);
		}
		return null;
	}

	public static String getSetterMethodName(String fieldName) {
		Assert.notNull(fieldName, "属性名不能为NULL");
		String first = fieldName.charAt(0) + "";
		String remain = fieldName.substring(1);
		return "set" + first.toUpperCase() + remain;
	}

}
