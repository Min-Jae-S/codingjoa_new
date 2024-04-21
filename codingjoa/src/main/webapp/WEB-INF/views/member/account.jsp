<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa : 계정 관리</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	.list-group-item:first-child {
		border-top: 2px solid #ccc;
	}

	.list-group-item:last-child {
		border-bottom: 2px solid #ccc;
	}
	
	.list-group-item {
		border-left: none;
		border-right: none;
		padding: 40px 35px 40px 35px;
		cursor: pointer;
		display: flex;
		align-items: center;
	}
	
	.item {
		margin-left: auto;
		margin-right: auto;
		width: 350px;
		font-weight: bold;
	}
	
	.item span {
		font-size: 18px;
		color: black;
	}
	
	.item p {
		margin-bottom: 0;
		margin-top: 10px;
		color: #888A88;
		font-size: 14px;
	}
	
	i {
		font-size: 1.5rem;
		color: #4F66AA;
	}
	
	.accout-wrap {
		width: 540px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container account-container">
	<div class="accout-wrap">
		<div class="list-group">
			<div class="list-group-item list-group-item-action" onclick="location.href='${contextPath}/member/account/info'">
				<span class="mr-4"><i class="fa-solid fa-user"></i></span>
				<div class="item">
					<span>계정 정보</span>
					<p>이메일, 주소 등의 개인정보를 확인하고 관리합니다.</p>
				</div>
				<span class="ml-4"><i class="fa-solid fa-angle-right"></i></span>
			</div>
			<div class="list-group-item list-group-item-action" onclick="location.href='${contextPath}/member/account/confirmPassword'">
				<span class="mr-4"><i class="fa-solid fa-lock"></i></span>
				<div class="item">
					<span>계정 보안</span>
					<p>비밀번호를 관리합니다.</p>
				</div>
				<span class="ml-4"><i class="fa-solid fa-angle-right"></i></span>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>