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
	
	div.test, div.input {
		display: flex;
		/* justify-content: space-between; */
		column-gap: 35px;
	}
	
	div.test button {
		width: 183px;
	}
	
	input::-webkit-input-placeholder {
    	font-size: 1rem !important;
	}
}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>ws.jsp</p>
	<form id="messageForm">
	<div class="test mt-5 mb-5 px-5">
		<div class="input w-50">
			<input class="form-control" type="text" name="content" placeholder="message">
			<input class="form-control" type="hidden" name="type" value="chat">
		</div>
		<button type="submit" class="btn btn-primary btn-lg">send message</button>
		<button type="reset" class="btn btn-secondary btn-lg">reset</button>
	</div>
	</form>
	<form id="alarmForm">
	<div class="test mb-5 px-5">
		<div class="input w-50">
			<input class="form-control w-50" type="time" name="time">
			<input class="form-control" type="text" name="content" placeholder="alarm message">
		</div>
		<button type="submit" class="btn btn-primary btn-lg">schedule alarm</button>
		<button type="reset" class="btn btn-secondary btn-lg">reset</button>
	</div>
	<div class="test mb-5 px-5">
		<button type="button" class="btn btn-primary btn-lg">stomp test</button>
	</div>
	</form>
	<div class="alert alert-primary mx-5 d-none">
		<!-- web-socket message -->
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	const host = window.location.host;
	const socketUrl = "ws://" + host + "${contextPath}/ws/test";
	const socket = new WebSocket(socketUrl);
	console.log("## socketUrl = %s", socketUrl);
	
	$(function() {
		$("#messageForm").on("submit", function(e) {
			e.preventDefault();
			sendMessage();
		});
		
		$("#alarmForm").on("submit", function(e) {
			e.preventDefault();
			scheduleAlarm();
		});
		
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
			let data = JSON.parse(result.data);
			console.log(JSON.stringify(data, null, 2));
			
			let alertMessage = data.content;
			if (alertMessage == null || alertMessage == "") {
				alertMessage = "no message";
			}
			
			$(".alert").html(alertMessage).removeClass("d-none");
		};

		socket.onerror = function(error) {
			console.log("## websocket error");
			console.log(error);
		};
		
		function sendMessage() {
			console.log("## sendMessage");
			let message = $("#messageForm").serializeObject();
			console.log(message);
			
			socket.send(JSON.stringify(message));
		}
		
		function scheduleAlarm() {
			console.log("## scheduleAlarm");
			let alarm = $("#alarmForm").serializeObject();
			console.log(alarm);
			
			if (alarm.time == null || alarm.time == "") {
				alert("알람시각을 정해주세요.");
				return;
			}

			if (alarm.content == null || alarm.content == "") {
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
	});
</script>
</body>
</html>