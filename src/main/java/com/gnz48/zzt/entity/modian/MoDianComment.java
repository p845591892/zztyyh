package com.gnz48.zzt.entity.modian;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @ClassName: MoDianComment
 * @Description: 摩点评论表
 *               <p>
 *               用于存储摩点评论。
 *               <p>
 *               但只存储集资评论，一般评论不存，即，存储个人单次集资信息。
 * @author JuFF_白羽
 * @date 2018年7月9日 上午11:30:58
 */
@Entity
@Table(name = "T_MODIAN_COMMENT")
public class MoDianComment {

	/**
	 * 序列，对应html中li标签的data-reply-id属性
	 */
	@Id
	@Column(name = "ID")
	private Long id;

	/**
	 * 摩点用户ID
	 */
	@Column(name = "USER_ID")
	private Long uid;

	/**
	 * 摩点用户名
	 */
	@Column(name = "USER_NAME", length = 100)
	private String uname;

	/**
	 * 已筹金额(元)
	 */
	@Column(name = "BACKER_MONEY")
	private Double backerMoney;

	/**
	 * 筹集时间
	 */
	@Column(name = "BACKER_DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date backerDate;

	/**
	 * 所属集资项目ID
	 */
	@Column(name = "PROJECT_ID")
	private Long projectId;

	/**
	 * 是否发送：1未发送，2已发送
	 * <p>
	 * 这里是指是否执行过发送任务，并不是说一定会发送到指定的QQ
	 */
	@Column(name = "IS_SEND")
	private Integer isSend;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Double getBackerMoney() {
		return backerMoney;
	}

	public void setBackerMoney(Double backerMoney) {
		this.backerMoney = backerMoney;
	}

	public Date getBackerDate() {
		return backerDate;
	}

	public void setBackerDate(Date backerDate) {
		this.backerDate = backerDate;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	@Override
	public String toString() {
		return "MoDianComment [id=" + id + ", uid=" + uid + ", backerMoney=" + backerMoney + ", backerDate="
				+ backerDate + "]";
	}

}
