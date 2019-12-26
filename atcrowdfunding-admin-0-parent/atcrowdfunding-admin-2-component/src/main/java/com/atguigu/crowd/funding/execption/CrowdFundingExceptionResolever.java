package com.atguigu.crowd.funding.execption;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.util.CrowdFundingConstant;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;
import com.google.gson.Gson;

@ControllerAdvice
public class CrowdFundingExceptionResolever {
	/*本类,以在mvc.xml中扫描本类包注解的方式.控制用户操作页面时抛出异常.把异常统一跳转到
	 * system-error页面
	 * */
	//表示扫描handler控制层所有的异常,
	@ExceptionHandler(value=Exception.class)
	public ModelAndView catchException(Exception exception,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		// 1.对当前请求进行检查
				boolean checkAsyncRequestResult = CrowdFundingUtils.checkAsyncRequest(request);
				
				// 2.如果是异步请求
				if(checkAsyncRequestResult) {
					
					// 根据异常类型在常量中的映射，使用比较友好的文字显示错误提示消息
					String exceptionClassName = exception.getClass().getName();
					
					String message = CrowdFundingConstant.EXCEPTION_MESSAGE_MAP.get(exceptionClassName);
					
					if(message == null) {
						message = "系统未知错误";
					}
					
					// 3.创建ResultEntity对象
					ResultEntity<String> resultEntity = ResultEntity.failed(ResultEntity.NO_DATA, message);
					
					// 4.将resultEntity转换为JSON格式
					Gson gson = new Gson();
					String json = gson.toJson(resultEntity);
					
					// 5.将json作为响应数据返回给浏览器
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(json);
					
					return null;
				}
				
		//创建一个视图模型对象
		ModelAndView mav = new ModelAndView();
		/*表示把异常添加到视图对象.再由页面,以请求对象调用异常对象,再掉消息对象(就是抛异常的提示消息)
		 *${requestScope.exception.message } message字段以及被封装到消息类中 */
		
		mav.addObject("exception", exception);
		//给视图对象设置一个页面名称
		mav.setViewName("system-error");
		//把视图返回
		return mav;
}
}
