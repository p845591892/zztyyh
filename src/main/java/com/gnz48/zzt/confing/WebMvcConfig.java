package com.gnz48.zzt.confing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * webmvc配置类
 * @author shiro
 */
@Configuration
@ConfigurationProperties(prefix = "web")
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(WebMvcConfig.class);
	
	private String uploadPath;
	
	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("设置静态资源");
		registry.addResourceHandler("/source/**").addResourceLocations(uploadPath);
		log.info("/source/**映射：{}", uploadPath);
		
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/", "classpath:/templates/");
		log.info("/**映射：{}, {}", "classpath:/static/", "classpath:/templates/");
		
        super.addResourceHandlers(registry);
	}

}
