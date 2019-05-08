//package com.gnz48.zzt;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.gnz48.zzt.service.Https2019Service;
//
///**
// * Https请求相关测试类 对应口袋48版本:6.0.0
// * 需要启动Spring Boot
// * 
// * @author shiro
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BootHttpsTest {
//
//	private static final Logger log = LoggerFactory.getLogger(BootHttpsTest.class);
//
//	@Autowired
//	Https2019Service https2019Service;
//
//	/**
//	 * 同步成员资料
//	 */
//	@Test
//	public void syncMemberTest() {
//		https2019Service.syncMember();
//	}
//	
//	/**
//	 * 同步口袋房间消息
//	 */
//	@Test
//	public void syncRoomMessageTest() {
//		https2019Service.syncRoomMessage();
//	}
//	
//	/**
//	 * 同步微博用户资料
//	 */
//	@Test
//	public void syncWeiboUserTest() {
//		https2019Service.syncWeiboUser();
//	}
//	
//	/**
//	 * 同步微博动态
//	 */
//	@Test
//	public void syncDynamicTest() {
//		https2019Service.syncDynamic();
//	}
//
//}
