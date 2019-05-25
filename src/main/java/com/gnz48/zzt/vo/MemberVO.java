package com.gnz48.zzt.vo;

import java.io.Serializable;

/**
 * 成员表VO类
 * 
 * @author shiro
 */
public class MemberVO implements Serializable {

	private static final long serialVersionUID = -133268782764501682L;

	private String groupName;
	private String teamName;
	private String searchText;
	private String name;
	private String abbr;
	private String roomMonitor;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getRoomMonitor() {
		return roomMonitor;
	}

	public void setRoomMonitor(String roomMonitor) {
		this.roomMonitor = roomMonitor;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
