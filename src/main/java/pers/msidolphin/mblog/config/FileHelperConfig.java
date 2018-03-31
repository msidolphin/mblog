package pers.msidolphin.mblog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.msidolphin.mblog.helper.FileHelper;

/**
 * Created by msidolphin on 2018/3/31.
 */
@Configuration
@EnableAutoConfiguration
public class FileHelperConfig {

	@Bean
	public FileHelper getFileHelper() {
		FileHelper fileHelper = new FileHelper("127.0.0.1", "admin", "123456", "F:/images/");
		return fileHelper;
	}
}
