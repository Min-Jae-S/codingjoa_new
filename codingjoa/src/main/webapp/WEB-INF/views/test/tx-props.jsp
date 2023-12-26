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
	<p class="mb-0">tx-props.jsp</p>
	<div class="d-flex justify-content-end">
		<button class="btn btn-warning mx-2" onclick="selectAll()">SELECT ALL</button>
		<button class="btn btn-warning mx-2" onclick="removeAll()">DELETE ALL</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Rollback</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest1()">TRY~CATCH</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest2()">THROWS</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest3()">THROWS E</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest4()">THROWS SQLE</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Propagation</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="propagationTest1()">REQUIRED</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="propagationTest2()">REQUIRES_NEW1</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="propagationTest3()">REQUIRES_NEW2</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="propagationTest4()">MANDATORY</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Isolation Level</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest1()">test1</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest2()">test2</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest3()">test3</button>
		<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest4()">test4</button>
	</div>
	<!-- <p class="sub-p mt-4 pl-4 mb-2">- Time out</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<p class="sub-p mt-4 pl-4 mb-2">- Read Only</p>
	<div class="test d-flex justify-content-center">
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
	</div> -->
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function selectAll() {
		console.log("## selectAll");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/select-all",
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
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function removeAll() {
		console.log("## removeAll");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/remove-all",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function rollbackTest1() {
		console.log("## rollbackTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest2() {
		console.log("## rollbackTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function rollbackTest3() {
		console.log("## rollbackTest3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest4() {
		console.log("## rollbackTest4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest1() {
		console.log("## propagationTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest2() {
		console.log("## propagationTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest3() {
		console.log("## propagationTest3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest4() {
		console.log("## propagationTest4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function isolationTest1() {
		console.log("## isolationTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function isolationTest2() {
		console.log("## isolationTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function isolationTest3() {
		console.log("## isolationTest3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function isolationTest4() {
		console.log("## isolationTest4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
</script>
</body>
</html>