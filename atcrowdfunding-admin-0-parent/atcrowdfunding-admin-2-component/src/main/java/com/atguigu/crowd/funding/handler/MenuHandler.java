package com.atguigu.crowd.funding.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.crowd.funding.entitys.Menu;
import com.atguigu.crowd.funding.entitys.ResultEntity;
import com.atguigu.crowd.funding.service.api.MenuService;

@RestController
//Menu菜单表的相关操作
public class MenuHandler {
	@Autowired
	private MenuService menuService;
	 /*menu是尚筹网首页中,权限管理中的菜单管理栏.需要单击时出现一排树形架构的菜单,树形结构的关联关系
	  * 在数据库中menu表中是以"自关联的形式"表中pid字段关联主键id字段,pid是父节点*/
	
	//menu菜单页面的模态框更新操作
	@RequestMapping("/menu/update")
	public ResultEntity<String> updateMenu(Menu menu){
		menuService.updateMenu(menu);
		return ResultEntity.successWithoutData();
	}
	
	//menu菜单页面的模态框更新前回显新操作
	@RequestMapping("/menu/get/{menuId}")
	public ResultEntity<Menu> getMenuById(@PathVariable("menuId") Integer menuId){
		 Menu menu = menuService.getMenuById(menuId);
		return ResultEntity.successWithData(menu);
	}
	
	//menu菜单页面的模态框新增操作
	@RequestMapping("/menu/save")
	public ResultEntity<String> saveMenu(Menu menu){
		menuService.saveMenu(menu);
		return ResultEntity.successWithoutData();
	}
	
	//查询所有的树形节点用于组装menu菜单
	@RequestMapping("/menu/get/whole/tree")
	public ResultEntity<Menu> getWholeTree(){
	
		//1:获取表中所有数据
		List<Menu> menuList	= menuService.getWholeTree();
		
		//2:创建一个Map集合,以键和值的形式.存储menu表中主键和对应的menu
		Map<Integer,Menu> menuMap = new HashMap<>();
	
		//3:遍历查询到的menuList集合,并把遍历的menu主键id和menu以键和值的形式添加到menuMap
		for (Menu menu : menuList) {
			Integer id = menu.getId();
			menuMap.put(id, menu);
		}
		
		//4:声明变量,用于存储根节点
		Menu rootNode = null;
		
		/*5:再次遍历查询结果menuList,判断集合中的menu中的pid是否为空,
		 * 为空的=rootNode根节点
		 * 不为空的,找到和pid一样的主键id是=father父节点
		 * 最后再把字节的添加到Menu类定义的父节点集合中=father.getChildren().add(menu);
		 * */
		for (Menu menu : menuList) {
			Integer pid = menu.getPid();
			//判断集合中的menu中的pid是否为空
			if(pid == null){
			//pid为空表示次menu是没有父节点的,那就是根节点.就是菜单展开的第一个
				rootNode = menu;
			//既然已经是根节点了,就没有必要再循环找子节的了,让次行循环终止,循环下一个menu
				continue;
			}
			/*既然pid不为null，那么我们根据这个pid查找当前节点的父节点。因为menu已经在添加到
			 * menuMap里了,根据集合的键获取值(就是pid对应的主键id,因为节点的父节点节点关联id)*/
			Menu father = menuMap.get(pid);
			// 10.组装：将menu添加到maybeFather的子节点集合中
			father.getChildren().add(menu);
		}
		
		/*6:最后把根节点返回,因为所有是父节点和字节的我们都封装好了,所有的父节点必须是关联根节点的
		 * 有了根节点就可以获取所有的父节点和字节点了*/
		
		return ResultEntity.successWithData(rootNode);
	}
	
	
}
