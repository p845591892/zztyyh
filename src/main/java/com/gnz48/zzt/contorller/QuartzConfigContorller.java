package com.gnz48.zzt.contorller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.entity.QuartzConfig;
import com.gnz48.zzt.service.QuartzConfigService;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: 定时任务配置列表操作控制类
 *               <p>
 *               提供轮询配置列表页面的操作请求接口。
 * @author JuFF_白羽
 * @date 2018年11月26日 下午4:17:36
 */
@RestController
@RequestMapping("/quartz-config")
public class QuartzConfigContorller {

	/**
	 * 定时任务配置列表操作服务类
	 */
	@Autowired
	private QuartzConfigService quartzConfigService;

	/**
	 * @Description: 修改定时任务配置
	 * @author JuFF_白羽
	 * @throws SchedulerException
	 */
	@PostMapping("/update")
	public ResultVO updateQuartzConfig(QuartzConfig quartzConfig) throws SchedulerException {
		ResultVO result = new ResultVO();
		int i = quartzConfigService.updateQuartzConfig(quartzConfig);
		if (i == 0) {
			result.setCause("轮询配置ID不能为空");
		} else if (i == 1) {
			result.setCause("轮询任务名不能为空");
		} else if (i == 2) {
			result.setCause("定时任务触发器名不能为空");
		} else if (i == 3) {
			result.setCause("定时公式不能为空");
		} else if (i == 4) {
			result.setCause("定时任务不在调度工厂中");
		}
		result.setStatus(i);
		return result;
	}

//	/**
//	 * 新增一条定时任务
//	 * @param quartzConfig
//	 */
//	@PostMapping("/add")
//	public ResultVO addQuartzConfig(QuartzConfig quartzConfig) {
//		
//		return null;
//	}

}
