<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>tx.jsp</title>
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
		font-size: 60px;
		font-weight: bold;
		padding-top: 10px;
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
	<p>tx.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-warning mx-3" onclick="dataSources()">dataSources</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="managers()">managers</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="factory()">factory</button>
		<button class="btn btn-lg btn-warning mx-3" onclick="template()">template</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="test1()">test1</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="test2()">test2</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="test3()">test3</button>
		<button class="btn btn-lg btn-outline-primary mx-3" onclick="test4()">test4</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-primary mx-3" onclick="select()">select</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="insert()">insert</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="update()">update</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="remove()">remove</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-secondary mx-3" onclick="select()">select</button>
		<button class="btn btn-lg btn-secondary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-secondary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-secondary mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function dataSources() {
		console.log("## dataSources");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/dataSources",
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

	function managers() {
		console.log("## managers");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/managers",
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
	
	function factory() {
		console.log("## factory");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/factory",
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

	function template() {
		console.log("## template");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/template",
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

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test1",
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
			url : "${contextPath}/test/tx/test2",
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

	function test3() {
		console.log("## test3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test3",
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
			url : "${contextPath}/test/tx/test4",
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

	function select() {
		console.log("## select");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/select",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> result = no records");
				} else {
					console.log("> result = ");
					console.log(JSON.stringify(result, null, 2));
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function insert() {
		console.log("## insert");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/insert",
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
	
	function update() {
		console.log("## update");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/update",
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
	
	function remove() {
		console.log("## remove");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/remove",
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
	
	function invoke() {
		console.log("## invoke");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke",
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