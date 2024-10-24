<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>aop.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
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
	
	.btn-fixed {
		width: 183px;
	}
	
	.btn-lg-fixed {
		width: 280px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>aop.jsp</p>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-warning btn-lg btn-lg-fixed" onclick="triggerNoHandlerFoundException()">NoHandlerFoundException</button>
		<button class="btn btn-warning btn-lg btn-lg-fixed" onclick="triggerExceptionByMvc()">MVC exception</button>
		<button class="btn btn-warning btn-lg btn-lg-fixed" onclick="triggerExceptionByAjax()">AJAX exception</button>
	</div>
	<h4 class="px-5">@ MVC Exception</h4>
	<div class="test mb-5 px-5">
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerControllerExceptionByMvc()">controller exception</button>
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerInterceptorExceptionByMvc()">interceptor exception</button>
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerFilterExceptionByMvc()">filter exception</button>
	</div>
	<h4 class="px-5">@ AJAX Exception</h4>
	<div class="test mb-5 px-5">
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerControllerExceptionByAjax()">controller exception</button>
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerInterceptorExceptionByAjax()">interceptor exception</button>
		<button class="btn btn-primary btn-lg btn-lg-fixed" onclick="triggerFilterExceptionByAjax()">filter exception</button>
	</div>
	<h4 class="invisible">invisible</h4>
	<div class="test mb-5 px-5">
		<button class="btn btn-primary btn-lg btn-fixed" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg btn-fixed" onclick="test2()">test2</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function triggerNoHandlerFoundException() {
		console.log("## triggerNoHandlerFoundException");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/aop/aa",
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
	
	function triggerExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception";
	}

	function triggerExceptionByAjax() {
		console.log("## triggerExceptionByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception",
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
	
	function triggerControllerExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/controller";
	}

	function triggerInterceptorExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/interceptor";
	}
	
	function triggerFilterExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/filter";
	}
	
	function triggerControllerExceptionByAjax() {
		console.log("## triggerExceptionInControllerByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/controller",
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
	
	function triggerInterceptorExceptionByAjax() {
		console.log("## triggerExceptionInInterceptorByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/interceptor",
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
	
	function triggerFilterExceptionByAjax() {
		console.log("## triggerExceptionInFilterByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/filter",
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

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/test1",
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
			url : "${contextPath}/test/api/aop/test2",
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