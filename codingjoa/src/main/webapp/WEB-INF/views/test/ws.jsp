<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
		justify-content: space-between;
		/* column-gap: 35px; */
	}
	
	div.test button {
		width: 183px;
	}
	
	input::-webkit-input-placeholder {
    	font-size: 1.25rem !important;
	}
	
	div.chat-container {
		display: flex;
		flex-direction: column;
	}
	
	div.chat {
		display: flex;
		flex-direction: column;
	}
	
	.other-chat {
		background-color: #fff;
   	 	border: 1px solid rgba(0, 0, 0, .125);
   	 	border-radius: 5px;
	}
	
	.my-chat {
		background-color: #fff3cd;
   	 	border-color: #ffeeba;
   	 	border-radius: 5px;
   	 	margin-left: auto
	}
}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>ws.jsp</p>
	<form id="alarmForm">
	<div class="test mb-3 px-5">
		<input class="form-control w-25" type="time" name="time">
		<input class="form-control w-25" type="text" name="content" placeholder="alarm message">
		<button type="submit" class="btn btn-primary btn-lg">schedule alarm</button>
		<button type="reset" class="btn btn-secondary btn-lg">reset</button>
	</div>
	</form>
	<div class="test mb-5 px-5">
		<button type="button" class="btn btn-primary btn-lg">stomp test</button>
	</div>
	<form id="chatForm">
	<div class="test mt-5 mb-3 px-5">
		<div class="input w-75">
			<sec:authentication property="principal" var="principal"/>
			<sec:authorize access="isAnonymous()">
				<input class="form-control" type="hidden" name="senderNickname" value="">
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<input class="form-control" type="hidden" name="senderNickname" value="${principal.nickname}">
			</sec:authorize>
			<input class="form-control" type="hidden" name="type" value="chat">
			<input class="form-control" type="text" name="content" placeholder="message">
		</div>
		<button type="submit" class="btn btn-primary btn-lg">send message</button>
	</div>
	</form>
	<div class="chat-container px-5">
		<!-- chat -->
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		const host = window.location.host;
		const socketUrl = "ws://" + host + "${contextPath}/ws/test";
		const socket = new WebSocket(socketUrl);
		console.log("## socketUrl = %s", socketUrl);
		
		$("#chatForm").on("submit", function(e) {
			e.preventDefault();
			let message = $(this).serializeObject();
			//console.log(message);
			
			let chatHtml = createChatHtml(message);
			let $chatHtml = $(chatHtml);
			$chatHtml.addClass("my-chat");
			$(".chat-container").append($chatHtml);
			
			socket.send(JSON.stringify(message));
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
			console.log("## websocket response");
			let data = JSON.parse(result.data);
			console.log(JSON.stringify(data, null, 2));
			
			let type = data.type;
			if (type == "push") {
				alert(data.content);
			} else if (type == "chat") {
				let chatHtml = createChatHtml(data);
				let $chatHtml = $(chatHtml);
				$chatHtml.addClass("other-chat");
				$(".chat-container").append($chatHtml);
			} else { // enter, exit
				$(".chat-container").append(createChatNotificationHtml(data));
			}
		};

		socket.onerror = function(error) {
			console.log("## websocket error");
			console.log(error);
		};
		
		function scheduleAlarm() {
			console.log("## scheduleAlarm");
			let alarm = $("#alarmForm").serializeObject();
			console.log(alarm);
			
			if (isEmpty(alarm.time)) {
				alert("알람시각을 정해주세요.");
				return;
			}

			if (isEmpty(alarm.content)) {
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
	
	function isEmpty(obj) {
		return (obj == null || obj == "");
	}
	
	function createChatNotificationHtml(data) {
		let html = '<div class="alert alert-secondary">';
		html += '<span class="font-weight-bold">' + (isEmpty(data.senderNickname) ? "익명" : data.senderNickname) + "</span>";
		if (data.type == "enter") {
			html += ' 님이 입장하였습니다.';
		} else if (data.type == "exit"){
			html += ' 님이 퇴장하였습니다.';
		}
		html += '</div>';
		return html;
	}
	
	function createChatHtml(data) {
		let html = '<div class="alert w-25 chat">';
		html += '<span class="font-weight-bold mb-2">' + (isEmpty(data.senderNickname) ? "익명" : data.senderNickname) + '</span>';
		html += '<span>' + (data.content.trim() == "" ? '&nbsp' : data.content)+ '</span>';
		html += '</div>';
		return html;
	}
</script>
</body>
</html>