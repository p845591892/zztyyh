package com.gnz48.zzt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: Community
 * @Description: （yyh）QQ群信息
 * @author JuFF_白羽
 * @date 2018年7月6日 下午4:42:37
 */
@Entity
@Table(name = "T_QQ_COMMUNITY")
public class QQCommunity {

	/**
	 * 序列(QQ群号)
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * QQ群名(窗口名)
	 */
	@Column(name = "COMMUNITY_NAME", length = 500)
	private String communityName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	@Override
	public String toString() {
		return "QQCommunity [id=" + id + ", communityName=" + communityName + "]";
	}

}
