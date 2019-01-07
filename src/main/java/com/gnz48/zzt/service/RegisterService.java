package com.gnz48.zzt.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnz48.zzt.entity.system.User;
import com.gnz48.zzt.repository.system.UserRepository;
import com.gnz48.zzt.util.DateUtil;
import com.gnz48.zzt.util.StringUtil;
import com.gnz48.zzt.vo.UserVO;

/**
 * @Description: 注册相关服务类
 * @author JuFF_白羽
 * @date 2018年12月7日 下午5:14:04
 */
@Service
public class RegisterService {

	private static final Logger log = LoggerFactory.getLogger(RegisterService.class);

	/**
	 * 用户表DAO组件
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * @Description: 验证用户名是否存在
	 * @author JuFF_白羽
	 * @param username
	 *            用户名
	 */
	public int validateUsername(String username) {
		if (username == null || username.trim().equals("")) {
			return 2;
		}
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @Description: 注册用户
	 * @author JuFF_白羽
	 */
	public int doRegister(UserVO vo) {
		if (vo.getUsername() == null) {
			return 2;
		}
		if (vo.getPassword() == null) {
			return 3;
		}
		if (vo.getNickname() == null) {
			return 4;
		}
		User user = new User();
		user.setNickname(vo.getNickname());
		user.setPassword(StringUtil.shiroMd5(vo.getUsername(), vo.getPassword()));
		user.setUsername(vo.getUsername());
		user.setState((byte) 0);
		user.setSalt(StringUtil.shiroMd5("zzt", DateUtil.getDate(new Date(), "yyyyMMdd")));
		try {
			userRepository.save(user);
			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
	}

}
