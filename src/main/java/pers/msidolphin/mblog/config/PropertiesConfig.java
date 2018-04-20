package pers.msidolphin.mblog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import pers.msidolphin.mblog.helper.PropertiesHelper;

/**
 * Created by msidolphin on 2018/3/31.
 */
@Order(4)
@Configuration
@EnableAutoConfiguration
public class PropertiesConfig {

	@Bean
	public PropertiesHelper getPropertiesHelper() {
		PropertiesHelper propertiesHelper = new PropertiesHelper();
		propertiesHelper.setResourceLocation("classpath:system.properties");
		return propertiesHelper;
	}
}
