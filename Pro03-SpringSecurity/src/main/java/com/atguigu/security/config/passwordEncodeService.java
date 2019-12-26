package com.atguigu.security.config;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.atguigu.security.utils.CrowdFundingUtils;

//此类是继承SprinSecurity提供的PasswordEncoder接口,实现给密码加密
//因为别的类需要用到我们这个类,也是加@Autowired 私有PasswordEncoder
//所以也需要加扫描注解提交到IOC容器,别的类才能自动装配找到我们passwordEncodeService类
@Service
public class passwordEncodeService implements PasswordEncoder {

	// 此方法对原始明文进行加密 raw是原始的意思.

	/*@Override 		        此方法中的参数值rawPassword不知何来  无用.
	public String encode(CharSequence rawPassword) {
		System.out.println("33333333333333333333---------" + rawPassword);
		// 在进行加密之前,用断言对象Assert的方法,判断下原始秘密是否为空,以避免程序出现空指针异常
		Assert.notNull(rawPassword, "rawPassword can not be null");

		// 先调用我们自己的加密工具MD5进行加密(注意我们加密的是字符串,所以要掉原始明文的toString方法)
		String password1 = CrowdFundingUtils.md5(rawPassword.toString());
		System.out.println("333333335555555555555----------" + password1);
		return password1;
	}
*/
	// 匹配方法.原始明文密码和加密后的密文进行比较
	@Override
	// 原始明文 123123 数据库通过MD5加密密文:4297F44B13955235245B2497399D7A93
	public boolean matches(CharSequence rawPassword, String encodePassword) {
		// 在进行加密之前,用断言对象Assert的方法,判断下原始秘密是否为空,以避免程序出现空指针异常
		Assert.notNull(rawPassword, "rawPassword can not be null");
								//调用我们自己写的MD5方法把123123进行加密
		String password = CrowdFundingUtils.md5(rawPassword.toString());
		
		//再用123123加密后的密文和数据库中的密文进行比较
		// 表示返回两个对象的比较结果,相同为ture 不同为false
		return Objects.equals(password, encodePassword);

	}

	@Override
	public String encode(CharSequence paramCharSequence) {
		// TODO Auto-generated method stub
		return null;
	}

}
