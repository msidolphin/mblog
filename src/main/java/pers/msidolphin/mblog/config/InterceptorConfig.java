package pers.msidolphin.mblog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pers.msidolphin.mblog.interceptor.HttpInterceptor;

/**
 * Created by msidolphin on 2018/3/31.
 */
@Order(3)
@Configuration
@EnableAutoConfiguration
public class InterceptorConfig implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getHttpInterceptor());
	}

	//采用如下方式解决拦截器无法注入的问题
	@Bean
	public HttpInterceptor getHttpInterceptor() {
		return new HttpInterceptor();
	}

}
