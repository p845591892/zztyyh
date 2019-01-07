package com.gnz48.zzt.confing.dataSource;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: MyBatis配置类
 * @author JuFF_白羽
 * @date 2018年9月26日 上午10:53:36
 */
@Configuration
public class MyBatisDataSourceConfig {
	
	@Autowired
    private DataSource dataSource;
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis")
	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		return factoryBean;
	}

}
