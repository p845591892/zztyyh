package com.gnz48.zzt.entity.weibo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: User
 * @Description: 微博用户表
 *               <p>
 *               用于存放需要同步微博消息的微博用户信息，和微博动态表以user_id字段关联。
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:12:30
 */
@Entity
@Table(name = "T_WEIBO_USER")
public class WeiboUser {

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 用户名
	 */
	@Column(name = "USERNAME", length = 50)
	private String userName;

	/**
	 * 容器ID：用户数据的关键字段
	 */
	@Column(name = "CONTAINER_USER_ID")
	private Long containerUserId;

	/**
	 * 容器ID：动态数据的关键字段
	 */
	@Column(name = "CONTAINER_DYNAMIC_ID")
	private Long containerDynamicId;

	/**
	 * 头像地址
	 */
	@Column(name = "AVATAR_HD", length = 500)
	private String avatarHd;

	/**
	 * 关注数
	 */
	@Column(name = "FOLLOW_COUNT")
	private Integer followCount;

	/**
	 * 粉丝数
	 */
	@Column(name = "FOLLOWERS_COUNT")
	private Integer followersCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getContainerUserId() {
		return containerUserId;
	}

	public void setContainerUserId(Long containerUserId) {
		this.containerUserId = containerUserId;
	}

	public Long getContainerDynamicId() {
		return containerDynamicId;
	}

	public void setContainerDynamicId(Long containerDynamicId) {
		this.containerDynamicId = containerDynamicId;
	}

	public String getAvatarHd() {
		return avatarHd;
	}

	public void setAvatarHd(String avatarHd) {
		this.avatarHd = avatarHd;
	}

	public Integer getFollowCount() {
		return followCount;
	}

	public void setFollowCount(Integer followCount) {
		this.followCount = followCount;
	}

	public Integer getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}

	@Override
	public String toString() {
		return "WeiboUser [id=" + id + ", userName=" + userName + ", containerUserId=" + containerUserId
				+ ", containerDynamicId=" + containerDynamicId + ", avatarHd=" + avatarHd + ", followCount="
				+ followCount + ", followersCount=" + followersCount + "]";
	}

}
