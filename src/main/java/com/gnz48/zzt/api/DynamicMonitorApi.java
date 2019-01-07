package com.gnz48.zzt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.entity.DynamicMonitor;
import com.gnz48.zzt.repository.DynamicMonitorRepository;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: 微博动态监控数据操作接口
 * @author JuFF_白羽
 * @date 2018年9月21日 上午11:21:14
 */
@RestController
@RequestMapping("/dynamic-monitor")
public class DynamicMonitorApi {

	/**
	 * 微博动态监控配置表DAO组件
	 */
	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

	/**
	 * @Description: 新增一条微博动态监控数据
	 * @author: JuFF_白羽
	 * @date: 2018年9月20日 下午10:33:07
	 */
	@PostMapping("/add")
	public ResultVO addDynamicMonitor(DynamicMonitor dynamicMonitor) {
		ResultVO result = new ResultVO();
		if (dynamicMonitor.getCommunityId() != null && dynamicMonitor.getUserId() != null) {
			DynamicMonitor newDynamicMonitor = dynamicMonitorRepository.save(dynamicMonitor);
			if (newDynamicMonitor != null) {
				result.setStatus(200);
			} else {
				result.setStatus(400);
				result.setCause("新增失败");
			}
		} else {
			result.setStatus(400);
			result.setCause("QQ号或者用户ID不能为空");
		}
		return result;
	}

	/**
	 * @Title: deleteRoomMonitor
	 * @Description: 删除一条微博动态监控配置
	 * @author JuFF_白羽
	 * @param id
	 *            配置ID
	 */
	@PostMapping("/delete")
	public ResultVO deleteRoomMonitor(Long id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			dynamicMonitorRepository.delete(id);
			result.setStatus(200);
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
