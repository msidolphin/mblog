package pers.msidolphin.mblog.helper.bean_helper.reflect;


/**
 * Created by msidolphin on 2018/1/1.
 */
@SuppressWarnings("ALL")
public enum PrimitiveEnum {

	INTEGER(int.class, Integer.class),
	LONG(long.class, Long.class),
	SHORT(short.class, Short.class),
	BYTE(byte.class, Byte.class),
	DOUBLE(double.class, Double.class),
	FLOAT(float.class, Float.class),
	BOOLEAN(boolean.class, Boolean.class),
	CHARACTER(char.class, Character.class),
	VOID(void.class, Void.class);
	private Class<?> type;
	private Class<?> wrapper;

	private PrimitiveEnum(Class<?> type, Class<?> wrapper) {
		this.type = type;
		this.wrapper = wrapper;
	}

	public Class<?> getType() {
		return type;
	}

	public Class<?> getWrapper() {
		return wrapper;
	}

	public static PrimitiveEnum getPrimitiveEnumByPrimitiveType(Class<?> type) {
		PrimitiveEnum[] primitiveEnums = PrimitiveEnum.values();
		for (PrimitiveEnum primitiveEnum : primitiveEnums) {
			if(primitiveEnum.getType() == type) {
				return primitiveEnum;
			}
		}
		return null;
	}

	public static PrimitiveEnum getPrimitiveEnumByWrapperType(Class<?> type) {
		PrimitiveEnum[] primitiveEnums = PrimitiveEnum.values();
		for (PrimitiveEnum primitiveEnum : primitiveEnums) {
			if(primitiveEnum.getWrapper() == type) {
				return primitiveEnum;
			}
		}
		return null;
	}


}
