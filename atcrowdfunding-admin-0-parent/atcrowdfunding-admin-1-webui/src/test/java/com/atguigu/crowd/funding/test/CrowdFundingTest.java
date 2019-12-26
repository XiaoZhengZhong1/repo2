package com.atguigu.crowd.funding.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atguigu.crowd.funding.entitys.Admin;
import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.mapper.AdminMapper;
import com.atguigu.crowd.funding.mapper.RoleMapper;
import com.atguigu.crowd.funding.service.api.AdminService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml",
		"classpath:spring-persist-tx.xml"})
public class CrowdFundingTest {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	
	@Test
	public void batchSaveAdmin2() {
		for(int i = 7; i < 30; i++) {
			roleMapper.insert(new Role(i, "log"));
		}
	}
	
	@Test
	public void batchSaveAdmin() {
		for(int i = 0; i < 30; i++) {
			adminMapper.insert(new Admin(null, "loginAcct"+i, "1111111", "userName"+i, "email"+i+"@qq.com", null));
		}
	}
	
	@Test
	public void testAdminMapperSearch(){
		String keyword = "a";
		List<Admin> admin = adminMapper.selectAdminListByKeyword(keyword);
		for (Admin admin2 : admin) {
			System.out.println(admin2);
		}
	}
	
	

	@Test
	public void testConnection() throws SQLException {
		Connection connection = dataSource.getConnection();

		System.out.println(connection);
	}


	@Autowired
	private AdminService adminService;
	
	
	
	@Test
	public void testMybatis() {
			// TODO Auto-generated method stub
			List<Admin> adminList = adminService.getAll();
			for (Admin admin : adminList) {
				System.out.println(admin);
			}
		}

	
}
