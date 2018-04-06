package pers.msidolphin.mblog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pers.msidolphin.mblog.common.annotation.Validation;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.BeanValidatorHelper;
import pers.msidolphin.mblog.helper.RequestHolder;

import javax.xml.validation.Validator;
import java.util.Map;

/**
 * 统一参数校验
 * Created by msidolphin on 2018/4/2.
 */
@Aspect
@Component
public class ValidateAspect {

	private Logger log = LoggerFactory.getLogger(Validator.class);

	@Pointcut("execution(public * pers.msidolphin.mblog.*.controller.*.*(..))")
	public void pointcut(){};

	@Before("pointcut()")
	public void validate(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for(Object arg : args) {
			if(arg != null) {
				Validation validation = arg.getClass().getAnnotation(Validation.class);
				if(validation != null) {
					log.info("{}开始进行参数校验...", arg);
					Map<String, String> validateRes = BeanValidatorHelper.validate(arg);
					if(!validateRes.isEmpty()) {
						log.warn("{}参数校验失败", arg);
						throw new InvalidParameterException("parameter invalidate", validateRes);
					}
				}
			}
		}
	}

}
