<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>stomp.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script src="${contextPath}/resources/js/plugin/ws/stomp.js"></script>
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
	<p>stomp.jsp</p>
	<div class="my-5 px-5">
		<button type="button" class="btn btn-warning btn-lg test-btn mr-4" id="connectBtn">connect (new)</button>
		<button type="button" class="btn btn-secondary btn-lg test-btn mr-4" id="disconnectBtn">disconnect</button>
		<button type="button" class="btn btn-info btn-lg test-btn mr-4" id="infoBtn">info</button>
	</div>
	<div class="mb-5 px-5 d-flex">
		<button type="button" class="btn btn-primary btn-lg test-btn mr-4" id="newsBtn">news</button>
		<input class="form-control w-70" type="text" name="news" placeholder="news">
	</div>
	<div class="mb-5 px-5">
		<button type="button" class="btn btn-warning btn-lg test-btn mr-4" id="enterBtn">enter</button>
		<button type="button" class="btn btn-secondary btn-lg test-btn mr-4" id="exitBtn">exit</button>
	</div>
	<div class="card chat-room mx-5 mb-5 d-none">
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
	const url = "ws://" + host + "${contextPath}/ws-stomp";
	const roomId = 5;
	let stompClient = null;
	let newsClient = null;
	let headers = { };
	let messageQueue = [];

	$(function() {
		$("#connectBtn").on("click", function() {
			connect();

		});

		$("#disconnectBtn").on("click", function() {
			disconnect();
		});

		$("#infoBtn").on("click", function() {
			info();
		});

		$("#newsBtn").on("click", function() {
			let news = $("input[name='news']").val().trim();
			if (isEmpty(news)) {
				alert("뉴스를 입력하세요.");
				return;
			}
			
			if (newsClient && newsClient.connected) {
				newsClient.send("/pub/news", headers, news); // { "content-type" : "text/plain;charset=utf-8" }
				return;
			}
			
			let	socket = new WebSocket(url);
			newsClient = Stomp.over(socket);
			newsClient.debug = false;
			
			console.log("\t > ws readyState = %s", getReadyState(socket));
			console.log("\t > stompClient connected = %s", stompClient.connected);
			
			newsClient.connect(headers, function(frame) {
				console.log("## newsClient connection callback");
				console.log(frame);
				
				let subscriptionUrl = "/sub/news";
				console.log("## newsClient subscribe: %s", subscriptionUrl);
				
				let subscription = newsClient.subscribe(subscriptionUrl, function(frame) { 
					console.log("## newsClient received message = %s", frame.body);
				});
				console.log(subscription);
				
				newsClient.send("/pub/news", headers, news); //  { "content-type" : "text/plain;charset=utf-8" }
			});
		});
		
		$("input[name='news']").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#newsBtn").trigger("click");
			}
		});

		$("#enterBtn").on("click", function() {
			connect();
			$("div.chat-room").removeClass("d-none");
		});

		$("#exitBtn").on("click", function() {
			disconnect();
			$("div.chat-room").addClass("d-none");
			$("div.chat-container").empty();
		});
		
		$("#chatForm").on("submit", function(e) {
			e.preventDefault();
			let message = $(this).serializeObject();
			$(".chat-container").append(createMyChatHtml(message));
			
			if (stompClient && stompClient.connected) {
				console.log("## stompClient is connected, so send message");
				sendMessage(message);
			} else {
				console.log("## no stompClient or stompClient is not connected");
				messageQueue.push(message);
				connect();
			}
			
			$(this).trigger("reset");
			$(this).find("input[name='content']").focus();
		});
		
		$("#chatForm input[name='content']").on("input", function() {
			let inputValue = $(this).val().trim();
			$("#chatForm").find("button[type='submit']").prop("disabled", isEmpty(inputValue));
		});
		
		$("#chatForm").on("reset", function() {
			$("#chatForm").find("button[type='submit']").prop("disabled", true);
		});
	});
	
	function connect() {
		console.log("## connect");
		if (stompClient && stompClient.connected) {
			console.log("\t > stompClient already connected");
			return;
		}
		
		let socket = new WebSocket(url);
		stompClient = Stomp.over(socket);
		stompClient.debug = false;
		
		console.log("\t > ws readyState = %s", getReadyState(socket));
		console.log("\t > stompClient connected = %s", stompClient.connected);
		
		stompClient.connect(headers, function(frame) {
			console.log("## stompClient connection callback");
			console.log(frame);
			
			let subscriptionUrl = "/sub/room/" + roomId;
			console.log("## stompClient subscribe: %s", subscriptionUrl);
			
			let subscription = stompClient.subscribe(subscriptionUrl, function(frame) { // "/sub/room/5"
				console.log("## stompClient recieved message");
				console.log(frame);
				
				let chatMessage = JSON.parse(frame.body); 
				console.log(JSON.stringify(chatMessage, null, 2));
				
				if (chatMessage.type == "PUSH") {
					alert(chatMessage.content);
				} else if (chatMessage.type == "TALK") {
					$(".chat-container").append(createOtherChatHtml(chatMessage));
				} else {
					$(".chat-container").append(createChatNotificationHtml(chatMessage)); // ENTER, EXIT
				}
			});
			console.log(subscription);
			
			while (messageQueue.length > 0) {
		 		let queuedMessage = messageQueue.shift();
		 		sendMessage(queuedMessage);
		 	}
		}, function(error) {
			console.log("## stompClient connection error callback");
		});
		
	}
	
	function disconnect() {
		if (!stompClient) {
			console.log("## no stompClient");
			return;
		}
		
		if (!stompClient.connected) {
			console.log("## stompClient not connected");
			return;
		}
		
		stompClient.disconnect(() => {
			console.log("## stompClient disconnected");
			if (stompClient) {
				console.log("\t > stompClient connected = %s", stompClient.connected);
			} else {
				console.log("\t > no stompClient");
			}
		});
	}
	
	function info() {
		console.log("## stompClient info");
		if (stompClient) {
			console.log("\t > ws readyState = %s", getReadyState(stompClient.ws));
			console.log("\t > stompClient connected = %s", stompClient.connected);
		} else {
			console.log("\t > no stompClient");
		}
		
		console.log("## newsClient info");
		if (newsClient) {
			console.log("\t > newsClient connected = %s", newsClient.connected);
		} else {
			console.log("\t > no newsClient");
		}
	}
	
	function sendMessage(message) {
		console.log("## send message");
		let json = JSON.stringify(message);
		stompClient.send("/pub/room/" + roomId, headers, json); // { "content-type" : "application/json;charset=utf-8" }
	}
	
	function isEmpty(obj) {
		return (obj == null || obj == "");
	}
	
	function getReadyState(socket) {
		if (socket == null) {
			return "NO SOCKET";
		}
		
		// 0(connecting), 1(open), 2(closing), 3(closed)
		if (socket.readyState === WebSocket.CONNECTING) { 
			return "CONNECTING";
		} else if (socket.readyState === WebSocket.OPEN) {
			return "OPEN";
		} else if (socket.readyState === WebSocket.CLOSING) {
			return "CLOSING";
		} else if (socket.readyState === WebSocket.CLOSED) {
			return "CLOSED";
		} else {
			return "UNKNOWN";
		}
	}
	
	function createChatNotificationHtml(chatMessage) {
		let html = '<div class="alert alert-secondary text-center">';
		let senderNickname = isEmpty(chatMessage.senderNickname) ? "익명" : chatMessage.senderNickname;
		html += '<span class="font-weight-bold">' + senderNickname + "</span>";
		if (chatMessage.type == "ENTER") {
			html += ' 님이 입장하였습니다.';
		} else if (data.type == "EXIT") {
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