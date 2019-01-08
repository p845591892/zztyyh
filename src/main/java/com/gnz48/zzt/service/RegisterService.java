package com.gnz48.zzt.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnz48.zzt.entity.system.User;
import com.gnz48.zzt.repository.system.UserRepository;
import com.gnz48.zzt.util.DateUtil;
import com.gnz48.zzt.util.Mail;
import com.gnz48.zzt.util.MathUtil;
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
	 * @param request
	 */
	public int doRegister(UserVO vo, HttpServletRequest request) {
		if (vo.getUsername() == null) {
			return 2;
		}
		if (vo.getPassword() == null) {
			return 3;
		}
		if (vo.getNickname() == null) {
			return 4;
		}
		if (vo.getEmail() == null) {
			return 5;
		}

		User user = new User();
		user.setNickname(vo.getNickname());
		user.setPassword(StringUtil.shiroMd5(vo.getUsername(), vo.getPassword()));
		user.setUsername(vo.getUsername());
		user.setState((byte) 0);
		user.setSalt(StringUtil.shiroMd5("zzt", DateUtil.getDate(new Date(), "yyyyMMdd")));
		user.setEmail(vo.getEmail());
		String emailCaptcha = MathUtil.random(6);
		user.setEmailCaptcha(emailCaptcha);

		try {
			user = userRepository.save(user);

			// 发送邮件验证码
			VelocityContext context = new VelocityContext();
			context.put("nickname", vo.getNickname());
			context.put("username", vo.getUsername());

			String url = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ "/register/activation?username=" + vo.getUsername() + "&emailCaptcha=" + emailCaptcha;

			context.put("url", url);
			Mail.sendTemplateMail("注册成功", "/templates/mail_template/register.vm", context, vo.getEmail());

			return 1;
		} catch (Exception e) {
			log.info(e.toString());
			userRepository.delete(user.getId());
			return 0;
		}
	}

	/**
	 * @Description: 激活账号
	 * @author JuFF_白羽
	 * @param username
	 *            用户名
	 * @param emailCaptcha
	 *            邮箱验证码
	 */
	public int activation(String username, String emailCaptcha) {

		if (username == null || emailCaptcha == null) {
			return 2;
		}

		User user = userRepository.findByUsername(username);
		if (user == null) {
			return 3;
		}

		if (user.getEmailCaptcha().equals(emailCaptcha)) {
			user.setEmailCaptcha(MathUtil.random(6));
			user.setState((byte) 1);
			userRepository.save(user);
			return 1;
		} else {
			return 3;
		}

	}

}
