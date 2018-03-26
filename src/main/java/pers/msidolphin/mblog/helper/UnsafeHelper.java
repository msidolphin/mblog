package pers.msidolphin.mblog.helper;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by msidolphin on 2018/1/3.
 */
public class UnsafeHelper {

	private static final Unsafe unsafe;

	/**对象头部的大小 */
	private static final int OBJECT_HEADER_SIZE = 8;
	/**对象占用内存的最小值*/
	private static final int MINIMUM_OBJECT_SIZE = 8;
	/**对象按多少字节的粒度进行对齐*/
	private static final int OBJECT_ALIGNMENT = 8;

	static  {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (Exception ex) { throw new Error(ex); }
	}


	public static long sizeof(Class<?> type) {
		if(type.getClass().isPrimitive()) {
			//基本数据类型的处理
		}else if(type.getClass().isArray()) {


		}else if(type.getClass().isAnnotation()) {

		}else if(type.getClass().isInterface()) {

		}else if(type.getClass().isEnum()) {

		}
		return 0;
	}
}
