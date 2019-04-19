package com.gnz48.zzt.job;

import java.text.ParseException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.gnz48.zzt.service.HttpsService;

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

	public SyncTask() {
	}

	/**
	 * Https请求服务
	 */
	@Autowired
	private HttpsService httpsService;

	/**
	 * @Title: syncMember
	 * @Description: 定时任务：1.同步成员的信息； 2.同步微博用户信息。
	 * @author JuFF_白羽
	 */
	public void syncMember() {
		/* 
		 *  接口已过时，无法再取得数据。
		 *  httpsService.syncMember();
		 */
		httpsService.syncWeiboUser();
	}

	/**
	 * @Title: syncDynamic
	 * @Description: 定时任务：同步微博动态
	 * @author JuFF_白羽
	 */
	public void syncDynamic() {
		httpsService.syncDynamic();
	}

	/**
	 * @Title: syncRoomMessage
	 * @Description: 定时任务：同步口袋房间消息
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncRoomMessage() throws JSONException, ParseException {
		/* 
		 * 接口已过时，无法再取得数据。
		 * httpsService.syncRoomMessage();
		 */
	}

	/**
	 * @Title: syncModianPool
	 * @Description: 定时任务：同步摩点集资项目
	 * @author JuFF_白羽
	 * @throws ParseException
	 * @throws JSONException
	 */
	public void syncModianPool() throws JSONException, ParseException {
		httpsService.syncModianPool();
	}

}
