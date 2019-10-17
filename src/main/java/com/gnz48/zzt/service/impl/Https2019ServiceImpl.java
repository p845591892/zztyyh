package com.gnz48.zzt.service.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.dao.Https2019Dao;
import com.gnz48.zzt.entity.snh48.Member;
import com.gnz48.zzt.entity.snh48.RoomMessage;
import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.repository.snh48.RoomMessageRepository;
import com.gnz48.zzt.service.Https2019Service;
import com.gnz48.zzt.service.HttpsService;
import com.gnz48.zzt.util.DateUtil;
import com.gnz48.zzt.util.Https;

/**
 * Https2019Service接口的实现类
 * @author shiro
 *
 */
@Service
@Transactional
public class Https2019ServiceImpl extends HttpsService implements Https2019Service {
	
	private static final Logger log = LoggerFactory.getLogger(Https2019ServiceImpl.class);
	
	/**
	 * 口袋48登录账号
	 */
	@Value(value = "${koudai48.username}")
	private String USERNAME;

	/**
	 * 口袋48登录密码
	 */
	@Value(value = "${koudai48.password}")
	private String PASSWORD;
	
	/**
	 * token参数，由getToken()提供。
	 */
	private String TOKEN = "";
	
	/**
	 * 请求头
	 */
	private static final String APP_HEADER_ACCEPT = "*/*";
	private static final String APP_HEADER_CONTENT_TYPE = "application/json;charset=UTF-8";
	private static final String APP_HEADER_USER_AGENT = "PocketFans201807/6.0.0 (iPhone; iOS 12.2; Scale/2.00)";
	private static final String APP_HEADER_APPINFO = "{\"vendor\":\"apple\",\"deviceId\":\"0\",\"appVersion\":\"6.0.0\",\"appBuild\":\"190409\",\"osVersion\":\"12.2.0\",\"osType\":\"ios\",\"deviceName\":\"iphone\",\"os\":\"ios\"}";
	
	/**
	 * SNH48Group所有成员列表接口
	 */
	private static final String URL_ALL_MEMBER_LIST = "https://h5.48.cn/memberPage/member_mapping.json";
	
	/**
	 * SNH48Group成员资料接口
	 */
	private static final String URL_MEMBER = "https://pocketapi.48.cn/user/api/v1/user/star/archives";
	
	/**
	 * SNH48Group成员房间资料接口
	 */
	private static final String URL_MEMBER_ROOM = "https://pocketapi.48.cn/im/api/v1/im/room/info/type/source";
	
	/**
	 * 登陆口袋48接口
	 */
	private static final String URL_TOKEN = "https://pocketapi.48.cn/user/api/v1/login/app/mobile";
	
	/**
	 * SNH48Group成员房间消息接口
	 */
	private static final String URL_ROOM_MESSAGE = "https://pocketapi.48.cn/im/api/v1/chatroom/msg/list/homeowner";
	
	/**
	 * SNH48Group成员房间生放送地址
	 */
	private static final String URL_LIVE = "https://h5.48.cn/2019appshare/memberLiveShare/?id=";
	
	/**
	 * 口袋48用户资料查询接口
	 */
	private static final String URL_USER = "https://pocketapi.48.cn/user/api/v1/user/info/home";
	
	/**
	 * 口袋48用户资料查询接口
	 */
	private static final String URL_ROOM_MESSAGE_FLIPCARD = "https://pocketapi.48.cn/idolanswer/api/idolanswer/v1/question_answer/detail";
	
	@Autowired
	private Https2019Dao https2019Dao;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private RoomMessageRepository roomMessageRepository;
	
	@Override
	public void syncMember() {
		log.info("同步成员资料...");
		JSONObject jsonObject  = null;
		try {
			jsonObject = this.getAllMember();
		} catch (Exception e) {
			log.info("【URL_ALL_MEMBER_LIST】接口调用发生错误：{}", e.getMessage());
			return;
		}
		
		/* 遍历全体成员信息对象 */
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {

			/* 获取成员普通资料 */
			String key = iterator.next();
			JSONObject memberObject = jsonObject.getJSONObject(key);
			long memberId = memberObject.getLong("memberId");
			try {
				memberObject = this.getMember(memberId);
			} catch (Exception e) {
				log.info("【URL_MEMBER】接口调用发生错误：{}。memberId = {}", e.getMessage(), memberId);
				continue;
			}
			if (memberObject == null) {
				continue;
			}
			
			JSONObject content = memberObject.getJSONObject("content");
			JSONObject starInfo = content.getJSONObject("starInfo");
			JSONArray history = content.getJSONArray("history");
			
			Member member = new Member();
			member.setId(memberId);// 成员ID
			member.setAvatar(super.getImageUrl(starInfo.getString("starAvatar")));// 成员头像地址
			member.setName(starInfo.getString("starName"));// 名字
			member.setPinyin(starInfo.getString("pinyin"));// 名字拼音
			member.setAbbr(starInfo.getString("abbr"));// 名字缩写
			member.setBirthday(starInfo.getString("birthday"));// 生日
			member.setBirthplace(starInfo.getString("birthplace"));// 出生地
			member.setBloodType(starInfo.getString("bloodType"));// 血型
			member.setConstellation(starInfo.getString("constellation"));// 星座
			member.setHeight(starInfo.getInt("height"));// 身高
			member.setTeamId(starInfo.getLong("starTeamId"));// 所属队伍ID
			member.setTeamName(starInfo.getString("starTeamName"));// 所属队伍名字
			member.setGroupId(starInfo.getLong("starGroupId"));// 所属团体ID
			member.setGroupName(starInfo.getString("starGroupName"));// 所属团体名字
			member.setHistory(history.toString());// 成员历史
			member.setHobbies(starInfo.getString("hobbies"));// 爱好
			try {
				member.setJoinTime(DateUtil.getDateFormat(starInfo.getString("joinTime"), "yyyy-MM-dd"));// 加入时间
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}
			member.setSpecialty(starInfo.getString("specialty"));// 特长
			member.setNickname(starInfo.getString("nickname"));// 昵称
			
			/* 获取成员房间资料 */
			JSONObject roomObject = null;
			try {
				roomObject = this.getMemberRoom(memberId, 0, 5);
			} catch (Exception e) {
				log.info("【URL_MEMBER_ROOM】接口调用发生错误：{}。memberId = {}", e.getMessage(), memberId);
				continue;
			}
			if (roomObject == null) {
				continue;
			}
			
			if (roomObject.getBoolean("success")) {// 房间存在
				JSONObject roomInfo = roomObject.getJSONObject("content").getJSONObject("roomInfo");
				member.setRoomId(roomInfo.getLong("roomId"));// 房间ID
				member.setRoomName(roomInfo.getString("roomName"));// 房间名
				member.setTopic(roomInfo.getString("roomTopic"));// 房间话题
				/* 判断房间旧监控状态 */
				Member oldMember = memberRepository.findOne(member.getId());
				if (oldMember != null) {
					int roomMonitor = oldMember.getRoomMonitor();
					if (roomMonitor != 1 && roomMonitor != 2) {
						member.setRoomMonitor(2);// 强制为监控关闭
					}
				} 
				
			} else {// 房间不存在
				member.setRoomMonitor(roomObject.getInt("status"));
			}
			
			/* 修改/新增成员资料 */
			int num = https2019Dao.updateMemberById(member);
			if (num == 0) {// 无修改则为新增
				member.setRoomMonitor(2);
				memberRepository.save(member);
			}
			
		}
		log.info("同步成员资料结束");
	}
	
	/**
	 * 发送请求获取SNH48Group所有成员的列表
	 * @return 全体成员资料的json对象
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private JSONObject getAllMember() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		String allMember = https.setDataType("GET").setUrl(URL_ALL_MEMBER_LIST).send();
		JSONObject allMemberObject = new JSONObject(allMember);
		return allMemberObject;
	}
	
	/**
	 * 发送请求获取SNH48Group成员的个人资料
	 * @param memberId 成员ID
	 * @return 成员个人资料的json对象
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private JSONObject getMember(long memberId) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		/* 请求参数 */
		String payloadJson = "{\"memberId\":\"" + String.valueOf(memberId) + "\"}";
		/* 发送请求 */
		String memberJson = https.setUrl(URL_MEMBER)
													.setDataType("POST")
													.setRequestProperty(requestPropertys)
													.setPayloadJson(payloadJson)
													.send();
		JSONObject memberObject = new JSONObject(memberJson);
		if (memberObject.getBoolean("success")) {
			return memberObject;
		} else {
			log.info("获取成员资料失败。原因: {}。memberId = [{}]", memberObject.getString("message"), memberId);
			return null;
		}
	}
	
	/**
	 * 发送请求获取SNH48Group成员的房间资料
	 * @param sourceId 成员ID
	 * @param type 请求类型(默认填0，暂不推荐其他参数。)
	 * @param again 再次尝试发送请求次数
	 * @return 房间资料的json对象
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private JSONObject getMemberRoom(long sourceId, int type, int again) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		if (again == 0) {
			log.info("已超过规定尝试次数，强制结束请求返回null");
			return null;
		}
		
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		requestPropertys.put("appInfo", APP_HEADER_APPINFO);
		requestPropertys.put("token", TOKEN);
		/* 请求参数 */
		String payloadJson = "{\"sourceId\":\"" + String.valueOf(sourceId) + "\",\"type\":\"" + String.valueOf(type) + "\"}";
		/* 发送请求 */
		String roomJson = https.setUrl(URL_MEMBER_ROOM)
													.setDataType("POST")
													.setRequestProperty(requestPropertys)
													.setPayloadJson(payloadJson)
													.send();
		JSONObject roomObject = new JSONObject(roomJson);
		if (roomObject.getBoolean("success") || roomObject.getString("message").equals("房间不存在")) {
			return roomObject;
		}
		log.info("获取成员房间资料失败。原因: {}。sourceId = [{}]；token = [{}]", roomObject.getString("message"), sourceId, TOKEN);
		this.setNewToken();
		again -= 1;
		return getMemberRoom(sourceId, type, again);
	}
	
	/**
	 * 发送请求获取token
	 * @return token字符串
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private String getToken() throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		requestPropertys.put("appInfo", APP_HEADER_APPINFO);
		/* 请求参数 */
		String payloadJson = "{\"mobile\":\""+ USERNAME +"\",\"pwd\":\""+ PASSWORD +"\"}";
		/* 发送请求 */
		String loginJson = https.setUrl(URL_TOKEN)
											.setDataType("POST")
											.setPayloadJson(payloadJson)
											.setRequestProperty(requestPropertys)
											.send();
		JSONObject loginObject = new JSONObject(loginJson);
		if (!loginObject.getBoolean("success")) {
			log.info("获取Token失败。 原因: {}。 当前账号密码为: username = [{}], password = [{}]。", loginObject.getString("message"), USERNAME, PASSWORD);
			return "";
		}
		String token = loginObject.getJSONObject("content")
														.getJSONObject("userInfo")
														.getString("token");
		return token;
	}
	
	/**
	 * 将token放入该类的静态参数TOKEN中，供其他请求接口调用
	 */
	private void setNewToken() {
		try {
			TOKEN = getToken();
			log.info("重置token = {}", TOKEN);
		} catch (Exception e) {
			log.info("【URL_TOKEN】接口调用发生错误：{}", e.getMessage());
		}
	}

	@Override
	public void syncRoomMessage()  {
		log.info("同步口袋房间消息...");
		// 获取开启了监控的成员，逐一发送请求获取其口袋房间消息
		List<Member> members = memberRepository.findByRoomMonitor(1);
		for (Member member : members) {
			JSONObject messageObject = null;
			try {
				messageObject = this.getRoomMessage(String.valueOf(member.getId()), String.valueOf(member.getRoomId()), member.getName(), 5);
			} catch (Exception e) {
				log.info("【URL_ROOM_MESSAGE】接口调用发生错误：{}。memberName = {}", e.getMessage(), member.getName());
				continue;
			}
			// 若取不到，则结束该成员房间消息的同步
			if (messageObject == null) {
				continue;
			}
			
			//遍历消息json数组
			JSONArray messageArray = messageObject.getJSONObject("content").getJSONArray("message");
			for (int i = 0; i < messageArray.length(); i++) {
				JSONObject indexObject = messageArray.getJSONObject(i);
				String roomMessageId = indexObject.getString("msgidClient");
				RoomMessage roomMessage = roomMessageRepository.findOne(roomMessageId);
				
				if (roomMessage != null) {// 当表中包含该条数据时，结束将该成员房间消息新数据插入的操作
					break;
				}
				
				JSONObject extInfoObject = new JSONObject(indexObject.getString("extInfo"));// 消息内容的主体对象
				JSONObject userObject = extInfoObject.getJSONObject("user");// 发送的用户对象
				
				roomMessage = new RoomMessage();
				roomMessage.setId(roomMessageId);// 消息ID
				roomMessage.setIsSend(1);// 是否发送，默认1(未发送)
				roomMessage.setMessageObject(extInfoObject.getString("messageType"));// 消息类型
				roomMessage.setMsgContent(this.getMsgContent(indexObject));// 消息内容
				roomMessage.setRoomId(extInfoObject.getLong("roomId"));// 房间ID
				roomMessage.setSenderId(userObject.getLong("userId"));// 发送人ID
				roomMessage.setSenderName(userObject.getString("nickName"));// 发送人昵称
				try {
					roomMessage.setMsgTime(DateUtil.getDateFormat(indexObject.getLong("msgTime")));// 发送时间
				} catch (Exception e) {
					log.info("格式化时间发生错误。{}", e.getMessage());
				}
				
				roomMessageRepository.save(roomMessage);
			}
		}
		log.info("同步口袋房间消息结束");
	}
	
	/**
	 * 发送请求获取指定成员房间的消息
	 * @param memberId 成员ID
	 * @param roomId 房间ID
	 * @param memberName 成员名字(日志用，可null)
	 * @param again 再次尝试发送请求次数
	 * @return 房间消息的json对象
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private JSONObject getRoomMessage(String memberId, String roomId, String memberName, int again) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		if (again == 0) {
			log.info("已超过规定尝试次数，强制结束请求返回null");
			return null;
		}
		
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		requestPropertys.put("token", TOKEN);
		/* 请求参数 */
		String payloadJson = "{\"ownerId\":\""+memberId+"\",\"needTop1Msg\":\"false\",\"nextTime\":\"0\",\"roomId\":\""+roomId+"\"}";
		/* 发送请求 */
		String messageJson = https.setDataType("POST")
														.setRequestProperty(requestPropertys)
														.setPayloadJson(payloadJson)
														.setUrl(URL_ROOM_MESSAGE)
														.send();
		JSONObject messageObject = new JSONObject(messageJson);
		if (messageObject.getBoolean("success")) {
			return messageObject;
		}
		log.info("获取 {} 口袋48房间消息失败。原因: {}。当前参数: {}。当前token = {}", memberName, messageObject.getString("message"), payloadJson, TOKEN);
		this.setNewToken();
		again -= 1;
		return getRoomMessage(memberId, roomId, memberName, again);
	}
	
	/**
	 * 格式化房间消息内容<p>
	 * 根据不同的消息类型，生成不同格式的消息内容，适用口袋48版本6.0.0以后
	 * @param indexObject 消息对象
	 * @return 返回处理后的消息内容
	 */
	private String getMsgContent(JSONObject indexObject) {
		StringBuffer msgContent = new StringBuffer();
		JSONObject extInfoObject = new JSONObject(indexObject.getString("extInfo"));// 消息内容的主体对象
		String messageType = extInfoObject.getString("messageType");// 消息类型
		
		if (messageType.equals("TEXT")) {// 普通文本类型
			msgContent.append(indexObject.getString("bodys"));
			
		} else if (messageType.equals("REPLY")) {// 回复类型
			msgContent.append("[回复]<br>");
			msgContent.append(indexObject.getString("bodys"));
			msgContent.append("<br>_____________________________<br>");
			msgContent.append(extInfoObject.getString("replyName"));
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("replyText"));
			
		} else if (messageType.equals("IMAGE")) {// 图片类型
			msgContent.append("<img>");
			JSONObject bodysObject = new JSONObject(indexObject.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));
			
		} else if (messageType.equals("LIVEPUSH")) {// 生放送类型
			msgContent.append("[生放送]<br>");
			msgContent.append(extInfoObject.getString("liveTitle"));
			msgContent.append("<br>");
			msgContent.append(URL_LIVE);
			msgContent.append(extInfoObject.getString("liveId"));
			msgContent.append("<img>");
			msgContent.append(super.getImageUrl(extInfoObject.getString("liveCover")));
			
		} else if (messageType.equals("FLIPCARD")) {// 翻牌类型
			JSONObject contentObject = null;
			String questionId = extInfoObject.getString("questionId");
			String answerId = extInfoObject.getString("answerId");
			try {
				contentObject = this.getFlipcardContent(questionId, answerId);// 获取翻牌详情，目的是拿到这个翻牌是谁发的
			} catch (KeyManagementException | NoSuchAlgorithmException | JSONException | IOException e) {
				log.info("获取翻牌消息异常questionId={}, answerId={} : {}", questionId, answerId, e.getMessage());
			}
			msgContent.append("[");
			msgContent.append(indexObject.getString("bodys"));
			msgContent.append("]<br>");
			msgContent.append(extInfoObject.getString("answer"));
			msgContent.append("<br>______________________________<br>");
			msgContent.append(contentObject != null ? contentObject.getString("userName") : questionId + "|" + answerId);
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("question"));
			
		} else if (messageType.equals("EXPRESS")) {// 特殊表情类型
			msgContent.append("发送了一个特殊表情[");
			msgContent.append(extInfoObject.getString("emotionName"));
			msgContent.append("]，请到口袋48App查看。");
			
		} else if (messageType.equals("VIDEO")) {// 视频类型
			JSONObject bodysObject = new JSONObject(indexObject.getString("bodys"));
			msgContent.append("[视频]<br>点击➡️");
			msgContent.append(bodysObject.getString("url"));
			
		} else if (messageType.equals("VOTE")) {// 投票类型
			msgContent.append("[投票]<br>");
			msgContent.append(extInfoObject.getString("text"));
			msgContent.append("<br>详情请到口袋48App查看。");
			
		} else if (messageType.equals("AUDIO")) {// 语音类型
			JSONObject bodysObject = new JSONObject(indexObject.getString("bodys"));
			msgContent.append("[语音]<br>点击➡️");
			msgContent.append(bodysObject.getString("url"));
			
		} else {
			msgContent.append("type error.");
			log.info("本条消息为未知的新类型: {}", messageType);
			log.info(indexObject.toString());
		}
		
		return msgContent.toString();
	}

	/**
	 * 获取口袋房间翻牌详情信息<p>
	 * 本请求返回值包括问题、回答、提问人昵称、回答人昵称（重要）等。
	 * @param questionId 问题ID
	 * @param answerId 回答ID
	 * @return 翻牌详情的json对象
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private JSONObject getFlipcardContent(String questionId, String answerId) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		requestPropertys.put("token", TOKEN);
		/* 请求参数 */
		String payloadJson = "{\"questionId\":\"" + questionId + "\",\"answerId\":\"" + answerId + "\"}";
		/* 发送请求 */
		String flipcardJson = https.setDataType("POST")
														.setRequestProperty(requestPropertys)
														.setPayloadJson(payloadJson)
														.setUrl(URL_ROOM_MESSAGE_FLIPCARD)
														.send();
		JSONObject flipcardObject = new JSONObject(flipcardJson);
		if (flipcardObject.getBoolean("success")) {
			return flipcardObject.getJSONObject("content");
		}
		return null;
	}

	/**
	 * 发送请求获取用户资料
	 * @param userId 用户ID
	 * @return 用户资料的json对象
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private JSONObject getBaseUserInfo(String userId) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", APP_HEADER_ACCEPT);
		requestPropertys.put("Content-Type", APP_HEADER_CONTENT_TYPE);
		requestPropertys.put("User-Agent", APP_HEADER_USER_AGENT);
		/* 请求参数 */
		String payloadJson = "{\"userId\":\"" + userId + "\",\"needMuteInfo\":\"True\"}";
		String baseUserInfoJson = https.setDataType("POST")
																.setPayloadJson(payloadJson)
																.setRequestProperty(requestPropertys)
																.setUrl(URL_USER)
																.send();
		JSONObject userObject = new JSONObject(baseUserInfoJson);
		if (userObject.getBoolean("success")) {
			return userObject.getJSONObject("content")
											.getJSONObject("baseUserInfo");
		}
		return null;
	}
	
	/**
	 * 根据userId获取用户的昵称
	 * @param userId 用户ID
	 * @return 用户昵称
	 */
	private String getNickname(String userId) {
		JSONObject baseUserInfo = null;
		try {
			baseUserInfo = this.getBaseUserInfo(userId);
		} catch (Exception e) {
			log.info("【URL_USER】接口调用发生错误：{}。userId = {}", e.getMessage(), userId);
		}
		if (baseUserInfo != null) {
			return baseUserInfo.getString("nickname");
		} else {
			return userId;
		}
	}

	@Override
	public void syncDynamic() {
		log.info("同步微博动态...");
		try {
			super.syncDynamic();
		} catch (Exception e) {
			log.info("同步微博动态异常：{}", e.getMessage());
		}
		log.info("同步微博动态结束");
	}

	@Override
	public void syncModianPool()  {
		log.info("同步摩点项目...");
		try {
			super.syncModianPool();
		} catch (Exception e) {
			log.info("同步摩点项目发生异常: {}", e.getMessage());
		}
		log.info("同步摩点项目结束");
	}

	@Override
	public void syncWeiboUser() {
		log.info("同步微博用户资料...");
		super.syncWeiboUser();
		log.info("同步微博用户资料结束");
	}

	
	
}
