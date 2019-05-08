package com.gnz48.zzt.service;

/**
 * 2019年版Https服务接口<p>
 * 主要提供各类应用的数据采集服务。<p>
 * 当前只针对升级为6.0.0以后版本的口袋48提供各种同步数据的服务。6.0.0版本之后，为了配合口袋接口的json格式对原代码做了调整和重构，
 * 并且更新了数据库版本，支持emoji表情。
 * @author shiro
 *
 */
public interface Https2019Service {
	
	/**
	 * 同步成员资料
	 */
	public void syncMember();
	
	/**
	 * 同步微博用户资料
	 */
	public void syncWeiboUser();
	
	/**
	 * 同步微博动态
	 */
	public void syncDynamic();

	/**
	 * 同步口袋48房间消息
	 */
	public void syncRoomMessage();

	/**
	 * 同步摩点集资项目
	 */
	public void syncModianPool();
	
}
