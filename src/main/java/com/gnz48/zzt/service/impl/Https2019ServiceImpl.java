package com.gnz48.zzt.service.impl;

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
	 * SNH48Group所有成员列表接口
	 */
	private static final String URL_ALL_MEMBER_LIST = "https://h5.48.cn/memberPage/member_mapping.json";
	
	/**
	 * SNH48Group成员房间资料接口
	 */
	private static final String URL_MEMBER_ROOM = "https://pocketapi.48.cn/im/api/v1/im/room/info/type/source";
	
	/**
	 * 登陆口袋48获取token(小赛艇聚聚项目提供的接口)
	 */
	private static final String URL_TOKEN = "https://xsaiting.com/pocket48/static/proxy.php?f=login";
	
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
	 * 2019年版Https服务的Dao组件
	 */
	@Autowired
	private Https2019Dao https2019Dao;
	
	/**
	 * 成员表DAO组件
	 */
	@Autowired
	private MemberRepository memberRepository;
	
	/**
	 * 口袋房间消息表DAO组件
	 */
	@Autowired
	private RoomMessageRepository roomMessageRepository;
	
	@Override
	public void syncMember() {
		JSONObject jsonObject = this.getAllMember();
		/* 遍历全体成员信息对象 */
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {

			/* 获取成员普通资料 */
			String key = iterator.next();
			JSONObject memberObject = jsonObject.getJSONObject(key);
			Member member = new Member();
			member.setId(memberObject.getLong("memberId"));// 成员ID
			member.setAvatar(super.getImageUrl(memberObject.getString("memberAvatar")));// 成员头像地址
			member.setName(memberObject.getString("memberName"));// 成员名字
			member.setPinyin(memberObject.getString("memberPinyin"));// 成员名字拼音
			member.setTeamId(memberObject.getLong("teamId"));// 所属队伍ID
			member.setTeamName(memberObject.getString("teamName"));// 所属队伍名字
			member.setGroupId(memberObject.getLong("groupId"));// 所属团体ID
			member.setGroupName(memberObject.getString("groupName"));// 所属团体名字

			/* 获取成员房间资料 */
			JSONObject roomObject = this.getMemberRoom(memberObject.getLong("memberId"), 0, 5);
			// 若取不到，则结束该成员资料的同步
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
	}
	
	/**
	 * 发送请求获取SNH48Group所有成员的列表
	 * @return 全体成员资料的json对象
	 */
	private JSONObject getAllMember() {
		Https https = new Https();
		String allMember = https.setDataType("GET").setUrl(URL_ALL_MEMBER_LIST).send();
		JSONObject allMemberObject = new JSONObject(allMember);
		return allMemberObject;
	}
	
	/**
	 * 发送请求获取SNH48Group成员的房间资料
	 * @param sourceId 成员ID
	 * @param type 请求类型(默认填0，暂不推荐其他参数。)
	 * @param again 再次尝试发送请求次数
	 * @return 房间资料的json对象
	 */
	private JSONObject getMemberRoom(long sourceId, int type, int again) {
		if (again == 0) {
			log.info("已超过规定尝试次数，强制结束请求返回null");
			return null;
		}
		
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
		requestPropertys.put("appInfo", "{\"vendor\":\"a\",\"appType\":\"POCKET48\",\"deviceName\":\"b\",\"deviceId\":\"c\",\"appVersion\":\"d\",\"appBuild\":\"e\",\"osType\":\"android\",\"osVersion\":\"g\"}");
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
		log.info("获取成员房间资料失败。原因: {}。成员ID = {}；当前token = {}", roomObject.getString("message"), sourceId, TOKEN);
		this.setNewToken();
		again -= 1;
		return getMemberRoom(sourceId, type, again);
	}
	
	/**
	 * 发送请求获取token
	 * @return token字符串
	 */
	private String getToken() {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
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
		TOKEN = getToken();
		log.info("重置token = {}", TOKEN);
	}

	@Override
	public void syncRoomMessage()  {
		// 获取开启了监控的成员，逐一发送请求获取其口袋房间消息
		List<Member> members = memberRepository.findByRoomMonitor(1);
		for (Member member : members) {
			JSONObject messageObject = this.getRoomMessage(String.valueOf(member.getId()), String.valueOf(member.getRoomId()), member.getName(), 5);
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
				} catch (JSONException | ParseException e) {
					log.info("格式化时间发生错误。{}", roomMessage.toString());
				}
				
				roomMessageRepository.save(roomMessage);
			}
		}
	}
	
	/**
	 * 发送请求获取指定成员房间的消息
	 * @param memberId 成员ID
	 * @param roomId 房间ID
	 * @param memberName 成员名字(日志用，可null)
	 * @param again 再次尝试发送请求次数
	 * @return 房间消息的json对象
	 */
	private JSONObject getRoomMessage(String memberId, String roomId, String memberName, int again) {
		if (again == 0) {
			log.info("已超过规定尝试次数，强制结束请求返回null");
			return null;
		}
		
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
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
			msgContent.append(extInfoObject.getString("replyName"));
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("replyText"));
			msgContent.append("<br>_____________________________<br>");
			msgContent.append(indexObject.getString("bodys"));
			
		} else if (messageType.equals("IMAGE")) {// 图片类型
			msgContent.append("<img>");
			JSONObject bodysObject = new JSONObject(indexObject.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));
			
		} else if (messageType.equals("LIVEPUSH")) {// 生放送类型
			msgContent.append("[口袋生放送]<br>");
			msgContent.append(extInfoObject.getString("liveTitle"));
			msgContent.append("<br>");
			msgContent.append(URL_LIVE);
			msgContent.append(extInfoObject.getString("liveId"));
			msgContent.append("<img>");
			msgContent.append(super.getImageUrl(extInfoObject.getString("liveCover")));
			
		} else if (messageType.equals("FLIPCARD")) {// 翻牌类型
			msgContent.append("[");
			msgContent.append(indexObject.getString("bodys"));
			msgContent.append("]<br>");
			msgContent.append(this.getNickname(extInfoObject.getString("answerId")));
			msgContent.append(" : ");
			msgContent.append(extInfoObject.getString("question"));
			msgContent.append("<br>______________________________<br>");
			msgContent.append(extInfoObject.getString("answer"));
			
		} else if (messageType.equals("EXPRESS")) {// 特殊表情类型
			msgContent.append("发送了一个特殊表情[");
			msgContent.append(extInfoObject.getString("emotionName"));
			msgContent.append("]，请到App查看。");
			
		} else if (messageType.equals("VIDEO")) {// 视频类型
			JSONObject bodysObject = new JSONObject(indexObject.getString("bodys"));
			msgContent.append("[视频]<br>点击➡️");
			msgContent.append(bodysObject.getString("url"));
			
		} else {
			msgContent.append("type error.");
			log.info("本条消息为未知的新类型: {}", messageType);
			log.info(indexObject.toString());
		}
		
		return msgContent.toString();
	}

	/**
	 * 发送请求获取用户资料
	 * @param userId 用户ID
	 * @return 用户资料的json对象
	 */
	private JSONObject getBaseUserInfo(String userId) {
		Https https = new Https();
		/* 请求头 */
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("Content-Type", "application/json;charset=UTF-8");
		requestPropertys.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Safari/605.1.15");
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
		JSONObject baseUserInfo = this.getBaseUserInfo(userId);
		if (baseUserInfo != null) {
			return baseUserInfo.getString("nickname");
		} else {
			return userId;
		}
	}

	@Override
	public void syncDynamic() {
		super.syncDynamic();
	}

	@Override
	public void syncModianPool()  {
		try {
			super.syncModianPool();
		} catch (JSONException | ParseException e) {
			log.info("同步摩点项目发生异常: {}", e.getMessage());
		}
	}

	@Override
	public void syncWeiboUser() {
		super.syncWeiboUser();
	}

	
	
}