package com.atguigu.crowd.funding.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.funding.config.SecurityAdmin;
import com.atguigu.crowd.funding.entitys.Admin;
import com.atguigu.crowd.funding.entitys.AdminExample;
import com.atguigu.crowd.funding.entitys.Auth;
import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.mapper.AdminMapper;
import com.atguigu.crowd.funding.mapper.AuthMapper;
import com.atguigu.crowd.funding.mapper.RoleMapper;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;

/*我们需要创建一个业务层对象,实现SpringBoot提供的UserDetailsService用户详细服务借口,
 * 把我们的admin用户封装到UserDetailsService提供的方法中,到数据库查询,用户具体信息.
 * 
 * */
@Service
public class CrowdFundingUserDetails implements UserDetailsService {

	// 装配admin用户的映射接口,操作数据库
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	 
	@Autowired
	private AuthMapper authMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		//逆向工程为所有的表创建了对应的几百条能用到的所有"增删改查"方法,封装到AdminExample类中
		AdminExample adminExample = new AdminExample();
		//当我们需要用里面方法时,只需要AdminExample对象,调用创建标准方法.把我们的参数封装到方法中
		//表示创建标准.并且登陆用户的名称要和本方法名称相同.
		adminExample.createCriteria().andLoginAcctEqualTo(username);
		
		//因为用户有多个字段值所以返回的是list
		List<Admin> list = adminMapper.selectByExample(adminExample);
	
		//检测查询结果是否为空,为空就结束方法
		if(!CrowdFundingUtils.collectionEffective(list)){
			return null;
		}
		
		//如果有值,就获取值,因为一个名字只可能有一个用户,所以应该在集合的0索引中
		Admin admin = list.get(0);
		
		//获取用户密码
		//String userPswd = admin.getUserPswd();
		
		//创建一个集合,封装角色和权限信息
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		
		//获取用户id
		Integer adminId = admin.getId();
		//查询用户角色信息
		List<Role> RoleList = roleMapper.selectAssignedRoleList(adminId);
		//遍历查询到的角色集合,并添加到上面创建的角色和权限的集合中
		for (Role role : RoleList) {
			String roleName = "ROLE_" + role.getName();
			authorities.add(new SimpleGrantedAuthority(roleName));
		}
		
		//查询用户权限
		List<Auth> authList = authMapper.selectAuthListByAdminId(adminId);
		//遍历用户的权限集合,并添加到上面创建的角色和权限的集合中.
		for (Auth auth : authList) {
			String authName = auth.getName();
			//判断authName是否为空窜,如果是空窜.就终止此次循环继续下次循环
			if(!CrowdFundingUtils.stringEffective(authName)){
				continue;
			}
			authorities.add(new SimpleGrantedAuthority(authName));
		}
		
		return new SecurityAdmin(admin,authorities);
	}

}
