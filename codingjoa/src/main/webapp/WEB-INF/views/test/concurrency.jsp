<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>concurrency.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
	<p>cncurrency.jsp</p>
	<div class="test mt-5 d-flex">
		<button class="btn btn-primary btn-lg" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg" onclick="test2()">test2</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		// ...
	});

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/concurrency/test1",
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