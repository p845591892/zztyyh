package com.gnz48.zzt.confing.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gnz48.zzt.vo.ResultVO;

/**
 * @Description: Authc的自定义过滤器
 * @author JuFF_白羽
 * @date 2018年11月5日 下午3:29:56
 */
public class ShiroAuthcFilter extends AccessControlFilter {

	private Logger log = LoggerFactory.getLogger(ShiroAuthcFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IOException {
		// log.info("----->> shiro-Authc过滤");
		Subject subject = SecurityUtils.getSubject();
		// 判断subject当前会话是否能提供证书认证
		if (subject.isAuthenticated()) {
			return true;
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		String requestedWith = httpServletRequest.getHeader("X-Requested-With");
		// 判断是否ajax请求
		if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {// 如果是ajax请求返回指定数据
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setContentType("application/json");
			ResultVO result = new ResultVO();
			result.setStatus(403);
			result.setCause("请先登录后再进行该操作");
			httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
		} else {// 不是ajax进行重定向处理
			httpServletResponse.sendRedirect("/login");
		}
		return false;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

}
