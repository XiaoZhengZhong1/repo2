package com.atguigu.crowd.funding.service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.funding.entitys.Auth;
import com.atguigu.crowd.funding.entitys.AuthExample;
import com.atguigu.crowd.funding.mapper.AuthMapper;
import com.atguigu.crowd.funding.service.api.AuthService;
import com.atguigu.crowd.funding.util.CrowdFundingUtils;
@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AuthMapper authMapper;

	//后端查询全部权限数据
	@Override
	public List<Auth> getAllAuth() {	
		return authMapper.selectByExample(new AuthExample());
	}

	//根据指定的角色id查询具体的权限
	@Override
	public List<Integer> getAssignedAuthIdList(Integer roleId) {
		return authMapper.selectAssignedAuthIdList(roleId);
		
	}

	//在角色和权限之间进行分配
	@Override
	public void updateRelationShipBetweenRoleAndAuth(
			Map<String, List<Integer>> assignDataMap) {
	//从参数中获取分配集合assignDataMap中的,角色id和权限id
		List<Integer> roleIdList = assignDataMap.get("roleIdList");
		List<Integer> authIdList = assignDataMap.get("authIdList");
		
		// 2.取出roleId
		Integer roleId = roleIdList.get(0);
		
		// 3.删除旧数据,因为当用户点击角色分配权限时,角色id只能是一个,就是map的key,就是0索引
		//删除了键key,对应的值"旧权限"也就一起删了.
		authMapper.deleteOldRelationship(roleId);
		
		// 4.保存新数据
		if(CrowdFundingUtils.collectionEffective(authIdList)) {
			authMapper.insertNewRelationship(roleId, authIdList);
		}
	}
		
}
