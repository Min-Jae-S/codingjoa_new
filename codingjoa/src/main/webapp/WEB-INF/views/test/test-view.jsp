<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>test-view</title>
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
		width: 160px;
	}
	
	div {
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div>
	<p>TEST</p>
	<div class="d-flex justify-content-center mt-5">
		<button class="btn btn-danger btn-lg mx-3" onclick="test1()">test1</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test2()">test2</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test4()">test4</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test5()">test5</button>
	</div>
	<div class="d-flex justify-content-center mt-5">
		<button class="btn btn-warning btn-lg mx-3" onclick="testException1()">e1</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="testException2()">e2</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="testException3()">e3</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="testResponse()">response</button>
		<button class="btn btn-secondary btn-lg mx-3" onclick="colored_console()">console</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function colored_console() {
		console.log("%c## SUCCESS","color:green;background-color:#dcedc8");
		console.log("%c## ERROR","color:red;background-color:#ffe6e6");
	}
	
	function parseError(jqXHR) {
		try {
			let errorResponse = JSON.parse(jqXHR.responseText);
			console.log(JSON.stringify(errorResponse, null, 2));
			return errorResponse;
		} catch(e) {
			alert("## Parsing Error");
			return null;
		}
	}
	
	function proccessError(jqXHR) {
		try {
			let errorResponse = JSON.parse(jqXHR.responseText);
			let errorMap = errorResponse.errorMap;
			if (errorMap != null) {
				$.each(errorMap, function(errorField, errorMessage) {
					console.log("> errorField = %s, errorMessage = %s", errorField, errorMessage);	
				});
			} else {
				console.log("> errorMessage = %s", errorResponse.errorMessage);
			}
		} catch(e) {
			console.log("## Parsing Error");
		}
	}

	function test1() { // errorResponse(errorMap=null, errorMeesage=OK)
		let url = "${contextPath}/test/test1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test2() { // errorResonse(errorMap=OK, errorMeesage=null)
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
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test3() { // no errorResponse
		let url = "${contextPath}/test/aaaa";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test4() {
		let url = "${contextPath}/test/test4";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			//dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
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
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}
	
	function testException1() {
		let url = "${contextPath}/test/test-exception1";
		console.log("## url = %s", url);
		$.ajax({
			type : "POST",
			url : url,
			/* data : JSON.stringify({
				param1 : "",	// String, @NotEmpty
				param2 : "cc",	// int, @Positive
				param3 : "dd",	// int, @BoardCategoryCode
			}), */
			data : JSON.stringify({
				param1 : "",	// String, 	@NotEmpty
				param2 : -1,	// int, 	@Positive
				param3 : 999,	// int, 	@BoardCategoryCode
				param4 : 1		// int, 	TestValidator(between 4 and 10);
			}),
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}

	function testException2() {
		let url = "${contextPath}/test/test-exception2";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}

	function testException3() {
		let url = "${contextPath}/test/test-exception3";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}
	
	function testResponse() {
		let url = "${contextPath}/test/test-response";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}

	function sampleAnno() {
		let url = "${contextPath}/test/sample-anno";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}

	function sample() {
		let url = "${contextPath}/test/sample";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				proccessError(jqXHR);
			}
		});
	}
	
</script>
</body>
</html>