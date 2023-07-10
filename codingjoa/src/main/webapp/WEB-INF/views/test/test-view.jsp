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
	<div class="d-flex justify-content-center mb-4">
		<button class="btn btn-primary mx-2" type="button" onclick="success1()">success1</button>
		<button class="btn btn-primary mx-2" type="button" onclick="success2()">success2</button>
		<button class="btn btn-primary mx-2" type="button" onclick="success3()">success3</button>
		<button class="btn btn-primary mx-2" type="button" onclick="success4()">success4</button>
	</div>
	<div class="d-flex justify-content-center">
		<button class="btn btn-danger mx-2" type="button" onclick="error1()">error1</button>
		<button class="btn btn-danger mx-2" type="button" onclick="error2()">error2</button>
		<button class="btn btn-danger mx-2" type="button" onclick="error3()">error3</button>
		<button class="btn btn-danger mx-2" type="button" onclick="error4()">error4</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function success1() {
		console.log('## success1');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/success1",
			dataType : "json",
			success : function(result) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}

	function success2() {
		console.log('## success2');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/success2",
			dataType : "json",
			success : function(result) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}
	
	function success3() {
		console.log('## success3');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/success3",
			dataType : "json",
			success : function(result) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}

	function success4s() {
		console.log('## success4');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/success4",
			dataType : "json",
			success : function(result) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}
	
	function error1() {
		console.log('## error1');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/error1",
			dataType : "json",
			success : function(reuslt) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}

	function error2() {
		console.log('## error2');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/error2",
			dataType : "json",
			success : function(reuslt) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}
	
	function error3() {
		console.log('## error3');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/error3",
			dataType : "json",
			success : function(reuslt) {
				console.log(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}

	function error4() {
		console.log('## error4');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/error4",
			dataType : "json",
			success : function(reuslt) {
				console.log(result);
			},
			error : function(jqXHR) {
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