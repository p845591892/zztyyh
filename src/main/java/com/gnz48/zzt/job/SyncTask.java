package com.gnz48.zzt.job;

import java.text.ParseException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.gnz48.zzt.service.Https2019Service;

/**
 * @ClassName: SyncTask
 * @Description: 同步信息任务类
 * @author JuFF_白羽
 * @date 2018年7月27日 下午5:59:14
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class SyncTask {
	
	private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

	public SyncTask() {
	}

	@Autowired
	Https2019Service https2019Service;

	/**
	 * @Title: syncMember
	 * @Description: 定时任务：1.同步成员的信息； 2.同步微博用户信息。
	 * @author JuFF_白羽
	 */
	public void syncMember() {
		https2019Service.syncMember();
		https2019Service.syncWeiboUser();
	}

	/**
	 * @Title: syncDynamic
	 * @Description: 定时任务：同步微博动态
	 * @author JuFF_白羽
	 */
	public void syncDynamic() {
		https2019Service.syncDynamic();
	}

	/**
	 * @Title: syncRoomMessage
	 * @Description: 定时任务：同步口袋房间消息
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncRoomMessage() throws JSONException, ParseException {
		https2019Service.syncRoomMessage();
	}

	/**
	 * @Title: syncModianPool
	 * @Description: 定时任务：同步摩点集资项目
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncModianPool() throws JSONException, ParseException {
		https2019Service.syncModianPool();
	}

}
