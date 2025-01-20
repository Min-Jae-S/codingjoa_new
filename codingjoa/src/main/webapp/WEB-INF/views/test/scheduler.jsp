<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>scheduler.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
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
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
	
	div.test button {
		width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>scheduler.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-primary btn-lg mx-3" onclick="startTimer()">startTimer</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="stopTimer()">stopTimer</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="timer()">timer</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-secondary btn-lg mx-3" onclick="startExecutor()">startExecutor</button>
		<button class="btn btn-secondary btn-lg mx-3" onclick="stopExecutor()">stopExecutor</button>
		<button class="btn btn-secondary btn-lg mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function timer() {
		console.log("## timer");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/scheduler/timer",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}
	
	function startTimer() {
		console.log("## startTimer");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/scheduler/startTimer",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}

	function stopTimer() {
		console.log("## stopTimer");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/scheduler/stopTimer",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}

	function startExecutor() {
		console.log("## startExecutor");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/scheduler/startExecutor",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}

	function stopExecutor() {
		console.log("## stopExecutor");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/scheduler/stopExecutor",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}
</script>
</body>
</html>