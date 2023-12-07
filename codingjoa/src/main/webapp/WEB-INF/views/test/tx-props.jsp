<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>tx-props.jsp</title>
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
	
	.sub-p {
		font-size: 25px;
		padding-top: 0;
		text-align: left;
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
<div class="container my-4 px-0">
	<p>tx-props.jsp</p>
	<p class="sub-p mt-4 pl-4 mb-2">- Transaction Propagation</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="propagation1()">propagation1</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="propagation2()">propagation2</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="propagation3()">propagation3</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Isolation Level</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="test1()">#</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="test2()">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Time out</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="test1()">#</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="test2()">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Read Only</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="test1()">test1</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="test2()">test2</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function propagation1() {
		console.log("## propagation1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation1",
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

	function propagation2() {
		console.log("## propagation2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation2",
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

	function propagation3() {
		console.log("## propagation3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation3",
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

	function test4() {
		console.log("## test4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/test4",
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