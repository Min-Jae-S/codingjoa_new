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
<script src="${contextPath}/resources/js/utils.js"></script>
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
	
	div.test button,
	div.test input {
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
		<button class="btn btn-lg btn-primary mx-3" onclick="test3()">test3</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="get(this)">GET</button>
			<input type="text" class="form-control text-center">
		</div>
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="post(this)">POST</button>
			<input type="text" class="form-control text-center">
		</div>
		<div class="d-flex flex-column mx-3 invisible">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="#">#</button>
			<input type="text" class="form-control text-center">
		</div>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="put(this)">PUT</button>
			<input type="text" class="form-control text-center">
		</div>
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="patch(this)">PATCH</button>
			<input type="text" class="form-control text-center">
		</div>
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-lg btn-outline-secondary mb-2" onclick="remove(this)">DELETE</button>
			<input type="text" class="form-control text-center">
		</div>
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
				parseError(jqXHR);
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
				parseError(jqXHR);
			}
		});		
	}

	function test3() {
		console.log("## test3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});		
	}

	function get(button) {
		console.log("## GET");
		let id = $(button).siblings('input').val();
		console.log("id = %s", id);
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test-members/" + id,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});		
	}

	function post(button) {
		console.log("## POST");
		$.ajax({
			type : "POST",
			url : "${contextPath}/test/rest-api/test3-numbers",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});		
	}

	function put(button) {
		console.log("## PUT");
		let id = $(button).siblings('input').val();
		console.log("id = %s", id);
		$.ajax({
			type : "PUT",
			url : "${contextPath}/test/rest-api/test-members/" + id,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});		
	}

	function patch(button) {
		console.log("## PATCH");
		let id = $(button).siblings('input').val();
		console.log("id = %s", id);
		$.ajax({
			type : "PATCH",
			url : "${contextPath}/test/rest-api/test-members/" + id,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});		
	}

	function remove() {
		console.log("## DELETE");
		let id = $(button).siblings('input').val();
		console.log("id = %s", id);
		$.ajax({
			type : "DELETE",
			url : "${contextPath}/test/rest-api/test-members/" + id,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
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