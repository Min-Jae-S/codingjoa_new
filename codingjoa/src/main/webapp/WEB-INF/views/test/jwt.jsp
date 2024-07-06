<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>jwt.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
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
	
	.test-btn, div.input-group {
		width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>jwt.jsp</p>
	<div class="test d-flex justify-content-center mt-5 mb-5">
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test2()">test2</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test4()">test4</button>
	</div>
	<div class="d-flex flex-column px-3">
		<button class="btn btn-primary btn-lg mx-3 mb-2" onclick="test5()">
			<span>test5</span>
		</button>
		<div class="px-3 d-flex justify-content-around">
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio1" value="undefined" checked>
			  <label class="form-check-label" for="test5Radio1">no header</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio2" value="">
			  <label class="form-check-label" for="test5Radio2">Authorization: blank header</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio3" value="Authorization">
			  <label class="form-check-label" for="test5Radio3">Authorization</label>
			</div>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
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
			url : "${contextPath}/test/jwt/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
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
			url : "${contextPath}/test/jwt/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function test4() {
		console.log("## test4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(result);
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test5() {
		console.log("## test5");
		let option = $("[name='test5Radio']:checked").val();
		console.log("> option = '%s'", option);
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test5",
			beforeSend : function(xhr) {
				if (option != "undefined") {
					xhr.setRequestHeader("authorization", option);
				}
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
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