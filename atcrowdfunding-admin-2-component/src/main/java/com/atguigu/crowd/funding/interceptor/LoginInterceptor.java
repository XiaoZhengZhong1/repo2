package com.atguigu.crowd.funding.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.atguigu.crowd.funding.entitys.Admin;
import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.util.CrowdFundingConstant;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;
import com.google.gson.Gson;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 通过request对象获取HttpSession对象
		HttpSession session = request.getSession();
		
		// 从Session域尝试获取已登录用户对象
		Admin admin = (Admin) session.getAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_ADMIN);
		
		// 如果没有获取到Admin对象
		if(admin == null) {
			
			// 进一步判断当前请求是否是异步请求
			boolean checkAsyncRequestResult = CrowdFundingUtils.checkAsyncRequest(request);
			if(checkAsyncRequestResult) {
				
				// 为异步请求的响应创建ResultEntity对象
				ResultEntity<String> resultEntity = ResultEntity.failed(ResultEntity.NO_DATA, CrowdFundingConstant.MESSAGE_ACCESS_DENIED);
				
				// 创建Gson对象
				Gson gson = new Gson();
				
				// 将ResultEntity对象转换为JSON字符串
				String json = gson.toJson(resultEntity);
				
				// 设置响应的内容类型
				response.setContentType("application/json;charset=UTF-8");
				
				// 将JSON字符串作为响应数据返回
				response.getWriter().write(json);
				
				// 表示不能放行，后续操作不执行
				return false;
				
			}
			
			// 将提示消息存入request域
			request.setAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_ACCESS_DENIED);
			
			// 转发到登录页面
			request.getRequestDispatcher("/WEB-INF/admin-login.jsp").forward(request, response);
			
			return false;
		}
		
		// 如果admin对象有效，则放行继续执行后续操作
		return true;
	}
	
}
