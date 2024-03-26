<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>rest-api.jsp</title>
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
	<p>rest-api.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-primary mx-3" onclick="test1()">test1</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="test2()">test2</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-none justify-content-center mt-5">
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="#">#</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="#">#</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test1",
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

	function test2() {
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test2",
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