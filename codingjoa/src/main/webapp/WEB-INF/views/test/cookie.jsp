<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>cookie.jsp</title>
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
		display: flex;
		column-gap: 30px;
	}
	
	div.test button {
		width: 183px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>cookie.jsp</p>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-secondary btn-lg" onclick="checkCookies()">check cookies</button>
		<button class="btn btn-secondary btn-lg" onclick="removeCookies()">remove cookies</button>
	</div>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-primary btn-lg" onclick="addCookie1()">addCookie<br>by CookieUtils</button>
		<button class="btn btn-primary btn-lg" onclick="addCookie2()">addCookie<br>by new Cookie()</button>
		<button class="btn btn-primary btn-lg" onclick="addCookies()">addCookies</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function checkCookies() {
		console.log("## checkCookies");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie/check",
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

	function removeCookies() {
		console.log("## removeCookies");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie/remove",
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

	function addCookie1() {
		console.log("## addCookie1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie/add/cookie1",
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

	function addCookie2() {
		console.log("## addCookie2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie/add/cookie2",
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

	function addCookies() {
		console.log("## addCookies");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/cookie/add/cookies",
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