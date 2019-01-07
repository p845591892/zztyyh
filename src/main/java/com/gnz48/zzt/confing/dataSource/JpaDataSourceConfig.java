package com.gnz48.zzt.confing.dataSource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**   
* Copyright: Copyright (c) 2018 LanRu-Caifu
* 
* @ClassName: JpaDataSourceConfig.java
* @Description: Jpa配置
*
* @version: v1.0.0
* @author: JuFF_白羽
* @date: 2018年5月22日 下午10:08:15 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 
*/
@Configuration
@EnableTransactionManagement // 开启注解事务管理
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", // 实体类工厂依赖
						transactionManagerRef = "transactionManager", // 事务依赖
						basePackages = "com.gnz48.zzt.repository") //repository类所在的包 
public class JpaDataSourceConfig {
	
	@Autowired
	private JpaProperties jpaProperties;
	
	@Autowired
    private DataSource dataSource;
	
	/*
	 * 通过LocalContainerEntityManagerFactoryBean来获取EntityManagerFactory实例
	 */
	@Bean(name = "entityManagerFactoryBean")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder){
		return builder
				.dataSource(dataSource)// 设置使用的数据源
				.properties(jpaProperties.getHibernateProperties(dataSource))// 设置属性
				.packages("com.gnz48.zzt.entity")// 设置实体类所在的包
				.persistenceUnit("testPersistenceUnit")// 持久性单元的名称,如果只构建一个EntityManagerFactory，可以省略它，但是如果在同一个应用程序中有多个，应该给它们起不同的名称。
				.build();
		// 不要在这里直接获取EntityManagerFactory
	}
	
	/*
	 * EntityManagerFactory类似于Hibernate的SessionFactory,mybatis的SqlSessionFactory
     * 总之,在执行操作之前,我们总要获取一个EntityManager,这就类似于Hibernate的Session,mybatis的sqlSession.
	 */
	@Bean(name = "entityManagerFactory")
	@Primary
	public EntityManagerFactory entityManagerFactory(EntityManagerFactoryBuilder builder){
		return this.entityManagerFactoryBean(builder).getObject();
	}
	
	/*
	 * 配置事务管理器
	 */
	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder){
		return new JpaTransactionManager(this.entityManagerFactory(builder));
	}

}
