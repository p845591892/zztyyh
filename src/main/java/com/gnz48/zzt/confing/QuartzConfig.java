package com.gnz48.zzt.confing;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.gnz48.zzt.job.ScheduleRefreshTask;
import com.gnz48.zzt.job.SendToQQTask;
import com.gnz48.zzt.job.SyncTask;

/**
 * @ClassName: QuartzConfigration
 * @Description: Quartz配置
 * @author JuFF_白羽
 * @date 2018年3月16日 上午11:32:50
 */
@Configuration
public class QuartzConfig {

	private Logger LOGGER = LoggerFactory.getLogger(QuartzConfig.class);

	/*----------------------------------- 0:查询数据库并发送新增的消息 ------------------------------------------*/
	/**
	 * attention: Details：配置定时任务
	 */
	@Bean(name = "sendMessageJobDetail")
	public MethodInvokingJobDetailFactoryBean sendMessageJobDetailFactoryBean(SendToQQTask sendToQQTask) {// ScheduleTask为需要执行的任务

		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		/*
		 * 是否并发执行 例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
		 * 如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
		 */
		jobDetail.setConcurrent(false);

		jobDetail.setName("sendMessageJobDetail");// 设置任务的名字
		jobDetail.setGroup("1");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用

		/*
		 * 为需要执行的实体类对应的对象
		 */
		jobDetail.setTargetObject(sendToQQTask);

		/*
		 * sayHello为需要执行的方法
		 * 通过这几个配置，告诉JobDetailFactoryBean我们需要执行定时执行ScheduleTask类中的sayHello方法
		 */
		jobDetail.setTargetMethod("sendMessage");
		LOGGER.info("定时任务【查询数据库并发送新增的消息】配置完成");
		return jobDetail;

	}

	/**
	 * attention: Details：配置定时任务的触发器，也就是什么时候触发执行定时任务
	 */
	@Bean(name = "sendMessageCronTrigger")
	public CronTriggerFactoryBean sendMessageCronTriggerFactoryBean(JobDetail sendMessageJobDetail) {

		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		/*
		 * 为需要执行的定时任务
		 */
		jobTrigger.setJobDetail(sendMessageJobDetail);
		jobTrigger.setCronExpression("0 0/1 * * * ?");// 初始时的cron表达式
		jobTrigger.setName("sendMessageCronTrigger");// trigger的name
		return jobTrigger;

	}

	/*----------------------------------- 1:每小时同步成员信息任务 ------------------------------------------*/
	@Bean(name = "syncMemberJobDetail")
	public MethodInvokingJobDetailFactoryBean syncMemberJobDetailFactoryBean(SyncTask syncTask) {
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("syncMemberJobDetail");// 设置任务的名字
		jobDetail.setGroup("60");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
		jobDetail.setTargetObject(syncTask);
		jobDetail.setTargetMethod("syncMember");
		LOGGER.info("定时任务【同步成员信息】配置完成");
		return jobDetail;
	}

	@Bean(name = "syncMemberCronTrigger")
	public CronTriggerFactoryBean syncMemberCronJobTrigger(JobDetail syncMemberJobDetail) {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setJobDetail(syncMemberJobDetail);
		jobTrigger.setCronExpression("0 0 0/1 * * ?");// 初始时的cron表达式
		jobTrigger.setName("syncMemberCronTrigger");// trigger的name
		return jobTrigger;
	}

	/*----------------------------------- 2:每5分钟同步微博动态任务 ------------------------------------------*/
	@Bean(name = "syncDynamicJobDetail")
	public MethodInvokingJobDetailFactoryBean syncDynamicJobDetailFactoryBean(SyncTask syncTask) {
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("syncDynamicJobDetail");// 设置任务的名字
		jobDetail.setGroup("5");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
		jobDetail.setTargetObject(syncTask);
		jobDetail.setTargetMethod("syncDynamic");
		LOGGER.info("定时任务【同步微博动态】配置完成");
		return jobDetail;
	}

	@Bean(name = "syncDynamicCronTrigger")
	public CronTriggerFactoryBean syncDynamicCronJobTrigger(JobDetail syncDynamicJobDetail) {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setJobDetail(syncDynamicJobDetail);
		jobTrigger.setCronExpression("0 0/5 * * * ?");// 初始时的cron表达式
		jobTrigger.setName("syncDynamicCronTrigger");// trigger的name
		return jobTrigger;
	}

	/*------------------------------- 3:每3分钟同步口袋房间消息任务 -------------------------------------*/
	@Bean(name = "syncRoomMessageJobDetail")
	public MethodInvokingJobDetailFactoryBean syncRoomMessageJobDetailFactoryBean(SyncTask syncTask) {
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("syncRoomMessageJobDetail");// 设置任务的名字
		jobDetail.setGroup("3");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
		jobDetail.setTargetObject(syncTask);
		jobDetail.setTargetMethod("syncRoomMessage");
		LOGGER.info("定时任务【同步袋房间消息】配置完成");
		return jobDetail;
	}

	@Bean(name = "syncRoomMessageCronTrigger")
	public CronTriggerFactoryBean syncRoomMessageCronJobTrigger(JobDetail syncRoomMessageJobDetail) {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setJobDetail(syncRoomMessageJobDetail);
		jobTrigger.setCronExpression("0 0/3 * * * ?");// 初始时的cron表达式
		jobTrigger.setName("syncRoomMessageCronTrigger");// trigger的name
		return jobTrigger;
	}

	/*-------------------------------- 4:每2分钟同步摩点项目任务 --------------------------------------*/
	@Bean(name = "syncModianPoolJobDetail")
	public MethodInvokingJobDetailFactoryBean syncModianPoolJobDetailFactoryBean(SyncTask syncTask) {
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("syncModianPoolJobDetail");// 设置任务的名字
		jobDetail.setGroup("2");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
		jobDetail.setTargetObject(syncTask);
		jobDetail.setTargetMethod("syncModianPool");
		LOGGER.info("定时任务【同步摩点项目】配置完成");
		return jobDetail;
	}

	@Bean(name = "syncModianPoolCronTrigger")
	public CronTriggerFactoryBean syncModianPoolCronTrigger(JobDetail syncModianPoolJobDetail) {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setJobDetail(syncModianPoolJobDetail);
		jobTrigger.setCronExpression("0 0/2 * * * ?");// 初始时的cron表达式
		jobTrigger.setName("syncModianPoolCronTrigger");// trigger的name
		return jobTrigger;
	}

	/*---------------------------- 5:每日9,17时发送的总结消息到指定QQ ---------------------------------*/
	@Bean(name = "sendDaySummarizeJobDetail")
	public MethodInvokingJobDetailFactoryBean sendDaySummarizeJobDetailFactoryBean(SendToQQTask sendToQQTask) {// ScheduleTask为需要执行的任务
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("sendDaySummarizeJobDetail");
		jobDetail.setGroup("z9w5");
		jobDetail.setTargetObject(sendToQQTask);
		jobDetail.setTargetMethod("sendDaySummarize");
		LOGGER.info("定时任务【查询数据库并发送前一日的总结消息】配置完成");
		return jobDetail;
	}

	@Bean(name = "sendDaySummarizeCronTrigger")
	public CronTriggerFactoryBean sendDaySummarizeJobTrigger(JobDetail sendDaySummarizeJobDetail) {
		CronTriggerFactoryBean jobTrigger = new CronTriggerFactoryBean();
		jobTrigger.setJobDetail(sendDaySummarizeJobDetail);
		jobTrigger.setCronExpression("0 0 9,17 1/1 * ?");// 初始时的cron表达式
		jobTrigger.setName("sendDaySummarizeCronTrigger");// trigger的name
		return jobTrigger;
	}

	/*---------------------------- 6:启动时同步数据库的轮询任务配置(仅执行一次) ---------------------------------*/
	@Bean(name = "scheduleUpdateCronJobDetail")
	public MethodInvokingJobDetailFactoryBean scheduleUpdateCronJobDetailFactoryBean(
			ScheduleRefreshTask scheduleRefreshTask) {// ScheduleTask为需要执行的任务
		MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
		jobDetail.setConcurrent(false);
		jobDetail.setName("scheduleUpdateCronJobDetail");
		jobDetail.setGroup("0");
		jobDetail.setTargetObject(scheduleRefreshTask);
		jobDetail.setTargetMethod("scheduleUpdateCron");
		LOGGER.info("定时任务【同步数据库的轮询任务配置】配置完成");
		return jobDetail;
	}

	@Bean(name = "scheduleUpdateCronSimpleTrigger")
	public SimpleTriggerFactoryBean scheduleUpdateCronSimpleTrigger(JobDetail scheduleUpdateCronJobDetail) {
		SimpleTriggerFactoryBean simpleTrigger = new SimpleTriggerFactoryBean();
		simpleTrigger.setJobDetail(scheduleUpdateCronJobDetail);
		simpleTrigger.setRepeatCount(0);// 仅调度一次
		simpleTrigger.setName("scheduleUpdateCronSimpleTrigger");
		return simpleTrigger;
	}

	/*----------------------------------- 调度工厂 ------------------------------------------*/
	/**
	 * attention: Details：定义quartz调度工厂
	 */
	@Bean(name = "scheduler")
	public SchedulerFactoryBean schedulerFactory(Trigger[] triggers) {
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		// 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
		bean.setOverwriteExistingJobs(true);
		// 延时启动，应用启动1秒后
		bean.setStartupDelay(1);
		// 注册触发器
		LOGGER.info("当前共有{}个trigger", triggers.length);
//		bean.setTriggers(triggers);
		LOGGER.info("调度工厂配置完成,Quartz在应用启动1秒后启动");
		return bean;
	}

}
