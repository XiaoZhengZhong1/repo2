package com.atguigu.crowd.funding.service.api;

import java.util.List;

import com.atguigu.crowd.funding.entitys.Admin;
import com.github.pagehelper.PageInfo;

public interface AdminService {

	List<Admin> getAll();

	
	


	Admin login(String loginAcct, String userPswd);
	
	//搜索查询和分页功能
	PageInfo<Admin> queryForKeywordSearch(Integer pageNum, Integer pageSize,
			String keyword);

	//批量删除的相关操作
	void BatchRemove(List<Integer> adminIdList);

	//注册新增操作
	void saveAdmin(Admin admin);


	//点击更新图标操作
	Admin getAdminById(Integer adminId);

	//具体更新操作
	void updateAdmin(Admin admin);


	


	


	
}
