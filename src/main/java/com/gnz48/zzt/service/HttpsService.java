package com.gnz48.zzt.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.modian.MoDianComment;
import com.gnz48.zzt.entity.modian.MoDianPoolProject;
import com.gnz48.zzt.entity.snh48.Member;
import com.gnz48.zzt.entity.snh48.RoomMessage;
import com.gnz48.zzt.entity.weibo.Dynamic;
import com.gnz48.zzt.entity.weibo.WeiboUser;
import com.gnz48.zzt.repository.modian.MoDianCommentRepository;
import com.gnz48.zzt.repository.modian.MoDianPoolProjectRepository;
import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.repository.snh48.RoomMessageRepository;
import com.gnz48.zzt.repository.weibo.DynamicRepository;
import com.gnz48.zzt.repository.weibo.WeiboUserRepository;
import com.gnz48.zzt.util.DateUtil;
import com.gnz48.zzt.util.Https;
import com.gnz48.zzt.util.StringUtil;

/**
 * @ClassName: HttpsService
 * @Description: 发送Https请求获取JSON数据的服务,相当于模拟ajax发送请求
 * @author JuFF_白羽
 * @date 2018年7月6日 上午10:06:42
 */
@Service
@Transactional
public class HttpsService {
	
	/**
	 * 口袋48登录账号
	 */
	@Value(value = "${koudai48.username}")
	private String username;

	/**
	 * 口袋48登录密码
	 */
	@Value(value = "${koudai48.password}")
	private String password;

	/**
	 * 图片 视频等域名地址
	 */
	private static final String SOURCE_URL = "https://source.48.cn";

	/**
	 * 口袋房间消息表DAO组件
	 */
	@Autowired
	private RoomMessageRepository roomMessageRepository;

	/**
	 * 成员表DAO组件
	 */
	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 微博用户表DAO组件
	 */
	@Autowired
	private WeiboUserRepository userRepository;

	/**
	 * 微博动态表DAO组件
	 */
	@Autowired
	private DynamicRepository dynamicRepository;

	/**
	 * 摩点集资项目表DAO组件
	 */
	@Autowired
	private MoDianPoolProjectRepository moDianPoolProjectRepository;

	/**
	 * 摩点评论表DAO组件
	 */
	@Autowired
	private MoDianCommentRepository moDianCommentRepository;

	/**
	 * 微博用户表DAO组件
	 */
	@Autowired
	private WeiboUserRepository weiboUserRepository;

	/**
	 * @Title: getToken
	 * @Description: 发送登录请求，获取token参数。
	 * @author JuFF_白羽
	 * @return String token的字符串
	 */
	private String getToken() {
		Https https = new Https();
		Map<String, String> requestPropertys = new HashMap<String, String>();// 设置请求头
		requestPropertys.put("Content-Type", "application/json; charset=UTF-8");
		String payloadJson = "{account: \"" + username + "\", password: \"" + password + "\"}";// 设置用输出流写出的json参数
		String result = https.setUrl("https://puser.48.cn/usersystem/api/user/v1/web/login/phone")
				.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).setDataType("POST").send();
		JSONObject jsonObject = new JSONObject(result);
		String message = jsonObject.getString("message");
		if (message.equals("success")) {
			JSONObject contentObject = jsonObject.getJSONObject("content");
			return contentObject.getString("token");
		}
		return null;
	}

	/**
	 * @Title: getMsgContent
	 * @Description: 根据消息类型来自动生成消息内容
	 * @author JuFF_白羽
	 * @param messageObject
	 *            消息类型:
	 *            text,image,live,diantai,faipaiText,idolFlip,audio,videoRecord...
	 * @param dataObject
	 *            消息json对象
	 * @return String 消息内容
	 */
	private String getMsgContent(String messageObject, JSONObject dataObject) {
		StringBuffer msgContent = new StringBuffer();
		JSONObject extInfoObject = new JSONObject(dataObject.getString("extInfo"));
		if (messageObject.equals("text")) {
			msgContent.append(extInfoObject.getString("text"));
		} else if (messageObject.equals("image")) {
			msgContent.append("<img>");
			JSONObject bodysObject = new JSONObject(dataObject.getString("bodys"));
			msgContent.append(bodysObject.getString("url"));
		} else if (messageObject.equals("live") || messageObject.equals("diantai")) {
			msgContent.append("[");
			msgContent.append(dataObject.getString("bodys"));
			msgContent.append("]<br>");
			msgContent.append(extInfoObject.getString("referenceTitle"));
			msgContent.append("：");
			msgContent.append(extInfoObject.getString("referenceContent"));
			msgContent.append("<img>");
			msgContent.append(SOURCE_URL);
			msgContent.append(extInfoObject.getString("referencecoverImage"));
		} else if (messageObject.equals("faipaiText")) {
			String nickName = getNameByFaipaiUserId(extInfoObject.getLong("faipaiUserId"));
			msgContent.append("回复@");
			msgContent.append(nickName);
			msgContent.append("：");
			msgContent.append(extInfoObject.getString("messageText"));
			msgContent.append("<br>");
			msgContent.append("----------<br>");
			msgContent.append(nickName);
			msgContent.append("：");
			msgContent.append(extInfoObject.getString("faipaiContent"));
		} else if (messageObject.equals("idolFlip")) {
			msgContent.append("[");
			msgContent.append(extInfoObject.getString("idolFlipTitle"));
			msgContent.append("]<br>");
			msgContent.append(extInfoObject.getString("idolFlipUserName"));
			msgContent.append("：<br>");
			msgContent.append(extInfoObject.getString("idolFlipContent"));
		} else if (messageObject.equals("audio")) {
			msgContent.append("发送了一条[语音消息]，请到口袋房间查看。");
		} else if (messageObject.equals("videoRecord")) {
			msgContent.append("发送了一条[视频消息]，请到口袋房间查看。");
		}
		return StringUtil.removeNonBmpUnicode(msgContent.toString());
	}

	/**
	 * @Title: syncMember
	 * @Description: 同步成员信息到数据库中
	 *               <p>
	 *               主要流程为：
	 *               <p>
	 *               向获取全体成员信息的地址发送请求，获取并遍历全体成员信息。 遍历时再根据成员ID获取成员房间信息。
	 *               将信息放入声明的Member对象中并存入数据库中。其中监控字段需要进行查询判断，若该房间信息已存在则沿用旧状态，
	 *               若为新增房间则给予默认的不监控状态(=2)。
	 * @author JuFF_白羽
	 */
	public void syncMember() {
		Https https = new Https();
		/* 获取成员信息 */
		String result = https.setDataType("GET").setUrl("https://h5.48.cn/memberPage/member_mapping.json").send();
		JSONObject jsonObject = new JSONObject(result);
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {// 遍历全体成员信息对象
			String key = (String) iterator.next();
			JSONObject memberObject = jsonObject.getJSONObject(key);
			Member member = new Member();
			member.setId(memberObject.getLong("memberId"));// 成员ID
			member.setAvatar(getAvatar(memberObject.getString("memberAvatar")));// 成员头像地址
			member.setName(memberObject.getString("memberName"));// 成员名字
			member.setPinyin(memberObject.getString("memberPinyin"));// 成员名字拼音
			member.setTeamId(memberObject.getLong("teamId"));// 所属队伍ID
			member.setTeamName(memberObject.getString("teamName"));// 所属队伍名字
			member.setGroupId(memberObject.getLong("groupId"));// 所属团体ID
			member.setGroupName(memberObject.getString("groupName"));// 所属团体名字
			/* 获取成员房间相关信息 */
			Map<String, String> requestPropertys = new HashMap<String, String>();// 设置请求头
			requestPropertys.put("Content-Type", "application/json; charset=UTF-8");
			requestPropertys.put("User-Agent",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
			String payloadJson = "{memberId: \"" + memberObject.getLong("memberId") + "\"}";// 设置json参数
			result = https.setDataType("POST")
					.setUrl("https://pjuju.48.cn/imsystem/api/im/v1/member/room/info/memberId")
					.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
			JSONObject jsonObject2 = new JSONObject(result);
			String message = jsonObject2.getString("message");
			if (message.equals("success")) {// 排除房间不存在的成员
				JSONObject roomObject = jsonObject2.getJSONObject("content");
				member.setRoomId(roomObject.getLong("roomId"));// 口袋房间ID
				member.setRoomName(StringUtil.removeNonBmpUnicode(roomObject.getString("roomName")));// 口袋房间名字
				Member oldmember = memberRepository.findOne(memberObject.getLong("memberId"));
				/* 判断是否开启监控 */
				if (oldmember != null && oldmember.getRoomMonitor() == 1) {
					member.setRoomMonitor(1);// 成员口袋房间当前状态
				} else {
					member.setRoomMonitor(2);// 成员口袋房间监控状态，默认2关闭
				}
				member.setTopic(StringUtil.removeNonBmpUnicode(roomObject.getString("topic")));// 话题
			} else if (message.equals("房间不存在")) {
				member.setRoomMonitor(jsonObject2.getInt("status"));// 成员口袋房间当前状态
			}
			memberRepository.save(member);
		}
	}

	/**
	 * @Title: syncDynamic
	 * @Description: 同步微博动态到数据库中
	 *               <p>
	 *               主要流程为：
	 *               <p>
	 *               获取需要同步动态的微博用户的容器ID集合，每个容器ID都将作为参数像接口发送一次请求。解析得到的JSON结果，遍历微博动态，
	 *               根据mblog中的id字段判断该条动态是否同步过，如果查询数据库中存在则说明未有新动态，立即停止遍历。反之就将该条动态解析
	 *               后存入数据库，并继续遍历下一条数据。
	 * @author JuFF_白羽
	 */
	public void syncDynamic() {
		List<WeiboUser> users = userRepository.findAll();
		for (WeiboUser user : users) {
			String url = "https://m.weibo.cn/api/container/getIndex?containerid=" + user.getContainerId();
			Https https = new Https();
			Map<String, String> requestPropertys = new HashMap<String, String>();
			requestPropertys.put("Accept", "application/json, text/plain, */*");
			requestPropertys.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
			requestPropertys.put("X-Requested-With", "XMLHttpRequest");
			String result = https.setDataType("GET").setUrl(url).setRequestProperty(requestPropertys).send();
			JSONObject json = new JSONObject(result);
			JSONObject data = json.getJSONObject("data");
			if (data.has("cards")) {
				JSONArray cards = data.getJSONArray("cards");
				for (int i = 0; i < cards.length(); i++) {
					JSONObject card = cards.getJSONObject(i);
					int cardType = card.getInt("card_type");
					if (cardType == 9) {// 当为本人发的微博时
						JSONObject mblog = card.getJSONObject("mblog");
						Dynamic oldDynamic = dynamicRepository.findOne(mblog.getLong("id"));
						if (oldDynamic != null) {// 当该条动态存在数据库中时,判断是否置顶
							if (mblog.has("title")) {// 若为置顶博，则结束本次循环
								continue;
							} else {// 否则结束循环
								break;
							}
						}
						Dynamic dynamic = new Dynamic();
						dynamic.setCreatedAt(mblog.getString("created_at"));// 创建时间
						dynamic.setId(mblog.getLong("id"));// 动态ID
						dynamic.setWeiboContent(getWeiboContent(mblog));// 微博内容
						JSONObject userObject = mblog.getJSONObject("user");
						dynamic.setSenderName(userObject.getString("screen_name"));// 发送者姓名
						dynamic.setUserId(userObject.getLong("id"));// 发送者ID
						dynamic.setIsSend(1);// 是否已执行发送任务，默认为1否
						dynamic.setSyncDate(new Date());// 同步时间
						dynamicRepository.save(dynamic);
					}
				}
			}
		}
	}

	/**
	 * @Title: getWeiboContent
	 * @Description: mblog对象进行解析，拼接出需要的格式的微博内容
	 * @author JuFF_白羽
	 * @param mblog
	 *            mblog的JSONObject对象
	 * @return String 处理过的微博内容
	 */
	private String getWeiboContent(JSONObject mblog) {
		StringBuffer sb = new StringBuffer();
		String text = null;
		// 获取文本
		if (mblog.has("raw_text")) {// 当有特殊行文本时直接取，而不去解析普通html文本
			sb.append(StringUtil.removeNonBmpUnicode(mblog.getString("raw_text")));
		} else {
			text = StringUtil.removeNonBmpUnicode(mblog.getString("text")).replace("<br />", "$br$");
			Document doc = Jsoup.parse(text);
			sb.append(doc.text().replace("$br$", "<br>"));
		}
		
		// 获取转发内容
		if (mblog.has("retweeted_status")) {
			sb.append("<br>----------");
			sb.append("<br>转发@");
			JSONObject retStat = mblog.getJSONObject("retweeted_status");
			String retStatUserName = retStat.getJSONObject("user").getString("screen_name");
			sb.append(retStatUserName);
			sb.append("：<br>");
			sb.append(getWeiboContent(retStat));
		}
		
		// 获取图片
		if (mblog.has("pics")) {
			JSONArray pics = mblog.getJSONArray("pics");
			for (int i = 0; i < pics.length(); i++) {
				JSONObject pic = pics.getJSONObject(i);
				sb.append("<img>");
				sb.append(pic.getString("url"));
			}
		}
		
		return sb.toString();
	}

	/**
	 * @Title: syncRoomMessage
	 * @Description: 同步口袋房间消息到数据库中
	 *               <p>
	 *               主要流程为：
	 *               <p>
	 *               发送https请求获取房间信息json数据，解析json数据后将数据与数据库中的数据进行匹配。若数据库中已有该数据，
	 *               则结束解析完成本次定时任务；若数据库中未有匹配的数据，则将该条信息存入数据库中。
	 *               <p>
	 *               该任务的最外层循环由成员表决定，循环获取成员表中的房间ID来进行房间消息的请求发送。有件要注意的事是，这个获取
	 *               房间消息的请求是必须携带token才能拿到的，所以要先调用一次登录请求来拿到token。
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncRoomMessage() throws JSONException, ParseException {
		Https https = new Https();
		/* 同步口袋房间消息 */
		List<Member> members = memberRepository.findByRoomMonitor(1);// 获取房间监控开启的成员信息
		if (members != null) {
			String token = getToken();// 获取token
			if (token != null) {
				for (Member member : members) {
					Map<String, String> requestPropertys = new HashMap<String, String>();// 设置请求头
					requestPropertys.put("Content-Type", "application/json; charset=UTF-8");
					requestPropertys.put("token", token);
					String payloadJson = "{roomId: " + member.getRoomId().toString()
							+ ", lastTime: \"0\", limit: 20, chatType: 1}";// 设置用输出流写出的json参数
					String result = https.setUrl("https://pjuju.48.cn/imsystem/api/im/v1/member/room/message/mainpage")
							.setRequestProperty(requestPropertys).setPayloadJson(payloadJson).setDataType("POST")
							.send();
					JSONObject jsonObject = new JSONObject(result);
					String message = jsonObject.getString("message");
					if (message.equals("success")) {
						JSONObject contentObject = jsonObject.getJSONObject("content");
						JSONArray dataArray = contentObject.getJSONArray("data");
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataObject = dataArray.getJSONObject(i);
							String roomMessageId = dataObject.getString("msgidClient");
							RoomMessage roomMessage = roomMessageRepository.findOne(roomMessageId);
							if (roomMessage == null) {
								// 没有该条消息时，将该消息存入数据库中
								JSONObject extInfoObject = new JSONObject(dataObject.getString("extInfo"));
								roomMessage = new RoomMessage();
								roomMessage.setId(dataObject.getString("msgidClient"));
								roomMessage.setMsgTime(DateUtil.getDateFormat(dataObject.getLong("msgTime")));
								roomMessage.setRoomId(member.getRoomId());
								roomMessage.setSenderName(extInfoObject.getString("senderName"));
								roomMessage.setSenderId(extInfoObject.getLong("senderId"));
								roomMessage.setMessageObject(extInfoObject.getString("messageObject"));
								roomMessage.setMsgContent(
										getMsgContent(extInfoObject.getString("messageObject"), dataObject));
								roomMessage.setIsSend(1);
								roomMessage = roomMessageRepository.save(roomMessage);
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @Title: syncModianPool
	 * @Description: 同步摩点项目信息到数据库中
	 *               <p>
	 *               主要流程为：
	 *               <p>
	 *               获取数据库中的[众筹中]的项目集合，遍历后使用项目ID向项目信息接口发送请求，通过判断是否需要更新项目信息。
	 *               <p>
	 *               若需要更新该条项目信息，则解析JSON结果后将信息更新到数据库中；若不需要更新则结束本次循环。
	 *               <p>
	 *               在需要更新项目信息的情况下，在更新完毕后判断是否需要获取新的评论（集资信息）。若需要则向获取评论的接口发送请求，并解析JSON
	 *               结果后，同步到数据库中。
	 * 
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncModianPool() throws JSONException, ParseException {
		Https https = new Https();
		// 获取众筹中或者未同步过的摩点项目
		List<MoDianPoolProject> moDianPoolProjects = moDianPoolProjectRepository.findByStatusOrStatusIsNull("众筹中");
		if (moDianPoolProjects != null) {
			for (MoDianPoolProject moDianPoolProject : moDianPoolProjects) {
				// 遍历项目发送请求获取项目信息
				String poolUrl = "https://zhongchou.modian.com/realtime/get_simple_product?jsonpcallback=jQuery11110635395145031731_1533282805127&ids="
						+ moDianPoolProject.getId();
				// 设置请求头
				Map<String, String> poolRequestPropertys = new HashMap<String, String>();
				poolRequestPropertys.put("Accept",
						"text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01");
				poolRequestPropertys.put("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
				poolRequestPropertys.put("X-Requested-With", "XMLHttpRequest");
				String poolResult = https.setDataType("GET").setRequestProperty(poolRequestPropertys).setUrl(poolUrl)
						.send();
				// 解析摩点项目JSON结果
				int beginIndex = poolResult.indexOf("{");
				int endIndex = poolResult.lastIndexOf("}") + 1;
				poolResult = poolResult.substring(beginIndex, endIndex);
				poolResult = poolResult.replace("\\n", "");
				poolResult = poolResult.replace("\\\"", "");
				poolResult = poolResult.replace("\\/", "/");
				JSONObject poolObject = new JSONObject(poolResult);
				double backerMoney = poolObject.getDouble("backer_money_rew");
				// 判断已筹金额是否有变化
				if (moDianPoolProject.getBackerMoney() == null || moDianPoolProject.getBackerMoney() != backerMoney) {

					moDianPoolProject.setName(poolObject.getString("name"));// 项目名称
					moDianPoolProject.setStartTime(DateUtil.getDateFormat(poolObject.getString("start_time")));// 项目开始时间
					moDianPoolProject.setEndTime(DateUtil.getDateFormat(poolObject.getString("end_time")));// 项目结束时间
					moDianPoolProject.setCity(poolObject.getString("city"));// 发起位置
					moDianPoolProject.setInstallMoney(poolObject.getDouble("install_money"));// 目标金额(元)
					moDianPoolProject.setBackerMoney(poolObject.getDouble("backer_money_rew"));// 已筹金额(元)
					moDianPoolProject.setRate(poolObject.getDouble("rate"));// 完成度(%)
					moDianPoolProject.setBackerCount(poolObject.getInt("backer_count"));// 支持人数(个)
					moDianPoolProject.setStatus(poolObject.getString("status"));// 项目状态
					moDianPoolProject.setPostId(poolObject.getLong("moxi_post_id"));// 评论请求ID(发送获取评论请求的参数)
					// 更新该项目信息
					moDianPoolProjectRepository.save(moDianPoolProject);

					/*
					 * 获取项目（集资）评论，并同步到数据库中
					 */
					int size = 10;// 请求的size参数
					String postId = String.valueOf(moDianPoolProject.getPostId());// 请求的post_id参数
					for (int i = 1;; i++) {
						// 设置请求头
						Map<String, String> commentRequestPropertys = new HashMap<String, String>();
						commentRequestPropertys.put("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
						commentRequestPropertys.put("X-Requested-With", "XMLHttpRequest");
						// 设置url?参数
						Map<String, String> commentParams = new HashMap<String, String>();
						commentParams.put("jsonpcallback", "jQuery1111023448056440131615_1533632018147");
						commentParams.put("post_id", postId);
						commentParams.put("page", String.valueOf(i));
						commentParams.put("page_size", String.valueOf(size));
						String commentResult = https.setDataType("GET").setRequestProperty(commentRequestPropertys)
								.setParams(commentParams).setUrl("https://zhongchou.modian.com/comment/ajax_comments")
								.send();
						int beginIndex2 = commentResult.indexOf("{");
						int endIndex2 = commentResult.lastIndexOf("}") + 1;
						commentResult = commentResult.substring(beginIndex2, endIndex2);
						commentResult = commentResult.replace("\\n", "");
						commentResult = commentResult.replace("\\\"", "");
						commentResult = commentResult.replace("\\/", "/");
						JSONObject commentObject = new JSONObject(commentResult);
						String html = commentObject.getString("html");
						if (!html.equals("")) {
							Document doc = Jsoup.parse(html);
							Elements lis = doc.select("li");
							for (int j = 0; j < lis.size(); j++) {
								Element li = lis.get(j);
								Element div_commentText = li.selectFirst(".comment-txt");
								Element i_icon = div_commentText.selectFirst("i");
								if (i_icon != null) {// 判断是否为集资评论
									MoDianComment oldMoDianComment = moDianCommentRepository
											.findOne(Long.parseLong(li.attr("data-reply-id")));
									if (oldMoDianComment == null) {// 判断该条评论是否未同步
										MoDianComment moDianComment = new MoDianComment();
										moDianComment.setId(Long.parseLong(li.attr("data-reply-id")));// 序列
										Element div_userInfo = li.selectFirst(".user-info");
										Element a_user = div_userInfo.selectFirst(".nickname").selectFirst("a[href]");
										moDianComment.setUid(Long.parseLong(a_user.attr("href")
												.replace("https://me.modian.com/u/detail?uid=", "")));// 摩点用户ID
										moDianComment.setUname(a_user.text());// 摩点用户名
										Element p_time = div_userInfo.selectFirst(".time");
										moDianComment.setBackerDate(
												DateUtil.getDateFormat(processPtime(p_time.text()), "yyyy-MM-ddHH:mm"));// 筹集时间
										String commentText = div_commentText.text();
										commentText = commentText.replace("支持了", "");
										commentText = commentText.replace("元", "");
										moDianComment.setBackerMoney(Double.parseDouble(commentText.trim()));// 已筹金额(元)
										moDianComment.setProjectId(moDianPoolProject.getId());// 所属集资项目ID
										moDianComment.setIsSend(1);// 是否发送：默认1未发送
										// 同步该条评论到数据库
										moDianCommentRepository.save(moDianComment);
									} else {
										break;
									}
								} else {
									continue;
								}
							}
						} else {
							break;
						}
					}
				} else {
					// 若金额没有变化，判断是否已结束
					String status = poolObject.getString("status");
					if (status.equals("已结束")) {
						moDianPoolProject.setStatus(status);
						// 更新该项目信息
						moDianPoolProjectRepository.save(moDianPoolProject);
					}
					continue;
				}
			}
		}
	}

	/**
	 * @Title: processPtime
	 * @Description: 处理JSON结果中，html中p标签class为time的时间文本格式转换成yyyy-MM-ddHH:mm
	 * @author JuFF_白羽
	 * @param p_time
	 *            html中p标签class为time的文本内容
	 * @return String 返回格式为yyyy-MM-ddHH:mm的时间字符串
	 */
	private String processPtime(String p_time) {
		if (p_time.indexOf("今天") != -1) {
			p_time = p_time.replace("今天", DateUtil.getDate(new Date(), "yyyy-MM-dd"));
		} else if (p_time.indexOf("昨天") != -1) {
			p_time = p_time.replace("昨天", DateUtil.countDay("yyyy-MM-dd", -1));
		} else if (p_time.indexOf("分钟前") != -1) {
			p_time = p_time.replace("分钟前", "");
			p_time = DateUtil.countMin("yyyy-MM-ddHH:mm", -Integer.parseInt(p_time));
		} else if (p_time.indexOf("秒前") != -1) {
			p_time = p_time.replace("秒前", "");
			p_time = DateUtil.getDate(new Date(), "yyyy-MM-ddHH:mm");
		}
		return p_time;
	}

	/**
	 * @Description: 获取被翻牌用户的昵称
	 * @author JuFF_白羽
	 * @param userId
	 *            口袋用户ID
	 * @return String 口袋用户昵称
	 */
	private String getNameByFaipaiUserId(Long userId) {
		Https https = new Https();
		String name = String.valueOf(userId);
		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Content-Type", "application/json; charset=UTF-8");
		String payloadJson = "{needChatInfo: false, needFriendsNum: false, needRecommend: false}";
		String result = https.setUrl("https://puser.48.cn/usersystem/api/user/v1/show/info/" + userId)
				.setDataType("POST").setRequestProperty(requestPropertys).setPayloadJson(payloadJson).send();
		JSONObject jsonObject = new JSONObject(result);
		if (jsonObject.getString("message").equals("success")) {
			JSONObject userInfo = jsonObject.getJSONObject("content").getJSONObject("userInfo");
			name = userInfo.getString("nickName");
		}
		return name;
	}

	/**
	 * @Description: 根据SOURCE_URL，获取完整的头像地址
	 * @author JuFF_白羽
	 * @param memberAvatar
	 *            原头像不完整的地址
	 * @return String 完整的头像地址
	 */
	private String getAvatar(String memberAvatar) {
		if (memberAvatar.indexOf("http://") == -1) {
			memberAvatar = SOURCE_URL + memberAvatar;
		}
		return memberAvatar;
	}

	/**
	 * @Description: 根据容器ID发送请求获取微博用户实体信息
	 * @author JuFF_白羽
	 * @param containerId
	 *            容器ID
	 * @return WeiboUser 微博用户
	 */
	public WeiboUser getWeiboUser(Long containerId) {
		Https https = new Https();
		Map<String, String> params = new HashMap<String, String>();
		params.put("containerid", String.valueOf(containerId));

		Map<String, String> requestPropertys = new HashMap<String, String>();
		requestPropertys.put("Accept", "application/json, text/plain, */*");
		requestPropertys.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Mobile Safari/537.36");
		requestPropertys.put("MWeibo-Pwa", "1");
		requestPropertys.put("X-Requested-With", "XMLHttpRequest");
		requestPropertys.put("Referer", "https://m.weibo.cn/p/" + String.valueOf(containerId));

		String result = https.setDataType("GET").setUrl("https://m.weibo.cn/api/container/getIndex").setParams(params)
				.setRequestProperty(requestPropertys).send();
		JSONObject jsonObject = new JSONObject(result);
		
		if (jsonObject.getInt("ok") == 1) {// 成功返回结果
			JSONObject data = jsonObject.getJSONObject("data");
			
			if (data.has("cards")) {// cards情况的结果
				JSONArray cards = data.getJSONArray("cards");
				
				for (int i = 0; i < cards.length(); i++) {
					JSONObject card = cards.getJSONObject(i);

					if (card.getInt("card_type") == 9) {
						JSONObject user = card.getJSONObject("mblog").getJSONObject("user");

						WeiboUser weiboUser = new WeiboUser();
						weiboUser.setAvatarHd(user.getString("avatar_hd"));
						weiboUser.setContainerId(containerId);
						weiboUser.setFollowCount(user.getInt("follow_count"));
						weiboUser.setFollowersCount(user.getInt("followers_count"));
						System.out.println("粉丝数:" + user.getInt("followers_count"));
						weiboUser.setId(user.getLong("id"));
						weiboUser.setUserName(user.getString("screen_name"));

						return weiboUser;
					} else {
						continue;
					}
				}
			}
			
			if (data.has("userInfo")) {// userInfo情况的结果
				JSONObject userInfo = data.getJSONObject("userInfo");
				
				WeiboUser weiboUser = new WeiboUser();
				weiboUser.setAvatarHd(userInfo.getString("avatar_hd"));
				weiboUser.setContainerId(containerId);
				weiboUser.setFollowCount(userInfo.getInt("follow_count"));
				weiboUser.setFollowersCount(userInfo.getInt("followers_count"));
				System.out.println("粉丝数:" + userInfo.getInt("followers_count"));
				weiboUser.setId(userInfo.getLong("id"));
				weiboUser.setUserName(userInfo.getString("screen_name"));
				
				return weiboUser;
			}

		}
		return null;
	}

	/**
	 * @Description: 同步微博用户信息到最新
	 * @author JuFF_白羽
	 */
	public void syncWeiboUser() {
		List<WeiboUser> weiboUsers = weiboUserRepository.findAll();
		for (WeiboUser weiboUser : weiboUsers) {
			WeiboUser newWeiboUser = getWeiboUser(weiboUser.getContainerId());
			weiboUserRepository.save(newWeiboUser);
		}
	}

}
