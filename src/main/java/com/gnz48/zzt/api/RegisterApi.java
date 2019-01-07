package com.gnz48.zzt.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gnz48.zzt.service.RegisterService;
import com.gnz48.zzt.util.Mail;
import com.gnz48.zzt.vo.ResultVO;
import com.gnz48.zzt.vo.UserVO;

/**
 * @Description: 注册相关控制类
 * @author JuFF_白羽
 * @date 2018年12月7日 下午5:04:20
 */
@RestController
public class RegisterApi {
	
	/**
	 * 注册相关服务类
	 */
	@Autowired
	private RegisterService registerService;

	/**
	 * @Description: 验证用户名是否可用
	 * @author JuFF_白羽
	 */
	@PostMapping("/register/validate-username")
	public ResultVO validateUsername(String username) {
		ResultVO result = new ResultVO();
		int i = registerService.validateUsername(username);
		if (i == 1) {
			result.setStatus(400);
			result.setCause("用户名已存在");
		} else if (i == 0) {
			result.setStatus(200);
		} else if (i == 2) {
			result.setStatus(400);
			result.setCause("用户名不能为空");
		}
		return result;
	}

	/**
	 * @Description: 注册账号
	 * @author JuFF_白羽
	 */
	@PostMapping("/doRegister")
	public ResultVO doRegister(HttpServletRequest request, UserVO user) {
		ResultVO result = new ResultVO();
		int i = registerService.doRegister(user);
		result.setStatus(400);
		if (i == 1) {
			result.setStatus(200);
		} else if (i == 2) {
			result.setCause("用户名不能为空");
		} else if (i == 3) {
			result.setCause("密码不能为空");
		} else if (i == 4) {
			result.setCause("昵称不能为空");
		} else {
			result.setCause("注册失败");
		}
		return result;
	}

}
