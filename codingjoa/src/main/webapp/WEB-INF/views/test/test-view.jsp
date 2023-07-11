<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>TEST</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align: center; 
		margin-top: 200px; 
		font-size: 100px;
		font-weight: bold;
	}
	
	button {
		width: 100px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div>
	<p>TEST</p>
	<div class="d-flex justify-content-center">
		<button class="btn btn-danger mx-3" onclick="test1()">test1</button>
		<button class="btn btn-danger mx-3" onclick="test2()">test2</button>
		<button class="btn btn-danger mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary mx-3" onclick="test4()">test4</button>
		<button class="btn btn-primary mx-3" onclick="test5()">test5</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function test1() {
		let url = "${contextPath}/test/test1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(result);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}

	function test2() {
		let url = "${contextPath}/test/test2";
		console.log("## url = %s", url);
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify({
				param1 : "",
				param2 : -1,
				param3 : 9999
			}),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(result);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}

	function test3() {
		let url = "${contextPath}/test/aaaa";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(result);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}

	function test4() {
		let url = "${contextPath}/test/test4";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(result);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}

	function test5() {
		let url = "${contextPath}/test/test5";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(result);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}
	
	function isJSON(data) {
		try {
			JSON.parse(data);
			return true;
		} catch(e) {
			return false;
		}
	}
</script>
</body>
</html>