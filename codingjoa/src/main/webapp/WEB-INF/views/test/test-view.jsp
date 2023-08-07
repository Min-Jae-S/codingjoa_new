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
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 80px;
		font-weight: bold;
	}
	
	div {
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
	
	button {
		width: 230px;
	}
	
	img {
		width: 80px;
		height: 80px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div>
	<p class="mt-5">TEST</p>
	<div class="d-none justify-content-center mt-5">
		<button class="btn btn-danger btn-lg mx-3" onclick="test1()">test1</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test2()">test2</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test4()">test4</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test5()">test5</button>
	</div>
	<div class="d-flex justify-content-center mt-5">
		<button class="btn btn-warning btn-lg mx-3" onclick="methodArgumentException()">method-argumentEx</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="expectedException1()">expectedEx1</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="expectedException2()">expectedEx2</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="testException()">textEx</button>
		<!-- <button class="btn btn-secondary btn-lg" onclick="colored_console()">console</button> -->
	</div>
	<div class="d-flex justify-content-center mt-5">
		<form class="d-none" id="testForm" method="POST" action="${contextPath}/test/test-form">
			<input type="hidden" name="foo[]" value="1">
			<input type="hidden" name="foo[]" value="2">
			<input type="hidden" name="foo[]" value="3">
		</form>
		<button class="btn btn-lg mx-3" onclick="submit()">submit</button>
		<button class="btn btn-lg mx-3" onclick="serialize()">serialize</button>
		<button class="btn btn-lg mx-3" onclick="serializeArray()">serializeArray</button>
		<button class="btn btn-lg mx-3" onclick="serializeObject()">serializeObject</button>
	</div>
	<div class="d-flex justify-content-center mt-5">
		<div class="d-flex flex-column px-5" style="border-right: 2px black solid;">
			<image class="mb-3 align-self-center border" id="testImage" src="${contextPath}/resources/image/img_profile.png">
			<button class="btn btn-success btn-lg" onclick="testUrlResource1()">testUrlResource1</button>
		</div>
		<div class="px-5" style="border-right: 2px black solid;">
			<image class="border" src="${contextPath}/test/test-url-resource2">
		</div>
		<div class="px-5">
			<button class="btn btn-success btn-lg" onclick="testUrlResource2()">testUrlResource2</button>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(document).ready(function() {
		$("#testForm").submit(function(e) {
			e.preventDefault();
			const formData = $(this).serializeObject();
			console.log("## formData");
			console.log(formData)
			
			const jsonData = JSON.stringify(formData);
			console.log("## jsonData");
			console.log(jsonData);
		});
		
		function submit() {
			$("form#testForm").submit();	
		}
	});

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
			console.log(JSON.stringify(errorResponse, null, 2));
			
			let details = errorResponse.details;
			if (details.length > 0) {
				$.each(details, function(index, item) {
					//console.log("> %s / %s", item.field, item.message);	
				});
			} else {
				alert(errorResponse.message);
			}
		} catch(e) {
			alert("## Parsing Error");
			console.log(jqXHR);
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
				proccessError(jqXHR);
			}
		});
	}
	
	function methodArgumentException() {
		let url = "${contextPath}/test/method-argument-exception";
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
				proccessError(jqXHR);
			}
		});
	}

	function expectedException1() {
		let url = "${contextPath}/test/expected-exception1";
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
				proccessError(jqXHR);
			}
		});
	}

	function expectedException2() {
		let url = "${contextPath}/test/expected-exception2";
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
				proccessError(jqXHR);
			}
		});
	}
	
	function testException() {
		let url = "${contextPath}/test/test-exception";
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
				proccessError(jqXHR);
			}
		});
	}
	
	function serialize() {
		console.log("## serialize");
		let FormData = $("#testForm").serialize();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}

	function serializeArray() {
		console.log("## serializeArray");
		let FormData = $("#testForm").serializeArray();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}
	
	function serializeObject() {
		console.log("## serializeObject");
		let FormData = $("#testForm").serializeObject();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}
	
	function testUrlResource1() {
		let url = "${contextPath}/test/test-url-resource1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
				$("#testImage").attr("src", result.data);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function testUrlResource2() {
		let url = "${contextPath}/test/test-url-resource2";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(result.data);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				console.log(jqXHR);
			}
		});
	}
</script>
</body>
</html>