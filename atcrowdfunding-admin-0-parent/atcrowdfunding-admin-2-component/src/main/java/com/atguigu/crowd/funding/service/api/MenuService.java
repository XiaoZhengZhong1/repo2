package com.atguigu.crowd.funding.service.api;

import java.util.List;

import com.atguigu.crowd.funding.entitys.Menu;

public interface MenuService {

	List<Menu> getWholeTree();

	//menu菜单页面的模态框新增操作
	void saveMenu(Menu menu);

	//menu菜单页面的模态框新增操作
	Menu getMenuById(Integer menuId);

	//menu菜单页面的模态框更新操作
	void updateMenu(Menu menu);

}
