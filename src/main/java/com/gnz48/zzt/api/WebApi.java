package com.gnz48.zzt.api;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gnz48.zzt.entity.system.User;
import com.gnz48.zzt.exception.shiro.ActivationAccountException;
import com.gnz48.zzt.repository.system.UserRepository;
import com.gnz48.zzt.service.WebService;
import com.gnz48.zzt.util.MathUtil;
import com.gnz48.zzt.util.StringUtil;

/**
 * @ClassName: WebApi
 * @Description: 页面跳转请求控制类
 * @author JuFF_白羽
 * @date 2018年7月19日 上午10:17:24
 */
@Controller
public class WebApi {

	private Logger log = LoggerFactory.getLogger(WebApi.class);

	/**
	 * 页面跳转请求服务类
	 */
	@Autowired
	private WebService webService;

	/**
	 * 用户表DAO组件
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * @Description: 跳转到首页
	 * @author JuFF_白羽
	 * @throws ParseException
	 */
	@GetMapping("/index")
	public ModelAndView toIndex(HttpServletRequest request) throws ParseException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mtbox", webService.getMtboxData());// .mtbox中的数据
		mav.addObject("ds", webService.getDsData());// .ds中的数据
		mav.setViewName("index");
		return mav;
	}

	/**
	 * @Description: 退出的时候是get请求，主要是用于退出
	 * @author JuFF_白羽
	 */
	@GetMapping(value = "/login")
	public String login() {
		return "login";
	}

	/**
	 * @Description: 登录
	 * @author JuFF_白羽
	 */
	@PostMapping("/doLogin")
	public ModelAndView doLogin(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		String username = request.getParameter("username");
		String password = StringUtil.shiroMd5(username, request.getParameter("password"));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 登录失败从request中获取shiro处理的异常信息。
		// ShiroLoginFailure:就是shiro异常类的全类名.
		ModelAndView mav = new ModelAndView();
		try {
			subject.login(token);
		} catch (UnknownAccountException unknownAccountException) {
			log.info("用户名错误：{}", username);
			mav.addObject("msg", "无效用户名");
			mav.setViewName("login");
			return mav;
		} catch (IncorrectCredentialsException incorrectCredentialsException) {
			log.info("密码错误：{}", username);
			mav.addObject("msg", "密码错误");
			mav.setViewName("login");
			return mav;
		} catch (LockedAccountException e) {
			log.info("用户被锁定：{}", username);
			mav.addObject("msg", "用户被锁定");
			mav.setViewName("login");
			return mav;
		} catch (ActivationAccountException e) {
			log.info("用户未激活：{}", username);
			mav.addObject("msg", "用户未激活");
			mav.setViewName("login");
			return mav;
		} catch (ExpiredCredentialsException e) {
			log.info("密码过期：{}", username);
			mav.addObject("msg", "凭证过期");
			mav.setViewName("login");
			return mav;
		} catch (ExcessiveAttemptsException e) {
			log.info("登录错误次数过多：{}", username);
			mav.addObject("msg", "登录错误次数过多");
			mav.setViewName("login");
			return mav;
		} catch (DisabledAccountException e) {
			log.info("账号被禁用：{}", username);
			mav.addObject("msg", "账号被禁用");
			mav.setViewName("login");
			return mav;
		} catch (Exception e) {
			log.info("其他错误");
			e.printStackTrace();
			mav.addObject("msg", e.getClass().getName());
			return mav;
		}
		Session session = subject.getSession();
		User user = userRepository.findByUsername(username);
		session.setAttribute("user", user);
		mav.addObject("msg", "");
		mav.setViewName("redirect:/index");
		return mav;
	}

	/**
	 * @Description: 登出
	 * @author JuFF_白羽
	 */
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		// 登录失败从request中获取shiro处理的异常信息。
		// shiroLoginFailure:就是shiro异常类的全类名.
		ModelAndView mav = new ModelAndView();
		String msg = "";
		try {
			subject.logout();
		} catch (Exception e) {
			log.info("其他错误:{}", e.toString());
		}
		mav.setViewName("login");
		return mav;
	}

	/**
	 * @Description: 跳转到403页
	 * @author JuFF_白羽
	 */
	@GetMapping("/403")
	public ModelAndView toUn(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("403");
		return mav;
	}

	/**
	 * @Title: toMemberTable
	 * @Description: 跳转到机器人配置-成员列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/resource-management/member-table")
	public ModelAndView toMemberTable(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("resource_management/member_table");
		return mav;
	}

	/**
	 * @Title: toQqTable
	 * @Description: 跳转到机器人配置-QQ列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/resource-management/qq-table")
	public ModelAndView toQqTable(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("resource_management/qq_table");
		return mav;
	}

	/**
	 * @Title: toModianTable
	 * @Description: 跳转到机器人配置-摩点项目列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/resource-management/modian-table")
	public ModelAndView toModianTable(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("resource_management/modian_table");
		return mav;
	}

	/**
	 * @Title: toModianTable
	 * @Description: 跳转到机器人配置-微博用户列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/resource-management/weibo-table")
	public ModelAndView toWeiboTable(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("resource_management/weibo_table");
		return mav;
	}

	/**
	 * @Description: 跳转到可视化数据-摩点项目
	 * @author JuFF_白羽
	 * @param projectIds
	 *            摩点项目ID，可用英文的“,”连接多个ID
	 */
	@GetMapping("/data-visualization/modian-visual")
	public ModelAndView toModianVisual(HttpServletRequest request, String projectIds) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("data_visualization/modian_visual");
		mav.addObject("projectIds", projectIds);
		if (projectIds != null && !projectIds.equals("")) {
			String[] ids = projectIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (!MathUtil.isNumeric(ids[i])) {
					mav.addObject("projectNames", null);
					mav.addObject("detailsTable", null);
					mav.addObject("msg", "查询ID填写错误");
					return mav;
				}
			}
			mav.addObject("projectNames", webService.getProjectNames(projectIds));
			mav.addObject("detailsTable", webService.getModianDetailsTable(projectIds));
		} else {
			mav.addObject("projectNames", null);
			mav.addObject("detailsTable", null);
		}
		return mav;
	}

	/**
	 * @Description: 跳转到机器人配置-轮询配置列表
	 * @author JuFF_白羽
	 */
	@GetMapping("/resource-management/quartz-confing-table")
	public ModelAndView toQuartzConfingTable(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("resource_management/quartz_confing_table");
		return mav;
	}

	/**
	 * @Description: 跳转到系统配置-用户管理
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-manage/system-user")
	public ModelAndView toSystemUser(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("system_manage/system_user");
		return mav;
	}

	/**
	 * @Description: 跳转到系统配置-角色管理
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-manage/system-role")
	public ModelAndView toSystemRole(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("system_manage/system_role");
		return mav;
	}

	/**
	 * @Description: 跳转到系统配置-用户管理-设置角色
	 * @author JuFF_白羽
	 * @param uid
	 *            用户ID
	 */
	@GetMapping("/system-manage/system-user/role/set/{uid}")
	public ModelAndView toSystemUserSetRole(HttpServletRequest request, @PathVariable Long uid) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("uid", uid);
		mav.setViewName("system_manage/system_user_role_layer");
		return mav;
	}

	/**
	 * @Description: 跳转到系统配置-角色管理-设置权限
	 * @author JuFF_白羽
	 * @param rid
	 *            角色ID
	 */
	@GetMapping("/system-manage/system-role/permission/set/{rid}")
	public ModelAndView toSystemRoleSetPermission(HttpServletRequest request, @PathVariable Long rid) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("rid", rid);
		mav.setViewName("system_manage/system_role_permission_layer");
		return mav;
	}

	/**
	 * @Description: 跳转到系统配置-资源管理
	 * @author JuFF_白羽
	 */
	@GetMapping("/system-manage/system-permission")
	public ModelAndView toSystemPermission(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("system_manage/system_permission");
		return mav;
	}

	/**
	 * @Description: 跳转到注册
	 * @author JuFF_白羽
	 */
	@GetMapping("/register")
	public ModelAndView toRegister(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("register");
		return mav;
	}

}
