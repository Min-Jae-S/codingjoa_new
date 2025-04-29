<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>batch-quartz.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
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
	<p>batch-quartz.jsp</p>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-warning" onclick="config()">config</button>
		<button class="btn btn-lg btn-warning" onclick="builders()">builders</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-outline-primary" onclick="jobRepository()">jobRepository</button>
		<button class="btn btn-lg btn-outline-primary" onclick="jobExplorer()">jobExplorer</button>
		<button class="btn btn-lg btn-outline-primary" onclick="jobLauncher()">jobLauncher</button>
		<button class="btn btn-lg btn-outline-primary" onclick="jobParameters()">jobParameters</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary" onclick="runJobA()">run JobA</button>
		<button class="btn btn-lg btn-primary" onclick="runJobB()">run JobB</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function config() {
		console.log("## config");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/config",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
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