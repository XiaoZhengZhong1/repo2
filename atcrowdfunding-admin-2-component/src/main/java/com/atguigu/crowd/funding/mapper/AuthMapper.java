package com.atguigu.crowd.funding.mapper;

import com.atguigu.crowd.funding.entitys.Auth;
import com.atguigu.crowd.funding.entitys.AuthExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);
    
    List<Integer> selectAssignedAuthIdList(Integer roleId);
    
    	// 3.删除旧数据,因为当用户点击角色分配权限时,角色id只能是一个,就是map的key,就是0索引
 		//删除了键key,对应的值"旧权限"也就一起删了.
	void deleteOldRelationship(Integer roleId);

	// 4.保存新数据
	void insertNewRelationship(@Param("roleId") Integer roleId,@Param("authIdList") List<Integer> authIdList);

	//查询用户权限
	List<Auth> selectAuthListByAdminId(Integer adminId); 
}