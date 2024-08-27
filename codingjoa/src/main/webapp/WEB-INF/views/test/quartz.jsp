<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>quartz.jsp</title>
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
	<p>quartz.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-warning mx-3" onclick="config()">config</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="state()">state</button>
		<button class="btn btn-lg btn-warning mx-3 invisible" onclick="pausedJobs()">pausedJobs</button>
		<button class="btn btn-lg btn-warning mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-primary btn-lg mx-3" onclick="start(this)" data-url="${contextPath}/test/quartz/start">start All</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="start(this)" data-url="${contextPath}/test/quartz/start/job-a">start JobA</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="start(this)" data-url="${contextPath}/test/quartz/start/job-b">start JobB</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="start(this)" data-url="${contextPath}/test/quartz/start/job-quartz">start QuartzJob</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-danger btn-lg mx-3" onclick="pause(this)" data-url="${contextPath}/test/quartz/pause">pause All</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="pause(this)" data-url="${contextPath}/test/quartz/pause/job-a">pause JobA</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="pause(this)" data-url="${contextPath}/test/quartz/pause/job-b" >pause JobB</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="pause(this)" data-url="${contextPath}/test/quartz/pause/job-quartz" >pause QuartzJob</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function config() {
		console.log("## config");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz/config",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function state() {
		console.log("## state");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz/state",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});	
	}
	
	function pausedJobs() {
		console.log("## pausedJobs");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz/paused-jobs",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});	
	}
	
	function start(button) {
		console.log("## " + $(button).text());
		$.ajax({
			type : "GET",
			url : $(button).data("url"),
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}

	function pause(button) {
		console.log("## " + $(button).text());
		$.ajax({
			type : "GET",
			url : $(button).data("url"),
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> %s", result);
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