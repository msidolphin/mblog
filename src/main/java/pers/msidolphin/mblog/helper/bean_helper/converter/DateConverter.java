package pers.msidolphin.mblog.helper.bean_helper.converter;
import pers.msidolphin.mblog.helper.bean_helper.converter.abst.AbstractConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("ALL")
public class DateConverter extends AbstractConverter<Date> {

	public final String[] format = new String[]{
			"yyyy-MM-dd", 
			"yyyy-MM-dd hh:mm:ss", 
			"yyyy/MM/dd", 
			"yyyy/MM/dd hh:mm:ss",
			"yyyyMMdd",
			"yyyy.MM.dd",
			"yyyy.MM.dd hh:mm:ss"
	};
	
	@Override
	public Date convert(Class<Date> type, Object source) {
		if(source == null) {
			if(log.isDebugEnabled()) {
				log.debug("不能将{}转换为{}类型", source, type.getName());
				throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
			}

		}
		
		if(source instanceof Date) {
			return type.cast(source);
		}
		
		//Long => Date
		if(source instanceof Long) {
			Long date = (Long) source;
			return new Date(date);
		}
		
		//String => Date
 		if(source instanceof String) {
 			String date = (String) source;
			for(int index = 0 ; index < format.length ; ++index) {
				try {
					if(date.length() != format[index].length()) {
						continue;
					}
					SimpleDateFormat sdf = new SimpleDateFormat(format[index]);
					return sdf.parse((String) source);
				}catch(ParseException e) {
					continue;
				}
			}			
		}
		if(log.isDebugEnabled()) {
			log.debug("不能将{}转换为{}类型", source, type.getName());
		}
 		throw new ClassCastException("不能将"+ source +"转换为" + type.getName() + "类型...");
	}

	
}
