<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="UTF-8">
<head>
<%@ include file="/WEB-INF/include-head.jsp" %>
</head>
<body>

	<%@ include file="/WEB-INF/include-nav.jsp" %>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="/WEB-INF/include-sidebar.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
								<ol class="breadcrumb">
					<li><a href="admin/to/main/page.html">首页</a></li>
					<li><a href="admin/query/for/search.html">数据列表</a></li>
					<li class="active">更新</li>
				</ol>
				<div class="panel panel-default">
					<div class="panel-heading">
						表单数据
					</div>
					<div class="panel-body">
						<form:form action="admin/update.html" method="post" modelAttribute="admin">
							<!-- 模型对象中包含的属性可以使用form:hidden -->
							<form:hidden path="id"/>
							
							<!-- 模型对象中没有的属性不能使用form:hidden -->
							<input type="hidden" name="pageNum" value="${param.pageNum }" />
							<div class="form-group">
								<label for="exampleInputPassword1">登录账号</label>
								<form:input path="loginAcct" cssClass="form-control"/>
							</div>
							<div class="form-group">
								<label for="exampleInputPassword1">登录密码</label>
								<form:input path="userPswd" cssClass="form-control"/>
							</div>
							<div class="form-group">
								<label for="exampleInputPassword1">用户昵称</label>
								<form:input path="userName" cssClass="form-control"/>
							</div>
							<div class="form-group">
								<label for="exampleInputEmail1">邮箱地址</label>
								<form:input path="email" cssClass="form-control"/>
							</div>
							<button type="submit" class="btn btn-success">
								<i class="glyphicon glyphicon-edit"></i> 更新
							</button>
							<button type="reset" class="btn btn-danger">
								<i class="glyphicon glyphicon-refresh"></i> 重置
							</button>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>