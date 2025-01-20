<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>render.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		display: flex;
		justify-content: center;
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
	
	div.test button {
		width: 230px;
	}
	
	#testDiv {
		min-height: 400px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>render.jsp</p>
	<div class="test mt-5">
		<button class="btn btn-primary btn-lg mx-3" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg mx-3 invisible" onclick="test2()">test2</button>
		<button class="btn btn-primary btn-lg mx-3 invisible" onclick="test3()">test3</button>
	</div>
	<div class="test mt-5">
		<button class="btn btn-secondary btn-lg mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-secondary btn-lg mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-secondary btn-lg mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="container border border-dark rounded-xl mt-5" id="testDiv">
	
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function test1() {
		console.log("## test1");
		$("#testDiv").load("${contextPath}/test/render/test1", function(responseText, status, ) {
			console.log(result);
		});
	}
</script>
</body>
</html>