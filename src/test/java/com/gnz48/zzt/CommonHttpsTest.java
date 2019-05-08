//package com.gnz48.zzt;
//
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
//
///**
// * Https请求相关测试类 对应口袋48版本:6.0.0
// * 不启动Spring Boot
// * @author shiro
// *
// */
//public class CommonHttpsTest {
//	
//	private static final Logger log = LoggerFactory.getLogger(CommonHttpsTest.class);
//
//	/**
//	 * 获取成员资料
//	 */
//	@Test
//	public void getMemberTest() {
////		https2019Service.syncMember();
//		
//		Https https = new Https();
//		
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "application/json, text/plain, */*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
//		requestPropertys.put("appInfo", "{\"vendor\":\"a\",\"appType\":\"POCKET48\",\"deviceName\":\"b\",\"deviceId\":\"c\",\"appVersion\":\"d\",\"appBuild\":\"e\",\"osType\":\"android\",\"osVersion\":\"g\"}");
//		requestPropertys.put("token", "UDCuoDHKqKX7bcYLfRzZ5OIWSXkh/cRgohhepIXYY4SK84Qzmej6QK3x7AxPds9qCei6XstbaWV8f+WmyJtpHQ==");
//		
//		String roomJson = https.setUrl("https://pocketapi.48.cn/im/api/v1/im/room/info/type/source")
//													.setDataType("POST")
//													.setRequestProperty(requestPropertys)
//													.setPayloadJson("{\"sourceId\":\"6742\",\"type\":\"0\"}")
//													.send();
//		log.info("json = {}", roomJson);
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
//		requestPropertys.put("Accept", "application/json, text/plain, */*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
//		String payloadJson = "{\"mobile\":\"18824171764\",\"pwd\":\"woshi182tidu\"}";
//		
//		String login = https.setUrl("https://xsaiting.com/pocket48/static/proxy.php?f=login")
//											.setDataType("POST")
//											.setPayloadJson(payloadJson)
//											.setRequestProperty(requestPropertys)
//											.send();
//		JSONObject loginObject = new JSONObject(login);
//		if (!loginObject.getBoolean("success")) {
//			log.info("获取Token失败");
//			return;
//		}
//		String token = loginObject.getJSONObject("content")
//														.getJSONObject("userInfo")
//														.getString("token");
//		log.info("token = {}", token);
//	}
//	
//	@Test
//	public void getRoomMessageTest() {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "application/json, text/plain, */*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
//		requestPropertys.put("token", "iF+q/8I0c4dNbTLMVZTy3l+bmVg7su9D93ckXlfaXK7zDWeuKsLvlNKTRx256T3+XGTZSia9gQddWomx0GJEZg==");
//		/* 请求参数 */
//		String payloadJson = "{\"ownerId\":\"1\",\"needTop1Msg\":\"false\",\"nextTime\":\"0\",\"roomId\":\"67313770\"}";
//		/* 发送请求 */
//		String messageJson = https.setDataType("POST")
//														.setRequestProperty(requestPropertys)
//														.setPayloadJson(payloadJson)
//														.setUrl("https://pocketapi.48.cn/im/api/v1/chatroom/msg/list/homeowner")
//														.send();
//		System.out.println(messageJson);
//		JSONObject messageObject = new JSONObject(messageJson);
//		JSONArray messageArray = messageObject.getJSONObject("content").getJSONArray("message");
//		System.out.println(messageArray.length());
//	}
//	
//}
