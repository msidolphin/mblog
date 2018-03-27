package pers.msidolphin.mblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//扫描mybatis mapper包路径
@MapperScan(basePackages = "pers.msidolphin.mblog.model.mapper")
@EnableTransactionManagement //开启注解事务管理，等同于xml配置方式的<tx:annotation-driven/>
public class MblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MblogApplication.class, args);
	}
}
