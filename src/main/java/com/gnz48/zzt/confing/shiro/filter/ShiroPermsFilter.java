package com.gnz48.zzt.confing.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: Perms的自定义过滤器
 * @author JuFF_白羽
 * @date 2018年11月6日 上午9:56:52
 */
public class ShiroPermsFilter extends PermissionsAuthorizationFilter {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(ShiroPermsFilter.class);

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		// log.info("----->> shiro-Perms过滤");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String requestedWith = httpServletRequest.getHeader("X-Requested-With");
		Subject subject = SecurityUtils.getSubject();
		// 判断是否ajax请求
		if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {// 如果是ajax请求返回指定数据
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setContentType("application/json");
			ResultVO result = new ResultVO();
			result.setStatus(403);
			// 判断subject当前会话是否能提供证书认证
			if (subject.isAuthenticated()) {
				result.setCause("无权进行该操作");
			} else {
				result.setCause("请先登录后再进行该操作");
			}
			httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
		} else {// 不是ajax进行重定向处理
			// 判断subject当前会话是否能提供证书认证
			if (subject.isAuthenticated()) {
				httpServletResponse.sendRedirect("/403");
			} else {
				httpServletResponse.sendRedirect("/login");
			}
		}
		return false;
	}

}
