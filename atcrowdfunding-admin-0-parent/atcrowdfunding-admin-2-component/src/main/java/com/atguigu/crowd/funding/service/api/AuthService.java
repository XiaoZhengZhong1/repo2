package com.atguigu.crowd.funding.service.api;

import java.util.List;
import java.util.Map;

import com.atguigu.crowd.funding.entitys.Auth;

public interface AuthService {

	//后端查询全部权限数据
	List<Auth> getAllAuth();
	
	//根据指定的角色id查询具体的权限
	List<Integer> getAssignedAuthIdList(Integer roleId);

	//在角色和权限之间进行分配
	void updateRelationShipBetweenRoleAndAuth(
			Map<String, List<Integer>> assignDataMap); 

}
