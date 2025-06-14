<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>redis-concurrency.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		column-gap: 2rem; 
	}

	div.test button {
		min-width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>redis-concurrency.jsp</p>
	<div class="test mt-5 d-flex">
		<button class="btn btn-warning btn-lg" onclick="hikariInfo()">HikariCP info</button>
		<button class="btn btn-warning btn-lg" onclick="redisInfo()">Redis info</button>
	</div>
	<div class="test mt-5 d-flex">
		<button class="btn btn-primary btn-lg" onclick="initRedis()">initiate redis</button>
		<button class="btn btn-primary btn-lg" onclick="redisSample()">redis sample</button>
		<button class="btn btn-primary btn-lg" onclick="redisEntries()">redis entries</button>
	</div>
	<div class="test mt-5 d-flex">
		<button class="btn btn-primary btn-lg" onclick="redisIncr()">redis INCR1</button>
		<button class="btn btn-primary btn-lg" onclick="redisIncr2()">redis INCR2</button>
	</div>
	<div class="test mt-5 d-none">
		<button class="btn btn-primary btn-lg" onclick="test1()">test1<br>(no CountDownLatch)</button>
		<button class="btn btn-primary btn-lg" onclick="test2()">test2<br>(CountDownLatch)</button>
	</div>
	<div class="test mt-5 d-flex">
		<button class="btn btn-primary btn-lg" onclick="incrCommentCountByBoardId()">INCR<br>comment_count</button>
		<button class="btn btn-primary btn-lg" onclick="decrCommentCountByBoardId()">DECR<br>comment_count</button>
		<button class="btn btn-primary btn-lg" onclick="flushCommentCount()">flush<br>comment_count</button>
		<button class="btn btn-primary btn-lg" onclick="flushViewCount()">flush<br>view_count</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		// ...
	});

	function hikariInfo() {
		console.log("## hikariInfo");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/hikari",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function redisInfo() {
		console.log("## redisInfo");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function initRedis() {
		console.log("## initRedis");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis/init",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});
	}
	
	function redisEntries() {
		console.log("## redisEntries");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis/entries",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function redisSample() {
		console.log("## redisSample");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis/sample",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function redisIncr1() {
		console.log("## redisInc1r");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis/incr1",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function redisIncr2() {
		console.log("## redisIncr2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/redis/incr2",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/test1",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function test2() {
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/test2",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}
	
	function incrCommentCountByBoardId() {
		console.log("## incrCommentCountByBoardId");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/incr/comment_count/board/1058121",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});	
	}
	
	function decrCommentCountByBoardId() {
		console.log("## decrCommentCountByBoardId");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/decr/comment_count/board/1058121",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});	
	}
	
	function flushCommentCount() {
		console.log("## flushCommentCount");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/flush/comment_count",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});	
	}

	function flushViewCount() {
		console.log("## flushViewCount");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/redis-concurrency/flush/view_count",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});	
	}
	
</script>
</body>
</html>