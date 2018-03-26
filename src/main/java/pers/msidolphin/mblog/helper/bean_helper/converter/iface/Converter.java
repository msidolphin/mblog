package pers.msidolphin.mblog.helper.bean_helper.converter.iface;

/**
 * 
 * Convert to T
 * @author yangyu91575
 * @createTime 2017-11-29 上午09:25:28
 * @modifier 
 * @modifyDescription 描述本次修改内容
 * @see
 */
@SuppressWarnings("ALL")
public interface Converter<T> {

	public T convert(Class<T> targtType, Object source);

	public T cast(Object source);
//
	public T cast(String source);

}
