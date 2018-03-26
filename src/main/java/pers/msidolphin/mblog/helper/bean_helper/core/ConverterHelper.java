package pers.msidolphin.mblog.helper.bean_helper.core;

import pers.msidolphin.mblog.helper.MapsHelper;
import pers.msidolphin.mblog.helper.bean_helper.converter.DateConverter;
import pers.msidolphin.mblog.helper.bean_helper.converter.DoubleConverter;
import pers.msidolphin.mblog.helper.bean_helper.converter.IntegerConverter;
import pers.msidolphin.mblog.helper.bean_helper.converter.LongConverter;
import pers.msidolphin.mblog.helper.bean_helper.converter.iface.Converter;
import pers.msidolphin.mblog.helper.bean_helper.reflect.PrimitiveEnum;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public final class ConverterHelper {

	public final static Map<Class<?>, Converter> converters = new HashMap();
	
	static {
		ConverterHelper.init();
	}
	
	private static void init() {
		//默认加载的Converter
		register(Double.class, new DoubleConverter());
		register(Date.class, new DateConverter());
		register(Integer.class, new IntegerConverter());
		register(Long.class, new LongConverter());
	}
	
	public static void register(Class<?> requiredType, Converter converter) {
		if(requiredType != null && converter != null) {
			ConverterHelper.converters.put(requiredType, converter);
		}
	}
	
	public static Converter<?> getConverter(Class<?> clazz) {
		return converters.get(clazz);
	} 
	
	public void setConverters(Map<String, Converter> map) throws ClassNotFoundException {
		if(MapsHelper.isNotEmpty(map)) {
			Collection<String> collection = map.keySet();
			for (String requiredType : collection) {
				converters.put(Class.forName(requiredType), map.get(requiredType));
			}
		}
	}
	
	/**
	 * <p>转换为新值</p>
	 * @param targetType
	 * @param oldValue
	 * @return
	 */
	public static <T> T convertToNewValue(Class<T> targetType, Object oldValue) {
		if(isInstance(targetType.getClass(), oldValue)) {
			//如果能够强制类型转换或者自动装箱
			return (T)oldValue;
		}
		//不能自动装箱或强制类型转化则需要进行转换
		Converter converter = ConverterHelper.getConverter(targetType);
		return converter == null ? (T)oldValue : (T)converter.convert(targetType, oldValue);
	}

	/**
	 * 判断目标对象否为指定类型派生类，实现类
	 * @param superType 待判断类型
	 * @param target	目标对象
	 * @return 布尔类型
	 */
	public static boolean isInstance(Class<?> superType, Object target) {
		return superType.isInstance(target);
	}
	
	/**
	 * <p>判断是否能够强制类型转化或者自动装箱</p>
	 * @param target
	 * @param source
	 * @return
	 */
	@Deprecated
	public static boolean canCast(Class<?> target, Class<?> source) {

		if(target == source) {
			return true;
		}

		//判断目标类型是否为源类型的父类
		for(Class<?> clz = source ; clz != null ; clz = clz.getSuperclass()) {
			if(clz.equals(target)) {
				return true;
			}
		}

		//如果目标类型是一个接口，则判断源类型是否实现该目标类型接口
		if(target.isInterface()) {
			for(Class cls : source.getInterfaces()) {
				if(cls.equals(target)) {
					return true;
				}
			}
		}

		//自动装箱判断
		if(source.isPrimitive() && !target.isPrimitive()) {
			//若源类型是基本数据类型，目标类型不是基本数据类型
			return PrimitiveEnum.getPrimitiveEnumByPrimitiveType(source)
					== PrimitiveEnum.getPrimitiveEnumByWrapperType(target);
		}

		if(target.isPrimitive() && !source.isPrimitive()) {
			//若目标类型是基本数据类型，源类型不能基本数据类型
			return PrimitiveEnum.getPrimitiveEnumByPrimitiveType(target)
					== PrimitiveEnum.getPrimitiveEnumByWrapperType(source);
		}

		return false;
	}

	/**
	 * 获取JAVA 基本数据类型的包装类
	 * int 		- 		java.lang.Integer
	 * long 	- 		java.lang.Long
	 * boolean 	- 		java.lang.Boolean
	 * short 	- 		java.lang.Short
	 * double 	- 		java.lang.Double
	 * float 	- 		java.lang.Float
	 * char 	- 		java.lang.Character
	 * void 	- 		java.lang.Void
	 * @param type
	 * @return
	 */
	@Deprecated
	public static Class<?> getPrimitiveWrapperType(Class<?> type) {
		if(type == null) {
			return null;
		}
		if(type.isPrimitive()) {
			//如果type是基本数据类型
			switch (PrimitiveEnum.getPrimitiveEnumByPrimitiveType(type)) {
				case INTEGER:
					return Integer.class;
				case LONG:
					return Long.class;
				case SHORT:
					return Short.class;
				case BYTE:
					return Byte.class;
				case DOUBLE:
					return Double.class;
				case FLOAT:
					return Float.class;
				case BOOLEAN:
					return Boolean.class;
				case CHARACTER:
					return Character.class;
				case VOID:
					return Void.class;
			}
		}
		return null;
	}
}
