<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>batch.jsp</title>
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
		column-gap: 2rem; 
	}
	
	div.test button {
		min-width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>batch.jsp</p>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-warning mx-3" onclick="config()">config</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="builders()">builders</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="jobRepository()">jobRepository</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="jobExplorer()">jobExplorer</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="jobLauncher()">jobLauncher</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="jobParameters()">jobParameters</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary mx-3" onclick="runJobA()">run JobA</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="runJobB()">run JobB</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
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
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function builders() {
		console.log("## builders");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/builders",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function jobParameters() {
		console.log("## jobParameters");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-parameters",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function jobExplorer() {
		console.log("## jobExplorer");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-explorer",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function jobRepository() {
		console.log("## jobRepository");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-repository",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function jobLauncher() {
		console.log("## jobLauncher");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/job-launcher",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function runJobA() {
		console.log("## runJobA");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/run/job-a",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function runJobB() {
		console.log("## runJobB");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch/run/job-b",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
</script>
</body>
</html>