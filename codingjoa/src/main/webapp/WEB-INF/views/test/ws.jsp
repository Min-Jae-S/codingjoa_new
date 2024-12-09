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
	<div class="test mt-5 mb-5 px-5">
		<input class="form-control" type="text" name="from" placeholder="from">
		<input class="form-control" type="text" name="content" placeholder="content">
		<button class="btn btn-primary btn-lg" id="btn1">test1</button>
	</div>
	<form id="alarmForm">
	<div class="test mb-3 px-5">
		<input class="form-control w-25" type="time" name="alarmTime">
		<input class="form-control" type="text" name="alarmMessage" placeholder="alarm message">
		<button type="submit" class="btn btn-primary btn-lg">schedule alarm</button>
		<button type="reset" class="btn btn-secondary btn-lg">reset</button>
	</div>
	</form>
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
			//console.log(result.data);
			let obj = JSON.parse(result.data);
			console.log(JSON.stringify(obj, null, 2));
		};

		socket.onerror = function(error) {
			console.log("## websocket error");
			console.log(error);
		};
		
		$("#btn1").on("click", function() {
			console.log("## btn1 click");
			//console.log("socket readyState = %s (CONNECTING:0, OPEN:1, CLOSING:2, CLOSED:3)", socket.readyState);
			
			let obj = {
				"from" : $("input[name='from']").val(),
				"content" : $("input[name='content']").val()
			};
			
			socket.send(JSON.stringify(obj));
		});
		
		$("#alarmForm").on("submit", function(e) {
			e.preventDefault();
			scheduleAlarm();
		});
	
	});
	
	function scheduleAlarm() {
		console.log("## scheduleAlarm");
		let alarm = $("#alarmForm").serializeObject();
		console.log(alarm);
		
		if (alarm.alarmTime == null || alarm.alarmTime == "") {
			alert("알람시각을 정해주세요.");
			return;
		}

		if (alarm.alarmMessage == null || alarm.alarmMessage == "") {
			alert("알람메시지를 입력해주세요.");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "${contextPath}/test/quartz2/alarm",
			data : JSON.stringify(alarm),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
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