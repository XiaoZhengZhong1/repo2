package com.atguigu.crowd.funding.handler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crowd.funding.entitys.Admin;
import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.service.api.AdminService;
import com.atguigu.crowd.funding.util.CrowdFundingConstant;
import com.github.pagehelper.PageInfo;

@Controller
public class AdminHandler {
	@Autowired
	private AdminService adminService;

	//具体的更新操作由admin-edit.jsp点击更新按钮后页面跳转
	//方法需要的参数:具体的用户,和用户所在的页面,方便更新后用户能在当前页面看到更新效果
	@RequestMapping("admin/update")
	public String updateAdmin(Admin admin, @RequestParam("pageNum") String pageNum){
		try {
			adminService.updateAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof DuplicateKeyException) {
				throw new RuntimeException(CrowdFundingConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		//在更新成功返回首页后带上用户请求一路带过来的当前页面,以方便用户能在当前页面看到更新结果
		return "redirect:/admin/query/for/search.html?pageNum="+pageNum;
	}
	
	
	//点击更新图标后的操作
	@RequestMapping("admin/to/edit/page")
	//更新需要指定参数,具体给那的个id用户更新,本方法只负责把更新后的结果存储到model中
	//具体的更新操作有admin-edit页面进行
	public String toEditPage(@RequestParam("adminId") Integer adminId,Model model){
	
		  Admin admin = adminService.getAdminById(adminId);
		  model.addAttribute("admin", admin);
		 return "admin-edit";
	}
	
	//注册新增操作
	@RequestMapping("admin/save")
	public String saveAdmin(Admin admin){
		
		try {
			adminService.saveAdmin(admin);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof DuplicateKeyException) {
				throw new RuntimeException(CrowdFundingConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		return "redirect:/admin/query/for/search.html";
	}
	
	
	//批量删除的相关操作
	//@ResponseBody将当前handler方法的返回值作为响应体返回，不经过视图解析器不需要前后缀
	//@ResponseBody就是把Java响应转换成json返回
	@ResponseBody
	@RequestMapping("admin/batch/remove")
	//@RequestBody是把请求的json转换成java
	public ResultEntity<String> BatchRemove(@RequestBody List<Integer> adminIdList ){
		try {
			adminService.BatchRemove(adminIdList);
			
			return ResultEntity.successWithoutData();
		} catch (Exception e) {

			return ResultEntity.failed(null,e.getMessage());
		}
			}
	
	//搜索查询和分页功能
	@RequestMapping("admin/query/for/search")
	public String queryForSearch(
			//表示,如果页面没有传递搜索参数,那就定义默认的参数,避免程序报错
			@RequestParam(value="pageNum" , defaultValue="1") Integer pageNum,
			@RequestParam(value="pageSize" , defaultValue="5") Integer pageSize,
			@RequestParam(value="keyword" , defaultValue="") String keyword,
			Model model){
		
		//调用业务层的方法执行搜索查询
		PageInfo<Admin> pageInfo = adminService.queryForKeywordSearch(pageNum, pageSize, keyword);
		
		//把查询到的信息pageInfo封装到conmon公用包中的信息类中.再添加到模型中
		model.addAttribute(CrowdFundingConstant.ATTR_NAME_PAGE_INFO, pageInfo);
		
		//最后让请求跳转到查询分页页面
		return "admin-page";
	}
	
	//演示登陆成功后退出操作
	@RequestMapping("admin/logout")
	public String logout(HttpSession session){
		//让用户是session域失效,就是退出登陆了
		session.invalidate();
		//退出后,让用户重定向到首页
		return "redirect:/index.html";
	}
	
	
	//演示,管理员登陆并进行用户名和密码的检验,并记住密码
	@RequestMapping("/admin/do/login")
	public String doLogin(
			@RequestParam("loginAcct") String loginAcct,
			@RequestParam("userPswd") String userPswd,
			HttpSession session,
			Model model){
		
		// 调用adminService的login方法执行登录业务逻辑，返回查询到的Admin对象
		Admin admin = adminService.login(loginAcct,userPswd);
		
		// 判断admin是否为null
		if(admin == null){
			
			model.addAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_ADMIN, CrowdFundingConstant.MESSAGE_LOGIN_FAILED);
			return "admin-login";
		}
		
		//如果登陆成功就记住密码,把用户插入session域
		session.setAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_ADMIN, admin);
		return "redirect:/admin/to/main/page.html";
	}
	
	
	
	//项目搭建环境后演示是否能连接到数据库,并到页面输出显示
	@RequestMapping("/admin/get/all")
	public String getAll(Model model){
		List<Admin> list = adminService.getAll();
	model.addAttribute("list", list);		
		return "admin-target";
	}
	
}
