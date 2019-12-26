// 由setting.view.addDiyDom属性引用，负责显示自定义图标
// treeId是<ul id="treeDemo" class="ztree"></ul>的id属性值
// treeNode对应每一个树形节点
function showMyIcon(treeId, treeNode) {
	
	// 获取当前节点的id
	var currentNodeId = treeNode.tId;
	
	// 获取新的class值用于替换
	var newClass = treeNode.icon;
	
	// 在当前节点id之后附加“_ico”得到目标span的id
	var targetSpanId = currentNodeId + "_ico";
	
	// 将目标span的旧class移除，添加新class
	$("#"+targetSpanId)
		.removeClass()
		.addClass(newClass)
		.css("background","");
	
	console.log(treeNode);
}

function initWholeTree() {
	// setting对象中包含zTree的设置属性
	var setting = {
		"view": {
			"addDiyDom": showMyIcon,
			"addHoverDom": addHoverDom,
			"removeHoverDom": removeHoverDom
		},
		"data": {
			"key": {
				"url": "notExistsProperty" // 阻止点击节点后跳转
			}
		}
	};

	// 发送Ajax请求获取zNodes数据
	$.ajax({
		"url":"menu/get/whole/tree.json",
		"type":"post",
		"dataType":"json",
		"success":function(response){
			
			var result = response.result;
			
			if(result == "SUCCESS") {
				// 用于显示树形结构的节点数据
				var zNodes = response.data;
				
		
				// 执行树形结构的初始操作
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			}
			
			if(result == "FAILED") {
				layer.msg("加载菜单数据失败！原因是："+response.message);
			}
		},
		"error":function(response){
			layer.msg("加载菜单数据失败！原因是："+response.message);
		}
	});
}

// 在鼠标移入节点范围时添加自定义控件
function addHoverDom(treeId, treeNode) {
	
	// 在执行添加前，先进行判断，如果已经添加过，就停止执行
	// 组装按钮组所在的span标签的id
	var btnGrpSpanId = treeNode.tId + "_btnGrp";
	
	var btnGrpSpanLength = $("#"+btnGrpSpanId).length;
	
	if(btnGrpSpanLength > 0) {
		return ;
	}
	
	// 由于按钮组是放在当前节点中的超链接（a标签）后面，所以先定位到a标签
	// 按id生成规则组装a标签的id
	var anchorId = treeNode.tId + "_a";
	
	// 调用已封装函数生成按钮组
	var $btnGrpSpan = generateBtnGrp(treeNode);
	
	// 在a标签后面追加按钮组
	$("#"+anchorId).after($btnGrpSpan);
	
}

// 在鼠标移出节点范围时删除自定义控件
function removeHoverDom(treeId, treeNode) {
	
	// 组装按钮组所在的span标签的id
	var btnGrpSpanId = treeNode.tId + "_btnGrp";
	
	// 删除对应的元素
	$("#"+btnGrpSpanId).remove();
}

// 专门生成按钮组的函数
function generateBtnGrp(treeNode) {
	
	// 获取当前节点的id（HTML中li标签的id）
	var treeNodeId = treeNode.tId;
	
	// 获取当前节点在数据库中的id值
	// Menu实体类中的属性，都可以通过treeNode以“.属性名”的方式直接访问
	var menuId = treeNode.id;
	
	// 组装按钮组所在的span的id
	var btnGrpSpanId = treeNodeId + "_btnGrp";
	
	// 生成span标签对应的jQuery对象
	var $span = $("<span id='"+btnGrpSpanId+"'></span>");
	
	// 获取当前节点的level值
	var level = treeNode.level;
	
	// 声明三种按钮
	var addBtn = "<a onclick='showAddModal(this)' id='"+menuId+"' class='btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg'></i></a>";
	var editBtn = "<a onclick='showEditModal(this)' id='"+menuId+"' class='btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='编辑节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg'></i></a>";
	var removeBtn = "<a onclick='showConfirmModal(this)' id='"+menuId+"' class='btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg'></i></a>";
	
	// 根据level进行判断
	if(level == 0) {
		$span.append(addBtn);
	}
	
	if(level == 1) {
		
		if(treeNode.children.length > 0) {
			
			$span.append(addBtn+" "+editBtn);
			
		} else {
			
			$span.append(addBtn+" "+editBtn+" "+removeBtn);
			
		}
		
	}
	
	if(level == 2) {
		
		$span.append(editBtn+" "+removeBtn);
		
	}
	
	return $span;
}

// 在点击添加按钮时执行这个函数，打开模态框
function showAddModal(currentBtn) {
	
	// 打开模态框
	$("#menuAddModal").modal("show");
	
	// 将当前节点的id存入全局变量
	window.menuId = currentBtn.id;
}

// 在点击编辑按钮时执行这个函数，打开模态框
function showEditModal(currentBtn) {
	
	// 打开模态框
	$("#menuEditModal").modal("show");
	
	// 获取当前节点的id存入全局变量
	window.menuId = currentBtn.id;
	
	// 发送请求查询Menu对象
	$.ajax({
		"url":"menu/get/"+window.menuId+".json",
		"type":"get",
		"dataType":"json",
		"success":function(response) {
			
			var result = response.result;
			
			if(result == "SUCCESS") {
				
				// 从响应数据中获取Menu对象
				var menu = response.data;
				
				// 获取各个属性值
				var name = menu.name;
				
				var url = menu.url;
				
				var icon = menu.icon;
				
				// 设置各个对应的表单项
				$("#menuEditModal [name='name']").val(name);
				
				$("#menuEditModal [name='url']").val(url);
				
				// radio需要让value值和后端查询到的icon一致的设置为被选中
				$("#menuEditModal [name='icon'][value='"+icon+"']").attr("checked",true);
			}
			
			if(result == "FAILED") {
				layer.msg(response.message);
			}
		},
		"error":function(response) {
			layer.msg(response.message);
		}
	});
	
}