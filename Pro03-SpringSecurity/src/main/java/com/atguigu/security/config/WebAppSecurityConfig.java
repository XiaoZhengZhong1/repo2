package com.atguigu.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.atguigu.security.service.AppUserDetailService;
//可配置的
//开启web安全服务
//WebSecurityConfigurerAdapter  网络安全配置程序
@Configurable		
@EnableWebSecurity						
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource; 
	
	//此注解加载的是从数据库获取的用户的名称,和密文,以及添加的角色和权限.
	@Autowired
	private AppUserDetailService userDetailService;
	 
	//装配我们自己写的类.有MD5加密方法和原密和加密比较方法.
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//重写配置带创建对象参数的方法
	@Override	
	protected void configure(AuthenticationManagerBuilder builder)
			throws Exception {
		/*builder						//创建对象
		.inMemoryAuthentication()	//在内存中身份验证
		.withUser("xiaolei66")	//设置账号密码
		.password("456456")
		.roles("大师")
		;*/
		//调用我们自定义的数据源 //调用我们自定义的密码加密和比较方法类
		builder
		.userDetailsService(userDetailService) 
		.passwordEncoder(passwordEncoder);	
		
	}
	//重写配置带安全参数的方法
	@Override		
	protected void configure(HttpSecurity security) throws Exception {
		//创建token库
		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
			repository.setDataSource(dataSource);
		
		security.				//调用父类安全参数对象
		authorizeRequests()	//对请求进行授权
		.antMatchers("/index.jsp","/layui/**") //使用ANT风格设置要授权的URL地址
		.permitAll()			//允许上面使用ANT风格设置的全部请求
		.antMatchers("/level1/**")
		.hasRole("学徒")
		.antMatchers("/level2/**")
		.hasRole("大师")
		.antMatchers("/level3/**")
		.hasRole("宗师")
		.antMatchers("/level3/**")	//表示匹配第3层的功法
		.hasAuthority("吐纳真气")		//有"吐纳真气"的权限也可以访问第三层
		.anyRequest()			 //其他未设置的全部请求
		.authenticated()		  //需要认证
		.and()
		.formLogin()				//设置未授权请求跳转到登录页面
		.loginPage("/index.jsp")	//指定登录页
		.permitAll()				//为登录页设置所有人都可以访问
		.defaultSuccessUrl("/main.html")	//设置登录成功后默认前往的URL地址
		.usernameParameter("loginacct")		//告诉security我们登陆账号请求参数名
		.passwordParameter("credential")	//告诉security我们登陆密码请求参数名
		.and()								//并且
		.logout()							//退出时
		.logoutUrl("/my/app/logout")		//到该页面
		.logoutSuccessUrl("/index.jsp")		//退出成功后到首页
		.and()								//并且
		.exceptionHandling()				//拒接后
		.accessDeniedPage("/to/no/auth/page.html")	//访问拒接后要去的页面(替代403)
		.and()
		.rememberMe()							//开启记住我功能(页面的"记住我"对应的name必须是rememberMe)
		.tokenRepository(repository)		//指定token库
		;
		

	}
}
