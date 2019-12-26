package com.atguigu.crowd.funding.mapper;

import com.atguigu.crowd.funding.entitys.Role;
import com.atguigu.crowd.funding.entitys.RoleExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
    
    //角色页面的关键字搜索查询操作
    List<Role> selectForKeywordSearch(String keyword);

	//查询已经分配的角色,查询接收分配的查询方法逆向工程中没有,需要我们自己写.
	List<Role> selectAssignedRoleList(Integer adminId);
	
	//查询未分配的校色.查询接收分配的查询方法逆向工程中没有,需要我们自己写.
	List<Role> selectUnAssignedRoleList(Integer adminId);

	//删除旧的记录
	void deleteOldAdminRelationship(Integer adminId);
	
	//保存新的记录
	void insertNewAdminRelationship(@Param("adminId") Integer adminId,@Param("roleIdList") List<Integer> roleIdList);
}