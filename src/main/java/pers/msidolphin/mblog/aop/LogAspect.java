package pers.msidolphin.mblog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pers.msidolphin.mblog.helper.JsonHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 日志增强
 * Created by msidolphin on 2018/3/26.
 */
@Aspect
@Component
public class LogAspect {

	private Logger log = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut("execution(public * pers.msidolphin.mblog.*.controller.*.*(..))")
	public void controller(){}


	@Around("controller()")
	public Object around(ProceedingJoinPoint joinPoint){
		Object returnVal = null;
		String head = "=========调用" + joinPoint.getTarget().getClass().getName()+"控制器=========";
		log.info(head);
		Long startTime = System.currentTimeMillis();
		log.info("请求方法: {}", joinPoint.getSignature().getName() + "()");
		log.info("方法描述: {}", joinPoint);
		log.info("请求参数: ");
		try {
			printMethodNameAndArgus((MethodSignature) joinPoint.getSignature(), joinPoint.getArgs());
			returnVal = joinPoint.proceed();
			Long endTime = System.currentTimeMillis();
			log.info("返回参数: {}", JsonHelper.object2String(returnVal));
			log.info("请求耗时: {}ms", endTime - startTime);

		} catch (Throwable throwable) {
			log.info("未能正常执行: {}", throwable.getMessage());
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < head.length() ; ++i) {
			sb.append("=");
		}
		log.info(sb.toString());
		return returnVal;
	}

	private void printMethodNameAndArgus(MethodSignature signature, Object[] objects) throws IOException{
		signature.getParameterNames();
		signature.getParameterTypes();
		String[] names = signature.getParameterNames();
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < objects.length ; ++i) {
			sb.append(names[i] + " : " + objects[i] + " | ");
		}
		String output = sb.toString();
		log.info(output.substring(0, output.lastIndexOf("|")));
	}
}
