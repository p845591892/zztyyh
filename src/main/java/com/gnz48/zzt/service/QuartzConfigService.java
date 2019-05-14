package com.gnz48.zzt.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnz48.zzt.entity.QuartzConfig;
import com.gnz48.zzt.repository.QuartzConfigRepository;
import com.gnz48.zzt.util.SpringUtil;
import com.gnz48.zzt.util.StringUtil;

/**
 * @Description: 定时任务配置列表操作服务类
 * @author JuFF_白羽
 * @date 2018年11月26日 下午4:20:05
 */
@Service
@Transactional
public class QuartzConfigService {

	private static final Logger log = LoggerFactory.getLogger(QuartzConfigService.class);

	/**
	 * 定时任务配置表Repository组件
	 */
	@Autowired
	private QuartzConfigRepository quartzConfigRepository;

	/**
	 * @Description: 修改一条定时任务配置
	 *               <p>
	 *               修改数据库数据成功后，再对定时任务调度工厂中的配置进行修改。
	 * @author lcy
	 * @throws SchedulerException
	 */
	public int updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		if (quartzConfig.getId() == null) {
			return 0;
		}
		if (StringUtil.isEmpty(quartzConfig.getJobName())) {
			return 1;
		}
		if (StringUtil.isEmpty(quartzConfig.getCronTrigger())) {
			return 2;
		}
		if (StringUtil.isEmpty(quartzConfig.getCron())) {
			return 3;
		}
		CronTrigger trigger = (CronTrigger) SpringUtil.getBean(quartzConfig.getCronTrigger());
		Scheduler scheduler = (Scheduler) SpringUtil.getBean("scheduler");
		trigger = (CronTrigger) scheduler.getTrigger(trigger.getKey());
		String currentCron = "";
		try {
			currentCron = trigger.getCronExpression();// 当前Trigger使用的规则
		} catch (NullPointerException e) {
			log.info("JobTrigger【{}】 not in org.quartz.CronTrigger.", quartzConfig.getJobName());
			return 4;
		}
		String searchCron = quartzConfig.getCron();// 从数据库查询出来的
		if (!currentCron.equals(searchCron)) {// 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
			log.info("JobTrigger【{}】's cron change to [{}], resetting......", quartzConfig.getJobName(), searchCron);
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);// 表达式调度构建器
			trigger = trigger.getTriggerBuilder().withIdentity(trigger.getKey()).withSchedule(scheduleBuilder).build();// 按新的cronExpression表达式重新构建trigger
			scheduler.rescheduleJob(trigger.getKey(), trigger);// 按新的trigger重新设置job执行
			log.info("Set up.");
		}
		quartzConfigRepository.save(quartzConfig);
		return 200;
	}

}
