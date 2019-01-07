package com.gnz48.zzt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gnz48.zzt.entity.QuartzConfig;

/**
 * @ClassName: QuartzConfigRepository
 * @Description: 定时任务配置Repository
 * @author JuFF_白羽
 * @date 2018年3月16日 下午3:14:37
 */
public interface QuartzConfigRepository extends JpaRepository<QuartzConfig, Long> {

}
