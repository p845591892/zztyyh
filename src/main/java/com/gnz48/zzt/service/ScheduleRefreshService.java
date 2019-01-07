package com.gnz48.zzt.service;

import javax.annotation.Resource;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.repository.QuartzConfigRepository;

/**
 * @Description: 轮询服务类
 *               <p>
 *               轮询任务Quartz的操作服务类，提供动态修改配置的服务。
 * @author JuFF_白羽
 * @date 2018年11月19日 下午4:11:12
 */
@Service
@Transactional
public class ScheduleRefreshService {

	private Logger log = LoggerFactory.getLogger(ScheduleRefreshService.class);

	/**
	 * 定时任务配置Repository
	 */
	@Autowired
	private QuartzConfigRepository quartzConfigRepository;

	/**
	 * 定时任务调度工厂
	 */
	@Resource(name = "scheduler")
	private Scheduler scheduler;

}
