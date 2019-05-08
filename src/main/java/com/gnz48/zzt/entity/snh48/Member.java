package com.gnz48.zzt.entity.snh48;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: Member
 * @Description: SNH48官方成员表
 * @author JuFF_白羽
 * @date 2018年7月10日 上午11:06:11
 */
@Entity
@Table(name = "T_SNH_MEMBER")
public class Member {

	/**
	 * 成员ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 成员头像地址
	 */
	@Column(name = "AVATAR", length = 500)
	private String avatar;

	/**
	 * 成员名字
	 */
	@Column(name = "NAME", length = 50)
	private String name;

	/**
	 * 成员名字拼音
	 */
	@Column(name = "PINYIN", length = 50)
	private String pinyin;

	/**
	 * 所属队伍ID
	 */
	@Column(name = "TEAM_ID")
	private Long teamId;

	/**
	 * 所属队伍名字
	 */
	@Column(name = "TEAM_NAME", length = 50)
	private String teamName;

	/**
	 * 所属团体ID
	 */
	@Column(name = "GROUP_ID")
	private Long groupId;

	/**
	 * 所属团体名字
	 */
	@Column(name = "GROUP_NAME", length = 50)
	private String groupName;

	/**
	 * 口袋房间ID
	 */
	@Column(name = "ROOM_ID")
	private Long roomId;

	/**
	 * 成员口袋房间监控状态：1开启，2关闭，404房间不存在
	 */
	@Column(name = "ROOM_MONITOR")
	private Integer roomMonitor;

	/**
	 * 口袋房间名字
	 */
	@Column(name = "ROOM_NAME", length = 200)
	private String roomName;

	/**
	 * 话题
	 */
	@Column(name = "TOPIC", length = 500)
	private String topic;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomMonitor() {
		return roomMonitor;
	}

	public void setRoomMonitor(Integer roomMonitor) {
		this.roomMonitor = roomMonitor;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", avatar=" + avatar + ", name=" + name + ", pinyin=" + pinyin + ", teamId="
				+ teamId + ", teamName=" + teamName + ", groupId=" + groupId + ", groupName=" + groupName + ", roomId="
				+ roomId + ", roomMonitor=" + roomMonitor + ", roomName=" + roomName + ", topic=" + topic + "]";
	}

}
