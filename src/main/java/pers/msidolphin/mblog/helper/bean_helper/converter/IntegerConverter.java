package pers.msidolphin.mblog.helper.bean_helper.converter;

import pers.msidolphin.mblog.helper.bean_helper.converter.abst.AbstractConverter;

@SuppressWarnings("ALL")
public class IntegerConverter extends AbstractConverter<Integer>{

	@Override
	public Integer convert(Class<Integer> type, Object source) {
		if(source == null) {
			throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
		}
		if(source instanceof Integer) {
			return type.cast(source);
		}
		return cast(source);
	}

	public Integer cast(Object source) {
		int newValue;
		//String转换为Integer
		if(source instanceof String) {
			return cast((String) source);
		}

		//Double类型强转为int
		if(source instanceof Double) {
			double num = (Double) source;
			newValue = (int) num;
			warn(num, newValue);
			return newValue;
		}

		//Long类型转换为int
		if(source instanceof Long) {
			long num = (Long) source;
			newValue = (int) num;
			warn(num, newValue);
			return (int) num;
		}
		throw new ClassCastException("不能将"+ source +"转换为" + Integer.class.getName() + "类型...");
	}

	/**
	 * 将
	 * @param source
	 * @return
	 */
	public Integer cast(String source) {
		String newValue = source;
		int index = source.indexOf('.');
		if(index != -1) {
			//截取小数点前的数值
			newValue = source.substring(0, index);
			warn(source, newValue);
		}
		return Integer.parseInt(newValue);
	}





}
