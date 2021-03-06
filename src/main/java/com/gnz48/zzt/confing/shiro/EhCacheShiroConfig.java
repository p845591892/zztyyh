package com.gnz48.zzt.confing.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gnz48.zzt.confing.shiro.filter.ShiroAuthcFilter;
import com.gnz48.zzt.confing.shiro.filter.ShiroPermsFilter;
import com.gnz48.zzt.confing.shiro.listener.ShiroSessionListener;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * @Description: shiro配置类(EhCache)
 * @author JuFF_白羽
 * @date 2018年10月31日 下午5:29:30
 */
@Configuration
public class EhCacheShiroConfig {

	private Logger log = LoggerFactory.getLogger(EhCacheShiroConfig.class);

	/**
	 * @Description: 自定义Shiro域
	 */
	@Bean
	public MyShiroRealm myShiroRealm() {
		log.info("===============>> shiro-设置域");
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		return myShiroRealm;
	}

	/**
	 * EhCache管理器
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		log.info("===============>> shiro-设置EhCache缓存");
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache.xml");
		return ehCacheManager;
	}

	/**
	 * 配置会话ID生成器
	 * 
	 * @return
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件 MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO
	 * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 * 
	 * @return
	 */
	@Bean
	public SessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		// 使用ehCacheManager
		enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
		// 设置session缓存的名字 默认为 shiro-activeSessionCache
		enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		// sessionId生成器
		enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return enterpriseCacheSessionDAO;
	}

	/**
	 * session管理器
	 */
	@Bean
	public SessionManager sessionManager() {
		log.info("===============>> shiro-设置session");
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 配置监听
		Collection<SessionListener> listeners = new ArrayList<SessionListener>();
		listeners.add(new ShiroSessionListener());

		sessionManager.setSessionListeners(listeners);
		sessionManager.setGlobalSessionTimeout(21600000);// 全局会话超时
		sessionManager.setDeleteInvalidSessions(true);// 过期后调用SessionDAO的delete方法删除会话
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setSessionIdCookie(new SimpleCookie("sid"));// 保存sessionId的cookie
		// 是否开启定时调度器进行检测过期session 默认为true
		sessionManager.setSessionValidationSchedulerEnabled(true);
		// 设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
		// 设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler
		// 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
		sessionManager.setSessionValidationInterval(3600000);
		// 取消url 后面的 JSESSIONID
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}

	/**
	 * @Description: 安全管理器
	 */
	@Bean
	public SecurityManager securityManager() {
		log.info("===============>> shiro-设置安全管理器");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		securityManager.setCacheManager(ehCacheManager());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * @Description: Shiro过滤器Bean工厂
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		log.info("===============>> shiro-设置过滤");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		// 设置过滤方法
		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("authc", new ShiroAuthcFilter());
		filters.put("perms", new ShiroPermsFilter());
		shiroFilterFactoryBean.setFilters(filters);

		// 拦截器.
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		// 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		// filterChainDefinitionMap.put("/logout", "logout");
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		// <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->

		/**
		 * ResourceApi 查询数据的接口
		 */
		filterChainDefinitionMap.put("/resource/**", "anon");
		/**
		 * L主页
		 */
		filterChainDefinitionMap.put("/index", "anon");// 首页跳转url
		/**
		 * L系统配置
		 */
		/*
		 * L--L用户管理
		 */
		filterChainDefinitionMap.put("/system-manage/system-user", "perms[system-user:url]");// 用户管理跳转url
		filterChainDefinitionMap.put("/system-user/update", "perms[system-user:update]");// 修改用户信息接口
		// L--L--L设置角色
		filterChainDefinitionMap.put("/system-manage/system-user/role/set/**", "perms[user-role:url]");// 设置角色跳转url
		filterChainDefinitionMap.put("/system-user/role/add", "perms[user-role:add]");// 为用户赋予新角色接口
		filterChainDefinitionMap.put("/system-user/role/delete", "perms[user-role:delete]");// 撤销已赋予的角色接口
		/*
		 * L--L角色管理
		 */
		filterChainDefinitionMap.put("/system-manage/system-role", "perms[system-role:url]");// 角色管理跳转url
		filterChainDefinitionMap.put("/system-role/add", "perms[system-role:add]");// 新增角色接口
		filterChainDefinitionMap.put("/system-role/update", "perms[system-role:update]");// 修改角色信息接口
		filterChainDefinitionMap.put("/system-role/delete", "perms[system-role:delete]");// 删除角色接口
		// L--L--L权限设置
		filterChainDefinitionMap.put("/system-manage/system-role/permission/set/**", "perms[role-permission:url]");// 权限设置跳转url
		filterChainDefinitionMap.put("/system-role/permission/add", "perms[role-permission:add]");// 赋予资源权限接口
		filterChainDefinitionMap.put("/system-role/permission/delete", "perms[role-permission:delete]");// 为角色撤销已赋予资源权限
		/*
		 * L--L资源管理
		 */
		filterChainDefinitionMap.put("/system-manage/system-permission", "perms[system-permission:url]");// 资源管理跳转url
		filterChainDefinitionMap.put("/system-permission/add", "perms[system-permission:add]");// 新增资源接口
		filterChainDefinitionMap.put("/system-permission/update", "perms[system-permission:update]");// 修改资源信息接口
		filterChainDefinitionMap.put("/system-permission/delete", "perms[system-permission:delete]");// 删除资源接口
		/**
		 * L机器人配置
		 */
		/*
		 * L--L成员列表
		 */
		filterChainDefinitionMap.put("/resource-management/member-table", "anon");// 成员列表跳转url
		filterChainDefinitionMap.put("/member/refresh", "perms[member:refresh]");// 同步到最新的成员房间信息接口
		filterChainDefinitionMap.put("/member/update/room-monitor", "perms[member:update]");// 修改成员房间监控状态接口
		filterChainDefinitionMap.put("/room-monitor/add", "perms[room-monitor:add]");// 新增成员房间监控配置接口
		filterChainDefinitionMap.put("/room-monitor/update/keyword", "perms[room-monitor:update]");// 修改成员房间监控配置接口
		filterChainDefinitionMap.put("/room-monitor/delete", "perms[room-monitor:delete]");// 删除成员房间监控配置接口
		/*
		 * L--L摩点项目列表
		 */
		filterChainDefinitionMap.put("/resource-management/modian-table", "anon");// 摩点项目列表跳转url
		filterChainDefinitionMap.put("/modian/add", "perms[modian:add]");// 新增摩点项目接口
		filterChainDefinitionMap.put("/modian/delete", "perms[modian:delete]");// 删除摩点项目接口
		filterChainDefinitionMap.put("/modian-monitor/add", "perms[modian-monitor:add]");// 新增摩点项目监控接口
		filterChainDefinitionMap.put("/modian-monitor/delete", "perms[modian-monitor:delete]");// 删除摩点项目监控接口
		/*
		 * L--L微博用户列表
		 */
		filterChainDefinitionMap.put("/resource-management/weibo-table", "anon");// 微博用户列表跳转url
		filterChainDefinitionMap.put("/weibo/add", "perms[weibo:add]");// 新增监控的微博用户接口
		filterChainDefinitionMap.put("/weibo/delete", "perms[weibo:delete]");// 删除监控的微博用户接口
		filterChainDefinitionMap.put("/dynamic-monitor/add", "perms[dynamic-monitor:add]");// 新增微博动态监控接口
		filterChainDefinitionMap.put("/dynamic-monitor/delete", "perms[dynamic-monitor:delete]");// 删除微博动态监控接口
		/*
		 * L--LQQ列表
		 */
		filterChainDefinitionMap.put("/resource-management/qq-table", "perms[qq-table:url]");// QQ列表跳转url
		filterChainDefinitionMap.put("/qq-community/add", "perms[qqcommunity:add]");// 新增QQ接口
		filterChainDefinitionMap.put("/qq-community/update", "perms[qqcommunity:update]");// 修改QQ接口
		filterChainDefinitionMap.put("/qq-community/delete", "perms[qqcommunity:delete]");// 删除QQ接口
		/*
		 * L--L轮询配置列表
		 */
		filterChainDefinitionMap.put("/resource-management/quartz-confing-table", "perms[quartz-confing-table:url]");// 轮询配置列表跳转url
		filterChainDefinitionMap.put("/quartz-config/update", "perms[quartz-config:update]");// 修改轮询配置接口
		/**
		 * L可视化数据
		 */
		/*
		 * L--L摩点项目数据
		 */
		filterChainDefinitionMap.put("/data-visualization/modian-visual", "perms[modian-visual:url]");// 可视化摩点数据跳转url
		/*
		 * L--L口袋房间消息数据
		 */
		filterChainDefinitionMap.put("/data-visualization/room-message", "anon");// 可视化口袋消息数据跳转url

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * @Description: Shiro注解
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		log.info("===============>> shiro-设置注解");
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * @Description: Shiro的thymeleaf标签
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		log.info("===============>> shiro-设置thymeleaf标签");
		ShiroDialect shiroDialect = new ShiroDialect();
		return shiroDialect;
	}

}
