package com.atguigu.crowd.funding.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.service.api.RoleService;
import com.github.pagehelper.PageInfo;

//@Controller
//@ResponseBody
//一个@RestController=@Controller+@ResponseBody=类中每个方法都有这两个注解
@RestController
public class RoleHandler {
	@Autowired
	private RoleService roleService;
	
	//角色页面role-page.jspg更新操作ajax的json请求
	//@ResponseBody
	@RequestMapping("/role/update/role")
	public ResultEntity<String> updateRole(Role role){
		roleService.updateRole(role);
		
		return ResultEntity.successWithoutData();
 
	}
	
	//角色页面role-page.jsp新增操作ajax的json请求
	//@ResponseBody
	@RequestMapping("/role/save/role")
	public ResultEntity<String> saveRole(@RequestParam("roleName") String roleName){
		roleService.saveRole(roleName);
		return ResultEntity.successWithoutData();
	}
	
	
	//在下面"角色页面的批量删除操作"进行之前,执行my-role.js页面的"url":"role/get/list/by/id/list.json",请求
	//批量删除前在模态框显示删除信息请求
	//@ResponseBody
	@RequestMapping("/role/get/list/by/id/list.json")
	//返回类型是我们自定义的类ResultEntity中返回数据方法,最终由@ResponseBody注解转换成json返回
	//方法参数是json请求中携带的参数,删除角色的id集合
	public ResultEntity<List<Role>> getRoleByRoleId(@RequestParam List<Integer> roleIdLidt){
		List<Role> roleList = roleService.getRoleByRoleId(roleIdLidt);
		return ResultEntity.successWithData(roleList); 
	}
	
	
	//角色页面的批量删除操作
	//@ResponseBody
	@RequestMapping("role/batch/remove")
	public ResultEntity<String> batchRemove(@RequestBody List<Integer> roleIdList) {
		roleService.batchRemove(roleIdList);
		return ResultEntity.successWithoutData();
	}
	
	/*接收页面由由单击首页的"角色"按钮,跳转到role-page.jsp页面取调用my-role.js页面发送的
	 * ajax请求.
	 * 
	 * 请求是:在角色页面进行关键字搜索请求.我们需要把查询后的结果进行重新分页,并且在页面的增删改操作
	 * 后都需要进行重新分页.并在用户当前页面显示操作结果.
	 * 
	 * 我们把分页功能需要的3个参数pageNum,pageSize,keyword都封装到my-role.js页面
	 * 最终用分解的办法,把获取3个参数,用3个函数方式获取,把所有分页功能在my-role.js页面完成
	 * 再到role-page.jsp页面头部份引入my-role.js文件.调用其中方法
	 * */
	/*因为是搜索查询,是json请求,要返回json字符串,我们在ResultEntily类中的,封装了返回
	 * 字符串数组的方法,来把查询的结果接返回.
	*/
	//需要传递分页需要从请求中获取3个参数,并给一个默认值
	//@ResponseBody
	@RequestMapping("/role/search/by/keyword")
	public ResultEntity<PageInfo<Role>> search(
				@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
				@RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
				@RequestParam(value="keyword", defaultValue="") String keyword
			) {
		
		// 1.查询得到PageInfo对象
		PageInfo<Role> pageInfo = roleService.queryForKeywordWithPage(pageNum, pageSize, keyword);
		
		// 2.封装结果对象返回
		return ResultEntity.successWithData(pageInfo);
	}

}
