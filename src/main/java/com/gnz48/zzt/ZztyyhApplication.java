package com.gnz48.zzt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = { 
		DataSourceAutoConfiguration.class , // 禁用默认数据源连接池
		SecurityAutoConfiguration.class,// 禁用自动安全配置
}) 
public class ZztyyhApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZztyyhApplication.class, args);
	}

	/**
	 * 该静态代码块禁用无头环境，防止 new Robot() 时报 java.awt.AWTException: headlessenvironment
	 */
	static {
		System.out.println("Set java.awt.headless=false.");
		System.setProperty("java.awt.headless", "false");
	}

}
