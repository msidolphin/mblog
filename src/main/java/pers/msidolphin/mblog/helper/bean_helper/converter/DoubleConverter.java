package pers.msidolphin.mblog.helper.bean_helper.converter;


import pers.msidolphin.mblog.helper.bean_helper.converter.abst.AbstractConverter;

/**
 * 
 * Double Converter
 * @author yangyu91575
 * @createTime 2017-11-29 上午10:43:24
 * @modifier 
 * @modifyDescription 描述本次修改内容
 * @see
 */
public class DoubleConverter extends AbstractConverter<Double> {

	@Override
	public Double convert(Class<Double> type, Object source) {
		if(type == null || source == null) {
			throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
		}
		
		if(source instanceof Double) {
			return type.cast(source);
		}
		
		//String -> Double
		if(source instanceof String) {
			//判断类型
			String str = (String) source;
			return Double.parseDouble(str);
		}
		
		throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
	}
}
