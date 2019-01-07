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
	 * 容器ID：获取用户微博数据的关键字段
	 */
	@Column(name = "CONTAINER_ID")
	private Long containerId;

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

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

}
