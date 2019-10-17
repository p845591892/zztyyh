//package com.gnz48.zzt;
//
//import java.awt.AWTException;
//import java.awt.Image;
//import java.io.IOException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.gnz48.zzt.dao.WebDao;
//import com.gnz48.zzt.repository.system.UserRepository;
//import com.gnz48.zzt.repository.weibo.WeiboUserRepository;
//import com.gnz48.zzt.service.HttpsService;
//import com.gnz48.zzt.service.SendMessageService;
//import com.gnz48.zzt.util.DateUtil;
//import com.gnz48.zzt.util.Https;
//import com.gnz48.zzt.util.ImageUtil;
//import com.gnz48.zzt.util.StringUtil;
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class ZztyyhApplicationTests {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(ZztyyhApplicationTests.class);
//
//	@Autowired
//	private HttpsService httpsService;
//
//	/* 获取成员的信息 */
//	@Test
//	public void syncMemberTest() {
//		httpsService.syncMember();
//	}
//
//	/* 获取房间消息 */
//	@Test
//	public void syncRoomMessageTest() throws JSONException, ParseException {
//		httpsService.syncRoomMessage();
//	}
//
//	/* 获取微博动态 */
//	@Test
//	public void syncDynamicTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		httpsService.syncDynamic();
//	}
//
//	/* 获取摩点集资 */
//	@Test
//	public void syncModianPoolTest() throws JSONException, ParseException {
//		httpsService.syncModianPool();
//	}
//
//	/* 发送微博动态 */
//	@Test
//	public void sendWeiboDynamicTest() {
//		try {
//			sendMessageService.sendWeiboDynamic();
//		} catch (IOException e) {
//			LOGGER.info(e.getMessage(), e.toString());
//		}
//	}
//
//	@Autowired
//	private WebDao webDao;
//
//	@Test
//	public void dataTest() throws ParseException {
//		// Date beginDate =
//		// DateUtil.getMidnight(DateUtil.getDateFormat(DateUtil.countDay(-7)));
//		// Date endDate =
//		// DateUtil.getNearMidnight(DateUtil.getDateFormat(DateUtil.countDay(-1)));
//		// Map<String, Date> params = new HashMap<String, Date>();
//		// params.put("beginDate", beginDate);
//		// params.put("endDate", endDate);
//		// List<MtboxVO> vos = webDao.findActiveRoom(params);
//		SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
//		Date date = sdf.parse("Sat Dec 29 09:21:59 +0800 2018");
//		System.out.println(DateUtil.getDate(date));
//	}
//
//	@Autowired
//	private SendMessageService sendMessageService;
//
//	@Test
//	public void sendModianPoolTest() throws AWTException {
//		try {
//			sendMessageService.sendModianPool();
//		} catch (IOException e) {
//			LOGGER.info(e.getMessage(), e.toString());
//		}
//	}
//
//	@Autowired
//	private WeiboUserRepository weiboUserRepository;
//
//	/* 获取监控的微博数 */
//	@Test
//	public void weiboUserCount() {
//		System.out.println(weiboUserRepository.count());
//	}
//
//	@Autowired
//	private UserRepository userRepository;
//
//	/* 密码测试 */
//	@Test
//	public void passwordTest() {
//		System.out.println(StringUtil.shiroMd5("lcy", "woshi182tidu"));
//	}
//
//	/* windows类测试 */
//	@Test
//	public void windowsTest() {
//		System.out.println();
//		// System.out.println(GraphicsEnvironment.isHeadless());
//		// System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance());
//	}
//
//	@Test
//	public void getKoudaiUser() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		String name = "821757";
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Content-Type", "application/json; charset=UTF-8");
//		String payloadJson = "{needChatInfo: false, needFriendsNum: false, needRecommend: false}";
//		String result = https.setUrl("https://puser.48.cn/usersystem/api/user/v1/show/info/821757").setDataType("POST")
//				.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
//		JSONObject jsonObject = new JSONObject(result);
//		System.out.println(jsonObject.toString());
//		if (jsonObject.getString("message").equals("success")) {
//			JSONObject userInfo = jsonObject.getJSONObject("content").getJSONObject("userInfo");
//			name = userInfo.getString("nickName");
//		}
//		System.out.println(name);
//	}
//
//	@Test
//	public void getMemberFansAndRoom() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Content-Type", "application/json; charset=utf-8");
//		String payloadJson = "{memberId: 722787}";
//		String result = https.setUrl("https://puser.48.cn/usersystem/api/user/member/v1/fans/room").setDataType("POST")
//				.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
//		JSONObject jsonObject = new JSONObject(result);
//		System.out.println(jsonObject.toString());
//	}
//
//	/* 获取网络图片 */
//	@Test
//	public void getImageTest() throws AWTException, IOException {
//		Https https = new Https();
//		Image image = https.setUrl("http://www.snh48.com/images/member/zp_10019.jpg").getImage();
//		ImageUtil.setImage(image);
//		// Robot r = new Robot();
//		// r.delay(3000);
//		// r.keyPress(KeyEvent.VK_CONTROL);
//		// r.keyPress(KeyEvent.VK_V);
//		// r.keyRelease(KeyEvent.VK_V);
//		// r.keyRelease(KeyEvent.VK_CONTROL);
//		// r.delay(300);
//		// KeyboardUtil.keyPress(r, KeyEvent.VK_ENTER);
//	}
//
//	/* 获取微博超话帖子 */
//	@Test
//	public void getWeiboChaohuaTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		// 设置请求头
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", "application/json, text/plain, */*");
//		requestPropertys.put("User-Agent",
//				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Mobile Safari/537.36");
//		requestPropertys.put("X-Requested-With", "XMLHttpRequest");
//		requestPropertys.put("", "");
//		// 设置参数
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("containerid", "100808be885afaab002c5fc91dcd5641ff0fd1_-_feed");
//		params.put("extparam", "章泽婷");
//		params.put("luicode", "10000011");
//		params.put("lfid", "1076036276999889");
//		// URL
//		String url = "https://m.weibo.cn/api/container/getIndex";
//		String result = https.setDataType("GET").setRequestProperty(requestPropertys).setParams(params).setUrl(url)
//				.send();
//		JSONObject jsonObject = new JSONObject(result);
//		JSONArray dynamicArray = jsonObject.getJSONObject("data").getJSONArray("cards").getJSONObject(4)
//				.getJSONArray("card_group");
//		for (int i = 0; i < dynamicArray.length(); i++) {
//			JSONObject dynamic = dynamicArray.getJSONObject(i);
//			JSONObject mblog = dynamic.getJSONObject("mblog");
//			// System.out.println("动态:\n" + getWeiboContent(mblog));
//		}
//	}
//
//	/* 发送邮件 */
//	@Test
//	public void sendMailTest() {
//		// Mail.sendTextMail("测试邮件", "这是一条测试邮件", "847109667@qq.com");
//
//		// Mail.sendFileMail("测试邮件", "这是一条测试邮件", "847109667@qq.com", new
//		// File("D:\\李超熠\\赴日签证材料\\赴日行程单.pdf"));
//
//		// Mail.sendInlineMail("测试邮件", "<html><body><img src=\"cid:1\"
//		// ></body></html>", "847109667@qq.com",
//		// new File("D:\\李超熠\\新建文件夹\\1.jpg"));
//
//		// VelocityContext model = new VelocityContext();
//		// model.put("username", "JuFF_白羽");
//		// Mail.sendTemplateMail("测试邮件",
//		// "/templates/mail_template/template1.vm", model, "847109667@qq.com");
//	}
//
//	
//	
//	/**
//	 * 请求头
//	 */
//	private static final String APP_HEADER_ACCEPT = "*/*";
//	private static final String APP_HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";
//	private static final String APP_HEADER_USER_AGENT = "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)";
//	private static final String APP_HEADER_APPINFO = "{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}";
//	
//	/**
//	 * 获取用户信息
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getBaseUserInfo() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
//		/* 请求参数 */
//		String payloadJson = "{\"userId\":\"" + 557652 + "\",\"needMuteInfo\":\"True\"}";
//		String baseUserInfoJson = https.setDataType("POST").setPayloadJson(payloadJson)
//				.setRequestProperty(requestPropertys).setUrl("https://pocketapi.48.cn/user/api/v1/user/info/home").send();
//		JSONObject userObject = new JSONObject(baseUserInfoJson);
//		System.out.println(userObject.toString());
//	}
//	
//	/**
//	 * 获取成员资料
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getMenber() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
//		/* 请求参数 */
//		String payloadJson = "{\"memberId\":\"" + String.valueOf(447924) + "\"}";
//		/* 发送请求 */
//		String memberJson = https.setUrl("https://pocketapi.48.cn/user/api/v1/user/star/archives")
//													.setDataType("POST")
//													.setRequestProperty(requestPropertys)
//													.setPayloadJson(payloadJson)
//													.send();
//		JSONObject memberObject = new JSONObject(memberJson);
//		System.out.println(memberObject.toString());
//	}
//	
//	/**
//	 * 获取token
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getToken() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
//		requestPropertys.put("appInfo", APP_HEADER_APPINFO);
//		/* 请求参数 */
//		String payloadJson = "{\"mobile\":\"\",\"pwd\":\"\"}";
//		/* 发送请求 */
//		String loginJson = https.setUrl("https://pocketapi.48.cn/user/api/v1/login/app/mobile")
//											.setDataType("POST")
//											.setPayloadJson(payloadJson)
//											.setRequestProperty(requestPropertys)
//											.send();
//		JSONObject loginObject = new JSONObject(loginJson);
//		System.out.println(loginObject.toString());
//	}
//	
//	/**
//	 * 获取口袋房间消息
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getRoomMessage() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
//		requestPropertys.put("token", "6CncBDAr/a2hxgwNxBua+3aADwFck7XBQOzz2mRgHlfhYJt7C2JOtCK0o5kKg68d/mfCP5kzN2k=");
//		/* 请求参数 */
//		String payloadJson = "{\"ownerId\":\"327569\",\"needTop1Msg\":\"false\",\"nextTime\":\"0\",\"roomId\":\"67217596\"}";
//		/* 发送请求 */
//		String messageJson = https.setDataType("POST")
//														.setRequestProperty(requestPropertys)
//														.setPayloadJson(payloadJson)
//														.setUrl("https://pocketapi.48.cn/im/api/v1/chatroom/msg/list/homeowner")
//														.send();
//		JSONObject messageObject = new JSONObject(messageJson);
//		System.out.println(messageObject.toString());
//	}
//	
//	/**
//	 * 获取鸡腿翻牌
//	 * @throws KeyManagementException
//	 * @throws NoSuchAlgorithmException
//	 * @throws IOException
//	 */
//	@Test
//	public void getFlipcard() throws KeyManagementException, NoSuchAlgorithmException, IOException {
//		Https https = new Https();
//		/* 请求头 */
//		Map<String, String> requestPropertys = new HashMap<String, String>();
//		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
//		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
//		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
//		requestPropertys.put("token", "6CncBDAr/a2hxgwNxBua+3aADwFck7XBQOzz2mRgHlfhYJt7C2JOtCK0o5kKg68d/mfCP5kzN2k=");
//		/* 请求参数 */
//		String payloadJson = "{\"questionId\":\"391203387848069120\",\"answerId\":\"447924\"}";
//		/* 发送请求 */
//		String flipcardJson = https.setDataType("POST")
//														.setRequestProperty(requestPropertys)
//														.setPayloadJson(payloadJson)
//														.setUrl("https://pocketapi.48.cn/idolanswer/api/idolanswer/v1/question_answer/detail")
//														.send();
//		JSONObject flipcardObject = new JSONObject(flipcardJson);
//		System.out.println(flipcardObject);
//	}
//
//}