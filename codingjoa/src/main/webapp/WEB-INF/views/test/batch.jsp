<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>batch.jsp</title>
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
		font-size: 60px;
		font-weight: bold;
		padding-top: 10px;
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
	<p>batch.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-warning mx-3" onclick="config()">config</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="jobParameters()">jobParameters</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="jobExplorer()">jobExplorer</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="jobRegistry()">jobRegistry</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-primary mx-3" onclick="runJobA()">run JobA</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="runJobB()">run JobB</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function config() {
		console.log("## config");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/config",
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
	
	function runJobA() {
		console.log("## runJobA");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/run/job-a",
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

	function runJobB() {
		console.log("## runJobB");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/run/job-b",
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
	
	function jobParameters() {
		console.log("## jobParameters");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-parameters",
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

	function jobExplorer() {
		console.log("## jobExplorer");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-explorer",
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

	function jobRegistry() {
		console.log("## jobRegistry");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-registry",
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