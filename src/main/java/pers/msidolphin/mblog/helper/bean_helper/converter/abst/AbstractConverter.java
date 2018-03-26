package pers.msidolphin.mblog.helper.bean_helper.converter.abst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.msidolphin.mblog.helper.bean_helper.converter.iface.Converter;

/**
 * Created by msidolphin on 2018/1/1.
 */
public abstract class AbstractConverter<T> implements Converter<T> {

	protected Logger log = LoggerFactory.getLogger(AbstractConverter.class);

	public void warn(Object oldValue, Object newValue) {
		if (log.isWarnEnabled() && oldValue.getClass() != newValue.getClass()) {
			log.warn("强制类型转换警告：{}:{} => {}:{} ", oldValue.getClass().getName(), oldValue,
					newValue.getClass().getName(), newValue);
		}
	}

	@Override
	public T cast(Object source) {
		return null;
	}

	@Override
	public T cast(String source) {
		return null;
	}
}
