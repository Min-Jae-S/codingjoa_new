<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 찾기 결과</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	dt {
		font-size: 14px;
		font-weight: bold;
	}
	
	p.title {
		margin-bottom: 60px;
		font-size: 14px;
		font-weight: bold;
	}
	
	p.content {
		margin-bottom: 60px;
		font-size: 22px;
		font-weight: 900;
		text-align: center;
		color: #343a40;
		background-color: #fee500;
		padding: 30px 0 30px 0;
	}
	
	h5 {
		font-weight: bold;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container find-password-result-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5>비밀번호 찾기 결과</h5>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>

</script>
</body>
</html>