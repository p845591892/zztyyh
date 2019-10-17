//package com.gnz48.zzt.confing.dataSource;
//
//import org.crazycake.shiro.RedisManager;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.gnz48.zzt.util.StringUtil;
//
//@Configuration
//public class RedisConfig {
//	
//	@Value("${redis.host}")
//	private String host;
//	
//	@Value("${redis.timeout}")
//	private int timeout;
//	
//	@Value("${redis.password}")
//	private String password;
//	
//	/**
//	 * redis管理器
//	 */
//	@Bean
//    public RedisManager redisManager() {
//		RedisManager redisManager = new RedisManager();
//		if (StringUtil.isNotBlank(host)) {
//			redisManager.setHost(host);
//		}
//		if (StringUtil.isNotBlank(password)) {
//			redisManager.setPassword(password);
//		}
//		if (timeout != 0) {
//			redisManager.setTimeout(timeout);
//		}
//        return redisManager;
//    }
//
//}
