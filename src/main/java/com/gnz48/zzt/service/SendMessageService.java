package com.gnz48.zzt.service;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.dao.SendMessageDao;
import com.gnz48.zzt.entity.modian.MoDianComment;
import com.gnz48.zzt.entity.modian.MoDianPoolProject;
import com.gnz48.zzt.entity.snh48.Member;
import com.gnz48.zzt.entity.snh48.RoomMessage;
import com.gnz48.zzt.entity.weibo.Dynamic;
import com.gnz48.zzt.repository.CommentMonitorRepostiory;
import com.gnz48.zzt.repository.DynamicMonitorRepository;
import com.gnz48.zzt.repository.RoomMonitorRepository;
import com.gnz48.zzt.repository.modian.MoDianCommentRepository;
import com.gnz48.zzt.repository.modian.MoDianPoolProjectRepository;
import com.gnz48.zzt.repository.weibo.DynamicRepository;
import com.gnz48.zzt.util.DateUtil;
import com.gnz48.zzt.util.Https;
import com.gnz48.zzt.util.KeyboardUtil;
import com.gnz48.zzt.vo.CommentMonitorVO;
import com.gnz48.zzt.vo.DynamicMonitorVO;
import com.gnz48.zzt.vo.MoDianCommentVO;
import com.gnz48.zzt.vo.RoomMonitorVO;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

/**
 * @ClassName: SendMessageService
 * @Description: 发送消息的服务
 *               <p>
 *               根据不同的消息类型，生成不同的消息，并发送到指定的地方的服务类。
 * @author JuFF_白羽
 * @date 2018年7月12日 下午4:02:15
 */
@Service
@Transactional
public class SendMessageService {

	private static final Logger log = LoggerFactory.getLogger(SendMessageService.class);

	/**
	 * QQ群监控口袋房间表DAO组件
	 */
	@Autowired
	private RoomMonitorRepository roomMonitorRepository;

	/**
	 * 微博动态表DAO组件
	 */
	@Autowired
	private DynamicRepository dynamicRepository;

	/**
	 * 微博动态监控配置表DAO组件
	 */
	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

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
	 * 摩点项目评论监控配置表DAO组件
	 */
	@Autowired
	private CommentMonitorRepostiory commentMonitorRepostiory;

	/**
	 * 发送消息的服务的DAO组件
	 */
	@Autowired
	private SendMessageDao sendMessageDao;

	/**
	 * @Title: sendRoomMessage
	 * @Description: 向QQ群发送房间消息
	 *               <p>
	 *               根据监控的房间所归属的QQ群，向群里发送该条监控消息。
	 * @author JuFF_白羽
	 * @param roomMessage
	 *            口袋房间消息对象
	 * @param member
	 *            成员信息
	 */
	public void sendRoomMessage(RoomMessage roomMessage, Member member) {
		StringBuffer sb = new StringBuffer();
		sb.append("来自房间：");
		sb.append(member.getRoomName());
		sb.append("\n【话题】");
		sb.append(member.getTopic());
		sb.append("\n");
		sb.append(roomMessage.getSenderName());
		sb.append(" ");
		sb.append(DateUtil.getDate(roomMessage.getMsgTime()));
		sb.append("：");
		sb.append(roomMessage.getMsgContent());
		String message = sb.toString().replace("<br>", "\n");
		Long roomId = member.getRoomId();
		List<RoomMonitorVO> RoomMonitorVOs = roomMonitorRepository.findRoomMonitorAndQQCommunityByRoomId(roomId);
		if (RoomMonitorVOs != null) {
			// 该房间有被QQ群设为监控对象时
			for (RoomMonitorVO roomMonitorVO : RoomMonitorVOs) {
				String keywords = roomMonitorVO.getRoomMonitor().getKeywords();
				String communityName = roomMonitorVO.getQqCommunity().getCommunityName();
				Long communityId = roomMonitorVO.getQqCommunity().getId();
				try {
					if (keywords != null && !keywords.equals("")) {// 当有关键字筛选的时候
						String[] keyword = keywords.split(",");
						for (String key : keyword) {
							if (message.indexOf(key) != -1) {
								sendQQMessage(message, communityId, communityName);
							}
						}
					} else {
						sendQQMessage(message, communityId, communityName);
					}
				} catch (AWTException e) {
					log.info("AWTException：口袋房间消息ID[{}]发送到QQ时，Robot发生异常。", roomMessage.getId());
				}
			}
		}
	}

	/**
	 * @Function: SendMessageService.java
	 * @Description: 向指定QQ群发送消息
	 * @param: communityName
	 *             为必填的非空参数，用于发送消息。
	 * @param: message
	 *             要发送的消息内容。
	 * @param: communityId
	 *             为非必填参数，可为空，用于记录日志。
	 * @author: JuFF_白羽
	 * @throws AWTException
	 * @date: 2018年7月15日 上午2:36:11
	 */
	private int sendQQMessage(String message, Long communityId, String communityName) throws AWTException {
		int i = 2;// 发送成功返回2，失败返回1
		WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, communityName); // 第一个参数是Windows窗体的窗体类，第二个参数是窗体的标题。不熟悉windows编程的需要先找一些Windows窗体数据结构的知识来看看，还有windows消息循环处理，其他的东西不用看太多。
		if (hwnd == null) {
			i = 1;
			log.info("找不到窗口：[{}]", communityName);
		} else {
			Robot robot = new Robot();
			boolean showWindow = User32.INSTANCE.ShowWindow(hwnd, 9); // SW_RESTORE
			if (showWindow) {
				boolean setForegroundWindow = User32.INSTANCE.SetForegroundWindow(hwnd);
				if (setForegroundWindow) {
					String[] msgs = null;
					// 当消息中含有图片部分的时候
					if (message.contains("<img>")) {
						msgs = message.split("<img>");// 分割
						message = msgs[0];// 图片都在正文后，因此第一条一定是正文
					}
					// 等3秒
					robot.delay(3000);
					// 输入文字内容
					KeyboardUtil.keyPressString(robot, message);
					// 输入图片内容
					if (msgs != null && msgs.length >= 2) {
						inputImages(msgs, robot);
					}
					// 等0.5秒
					robot.delay(500);
					// 按下回车
					KeyboardUtil.keyPress(robot, KeyEvent.VK_ENTER);
				} else {
					i = 1;
					log.info("置顶窗口失败：[{}]", communityName);
				}
			} else {
				i = 1;
				log.info("显示窗口失败：[{}]", communityName);
			}
		}
		return i;
	}

	/**
	 * @Title: sendWeiboDynamic
	 * @Description: 向指定的QQ发送微博动态
	 * @author JuFF_白羽
	 * @throws AWTException
	 */
	public void sendWeiboDynamic() {
		List<Dynamic> dynamics = dynamicRepository.findByIsSendOrderBySyncDateAsc(1);
		if (dynamics != null) {
			for (Dynamic dynamic : dynamics) {
				List<DynamicMonitorVO> vos = dynamicMonitorRepository
						.findDynamicMonitorAndQQCommunityByUserId(dynamic.getUserId());
				if (vos != null) {
					StringBuffer sb = new StringBuffer();
					sb.append("来自微博：");
					sb.append(dynamic.getSenderName());
					sb.append("\n");
					sb.append(dynamic.getWeiboContent());
					String message = sb.toString().replace("<br>", "\n");
					for (DynamicMonitorVO vo : vos) {
						try {
							sendQQMessage(message, vo.getQqCommunity().getId(), vo.getQqCommunity().getCommunityName());
						} catch (AWTException e) {
							log.info("AWTException：微博动态ID[{}]发送到QQ时，Robot发生异常。", dynamic.getId());
						}
					}
				}
				dynamic.setIsSend(2);// 修改微博动态的状态
				dynamicRepository.save(dynamic);
			}
		}

	}

	/**
	 * 
	 * @Title: sendMoDianComment
	 * @Description: 向指定QQ发送摩点评论
	 * @author JuFF_白羽
	 * @throws AWTException
	 */
	public void sendMoDianComment() {
		List<MoDianCommentVO> commentVOs = moDianCommentRepository.findMoDianCommentAndMoDianPoolProjectBySendQQ(1);
		if (commentVOs != null) {
			for (MoDianCommentVO commentVO : commentVOs) {
				MoDianComment comment = commentVO.getMoDianComment();// 评论
				MoDianPoolProject project = commentVO.getMoDianPoolProject();// 评论对应项目
				List<CommentMonitorVO> vos = commentMonitorRepostiory
						.findMoDianCommentAndQQCommunityByProjectId(comment.getProjectId());
				if (vos != null) {
					StringBuffer sb = new StringBuffer();
					sb.append("来自摩点：");
					sb.append(project.getName());
					sb.append("\n");
					sb.append(comment.getUname());
					sb.append(" ");
					sb.append(DateUtil.getDate(comment.getBackerDate(), "yyyy-MM-dd HH:mm"));
					sb.append("：\n");
					sb.append("集资了");
					sb.append(comment.getBackerMoney());
					sb.append("元，非常感谢您的支持。");
					sb.append("\n\n-------当前集资总额前五位\n");
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("id", project.getId());
					param.put("limit", 5);
					List<MoDianComment> comments = sendMessageDao.findMoDianCommentByProjectId(param);
					if (comments != null) {
						for (int i = 1; i <= comments.size(); i++) {
							sb.append("第" + i + "位：");
							sb.append(comments.get(i - 1).getUname());
							sb.append(" " + comments.get(i - 1).getBackerMoney() + "元");
							if (i != comments.size()) {
								sb.append("\n");
							}
						}
					}
					for (CommentMonitorVO vo : vos) {
						try {
							sendQQMessage(sb.toString(), vo.getQqCommunity().getId(),
									vo.getQqCommunity().getCommunityName());
						} catch (AWTException e) {
							log.info("AWTException：摩点评论ID[{}]发送到QQ时，Robot发生异常。", comment.getId());
						}
					}
				}
				comment.setIsSend(2);// 修改评论的发送状态
				moDianCommentRepository.save(comment);
			}
		}
	}

	/**
	 * @Description: 发送摩点项目的项目总状态
	 *               <p>
	 *               按照摩点项目监控配置，获取摩点项目信息发送到配置的QQ中。
	 *               <p>
	 *               其中还包括集资总额前5的排名。
	 * @author JuFF_白羽
	 * @throws AWTException
	 */
	public void sendModianPool() {
		List<MoDianPoolProject> modianPools = moDianPoolProjectRepository.findByStatus("众筹中");
		if (modianPools != null) {
			for (MoDianPoolProject modianPool : modianPools) {
				StringBuffer sb = new StringBuffer();
				sb.append("摩点项目：");
				sb.append(modianPool.getName());
				sb.append("\n项目状态：");
				sb.append(modianPool.getStatus());
				sb.append("\n目标金额：");
				sb.append(modianPool.getInstallMoney());
				sb.append("元\n已筹金额：");
				sb.append(modianPool.getBackerMoney());
				sb.append("元\n完成度：");
				sb.append(modianPool.getRate());
				sb.append("%\n项目结束时间：");
				sb.append(DateUtil.getDate(modianPool.getEndTime()));
				sb.append("\n\n-------当前集资总额前十位\n");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("id", modianPool.getId());
				param.put("limit", 10);
				List<MoDianComment> comments = sendMessageDao.findMoDianCommentByProjectId(param);
				if (comments != null) {
					for (int i = 1; i <= comments.size(); i++) {
						sb.append("第" + i + "位：");
						sb.append(comments.get(i - 1).getUname());
						sb.append(" " + comments.get(i - 1).getBackerMoney() + "元");
						if (i != comments.size()) {
							sb.append("\n");
						}
					}
				}
				// ---内容拼接end
				List<CommentMonitorVO> cmvs = commentMonitorRepostiory
						.findMoDianCommentAndQQCommunityByProjectId(modianPool.getId());
				for (CommentMonitorVO cmv : cmvs) {
					try {
						sendQQMessage(sb.toString(), cmv.getQqCommunity().getId(),
								cmv.getQqCommunity().getCommunityName());
					} catch (AWTException e) {
						log.info("AWTException：摩点项目总状态发送到QQ时，Robot发生异常。");
					}
				}
			}
		}
	}

	/**
	 * @Description: 获取图片并输入到文本框中
	 * @author JuFF_白羽
	 * @param robot
	 *            Java的自动化系统输入事件对象
	 * @param urls
	 *            图片地址数组
	 */
	private void inputImages(String[] urls, Robot robot) {
		Https https = new Https();
		for (int i = 1; i < urls.length; i++) {// 0为文字部分，故从1开始
			String url = urls[i];
			// 发送请求获取图片超类
			Image image = https.setUrl(url).getImage();
			// 输入图片
			KeyboardUtil.keyPressImage(robot, image);
		}
	}

}
