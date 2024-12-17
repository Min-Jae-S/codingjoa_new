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
		justify-content:space-between
		/* column-gap: 35px; */
	}
	
	div.test button, button.test-btn {
		width: 183px;
	}
	
	input::-webkit-input-placeholder {
    	font-size: 1.25rem !important;
	}
	
	div.chat-container {
		display: flex;
		flex-direction: column;
		height: 350px;
		overflow-y: auto;
	}
	
	div.chat {
		display: flex;
		flex-direction: column;
	}
	
	div.other-chat {
		background-color: #fff;
   	 	border: 1px solid rgba(0, 0, 0, .125);
   	 	border-radius: 5px;
   	 	max-width: 45%;
   	 	margin-bottom: 1.25rem;
	}
	
	div.my-chat {
		background-color: #fff3cd;
   	 	border-color: #ffeeba;
   	 	border-radius: 5px;
   	 	margin-left: auto;
   	 	max-width: 45%;
   	 	margin-bottom: 1.25rem;
	}
	
	.w-70 {
		width: 70%;
	}
}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>ws.jsp</p>
	<form id="alarmForm">
	<div class="test mb-5 px-5">
		<input class="form-control w-25" type="time" name="time">
		<input class="form-control w-25" type="text" name="content" placeholder="alarm message">
		<button type="submit" class="btn btn-primary btn-lg" id="scheduleAlarmBtn" disabled>schedule alarm</button>
		<button type="reset" class="btn btn-secondary btn-lg">reset</button>
	</div>
	</form>
	<div class="mb-4 px-5">
		<button type="button" class="btn btn-warning btn-lg test-btn mr-4" id="enterChatBtn">enter</button>
		<button type="button" class="btn btn-secondary btn-lg test-btn mr-4" id="exitChatBtn">exit</button>
		<button type="button" class="btn btn-primary btn-lg test-btn" id="socketInfoBtn">socket info</button>
	</div>
	<div class="card chat-room mx-5 d-none">
		<div class="card-body chat-container p-5">
			<!-- chat -->
		</div>
		<div class="card-footer py-4 px-5">
			<form id="chatForm">
				<div class="test">
					<div class="input w-70">
						<input class="form-control" type="hidden" name="type" value="talk">
						<input class="form-control" type="text" name="content" placeholder="message">
					</div>
					<button type="submit" class="btn btn-primary btn-lg" id="sendMessageBtn" disabled>send</button>
				</div>
			</form>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	const host = window.location.host;
	const url = "ws://" + host + "${contextPath}/ws";
	let socket = null;
	
	$(function() {
		$("#chatForm").on("submit", function(e) {
			e.preventDefault();
			let message = $(this).serializeObject();
			$(".chat-container").append(createMyChatHtml(message));
			//console.log(message);
			
			let json = JSON.stringify(message);
			socket.send(json);
			
			$(this).trigger("reset");
			$(this).find("input[name='content']").focus();
		});

		$("#chatForm input[name='content']").on("input", function() {
			let inputValue = $(this).val().trim();
			$("#sendMessageBtn").prop("disabled", isEmpty(inputValue));
		});
		
		$("#chatForm").on("reset", function() {
			$("#sendMessageBtn").prop("disabled", true);
		});
		
		$("#alarmForm").on("submit", function(e) {
			e.preventDefault();
			scheduleAlarm();
		});
		
		$("#alarmForm input").on("input", function() {
			let anyEmpty = $("#alarmForm input").toArray().some(input => {
				let inputValue = $(input).val().trim();
				return isEmpty(inputValue);
			});
			
			$("#scheduleAlarmBtn").prop("disabled", anyEmpty);
		});
		
		$("#alarmForm").on("reset", function() {
			$("#scheduleAlarmBtn").prop("disabled", true);
		});
		
		$("#enterChatBtn").on("click", function() {
			connect();
		});

		$("#exitChatBtn").on("click", function() {
			disconnect();
		});
		
		$("#socketInfoBtn").on("click", function() {
			let status, state;
			
			if (socket) {
				// 0(connecting), 1(open), 2(closing), 3(closed)
				state = socket.readyState;
				if (state === WebSocket.CONNETING) { 
					status = "connecting";
				} else if (state === WebSocket.OPEN) {
					status = "open";
				} else if (state === WebSocket.CLOING) {
					status = "closing";
				} else if (state === WebSocket.CLOSED) {
					status = "closed";
				} else {
					status = "unknown";
				}
			} else {
				status = "no socket";
				state = "N/A";
			}
			
			console.log("## socket status = %s, %s", status, state); 
		});
		
	});
	
	function connect() {
		if (socket && socket.readyState === WebSocket.OPEN) {
			return;
		}
		
		socket = new WebSocket(url);
		
		socket.onopen = function(e) {
			console.log("## websocket is connected");
			console.log(e);
			$("div.chat-room").removeClass("d-none");
		};
		
		socket.onclose = function(e) {
			console.log("## websocket connection is closed");
			console.log(e);
			$("div.chat-room").addClass("d-none");
			$("div.chat-container").empty();
		};
		
		socket.onmessage = function(result) {
			console.log("## websocket received data");
			let chatMessage = JSON.parse(result.data);
			console.log(JSON.stringify(chatMessage, null, 2));
			
			if (chatMessage.type == "PUSH") {
				alert(chatMessage.content);
			} else if (chatMessage.type == "TALK") {
				$(".chat-container").append(createOtherChatHtml(chatMessage));
			} else {
				$(".chat-container").append(createChatNotificationHtml(chatMessage)); // ENTER, EXIT
			}
		};

		socket.onerror = function(error) {
			console.log("## websocket error");
			console.log(error);
		};
	}
	
	function disconnect() {
		if (socket && socket.readyState === WebSocket.OPEN) {
			socket.close();
		}
	}
	
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
	
	function isEmpty(obj) {
		return (obj == null || obj == "");
	}
	
	function createChatNotificationHtml(chatMessage) {
		let html = '<div class="alert alert-secondary text-center">';
		let senderNickname = isEmpty(chatMessage.senderNickname) ? "익명" : chatMessage.senderNickname;
		html += '<span class="font-weight-bold">' + senderNickname + "</span>";
		if (chatMessage.type == "ENTER") {
			html += ' 님이 입장하였습니다.';
		} else if (chatMessage.type == "EXIT") {
			html += ' 님이 퇴장하였습니다.';
		}
		html += '</div>';
		return html;
	}
	
	function createMyChatHtml(chatMessage) {
		let html = '<div class="alert chat my-chat">';
		html += '<span>' + chatMessage.content + '</span>';
		html += '</div>';
		return html;
	}
	
	function createOtherChatHtml(chatMessage) {
		let html = '<div class="alert chat other-chat">';
		let senderNickname = isEmpty(chatMessage.senderNickname) ? "익명" : chatMessage.senderNickname;
		html += '<span class="font-weight-bold mb-2">' + senderNickname + '</span>';
		html += '<span>' + chatMessage.content + '</span>';
		html += '</div>';
		return html;
	}
</script>
</body>
</html>