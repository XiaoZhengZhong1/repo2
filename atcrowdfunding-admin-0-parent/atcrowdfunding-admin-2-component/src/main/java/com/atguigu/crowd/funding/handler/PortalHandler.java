package com.atguigu.crowd.funding.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalHandler {
	//本类为展示原始欢迎页面
	@RequestMapping("/index")
	public String showIndex(){
		
		// 从数据库加载页面要显示的数	
		return "index-page";
	}
}
