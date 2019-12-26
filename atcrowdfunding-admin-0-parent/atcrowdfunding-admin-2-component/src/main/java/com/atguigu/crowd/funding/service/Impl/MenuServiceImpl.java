package com.atguigu.crowd.funding.service.Impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.funding.entitys.Menu;
import com.atguigu.crowd.funding.entitys.MenuExample;
import com.atguigu.crowd.funding.mapper.MenuMapper;
import com.atguigu.crowd.funding.service.api.MenuService;
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuMapper menumapper;

	// 1.查询所有的树形节点用于组装menu菜单
	@Override
	public List<Menu> getWholeTree() {
		return menumapper.selectByExample(new MenuExample());
		
	}

	//menu菜单页面的模态框新增操作
	@Override
	public void saveMenu(Menu menu) {
		menumapper.insert(menu);
	}

	//menu菜单页面的模态框新增操作
	@Override
	public Menu getMenuById(Integer menuId) {
		return menumapper.selectByPrimaryKey(menuId);
		
	}

	//menu菜单页面的模态框更新操作
	@Override
	public void updateMenu(Menu menu) {
		menumapper.updateByPrimaryKeySelective(menu);
	}
}
