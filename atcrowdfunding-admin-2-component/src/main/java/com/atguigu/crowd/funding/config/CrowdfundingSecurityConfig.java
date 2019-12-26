package com.atguigu.crowd.funding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//本类引用SpringBoot框架的SpringSecurily中的类,进行本项目的用户登陆相关的验证和加密操作
@Configuration
// 可配置的
@EnableWebSecurity
// 开启web安全
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 开启全局安全性(预设值启用为ture)
// 让自定义的类继承SpringBoot框架的WebSecurityConfigurerAdapter网络安全配置程序类
public class CrowdfundingSecurityConfig extends WebSecurityConfigurerAdapter {

	// @Autowired
	// private PasswordEncoder encod;

	@Autowired
	private UserDetailsService userDetailsService;

	// Spring在真正调用这个方法前,会检查IOC容器中是否已经有了对应的bean，
	// 如果有，则不会真正调用这个方法。而是直接把IOC容器中的bean返回。
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 重写可配置方法中带创建参数的方法
	@Override
	protected void configure(AuthenticationManagerBuilder builder)
			throws Exception {
		// 测试用的
		/*
		 * builder //身份验证管理对象 .inMemoryAuthentication() //在内存中
		 * .withUser("xiaolei") //的名称是 .password("123123") //的密码是
		 * .roles("baobao"); //的角色是
		 */

		builder
		.userDetailsService(userDetailsService) //获取用户详细信息
		.passwordEncoder(getPasswordEncoder())	//调用SpringSecurity框架提供的校检原始密码和"随机盐值加密"是否匹配方法
		;
	}

	@Override
	// 重写可配置方法中的带安全参数方法
	protected void configure(HttpSecurity security) throws Exception {
		security // 调用网络安全配置程序类的安全参数
		.authorizeRequests() // 调用请求验证方法
				.mvcMatchers("/index.html", "/bootstrap/**", "/css/**",
						"/fonts/**", "/img/**", "/jquery/**", "/layer/**",
						"/script/**", "/ztree/**") // 再调用匹配方法.匹配项目首页和所有静态页面
				.permitAll() // 再调许可证方法,许可上面方法中匹配的页面
				.anyRequest() // 再调用拦截方法表示,其它任何一个请求
				.authenticated() // 都需要验证
				.and() // 并且
				.formLogin() // 登陆表单的
				.loginPage("/admin/to/login/page.html") // 登陆页面
				.permitAll() // 可以访问
				.loginProcessingUrl("/admin/security/do/login.html") // 登陆后的地址
				.permitAll() // 也可以访问
				.usernameParameter("loginAcct") // 把名称参数设置成我们登陆页面的名称
				.passwordParameter("userPswd") // 把密码参数 设置成我们页面的密码参数
				.defaultSuccessUrl("/admin/to/main/page.html") // 登陆成功后默认去主页面
				.and() // 并且
				.logout() // 开启退出功能
				.logoutUrl("/admin/security/do/logout.html") // 退出页面为
				.logoutSuccessUrl("/index.html") // 突出成功后,到主页面去
				.and() // 并且
				.csrf() // 将SpringSecurily提供的参数带"CSRF_"的功能
				.disable();// 禁用
	}
}
