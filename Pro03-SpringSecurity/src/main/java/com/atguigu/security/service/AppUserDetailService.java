package com.atguigu.security.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/*此类实现SpringSecurity框架提供的UserDetailsService用户详细服务的接口.就出现一个方法
 *loadUserByUsername根据用户名称加载用户信息方法,就是根据登陆页面的名称信息到数据库查询此用户的登录名和
 *密文密码再给该用户添加角色或权限.最后new一个用户,把用户的,名称. 密文. 封装好的角色和权限,返回给方法 */
@Service
public class AppUserDetailService implements UserDetailsService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		//使用sql语句根据用户查询用户对象
		String sql = "SELECT id,loginacct,userpswd,username,email,createtime FROM t_admin WHERE loginacct=?";
	
		//获取查询结果
		Map<String, Object> resultmap = jdbcTemplate.queryForMap(sql, username);
	
		//获取用户名和密码
		String loginacct = resultmap.get("loginacct").toString();
		//System.out.println("11111111111111----"+loginacct);
		String userpswd = resultmap.get("userpswd").toString();
		//System.out.println("22222222222222----"+userpswd);
		//创建权限列表   注意这里一定要加前缀"ROLE_"拼角色,权限不用加前缀
		List<GrantedAuthority> List = AuthorityUtils.createAuthorityList("ROLE_学徒","ROLE_大师","吐纳真气");	
		//返回一个org.springframework.security.core.userdetails.User
		return new User(loginacct, userpswd, List);
	}
	
}
