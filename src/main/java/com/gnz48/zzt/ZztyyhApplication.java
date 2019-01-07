package com.gnz48.zzt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }) // 禁用默认数据源连接池
public class ZztyyhApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZztyyhApplication.class, args);
	}

	/**
	 * 该静态代码块禁用无头环境，防止 new Robot() 时报 java.awt.AWTException: headlessenvironment
	 */
	static {
		System.setProperty("java.awt.headless", "false");
	}

}
