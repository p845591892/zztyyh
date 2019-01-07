//package com.gnz48.zzt.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.springframework.format.annotation.DateTimeFormat;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//
///**
// * @ClassName: Param
// * @Description: 请求参数表
// *               <p>
// *               用于存放URL携带的参数。
// * @author lcy
// * @date 2018年7月6日 上午11:09:00
// */
//@Entity
//@Table(name = "T_PARAM")
//public class Param {
//
//	/**
//	 * 自增序列
//	 */
//	@Id
//	@Column(name = "ID")
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//
//	/**
//	 * 关联的URL表的ID
//	 */
//	@Column(name = "T_URL_ID")
//	private Long urlId;
//
//	/**
//	 * 参数名
//	 */
//	@Column(name = "PARAM_KEY", length = 100)
//	private String key;
//
//	/**
//	 * 参数值
//	 */
//	@Column(name = "PARAM_VALUE", length = 1000)
//	private String value;
//
//	/**
//	 * 更新时间
//	 */
//	@Column(name = "UPDATE_TIME")
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//	private Date upDateTime;
//
//	/**
//	 * 更新人
//	 */
//	@Column(name = "RELANAME", length = 20)
//	private String realname;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Long getUrlId() {
//		return urlId;
//	}
//
//	public void setUrlId(Long urlId) {
//		this.urlId = urlId;
//	}
//
//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}
//
//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//
//	public Date getUpDateTime() {
//		return upDateTime;
//	}
//
//	public void setUpDateTime(Date upDateTime) {
//		this.upDateTime = upDateTime;
//	}
//
//	public String getRealname() {
//		return realname;
//	}
//
//	public void setRealname(String realname) {
//		this.realname = realname;
//	}
//
//}
