package com.gnz48.zzt.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.entity.DynamicMonitor;
import com.gnz48.zzt.entity.weibo.WeiboUser;
import com.gnz48.zzt.repository.DynamicMonitorRepository;
import com.gnz48.zzt.repository.weibo.WeiboUserRepository;
import com.gnz48.zzt.service.HttpsService;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: 微博动态监控数据操作接口
 * @author JuFF_白羽
 * @date 2018年9月21日 上午11:21:14
 */
@RestController
public class DynamicMonitorContorller {

	/**
	 * 微博动态监控配置表DAO组件
	 */
	@Autowired
	private DynamicMonitorRepository dynamicMonitorRepository;

	/**
	 * 发送Https请求获取JSON数据的服务
	 */
	@Autowired
	private HttpsService httpsService;
	
	/**
	 * 微博用户表DAO组件
	 */
	@Autowired
	private WeiboUserRepository weiboUserRepository;

	/**
	 * @Description: 新增一条微博动态监控数据
	 * @author: JuFF_白羽
	 * @date: 2018年9月20日 下午10:33:07
	 */
	@PostMapping("/dynamic-monitor/add")
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
	@PostMapping("/dynamic-monitor/delete")
	public ResultVO deleteDynamicMonitor(Long id) {
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

	/**
	 * @Description: 新增一条微博用户到微博监控
	 * @author JuFF_白羽
	 * @param containerId
	 *            容器ID
	 */
	@PostMapping("/weibo/add")
	public ResultVO addWeiboUser(Long containerId) {
		ResultVO result = new ResultVO();
		if (containerId == null) {
			result.setStatus(400);
			result.setCause("containerId不能为空");
		} else {
			WeiboUser weiboUser = httpsService.getWeiboUser(containerId);
			if (weiboUser != null) {
				weiboUserRepository.save(weiboUser);
				result.setStatus(200);
			} else {
				result.setStatus(400);
				result.setCause("新增失败");
			}
		}
		return result;
	}
	
	/**
	 * @Description: 根据微博ID删除监控微博信息以及相关配置，多个id可使用“,”连接
	 * @author JuFF_白羽
	 * @param id 微博用户ID
	 */
	@PostMapping("/weibo/delete")
	public ResultVO deleteWeiboUser(String id) {
		ResultVO result = new ResultVO();
		if (id != null) {
			String[] ids = id.split(",");
			for (String userId : ids) {
				dynamicMonitorRepository.deleteByUserId(Long.parseLong(userId));
				weiboUserRepository.delete(Long.parseLong(userId));
			}
			result.setStatus(200);
		} else {
			result.setStatus(400);
			result.setCause("ID不能为空");
		}
		return result;
	}

}
