<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>stomp.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
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
	
	div.chat-alert {
		border-radius: 10px !important;
		margin-bottom: 1.5rem !important;
	}
	
	div.chat-others {
		background-color: #fff;
   	 	border: 1px solid rgba(0, 0, 0, .125);
   	 	border-radius: 10px;
   	 	max-width: 45%;
   	 	margin-bottom: 1.5rem;
	}
	
	div.chat-mine {
		background-color: #fff3cd;
   	 	border-color: #ffeeba;
   	 	border-radius: 10px;
   	 	margin-left: auto;
   	 	max-width: 45%;
   	 	margin-bottom: 1. 5rem;
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
		<button type="button" class="btn btn-primary btn-lg test-btn mr-4" id="broadcastBtn">broadcast</button>
		<input class="form-control w-70" type="text" name="broadcast">
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
	let broadcastClient = null;
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

		$("#broadcastBtn").on("click", function() {
			let broadcast = $("input[name='broadcast']").val().trim();
			if (isEmpty(broadcast)) {
				alert("내용을 입력하세요.");
				return;
			}
			
			if (broadcastClient && broadcastClient.connected) {
				broadcastClient.send("/pub/broadcast", { }, broadcast); // { "content-type" : "application/json;charset=utf-8" }
				return;
			}
			
			let	socket = new WebSocket(url);
			broadcastClient = Stomp.over(socket);
			broadcastClient.debug = null;
			
			broadcastClient.connect({ }, function(frame) {
				console.log("## broadcastClient connection callback");
				//console.log(frame);
				
				console.log("## broadcastClient subscribe");
				let subscription = broadcastClient.subscribe("/sub/broadcast", function(frame) { 
					console.log("## broadcastClient received message");
					//console.log(frame);

					try {
						let message = JSON.parse(frame.body); 
						console.log(JSON.stringify(message, null, 2));
						
						let sender = message.sessionMatched ? message.sender + '(나)' : message.sender;
						alert("[broadcast] " + sender + ": " + message.content);
					} catch(e) {
						alert(frame.body);
					}
				});
				//console.log(subscription);
				
				broadcastClient.send("/pub/broadcast", { }, broadcast); //  { "content-type" : "application/json;charset=utf-8" }
			});
		});
		
		$("input[name='broadcast']").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#broadcastBtn").trigger("click");
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
				sendMessage(message);
			} else {
				console.log("## stompClient disconnected, queuing the message and attempting to connect");
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
		if (stompClient && stompClient.connected) {
			console.log("## stompClient already connected");
			return;
		}
		
		let socket = new WebSocket(url);
		stompClient = Stomp.over(socket);
		stompClient.debug = null;
		
		stompClient.connect({ }, function(frame) {
			console.log("## stompClient connection callback");
			//console.log(frame);
			
			console.log("## stompClient subscribe");
			let subscription = stompClient.subscribe("/sub/room/" + roomId, function(frame) { // "/sub/room/5"
				console.log("## stompClient received message");
				//console.log(frame);
				
				let message = JSON.parse(frame.body); 
				console.log(JSON.stringify(message, null, 2));
				
				let chatHtml = createChatHtml(message);
				$(".chat-container").append(chatHtml);
			});
			console.log("\t > subcription id = %s", subscription.id);
			
			console.log("## send enter message");
			stompClient.send("/pub/enter/room/" + roomId, { }, '');
			
			if (messageQueue.length == 0) {
				return;
			}
			
			console.log("## send queued message");
			while (messageQueue.length > 0) {
		 		let queuedMessage = messageQueue.shift();
		 		console.log(JSON.stringify(queuedMessage, null, 2));
		 		sendMessage(queuedMessage);
		 	}
		}, function(error) {
			console.log("## stompClient connection error callback");
			console.log("\t > %s", error);
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
		
		console.log("## send exit message");
		stompClient.send("/pub/exit/room/" + roomId, { }, '');
		
		console.log("## disconnect stompClient");
		stompClient.disconnect();
	}
	
	function info() {
		console.log("## stompClient info");
		if (stompClient) {
			console.log(stompClient);
			console.log("\t > stompClient.ws readyState = %s", getReadyState(stompClient.ws));
			console.log("\t > stompClient connected = %s", stompClient.connected);
		} else {
			console.log("\t > no stompClient");
		}
		
		console.log("## messageQueue info");
		if (messageQueue.length > 0) {
			messageQueue.foreach((message, index) => {
				console.log(message);
			});
		} else {
			console.log("\t > messageQueue empty");
		}
		
		console.log("## broadcastClient info");
		if (broadcastClient) {
			console.log(broadcastClient);
			console.log("\t > broadcastClient.ws readyState = %s", getReadyState(broadcastClient.ws));
			console.log("\t > broadcastClient connected = %s", broadcastClient.connected);
		} else {
			console.log("\t > no broadcastClient");
		}
	}
	
	function sendMessage(message) {
		console.log("## send chat message");
		let json = JSON.stringify(message);
		stompClient.send("/pub/chat/room/" + roomId, { }, json); // { "content-type" : "application/json;charset=utf-8" }
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
	
	function createMyChatHtml(message) {
		let html = '';
		html += '<div class="alert chat chat-mine">';
		html += '<span>' + message.content + '</span>';
		html += '</div>';
		return html;
	}
	
	function createChatHtml(message) {
		let html = '';
		if (message.type == "TALK") {
			if (!message.sessionMatched) {
				html += '<div class="alert chat chat-others">';
				html += '<span class="font-weight-bold mb-2">' + message.sender + '</span>';
				html += '<span>' + message.content + '</span>';
				html += '</div>';
			}
		} else { // ENTER, EXIT
			html += '<div class="alert alert-secondary text-center chat-alert">';
			let sender = message.sessionMatched ? message.sender + '(나)' : message.sender;
			html += '<span class="font-weight-bold mr-1">' + sender + '</span>' + message.content;
			html += '</div>';
		}
		
		return html;
	}

</script>
</body>
</html>