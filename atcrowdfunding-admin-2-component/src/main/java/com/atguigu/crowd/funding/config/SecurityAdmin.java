package com.atguigu.crowd.funding.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.atguigu.crowd.funding.entitys.Admin;

/**
 * 扩展User类
 * 创建SecurityAdmin对象时调用构造器，传入originalAdmin和authorities
 * 可以通过getOriginalAdmin()方法获取原始Admin对象
 *
 */
public class SecurityAdmin extends User {

	private static final long serialVersionUID = 1L;
	
	private Admin originalAdmin;

	public SecurityAdmin(Admin originalAdmin, Collection<GrantedAuthority> authorities) {
		
		// 调用父类的构造器
		super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
		
		this.originalAdmin = originalAdmin;
		
	}

	public Admin getOriginalAdmin() {
		return originalAdmin;
	}

}
