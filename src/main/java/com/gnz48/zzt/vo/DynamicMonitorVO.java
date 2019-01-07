package com.gnz48.zzt.vo;

import com.gnz48.zzt.entity.DynamicMonitor;
import com.gnz48.zzt.entity.QQCommunity;

/**
 * @ClassName: DynamicMonitorVO
 * @Description: 微博动态监控配置VO类
 *               <p>
 * 				包含了微博动态监控和QQ表字段。
 * @author lcy
 * @date 2018年8月1日 上午11:53:35
 */
public class DynamicMonitorVO {

	/**
	 * 微博动态监控配置
	 */
	private DynamicMonitor dynamicMonitor;

	/**
	 * （yyh）QQ群信息
	 */
	private QQCommunity qqCommunity;

	public DynamicMonitorVO() {
	}

	public DynamicMonitorVO(DynamicMonitor dynamicMonitor) {
		QQCommunity qqCommunity = new QQCommunity();
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

	public DynamicMonitorVO(QQCommunity qqCommunity) {
		DynamicMonitor dynamicMonitor = new DynamicMonitor();
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

	public DynamicMonitorVO(DynamicMonitor dynamicMonitor, QQCommunity qqCommunity) {
		this.dynamicMonitor = dynamicMonitor;
		this.qqCommunity = qqCommunity;
	}

	public DynamicMonitor getDynamicMonitor() {
		return dynamicMonitor;
	}

	public void setDynamicMonitor(DynamicMonitor dynamicMonitor) {
		this.dynamicMonitor = dynamicMonitor;
	}

	public QQCommunity getQqCommunity() {
		return qqCommunity;
	}

	public void setQqCommunity(QQCommunity qqCommunity) {
		this.qqCommunity = qqCommunity;
	}

}
