package com.atguigu.crowd.funding.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crowd.funding.entitys.Auth;
import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.service.api.AuthService;
import com.atguigu.crowd.funding.service.api.RoleService;

@Controller
public class AssignHandler {
	/*本类涉及多表查询,根据中间表t_admin_role的字段显示admin表和role表的关联关系.
	 * 查询用户Admin表的一个用户分配那些角色,和角色表中的一个角色分配那些用户*/
	@Autowired  //用户分配角色注解
	private RoleService roleService;
	
	//在角色和权限之间进行分配
	@ResponseBody
	@RequestMapping("/assign/do/assign")
	public ResultEntity<String> doRoleAssignAuth(
			@RequestBody Map<String, List<Integer>> assignDataMap) {
		
		authService.updateRelationShipBetweenRoleAndAuth(assignDataMap);
		
		return ResultEntity.successWithoutData();
	}
	
	
	
	@Autowired  //角色分配权限注解
	private AuthService authService;
	
	
	//根据指定的角色id查询具体的权限
	@ResponseBody
	@RequestMapping("/assign/get/assigned/auth/id/list")
	public ResultEntity<List<Integer>> getAssignedAuthIdList(@RequestParam("roleId") Integer roleId) {
		
		List<Integer> authIdList = authService.getAssignedAuthIdList(roleId);
		return ResultEntity.successWithData(authIdList);
	}
	
	//后端查询全部权限数据
	@ResponseBody	  
	@RequestMapping("/assign/get/all/auth")
	public ResultEntity<List<Auth>> getAllAuth(){
		List<Auth> authList = authService.getAllAuth();
		
		return ResultEntity.successWithData(authList);
	}

	/*在assign-role.jsp页面分配完用户和角色后,点击分配按钮时,表单发送"assign/role.html"
	 *请求,由本方法接收请求,和分配号的用户和角色id重新更新到中间表中 */
	//让方法执行后返回到主页面,带上请求时的pageNum让用户在返回首页时还在当前页面
	//因为是给指定用户分配角色,可能没有角色,页可能有多个角色.所有角色id设置为非必须的
	@RequestMapping("/assign/role")
	public String doAssignRole(
			// roleIdList不一定每一次都能够提供，没有提供我们也接受
			@RequestParam(value="roleIdList", required=false) List<Integer> roleIdList, 
			@RequestParam("adminId") Integer adminId,
			@RequestParam("pageNum") String pageNum) {
		
		roleService.updateRelationship(adminId, roleIdList);
		
		return "redirect:/admin/query/for/search.html?pageNum="+pageNum;
	}
		
	@RequestMapping("/assign/to/assign/role/page")
	public String toAssignRolePage(@RequestParam("adminId") Integer adminId, Model model) {
	
		// 1.查询已分配角色
		List<Role> assignedRoleList = roleService.getAssignedRoleList(adminId);
		
		// 2.查询未分配角色
		List<Role> unAssignedRoleList = roleService.getUnAssignedRoleList(adminId);
		
		// 3.存入模型
		model.addAttribute("assignedRoleList", assignedRoleList);
		model.addAttribute("unAssignedRoleList", unAssignedRoleList);
		
		return "assign-role";
	}
	
}
