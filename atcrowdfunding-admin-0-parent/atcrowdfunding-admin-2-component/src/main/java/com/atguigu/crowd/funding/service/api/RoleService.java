package com.atguigu.crowd.funding.service.api;

import java.util.List;

import com.atguigu.crowd.funding.entitys.Role;
import com.github.pagehelper.PageInfo;

public interface RoleService {
	//角色页面关键字搜索查询,返回一个分页页面(所以需要每页显示条数和总页数)再加上搜索关键字参数
	//调用查询结果,得到查询结果后就能封装成一个分页页面
	PageInfo<Role> queryForKeywordWithPage(Integer pageNum,Integer pageSize,String keyword);

	//角色页面的批量删除操作
	void batchRemove(List<Integer> roleIdList);

	//角色页面role-page.jsp新增操作ajax的json请求
	void saveRole(String roleName);

	void updateRole(Role role);

	List<Role> getRoleByRoleId(List<Integer> roleIdLidt);

	//查询已经分配的角色
	List<Role> getAssignedRoleList(Integer adminId);
			   
	//查询未分配的校色
	List<Role> getUnAssignedRoleList(Integer adminId);

	/*在assign-role.jsp页面分配完用户和角色后,点击分配按钮时,表单发送"assign/role.html"
	 *请求,由本方法接收请求,和分配号的用户和角色id重新更新到中间表中 */
	void updateRelationship(Integer adminId, List<Integer> roleIdList);
}
