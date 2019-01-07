package com.gnz48.zzt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: RoomMonitor
 * @Description: QQ群监控口袋房间表
 *               <p>
 *               QQ群与口袋房间监控关系中间表，并且可以设置房间消息关键字筛选。
 *               <p>
 *               筛选功能：若配置了关键字并开启筛选，则只会向QQ群返回包含关键字的消息，不包含则不发送。
 * @author JuFF_白羽
 * @date 2018年7月12日 下午5:44:59
 */
@Entity
@Table(name = "T_MONITOR_ROOM_COMMUNITY")
public class RoomMonitor {

	/**
	 * 自增序列
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	/**
	 * 口袋房间ID
	 */
	@Column(name = "ROOM_ID")
	private Long roomId;

	/**
	 * 序列(QQ群号)
	 */
	@Column(name = "COMMUNITY_ID")
	private Long communityId;

	/**
	 * 筛选的关键字
	 * <p>
	 * 关键字之间使用,隔开
	 */
	@Column(name = "KEY_WORDS", length = 1000)
	private String keywords;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

}
