package pers.msidolphin.mblog.helper.bean_helper.converter;

import pers.msidolphin.mblog.helper.bean_helper.converter.abst.AbstractConverter;


public class LongConverter extends AbstractConverter<Long> {

	@Override
	public Long convert(Class<Long> type, Object source) {
		if(source == null) {
			throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
		}
		if(source instanceof Long) {
			return type.cast(source);
		}
		//String => Long
		if(source instanceof String) {
			String num = (String) source;
			return Long.parseLong(num);
		}
		throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
	}

	
	
}
