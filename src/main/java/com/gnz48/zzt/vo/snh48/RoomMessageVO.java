package com.gnz48.zzt.vo.snh48;

import java.io.Serializable;

/**
 * @Description: 口袋房间消息VO
 * @author JuFF_白羽
 * @date 2019年4月10日 下午6:16:01
 */
public class RoomMessageVO implements Serializable {

	private static final long serialVersionUID = 7519962805620855349L;

	/**
	 * 消息ID，对应msgidClient字段
	 */
	private String id;

	/**
	 * 发送消息时间
	 */
	private String msgTime;

	/**
	 * 发送人姓名
	 */
	private String senderName;

	/**
	 * 发送人ID
	 */
	private Long senderId;

	/**
	 * 所属房间ID
	 */
	private Long roomId;

	/**
	 * 消息类型: text,image,live,diantai...
	 */
	private String msgObject;

	/**
	 * 消息内容: text类型
	 */
	private String msgContent;

	/**
	 * 是否发送
	 * <p>
	 * 这里是指是否执行过发送任务，并不是说一定会发送到指定的QQ
	 */
	private String isSend;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
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

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgObject() {
		return msgObject;
	}

	public void setMsgObject(String msgObject) {
		this.msgObject = msgObject;
	}

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

}
