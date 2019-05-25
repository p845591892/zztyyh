//package com.gnz48.zzt;
//
//import java.awt.Image;
//import java.io.File;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.gnz48.zzt.util.Https;
//import com.gnz48.zzt.util.StringUtil;
//
///**
// * Https请求相关测试类 对应口袋48版本:6.0.0 不启动Spring Boot
// * 
// * @author shiro
// *
// */
//public class CommonHttpsTest {
//
//	private static final Logger log = LoggerFactory.getLogger(CommonHttpsTest.class);
//
//	/**
//	 * 获取成员房间资料
//	 */
//	@Test
//	public void getMemberRoomTest() {
////		https2019Service.syncMember();
//
//		Https https = new Https();
//
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "*/*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)");
//		requestPropertys.put("appInfo",
//				"{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}");
//		requestPropertys.put("token",
//				"0oQjDsc2CyV9VfsovvfHGRKTKWXFnswKAXdmu7V7Ehx6M/fvj/YKgcQ5uvbF3VnpQDY/9BruxlI=");
//
//		String roomJson = https.setUrl("https://pocketapi.48.cn/im/api/v1/im/room/info/type/source").setDataType("POST")
//				.setRequestProperty(requestPropertys).setPayloadJson("{\"sourceId\":\"6742\",\"type\":\"0\"}").send();
//		log.info("json = {}", roomJson);
//	}
//
//	/**
//	 * 获取成员资料
//	 */
//	@Test
//	public void getMemberTest() {
//		Https https = new Https();
//
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "*/*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent",
//				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
//		String memberJson = https.setUrl("https://pocketapi.48.cn/user/api/v1/user/star/archives").setDataType("POST")
//				.setRequestProperty(requestPropertys).setPayloadJson("{\"memberId\":\"459991\"}").send();
//		JSONObject memberObject = new JSONObject(memberJson);
//
//		System.out.println(memberJson);
//		System.out.println(memberObject.getJSONObject("content").getJSONArray("history").toString());
//	}
//
//	/**
//	 * 获取Token
//	 */
//	@Test
//	public void getTokenTest() {
//		Https https = new Https();
//		/* 请求参数 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "*/*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)");
//		requestPropertys.put("appInfo",
//				"{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}");
////		String payloadJson = "{\"mobile\":\"18824171764\",\"pwd\":\"woshi182tidu\"}";
//		String payloadJson = "{\"mobile\": \"18824171764\",\"pwd\": \"woshi182tidu\"}";
//
//		String login = https.setUrl("https://pocketapi.48.cn/user/api/v1/login/app/mobile").setDataType("POST")
//				.setPayloadJson(payloadJson).setRequestProperty(requestPropertys).send();
//		JSONObject loginObject = new JSONObject(login);
//		System.out.println(loginObject.toString());
//	}
//
//	/**
//	 * 获取房间资料
//	 */
//	@Test
//	public void getRoomMessageTest() {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "application/json, text/plain, */*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent",
//				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
//		requestPropertys.put("token",
//				"iF+q/8I0c4dNbTLMVZTy3l+bmVg7su9D93ckXlfaXK7zDWeuKsLvlNKTRx256T3+XGTZSia9gQddWomx0GJEZg==");
//		/* 请求参数 */
//		String payloadJson = "{\"ownerId\":\"1\",\"needTop1Msg\":\"false\",\"nextTime\":\"0\",\"roomId\":\"67313770\"}";
//		/* 发送请求 */
//		String messageJson = https.setDataType("POST").setRequestProperty(requestPropertys).setPayloadJson(payloadJson)
//				.setUrl("https://pocketapi.48.cn/im/api/v1/chatroom/msg/list/homeowner").send();
//		System.out.println(messageJson);
//		JSONObject messageObject = new JSONObject(messageJson);
//		JSONArray messageArray = messageObject.getJSONObject("content").getJSONArray("message");
//		System.out.println(messageArray.length());
//	}
//
//	/**
//	 * 反射测试
//	 */
//	@Test
//	public void pathTest() {
//		try {
//			URL url = new File("/Users/shiro/Documents/fileDepot/java/job/20190513184118AHVT.java").toURI().toURL();
//			ClassLoader loader = new URLClassLoader(new URL[] { url });// 类加载器
//			Class<?> cls = loader.loadClass("com.gnz48.zzt.util.ImageUtil");// 加载外部类
//			Object object = cls.newInstance();// 实例化
//			Method method = cls.getMethod("setImage", Image.class);// 获得方法
//			method.invoke(null, null);// 执行方法
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void stringTest() {
//		System.out.println(StringUtil.isEnglish(""));
//		System.out.println(StringUtil.isEnglish(" "));
//		System.out.println(StringUtil.isEnglish("你好"));
//		System.out.println(StringUtil.isEnglish("ABC"));
//		System.out.println(StringUtil.isEnglish("abc你好"));
//	}
//
//}
