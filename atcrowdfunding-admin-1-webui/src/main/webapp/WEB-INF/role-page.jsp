<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="UTF-8">
<head>
<%@ include file="/WEB-INF/include-head.jsp"%>
<link rel="stylesheet" href="css/pagination.css" />
<link rel="stylesheet" href="ztree/zTreeStyle.css" />
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="script/jquery.pagination.js"></script>
<script type="text/javascript" src="script/my-role.js"></script>
<script type="text/javascript">
	$(function(){
		
		// 调用分页参数初始化方法
		initGlobalVariable();
		
		// 执行分页
		showPage();
		
		$("#searchBtn").click(function(){
			// 在单击响应函数中获取文本框中输入的数据
			var keyword = $.trim($("#keywordInput").val());
			
			// 验证输入数据是否有效
			if(keyword == null || keyword == "") {
				// 如果无效，提示，停止函数执行
				layer.msg("请输入关键词！");
				return ;
			}
			
			// 如果有效，赋值给window.keyword
			window.keyword = keyword;
			
			// 调用showPage()重新分页
			showPage();
		});
		
		// 全选/全不选功能
		$("#summaryBox").click(function(){
			
			// 1.获取当前checkbox的选中状态
			var currentStatus = this.checked;
			
			// 2.设置itemBox的选中状态
			$(".itemBox").prop("checked",currentStatus);
			
		});
		
		// 给批量删除按钮绑定单击响应函数
		$("#batchRemoveBtn").click(function(){
			
			// 获取被选中的itemBox数组长度
			var length = $(".itemBox:checked").length;
			
			// 如果长度为0，说明没有选择itemBox
			if(length == 0) {
				layer.msg("请至少选择一条！");
				return ;
			}
			
			// 在全局作用域内创建roleIdArray
			window.roleIdArray = new Array();
			
			// 遍历$(".itemBox:checked")
			$(".itemBox:checked").each(function(){
				
				// 通过checkbox的roleid属性获取roleId值
				var roleId = $(this).attr("roleid");
				
				// 存入数组
				window.roleIdArray.push(roleId);
				
			});
			
			// 调用函数打开模态框
			showRemoveConfirmModal();
		});
		
		// 给确认模态框中的OK按钮绑定单击响应函数
		$("#confirmModalBtn").click(function(){
			
			var requestBody = JSON.stringify(window.roleIdArray);
			
			$.ajax({
				"url":"role/batch/remove.json",
				"type":"post",
				"data":requestBody,
				"contentType":"application/json;charset=UTF-8",
				"dataType":"json",
				"success":function(response){
					
					var result = response.result;
					
					if(result == "SUCCESS") {
						layer.msg("操作成功！");
						
						// 如果删除成功，则重新调用分页方法
						showPage();
					}
					
					if(result == "FAILED") {
						layer.msg(response.message);
					}
					
					// 不管成功还是失败，都需要关掉模态框
					$("#confirmModal").modal("hide");
					
				},
				"error":function(response){
					layer.msg(response.message);
				}
			});
			
		});
		
//		只能生效一次，重新分页（也就是重新生成这些动态元素）后失效
// 		$(".removeBtn").click(function(){
			
// 			// 获取当前记录的roleId
// 			var roleId = $(this).attr("roleId");
			
// 			// 存入全局变量数组
// 			window.roleIdArray = new Array();
			
// 			window.roleIdArray.push(roleId);
			
// 			// 打开模态框（后续所有操作都和批量删除一样）
// 			showRemoveConfirmModal();
			
// 		});

		// 针对.removeBtn这样动态生成的元素对象使用on()函数方式绑定单击响应函数
		// $("动态元素所依附的静态元素").on("事件类型","具体要绑定事件的动态元素的选择器", 事件响应函数);
		$("#roleTableBody").on("click",".removeBtn", function(){
			// 获取当前记录的roleId
			var roleId = $(this).attr("roleId");
			
			// 存入全局变量数组
			window.roleIdArray = new Array();
			
			window.roleIdArray.push(roleId);
			
			// 打开模态框（后续所有操作都和批量删除一样）
			showRemoveConfirmModal();
		});
		
		$("#addBtn").click(function(){
			
			$("#addModal").modal("show");
			
		});
		
		$("#addModalBtn").click(function(){
			
			// 1.收集文本框内容
			var roleName = $.trim($("#roleNameInput").val());
			
			if(roleName == null || roleName == "") {
				layer.msg("请输入有效角色名称！");
				return ;
			}
			
			// 2.发送请求
			$.ajax({
				"url":"role/save/role.json",
				"type":"post",
				"data":{
					"roleName":roleName
				},
				"dataType":"json",
				"success":function(response){
					
					var result = response.result;
					
					if(result == "SUCCESS") {
						layer.msg("操作成功！");
						
						// 3.操作成功重新分页
						// 前往最后一页
						window.pageNum = 999999;
						showPage();
					}
					
					if(result == "FAILED") {
						layer.msg(response.message);
					}
					
					// 4.不管成功还是失败，关闭模态框
					$("#addModal").modal("hide");
					
					// 5.清理本次在文本框填写的数据
					$("#roleNameInput").val("");
					
				},
				"error":function(response){
					layer.msg(response.message);
				}
			});
			
		});
		
		$("#roleTableBody").on("click",".editBtn",function(){
			
			// 1.获取当前按钮的roleId
			window.roleId = $(this).attr("roleId");
			
			// 2.获取当前按钮所在行的roleName
			var roleName = $(this).parents("tr").children("td:eq(2)").text();
			
			// 3.修改模态框中文本框的value值，目的是在显示roleName
			$("#roleNameInputEdit").val(roleName);
			
			// 4.打开模态框
			$("#editModal").modal("show");
		});
		
		$("#editModalBtn").click(function(){
			
			// 1.获取文本框值
			var roleName = $.trim($("#roleNameInputEdit").val());
			
			if(roleName == null || roleName == "") {
				layer.msg("请输入有效角色名称！");
				
				return ;
			}
			
			// 2.发送请求
			$.ajax({
				"url":"role/update/role.json",
				"type":"post",
				"data":{
					"id":window.roleId,
					"name":roleName
				},
				"dataType":"json",
				"success":function(response){
					var result = response.result;
					
					if(result == "SUCCESS") {
						layer.msg("操作成功！");
						
						// 3.操作成功重新分页
						showPage();
					}
					
					if(result == "FAILED") {
						layer.msg(response.message);
					}
					
					// 4.不管成功还是失败，关闭模态框
					$("#editModal").modal("hide");
					
				}
			});
		});
		
		$("#roleTableBody").on("click",".checkBtn",function(){
			
			// 将角色id存入全局变量
			window.roleId = $(this).attr("roleId");
			
			$("#roleAssignAuthModal").modal("show");
			
			// 1.创建setting对象
			var setting = {
					"data": {
						"simpleData": {
							"enable": true,
							"pIdKey": "categoryId"
						},
						"key": {
							"name": "title"
						}
					},
					"check": {
						"enable": true
					}
				};
			
			// 2.获取JSON数据
			var ajaxResult = $.ajax({
				"url": "assign/get/all/auth.json",
				"type": "post",
				"dataType": "json",
				"async": false
			});
			
			if(ajaxResult.responseJSON.result == "FAILED") {
				layer.msg(ajaxResult.responseJSON.message);
				
				return ;
			}
			
			var zNodes = ajaxResult.responseJSON.data;
			
			// 3.初始化树形结构
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			
			// 4.将树形结构展开
			$.fn.zTree.getZTreeObj("treeDemo").expandAll(true);
			
			// 5.查询以前已经分配过的authId
			ajaxResult = $.ajax({
				"url": "assign/get/assigned/auth/id/list.json",
				"type": "post",
				"data": {
					"roleId": $(this).attr("roleId"),
					"random": Math.random()
				},
				"dataType": "json",
				"async": false
			});
			
			if(ajaxResult.responseJSON.result == "FAILED") {
				layer.msg(ajaxResult.responseJSON.message);
				return ;
			}
			
			var authIdList = ajaxResult.responseJSON.data;
			
			// 6.使用authIdList勾选对应的树形节点
			// ①遍历authIdList
			for (var i = 0; i < authIdList.length; i++) {
				// ②在遍历过程中获取每一个authId
				var authId = authIdList[i];
				
				// ③根据authId查询到一个具体的树形节点
				// key：查询节点的属性名
				// value：查询节点的属性值，这里使用authId
				var key = "id";
				
				var treeNode = $.fn.zTree.getZTreeObj("treeDemo").getNodeByParam(key, authId);
				
				// ④勾选找到的节点
				// treeNode：当前要勾选的节点
				// true：表示设置为勾选状态
				// false：表示不联动
				$.fn.zTree.getZTreeObj("treeDemo").checkNode(treeNode, true, false);
				
			}
		});
		
		// 给在模态框中的分配按钮绑定单击响应函数
		$("#roleAssignAuthBtn").click(function(){
			
			var authIdArray = new Array();
			
			// 调用zTreeObj的方法获取当前已经被勾选的节点
			var checkedNodes = $.fn.zTree.getZTreeObj("treeDemo").getCheckedNodes();
			
			// 遍历checkedNodes
			for(var i = 0; i < checkedNodes.length; i++) {
				
				// 获取具体的一个节点
				var node = checkedNodes[i];
				
				// 获取当前节点的id属性
				var authId = node.id;
				
				// 将authId存入数组
				authIdArray.push(authId);
			}
			
			// 在handler方法中使用@RequestBody接收
			// 方便使用的数据类型是：@RequestBody Map<String, List<Integer>>
			// {"roleIdList":[2],"authIdList":[2,3,5,7]}
			// 封装要发送给handler的JSON数据
			var requestBody = {"roleIdList":[window.roleId], "authIdList": authIdArray};
			
			// 发送请求
			var ajaxResult = $.ajax({
				"url": "assign/do/assign.json",
				"type": "post",
				"data": JSON.stringify(requestBody),
				"contentType": "application/json;charset=UTF-8",
				"dataType": "json",
				"async": false
			});
			
			if(ajaxResult.responseJSON.result == "SUCCESS") {
				
				layer.msg("操作成功！");
				
			}
			
			if(ajaxResult.responseJSON.result == "FAILED") {
				
				layer.msg(ajaxResult.responseJSON.message);
				
			}
			
			$("#roleAssignAuthModal").modal("hide");
			
		});
	});
</script>
</head>
<body>

	<%@ include file="/WEB-INF/include-nav.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="/WEB-INF/include-sidebar.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">
							<i class="glyphicon glyphicon-th"></i> 数据列表
						</h3>
					</div>
					<div class="panel-body">
						<form class="form-inline" role="form" style="float: left;">
							<div class="form-group has-feedback">
								<div class="input-group">
									<div class="input-group-addon">查询条件</div>
									<input id="keywordInput" class="form-control has-success" type="text"
										placeholder="请输入查询条件">
								</div>
							</div>
							<button id="searchBtn" type="button" class="btn btn-warning">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
						</form>
						<button id="batchRemoveBtn" type="button" class="btn btn-danger"
							style="float: right; margin-left: 10px;">
							<i class=" glyphicon glyphicon-remove"></i> 批量删除
						</button>
						<button id="addBtn" type="button" class="btn btn-primary"
							style="float: right;">
							<i class="glyphicon glyphicon-plus"></i> 新增
						</button>
						<br>
						<hr style="clear: both;">
						<div class="table-responsive">
							<table class="table  table-bordered">
								<thead>
									<tr>
										<th width="30">#</th>
										<th width="30"><input id="summaryBox" type="checkbox"></th>
										<th>名称</th>
										<th width="100">操作</th>
									</tr>
								</thead>
								<tbody id="roleTableBody"></tbody>
								<tfoot>
									<tr>
										<td colspan="6" align="center">
											<div id="Pagination" class="pagination">
												<!-- 这里显示分页 -->
											</div>
										</td>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="/WEB-INF/include-modal-role-confirm.jsp" %>
	<%@ include file="/WEB-INF/include-modal-role-add.jsp" %>
	<%@ include file="/WEB-INF/include-modal-role-edit.jsp" %>
	<%@ include file="/WEB-INF/include-modal-assign-auth.jsp" %>

</body>
</html>