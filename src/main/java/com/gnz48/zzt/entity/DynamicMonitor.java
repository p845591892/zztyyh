package com.gnz48.zzt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: DynamicMonitor
 * @Description: 微博动态监控配置表
 *               <p>
 *               QQ群与微博动态监控关系中间表。
 * @author JuFF_白羽
 * @date 2018年8月1日 上午10:53:06
 */
@Entity
@Table(name = "T_MONITOR_WEIBO_COMMUNITY")
public class DynamicMonitor {

	/**
	 * 自增序列
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	/**
	 * 序列(QQ群号)
	 */
	@Column(name = "COMMUNITY_ID")
	private Long communityId;

	/**
	 * 微博用户ID
	 */
	@Column(name = "USER_ID")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
