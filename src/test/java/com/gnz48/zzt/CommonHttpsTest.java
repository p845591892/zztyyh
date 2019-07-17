//package com.gnz48.zzt;
//
//import java.awt.Image;
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.gnz48.zzt.util.DateUtil;
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
//	private static final String HEADER_ACCEPT = "*/*";
//	private static final String HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";
//	private static final String HEADER_USER_AGENT = "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)";
//	private static final String HEADER_APPINFO = "{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}";
//
//	/**
//	 * 获取成员房间资料
//	 * @throws IOException 
//	 * @throws NoSuchAlgorithmException 
//	 * @throws KeyManagementException 
//	 */
//	@Test
//	public void getMemberRoomTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
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
//	 * @throws IOException 
//	 * @throws NoSuchAlgorithmException 
//	 * @throws KeyManagementException 
//	 */
//	@Test
//	public void getMemberTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
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
//	 * @throws IOException 
//	 * @throws NoSuchAlgorithmException 
//	 * @throws KeyManagementException 
//	 */
//	@Test
//	public void getTokenTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求参数 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "*/*");
//		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
//		requestPropertys.put("User-Agent", "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)");
//		requestPropertys.put("appInfo",
//				"{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}");
////		String payloadJson = "{\"mobile\":\"18824171764\",\"pwd\":\"woshi182tidu\"}";
//		String payloadJson = "{\"mobile\": \"15877240295\",\"pwd\": \"woshi182tidu\"}";
//
//		String login = https.setUrl("https://pocketapi.48.cn/user/api/v1/login/app/mobile").setDataType("POST")
//				.setPayloadJson(payloadJson).setRequestProperty(requestPropertys).send();
//		JSONObject loginObject = new JSONObject(login);
//		System.out.println(loginObject.toString());
//	}
//
//	/**
//	 * 获取房间消息
//	 * @throws IOException 
//	 * @throws NoSuchAlgorithmException 
//	 * @throws KeyManagementException 
//	 */
//	@Test
//	public void getRoomMessageTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", HEADER_USER_AGENT);
//		requestPropertys.put("token",
//				"PfCR4TzfYr0I7+usUFwEgEnTQRii1xAI3Y72gAwyK1VoXugczM/4aSBOtOrJK/NdOdwl7bnLE5M=");
//		/* 请求参数 */
//		String payloadJson = "{\"ownerId\":\"327567\",\"needTop1Msg\":\"false\",\"nextTime\":\"1558885156942\",\"roomId\":\"67342026\"}";
//		/* 发送请求 */
//		String messageJson = https.setDataType("POST").setRequestProperty(requestPropertys).setPayloadJson(payloadJson)
//				.setUrl("https://pocketapi.48.cn/im/api/v1/chatroom/msg/list/homeowner").send();
//		JSONObject messageObject = new JSONObject(messageJson);
//		JSONArray messageArray = messageObject.getJSONObject("content").getJSONArray("message");
//		System.out.println(messageArray.length());
//		for (int i = 0; i < messageArray.length(); i++) {
//			System.out.println(messageArray.get(i).toString());
//		}
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
//		String imageUrl = "http://203.195.205.79:1029/index";
//		System.out.println(imageUrl.startsWith("http://"));
//	}
//	
//	@Test
//	public void elementTest() {
//		String a = "<li> \n" + 
//				" <div class=\"sub-comment-txt\"> \n" + 
//				"  <a href=\"https://me.modian.com/u/detail?uid=2546194\"><span>GNZ48张润应援会</span></a> 回复 \n" + 
//				"  <a href=\"https://me.modian.com/u/detail?uid=2546194\"><span>GNZ48张润应援会</span></a>: 代投 \n" + 
//				" </div> \n" + 
//				" <div class=\"sub-b\" clearfix> \n" + 
//				"  <span class=\"sub-time\">2018-10-12 01:10</span> \n" + 
//				"  <div class=\"sub-btn\"> \n" + 
//				"   <span style=\"display:none\"><i class=\"iconfont\" icon-report></i> <i>举报</i></span> \n" + 
//				"   <span class=\"comment-replay\" data-cur=\"child\" data-reply_rid=\"601721\" data-reply_ruid=\"2546194\" data-post_id=\"48411\" data-reply_sub_rid=\"601732\"><i class=\"iconfont\" icon-edit></i> <i>回复</i></span> \n" + 
//				"  </div> \n" + 
//				" </div> </li>";
//		Element li = new Element(a);
//		System.out.println(li.toString());
//		Element div_commentText = li.selectFirst(".comment-txt");
//		if (div_commentText == null) {
//			System.out.println("空哦那个");
//		}
//		Element i_icon = div_commentText.selectFirst("i");
//	}
//
//}
