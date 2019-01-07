package com.gnz48.zzt.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.gnz48.zzt.entity.snh48.Member;
import com.gnz48.zzt.entity.snh48.RoomMessage;
import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.repository.snh48.RoomMessageRepository;
import com.gnz48.zzt.service.SendMessageService;

/**
 * @ClassName: SendToQQTask
 * @Description: 定时任务：同步最新数据并发送消息到QQ群
 * @author JuFF_白羽
 * @date 2018年7月10日 上午11:42:09
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class SendToQQTask {

	public SendToQQTask() {
	}

	/**
	 * 成员表DAO组件
	 */
	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 发送消息的服务
	 */
	@Autowired
	private SendMessageService sendMessageService;

	/**
	 * 口袋房间消息表DAO组件
	 */
	@Autowired
	private RoomMessageRepository roomMessageRepository;

	/**
	 * @Title: sendMessage
	 * @Description: 将新增的消息发送到指定的QQ群中
	 *               <p>
	 *               主要流程为：
	 *               <p>
	 *               发送新增的口袋房间消息到QQ窗口中，并将动态状态修改为2（已执行过发送任务）。
	 *               <p>
	 *               2018-08-02更新：为了防止多任务调用windows窗口，于是需要将所有调用QQ窗口发送消息的功能写到同一个任务中。
	 *               这里是新增发送微博动态到QQ窗口中，并将动态状态修改为2（已执行过发送任务）。
	 * @author JuFF_白羽
	 */
	public void sendMessage() {
		/* 获取口袋房间消息，发送到QQ */
		List<Member> members = memberRepository.findByRoomMonitor(1);// 获取房间监控开启的成员信息
		if (members != null) {
			for (int i = 0; i < members.size(); i++) {
				Member member = members.get(i);
				List<RoomMessage> roomMessages = roomMessageRepository
						.findByRoomIdAndIsSendOrderByMsgTimeAsc(member.getRoomId(), 1);
				if (roomMessages.size() > 0) {
					for (RoomMessage roomMessage : roomMessages) {
						sendMessageService.sendRoomMessage(roomMessage, member);
						roomMessage.setIsSend(2);
						roomMessageRepository.save(roomMessage);// 修改口袋房间消息状态
					}
				}
			}
		}

		/* 获取未发送过的微博动态，发送到QQ */
		sendMessageService.sendWeiboDynamic();

		/* 获取未发送过的摩点评论，发送到QQ */
		sendMessageService.sendMoDianComment();
	}

	/**
	 * @Description: 每日09:00和17:00发送当前总结消息到指定QQ
	 * @author JuFF_白羽
	 */
	public void sendDaySummarize() {
		/* 获取摩点众筹中的项目状况，发送到QQ */
		sendMessageService.sendModianPool();
	}

}
