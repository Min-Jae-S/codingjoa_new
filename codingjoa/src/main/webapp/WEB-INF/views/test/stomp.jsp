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
				newsClient.send("/pub/news", { }, news); // { "content-type" : "text/plain;charset=utf-8" }
				return;
			}
			
			let	socket = new WebSocket(url);
			newsClient = Stomp.over(socket);
			newsClient.debug = null;
			
			newsClient.connect({ }, function(frame) {
				console.log("## newsClient connection callback");
				//console.log(frame);
				
				console.log("## newsClient subscribe");
				let subscription = newsClient.subscribe("/sub/news", function(frame) { 
					console.log("## newsClient received message = %s", frame.body);
				});
				//console.log(subscription);
				
				newsClient.send("/pub/news", { }, news); //  { "content-type" : "text/plain;charset=utf-8" }
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
			console.log(message);

			$(".chat-container").append(createMyChatHtml(message));
			
			if (stompClient && stompClient.connected) {
				console.log("## stompClient already connected, sending the message immediately");
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
		console.log("## connect");
		if (stompClient && stompClient.connected) {
			console.log("\t > stompClient already connected");
			return;
		}
		
		let socket = new WebSocket(url);
		stompClient = Stomp.over(socket);
		stompClient.debug = null;
		
		stompClient.connect({ }, function(frame) {
			console.log("## stompClient connection callback");
			console.log(frame);
			
			console.log("## send enter message");
			stompClient.send("/pub/enter/room/" + roomId, { }, '');
			
			console.log("## stompClient subscribe");
			let subscription = stompClient.subscribe("/sub/room/" + roomId, function(frame) { // "/sub/room/5"
				console.log("## stompClient received message");
				console.log(frame);
				
				let message = JSON.parse(frame.body); 
				console.log(JSON.stringify(message, null, 2));
				
				if (message.type == "PUSH") {
					alert(message.content);
				} else { // ENTER, EXIT, TALK
					let chatHtml = createChatHtml(message);
					$(".chat-container").append(chatHtml);
				}
			});
			//console.log(subscription);
			
			if (messageQueue.length == 0) {
				return;
			}
			
			console.log("## messageQueue");
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
		
		stompClient.disconnect(() => {
			console.log("## stompClient disconnection callback");
			if (stompClient) {
				if (stompClient.connected) {
					console.log("\t > stompClient connected");
				} else {
					console.log("\t > stompClient disconnected");
				}
			} else {
				console.log("\t > no stompClient");
			}
		});
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
		
		console.log("## newsClient info");
		if (newsClient) {
			console.log(newsClient);
			console.log("\t > newsClient.ws readyState = %s", getReadyState(newsClient.ws));
			console.log("\t > newsClient connected = %s", newsClient.connected);
		} else {
			console.log("\t > no newsClient");
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
		html += '<div class="alert chat my-chat">';
		html += '<span>' + message.content + '</span>';
		html += '</div>';
		return html;
	}
	
	function createChatHtml(message) {
		let html = '';
		if (message.sessionMatched) {
			return html;
		}
		
		if (message.type == "TALK") {
			html += '<div class="alert chat other-chat">';
			html += '<span class="font-weight-bold mb-2">' + message.sender + '</span>';
			html += '<span>' + message.content + '</span>';
			html += '</div>';
		} else {
			html += '<div class="alert alert-secondary text-center">';
			html += '<span class="font-weight-bold mr-1">' + message.sender + '</span>' + message.content;
			html += '</div>';
		}
		
		return html;
	}

</script>
</body>
</html>