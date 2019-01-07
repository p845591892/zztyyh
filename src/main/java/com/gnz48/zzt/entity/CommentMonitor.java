package com.gnz48.zzt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: CommentMonitor
 * @Description: 摩点项目评论监控配置表
 *               <p>
 *               目标QQ与被监控的摩点项目关系的中间表
 * @author JuFF_白羽
 * @date 2018年8月9日 上午9:26:30
 */
@Entity
@Table(name = "T_MONITOR_MODIAN_COMMENT")
public class CommentMonitor {

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
	 * 摩点项目ID
	 */
	@Column(name = "PROJECT_ID")
	private Long projectId;

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

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

}
