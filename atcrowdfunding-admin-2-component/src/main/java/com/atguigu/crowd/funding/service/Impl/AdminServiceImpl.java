package com.atguigu.crowd.funding.service.Impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.funding.entitys.Admin;
import com.atguigu.crowd.funding.entitys.AdminExample;
import com.atguigu.crowd.funding.entitys.AdminExample.Criteria;
import com.atguigu.crowd.funding.mapper.AdminMapper;
import com.atguigu.crowd.funding.service.api.AdminService;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminMapper adminMapper;

	//用SPringSecurity提供的"随机盐值加密接口":PasswordEncoder来加密我们的新增用户的登陆密码
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	@Override
	public List<Admin> getAll() {

		return adminMapper.selectByExample(new AdminExample());
	}



	// 演示,管理员登陆并进行用户名和密码的检验,并记住密码
	@Override
	public Admin login(String loginAcct, String userPswd) {
		
		// 1:new一个管理员对象实例(由使用"逆向工程"是通过数据库,一张表生成一个javaBean类,和一个xxxExample)
		//因为是校验管理员的用户名和密码,所以需要数据库的管理员对象
		AdminExample adminExample = new AdminExample();

		// 2:通过new出来的管理员实例对象,调用创建标准方法创建,并且登陆用户要和loginAcct参数用户一样
		//就是传递的参数创建一个登陆用户
		adminExample.createCriteria().andLoginAcctEqualTo(loginAcct);
		
		//3:查询上面创建的用户
		List<Admin> list = adminMapper.selectByExample(adminExample);
		
		//4:调用我们自己写的密码校验工具类,校验上面查询到的用户
		if(!CrowdFundingUtils.collectionEffective(list)){
		//表示如果list=null或者list.size<0 那整个校验结果为false 那这里非false就是tuee
		//那说明校验失败了,返回null
			return null;
		}
		
		//5:如查询结果校验成功,说明登陆用户不为空并且长度大于0,那就获取查询的集合中的用户
		Admin admin = list.get(0);
		
		//6:再次确认用户是否为空
		if(admin == null){
			return null;
		}
		
		//7:获取管理员数据库的密码(数据库的密码以及是加密过的32个字节的密码)
		String userPswd2 = admin.getUserPswd();
	
		//8:加密方法参数传入的密码(就是浏览器登陆页面传入的密码就是把123明文,加密成32个字节的密文)
		String md5 = CrowdFundingUtils.md5(userPswd);
	
		//:再把数据库的密文和浏览器传入的加密后的密文密码进行比较
		//Objects.equals(a,b)是object自带的比较方法
		if(Objects.equals(md5 , userPswd2)){
			//如果比较结果密码一样,就返回管理员对象Admin
			return admin;
		}
		
		//否则返回空.表示密码不一样,没有该管理员,返回空
		return null;
	}

	//搜索查询和分页功能
	@Override
	public PageInfo<Admin> queryForKeywordSearch(Integer pageNum,Integer pageSize,String keyword) {
		
		//1:调用PageHelper工具的分页方法,开启分页功能
		PageHelper.startPage(pageNum, pageSize);
		
		//2:调用adminMapper执行分页查询
		List<Admin> list = adminMapper.selectAdminListByKeyword(keyword);
		
		//3:mew分页对象,把查询分页结果传入分页对象参数中,让结果变成分页对象		
		return new PageInfo<>(list);
	}

	//批量删除的相关操作
	@Override
	public void BatchRemove(List<Integer> adminIdList) {
		// QBC：Query By Criteria
		
		// 创建AdminExample对象（不要管Example单词是什么意思，它没有意思）
		AdminExample adminExample = new AdminExample();
		
		// 创建Criteria对象（不要管Criteria单词是什么意思，它没有意思）
		// Criteria对象可以帮助我们封装查询条件
		// 通过使用Criteria对象，可以把Java代码转换成SQL语句中WHERE子句里面的具体查询条件
		Criteria criteria = adminExample.createCriteria();
		// 针对要查询的字段封装具体的查询条件
		criteria.andIdIn(adminIdList);
		
		// 执行具体操作时把封装了查询条件的Example对象传入
		adminMapper.deleteByExample(adminExample);
		
	}

	//注册新增操作
	@Override
	public void saveAdmin(Admin admin) {
		//获取注册用户的密码
		String userPswd = admin.getUserPswd();
		//对密码进行加密:用SPringSecurity提供的"随机盐值加密接口"
		String userPswd3 = passwordEncoder.encode(userPswd);
		//把加密后的密码,重新装入注册用户信息中.
		admin.setUserPswd(userPswd3);
		//执行增加用户操作(因为注册页面已经把注册的信息,按照数据库列表的格式排列的,参数中已经有数据).
		 adminMapper.insert(admin);
		
	}

	//点击更新图标操作
	@Override
	public Admin getAdminById(Integer adminId) {
		 return adminMapper.selectByPrimaryKey(adminId);
		
	}


	//具体更新操作
	@Override
	public void updateAdmin(Admin admin) {
		//获取注册用户的密码
				String userPswd = admin.getUserPswd();
				//对密码进行加密
				String userPswd4 = CrowdFundingUtils.md5(userPswd);
				//把加密后的密码,重新装入注册用户信息中.
				admin.setUserPswd(userPswd4);
				//执行增加用户操作(因为注册页面已经把注册的信息,按照数据库列表的格式排列的,参数中已经有数据).
				adminMapper.updateByPrimaryKey(admin);
	}

	
	

}
