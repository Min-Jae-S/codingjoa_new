<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>ws.jsp</title>
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
		display: flex;
		/* justify-content: space-between; */
		column-gap: 35px;
	}
	
	div.test button {
		width: 183px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>ws.jsp</p>
	<div class="test mt-5 mb-4 px-5">
		<button class="btn btn-primary btn-lg" id="btn1">test1</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		const host = window.location.host;
		const socketUrl = "ws://" + host + "${contextPath}/test/ws/socket";
		console.log("## socketUrl = %s", socketUrl);

		const socket = new WebSocket(socketUrl);
		
		socket.onopen = function(e) {
			console.log("## websocket connected");
			console.log(e);
		};
		
		socket.onclose = function(e) {
			console.log("## websocket closed");
			console.log(e);
		};
		
		socket.onmessage = function(result) {
			console.log("## websocket received response");
			consoel.log(result);
		};

		socket.onerror = function(error) {
			console.log("## websocket error");
			console.log(error);
		};
		
		$("#btn1").on("click", function() {
			//console.log("socket readyState = %s (CONNECTING:0, OPEN:1, CLOSING:2, CLOSED:3)", socket.readyState);
			socket.send("현재 시각에 대해서 알려줘");
		});
	});

</script>
</body>
</html>