<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>aop.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
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
		display: flex;
		column-gap: 30px !important;
	}

	div.test button {
		flex-grow: 1;
	}
	
	.btn-fixed {
		max-width: 190px !important;
	}
	
	.btn-lg-fixed {
		max-width: 280px !important;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>aop.jsp</p>
	<div class="test mt-5 mb-3 px-5">
		<button class="btn btn-warning btn-lg" onclick="triggerNoHandlerFoundExceptionByAjax()">NoHandlerFoundEx<br>(AJAX)</button>
		<button class="btn btn-warning btn-lg" onclick="triggerHttpRequestMethodNotSupportedExceptionByAjax()">HttpRequestMethodNotSupportedEx<br>(AJAX)</button>
		<button class="btn btn-warning btn-lg" onclick="triggerExpectedExceptionByAjax()">ExpectedEx<br>(AJAX)</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-warning btn-lg" onclick="triggerNoHandlerFoundExceptionByMvc()">NoHandlerFoundEx<br>(MVC)</button>
		<button class="btn btn-warning btn-lg" onclick="triggerHttpRequestMethodNotSupportedExceptionByMvc()">HttpRequestMethodNotSupportedEx<br>(MVC)</button>
		<button class="btn btn-warning btn-lg" onclick="triggerExpectedExceptionByMvc()">ExpectedEx<br>(MVC)</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-primary btn-lg" onclick="triggerControllerExceptionByAjax()">controller exception<br>(AJAX)</button>
		<button class="btn btn-primary btn-lg" onclick="triggerInterceptorExceptionByAjax()">interceptor exception<br>(AJAX)</button>
		<button class="btn btn-primary btn-lg" onclick="triggerFilterExceptionByAjax()">filter exception<br>(AJAX)</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-primary btn-lg" onclick="triggerControllerExceptionByMvc()">controller exception<br>(MVC)</button>
		<button class="btn btn-primary btn-lg" onclick="triggerInterceptorExceptionByMvc()">interceptor exception<br>(MVC)</button>
		<button class="btn btn-primary btn-lg" onclick="triggerFilterExceptionByMvc()">filter exception<br>(MVC)</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-warning btn-lg btn-lg-fixed" onclick="getExceptionHandler()">ExHandler</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-primary btn-lg" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg" onclick="test2()">test2</button>
		<button class="btn btn-primary btn-lg" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg" onclick="test4()">test4<br>(response.sendError)</button>
		<button class="btn btn-primary btn-lg" onclick="location.href='${contextPath}/test/aop/test5'">test5<br>(noView)</button>
		<button class="btn btn-primary btn-lg" onclick="location.href='${contextPath}/test/aop/test6'">test6<br>(testEx)</button>
	</div>
	<div class="test mb-3 px-5">
		<button class="btn btn-warning btn-lg btn-lg-fixed" onclick="triggerAsyncEx()">triggerAsyncException</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function triggerNoHandlerFoundExceptionByAjax() {
		console.log("## triggerNoHandlerFoundExceptionByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/aop/aa",
			dataType: "json",
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

	function triggerHttpRequestMethodNotSupportedExceptionByAjax() {
		console.log("## triggerHttpRequestMethodNotSupportedExceptionByAjax");
		$.ajax({
			type : "POST",
			url : "${contextPath}/test/api/aop/exception",
			dataType: "json",
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
	
	function triggerExpectedExceptionByAjax() {
		console.log("## triggerExpectedExceptionByAjax");
		$.ajax({
			type : "GET",
			dataType: "json",
			url : "${contextPath}/test/api/aop/exception",
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
	
	function triggerNoHandlerFoundExceptionByMvc() {
		location.href = "${contextPath}/test/aop/aa";
	}

	function triggerHttpRequestMethodNotSupportedExceptionByMvc() {
		let $form = $('<form></form>');
		$form.attr("method", "POST");
		$form.attr("action", "${contextPath}/test/aop/aa");
        $(document.body).append($form);
        $form.submit();
        $form.remove();
	}
	
	function triggerExpectedExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception";
	}
	
	function triggerControllerExceptionByAjax() {
		console.log("## triggerExceptionInControllerByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/controller",
			dataType: "json",
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
	
	function triggerInterceptorExceptionByAjax() {
		console.log("## triggerExceptionInInterceptorByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/interceptor",
			dataType: "json",
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
	
	function triggerFilterExceptionByAjax() {
		console.log("## triggerExceptionInFilterByAjax");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/filter",
			dataType: "json",
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
	
	function triggerControllerExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/controller";
	}

	function triggerInterceptorExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/interceptor";
	}
	
	function triggerFilterExceptionByMvc() {
		location.href = "${contextPath}/test/aop/exception/filter";
	}
	
	function getExceptionHandler() {
		console.log("## getExceptionHandler");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception-handler",
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

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/test1",
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
			url : "${contextPath}/test/api/aop/test2",
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
			url : "${contextPath}/test/api/aop/test3",
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
			url : "${contextPath}/test/api/aop/test4",
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
	
	function triggerAsyncEx() {
		console.log("## triggerAsyncEx");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/api/aop/exception/async",
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