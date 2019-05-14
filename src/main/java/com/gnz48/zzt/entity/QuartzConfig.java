package com.gnz48.zzt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @ClassName: QuartzConfig
 * @Description: 定时任务配置实体类
 * @author JuFF_白羽
 * @date 2018年3月16日 下午2:58:19
 */
@Entity(name = "T_CONFIG_QUARTZ")
public class QuartzConfig {

	@Id
	@Column(name = "ID", length = 10)
	private Long id;

	@Column(name = "JOB_NAME", length = 50)
	private String jobName;

	@Column(name = "CRON_TRIGGER", length = 50)
	private String cronTrigger;

	@Column(name = "CRON", length = 20)
	private String cron;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getCronTrigger() {
		return cronTrigger;
	}

	public void setCronTrigger(String cronTrigger) {
		this.cronTrigger = cronTrigger;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

}
