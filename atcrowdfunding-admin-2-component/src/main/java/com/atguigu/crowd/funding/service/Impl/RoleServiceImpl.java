package com.atguigu.crowd.funding.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.entitys.RoleExample;
import com.atguigu.crowd.funding.mapper.RoleMapper;
import com.atguigu.crowd.funding.service.api.RoleService;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleMapper roleMapper;

	//角色页面关键字搜索查询,返回一个分页页面(所以需要每页显示条数和总页数)再加上搜索关键字参数
	//调用查询结果,得到查询结果后就能封装成一个分页页面
	@Override
	public PageInfo<Role> queryForKeywordWithPage(
			Integer pageNum, 
			Integer pageSize, 
			String keyword) {
		
		// 1.开启分页功能
		PageHelper.startPage(pageNum, pageSize);
		
		// 2.执行查询
		List<Role> list = roleMapper.selectForKeywordSearch(keyword);
		
		// 3.封装为PageInfo对象
		return new PageInfo<>(list);
	}

	//角色页面的批量删除操作
	@Override
	public void batchRemove(List<Integer> roleIdList) {
		// TODO Auto-generated method stub
		RoleExample roleExample = new RoleExample();
		roleExample.createCriteria().andIdIn(roleIdList);
		roleMapper.deleteByExample(roleExample);
	}

	//角色页面role-page.jsp新增操作ajax的json请求
	@Override
	public void saveRole(String roleName) {
		roleMapper.insert(new Role(null,roleName));
	}

	//角色页面role-page.jspg更新操作ajax的json请求
	@Override
	public void updateRole(Role role) {
		roleMapper.updateByPrimaryKey(role);
	}

	//批量删除前在模态框显示删除信息请求.
	@Override
	public List<Role> getRoleByRoleId(List<Integer> roleIdList) {
		RoleExample roleExample = new RoleExample();
		roleExample.createCriteria().andIdIn(roleIdList);
		return roleMapper.selectByExample(roleExample);
		
	}

	//查询已经分配的角色,查询接收分配的查询方法逆向工程中没有,需要我们自己写.
	@Override
	public List<Role> getAssignedRoleList(Integer adminId) {
		List<Role> roleList = roleMapper.selectAssignedRoleList(adminId);
		return roleList;				 
	}

	//查询未分配的校色.查询接收分配的查询方法逆向工程中没有,需要我们自己写.
	@Override
	public List<Role> getUnAssignedRoleList(Integer adminId) {
		List<Role> roleList = roleMapper.selectUnAssignedRoleList(adminId);
		return roleList;
	}

	/*在assign-role.jsp页面分配完用户和角色后,点击分配按钮时,表单发送"assign/role.html"
	 *请求,由本方法接收请求,和分配号的用户和角色id重新更新到中间表中 */
	@Override
	public void updateRelationship(Integer adminId, List<Integer> roleIdList) {
		//更新中间表需要做两两步操作,删除旧的记录,保存新的记录
		//删除旧的记录
		roleMapper.deleteOldAdminRelationship(adminId);
		//保存新的记录,保存之前判断下,角色orleList是否为空,用我们自定义的判断集合是否为空方法
		if(CrowdFundingUtils.collectionEffective(roleIdList)){
			roleMapper.insertNewAdminRelationship(adminId, roleIdList);
					 
			           
		}
		
		
	}


	
}
