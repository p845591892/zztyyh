package com.gnz48.zzt.job;

import java.util.List;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.gnz48.zzt.entity.QuartzConfig;
import com.gnz48.zzt.repository.QuartzConfigRepository;
import com.gnz48.zzt.util.SpringUtil;

/**
 * @Description: 同步数据库中的轮询配置
 * @author JuFF_白羽
 * @date 2018年11月19日 下午5:17:02
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class ScheduleRefreshTask {

	public ScheduleRefreshTask() {
	}

	private static final Logger log = LoggerFactory.getLogger(ScheduleRefreshTask.class);

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

	/**
	 * @Description: 全量对比轮询任务的cron，并与数据库配置同步
	 * @author JuFF_白羽
	 * @throws SchedulerException
	 */
	public void scheduleUpdateCron() throws SchedulerException {
		List<QuartzConfig> configs = quartzConfigRepository.findAll();
		for (QuartzConfig config : configs) {
			String jobTrigger = config.getCronTrigger();// 获取triggerBean的name
			CronTrigger trigger = (CronTrigger) SpringUtil.getBean(jobTrigger);// 根据name获取对应的CronTrigger的bean
			/*
			 * 这个是关键点， 需要通过从调度工厂获取的当前trigger才能进行接下来的操作，
			 * 否则只通过bean的name拿到的trigger的配置都会一直是QuartzConfigration类中的配置
			 */
			trigger = (CronTrigger) scheduler.getTrigger(trigger.getKey());
			String currentCron = "";
			try {
				currentCron = trigger.getCronExpression();// 当前Trigger使用的规则
			} catch (NullPointerException e) {
				log.info("JobTrigger【{}】 not in org.quartz.CronTrigger.", config.getJobName());
				continue;
			}
			String searchCron = config.getCron();// 从数据库查询出来的
			if (!currentCron.equals(searchCron)) {// 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
				log.info("JobTrigger【{}】's cron change to [{}], resetting......", config.getJobName(), searchCron);
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(searchCron);// 表达式调度构建器
				trigger = trigger.getTriggerBuilder().withIdentity(trigger.getKey()).withSchedule(scheduleBuilder)
						.build();// 按新的cronExpression表达式重新构建trigger
				scheduler.rescheduleJob(trigger.getKey(), trigger);// 按新的trigger重新设置job执行
				log.info("Set up.");
			}
		}
		log.info("End of the task 【scheduleUpdateCron】.");
	}

}
