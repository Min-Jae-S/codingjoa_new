<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>oauth2.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		display: flex;
		column-gap: 30px;
	}
	
	div.test button {
		width: 183px;
	}
	
	div.form-check {
		font-size: 1rem;
		font-weight: 400;
	}
	
	.social-login-btn {
		width: 183px;
		height: 45px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>oauth2.jsp</p>
	<div class="test mt-5 mb-5 px-5">
		<a class="btn btn-warning" href="${kakaoLoginUrl}">Kakao Login</a>
		<a class="btn btn-warning" href="${naverLoginUrl}">Naver Login</a>
		<a class="btn btn-danger" href="${contextPath}/test/oauth2/aaa/callback">trigger invalid auth</a>
	</div>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-primary" onclick="test1()">test1</button>
		<button class="btn btn-primary" onclick="test2()">test2</button>
		<button class="btn btn-primary" onclick="test3()">test3</button>
		<button class="btn btn-primary" onclick="test4()">test4</button>
	</div>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-primary" onclick="test5()">test5</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function test1() {
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/oauth2/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test2() {
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/oauth2/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test3() {
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/oauth2/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test4() {
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/oauth2/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
</script>
</body>
</html>