package com.gnz48.zzt.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.repository.snh48.MemberRepository;
import com.gnz48.zzt.service.Https2019Service;
import com.gnz48.zzt.service.HttpsService;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @ClassName: MemberApi
 * @Description: 成员表操作控制类
 *               <p>
 *               主要用于提供成员表的增、删、改接口。
 * @author JuFF_白羽
 * @date 2018年7月24日 下午4:46:04
 */
@RestController
@RequestMapping("/member")
public class MemberApi {

	@Autowired
	private MemberRepository memberRepository;
	
	/**
	 * Https请求服务
	 */
	@Autowired
	private Https2019Service httpsService;

	/**
	 * @Description: 修改房间监控状态
	 * @author JuFF_白羽
	 */
	@PostMapping("/update/room-monitor")
	public ResultVO updateRoomMonitor(@RequestParam Long id, @RequestParam int roomMonitor) {
		ResultVO result = new ResultVO();
		int i = memberRepository.updateRoomMonitorById(id,roomMonitor);
		if (i == 1) {
			result.setStatus(200);
			result.setCause("成功");
		} else {
			result.setStatus(400);
			result.setCause("发生错误");
		}
		return result;
	}
	
	/**
	 * @Description: 同步成员信息到最新
	 * @author JuFF_白羽
	 */
	@GetMapping("/refresh")
	public ResultVO refreshMember(HttpServletRequest request) {
		ResultVO result = new ResultVO();
		httpsService.syncMember();
		result.setStatus(200);
		return result;
	}

}
