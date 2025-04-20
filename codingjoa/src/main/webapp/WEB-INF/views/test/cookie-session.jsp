<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>cookie-session.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		padding-left: 1.5rem;
		padding-right: 1.5rem;
	}
	
	.test-btn, div.input-group {
		width: 230px;
	}
	
	div.form-check {
		font-size: 1rem;
		font-weight: 400;
	}
	
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>cookie-session.jsp</p>
	<div class="test d-flex justify-content-between mt-5 mb-5">
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test2()">test2</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="createCookie()">create cookie</button>
		<button class="btn btn-secondary btn-lg test-btn mx-3" onclick="checkCookie()">check cookie</button>
	</div>
	
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function checkCookie() {
		console.log("## checkCookie");
		alert(document.cookie);
	}

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie-session/test1",
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
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie-session/test2",
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
	
	function createCookie() {
		console.log("## createCookie");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie-session/create-cookie",
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