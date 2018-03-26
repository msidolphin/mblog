package pers.msidolphin.mblog.helper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * java bean 校验合法性帮助类 推荐配合拦截器、Filter使用
 *
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class BeanValidatorHelper {

	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	/***
	 * 校验普通java bean
	 * @param target    目标对象
	 * @param classes    classes
	 * @param <T>		目标对象类型
	 * @return 错误结果集, Map<String String>
	 */
	private static <T> Map<String, String> validateBean(T target, Class... classes) {
		if (target != null) {
			Validator validator = validatorFactory.getValidator();
			Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(target, classes);
			LinkedHashMap<String, String> validateResult = Maps.newLinkedHashMap();
			for (ConstraintViolation<T> tConstraintViolation : constraintViolationSet) {
				//K： 字段名称	V：错误原因
				validateResult.put(tConstraintViolation.getPropertyPath().toString(), tConstraintViolation.getMessage());
			}
			return validateResult;
		}
		return Collections.emptyMap();
	}


	/**
	 * 检验List类型，List元素为java bean，如果一个校验失败，后续不再进行
	 *
	 * @param collection 待校验集合
	 * @return 错误集合 , Map<String String> K：类型名	V：错误集合
	 */
	private static Map<String, String> validateList(Collection<?> collection) {
		Preconditions.checkNotNull(collection);    //如果为null 抛出空指针异常
		Map<String, String> errors = Maps.newHashMap();
		for (Object o : collection) {
			errors = validateBean(o, o.getClass().getInterfaces());
			if (errors.isEmpty()) {
				return errors;
			}
		}
		return errors;
	}

	/**
	 * 校验数组、集合类型、普通JavaBean
	 *
	 * @param objects 校验对象
	 * @return 错误集合, Map<String String>
	 */
	public static Map<String, String> validate(Object... objects) {
		Preconditions.checkNotNull(objects);
		Object first = objects[0];
		if (objects.length > 1) {
			return validateList(Lists.asList(first, Arrays.copyOfRange(objects, 1, objects.length)));
		}
		return validateBean(first, first.getClass().getInterfaces());
	}

	/**
	 * 统一校验接口
	 *
	 * @param target 目标对象
	 * @return 错误集合, Map<String String>
	 */
	public static Map<String, String> validate(Object target) {
		if (target instanceof Collection) {
			Collection collection = (Collection) target;
			return validate(((Collection) target).toArray());
		}
		//校验普通javaBean
		return validateBean(target);
	}
}
