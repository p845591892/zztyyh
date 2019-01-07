package com.gnz48.zzt.vo;

import com.gnz48.zzt.entity.QQCommunity;
import com.gnz48.zzt.entity.RoomMonitor;

/**
 * 
 * Copyright: Copyright (c) 2018 LanRu-Caifu
 * 
 * @ClassName: RoomMonitorVO.java
 * @Description: QQ群监控口袋房间的VO类
 *               <p>
 *               包含了QQ群监控口袋房间表和QQ群表的字段。
 *
 * @version: v1.0.0
 * @author: JuFF_白羽
 * @date: 2018年7月13日 上午12:05:53
 *
 */
public class RoomMonitorVO {

	/**
	 * QQ群监控口袋房间表
	 */
	private RoomMonitor roomMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public RoomMonitorVO() {
	}

	public RoomMonitorVO(RoomMonitor roomMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

	public RoomMonitorVO(QQCommunity qqCommunity) {
		RoomMonitor roomMonitor = new RoomMonitor();
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

	public RoomMonitorVO(RoomMonitor roomMonitor, QQCommunity qqCommunity) {
		this.roomMonitor = roomMonitor;
		this.qqCommunity = qqCommunity;
	}

	public RoomMonitor getRoomMonitor() {
		return roomMonitor;
	}

	public void setRoomMonitor(RoomMonitor roomMonitor) {
		this.roomMonitor = roomMonitor;
	}

	public QQCommunity getQqCommunity() {
		return qqCommunity;
	}

	public void setQqCommunity(QQCommunity qqCommunity) {
		this.qqCommunity = qqCommunity;
	}

}
