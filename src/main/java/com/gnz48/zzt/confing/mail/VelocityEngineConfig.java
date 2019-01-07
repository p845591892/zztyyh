package com.gnz48.zzt.confing.mail;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 供邮件使用的模板引擎配置类
 * @author JuFF_白羽
 * @date 2019年1月4日 上午9:43:18
 */
@Configuration
public class VelocityEngineConfig {

	@Bean
	public VelocityEngine velocityEngine() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("input.encoding", "UTF-8");
		properties.setProperty("output.encoding", "UTF-8");
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine velocityEngine = new VelocityEngine(properties);
		return velocityEngine;
	}

}
