package com.gnz48.zzt.entity.snh48;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: RoomMessage
 * @Description: 口袋房间消息表
 * @author JuFF_白羽
 * @date 2018年7月10日 下午3:18:38
 */
@Entity
@Table(name = "T_SNH_ROOM_MESSAGE")
public class RoomMessage {

	/**
	 * 消息ID，对应msgidClient字段
	 */
	@Id
	@Column(name = "ID", length = 200)
	private String id;

	/**
	 * 发送消息时间
	 */
	@Column(name = "MESSAGE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date msgTime;

	/**
	 * 发送人姓名
	 */
	@Column(name = "SENDER_NAME", length = 50)
	private String senderName;

	/**
	 * 发送人ID
	 */
	@Column(name = "SENDER_ID")
	private Long senderId;

	/**
	 * 所属房间ID
	 */
	@Column(name = "ROOM_ID")
	private Long roomId;

	/**
	 * 消息类型: text,image,live,diantai...
	 */
	@Column(name = "MESSAGE_OBJECT", length = 50)
	private String messageObject;

	/**
	 * 消息内容: text类型
	 */
	@Column(name = "MESSAGE_CONTENT", columnDefinition = "text")
	private String msgContent;

	/**
	 * 是否发送：1未发送，2已发送
	 * <p>
	 * 这里是指是否执行过发送任务，并不是说一定会发送到指定的QQ
	 */
	@Column(name = "IS_SEND")
	private Integer isSend;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getMessageObject() {
		return messageObject;
	}

	public void setMessageObject(String messageObject) {
		this.messageObject = messageObject;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	@Override
	public String toString() {
		return "RoomMessage [id=" + id + ", msgTime=" + msgTime + ", senderName=" + senderName + ", senderId="
				+ senderId + ", roomId=" + roomId + ", messageObject=" + messageObject + ", msgContent=" + msgContent
				+ ", isSend=" + isSend + "]";
	}
	
}
