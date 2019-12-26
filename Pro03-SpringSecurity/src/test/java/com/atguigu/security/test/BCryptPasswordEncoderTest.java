package com.atguigu.security.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*此测试类是为了演示.SPringSecurity框架的核心技术.用框架提供的BCryptPasswordEncoder
 * 密码随机加入"盐值"的方法对象,可以直接new来用.把我们自定义的"原始密码"进行随机加入"盐值"
 * 就是一个原始密码,可以在每次都能生成不同的加入"盐值"的密文.也就是说,一个原始密码.可以有n个不重复的60多
 * 位数的加密后的密码.所以的密码都能匹配上我们的"原始密码".但是密文却不重复,也就无法通过密文破解.所以很安全
 * */
public class BCryptPasswordEncoderTest {

	public static void main(String[] args) {
		//SpringSecurity的随机"盐值"加密方法.可以直接new
		BCryptPasswordEncoder encoding = new BCryptPasswordEncoder();
		CharSequence rawPassword = "123123";
		for (int i = 0; i < 5; i++) {
			String encode2 = encoding.encode(rawPassword);
			System.out.println(encode2);
		}
			//调用加密对象的匹配方法,把原密码和加密后的匹配就是123123匹配随机盐值加密后的64位密文
		boolean matches = encoding.matches(rawPassword,
				"$2a$10$h1JuoyKTy3mf.bY3tBCIPe0QDHnfRMu.0BO2Y87aCmM.OByUy9aMS");
		System.out.println(matches);
		//打印结果为true(说明盐值加密后的64位密文和123123是匹配的)
	}

}
