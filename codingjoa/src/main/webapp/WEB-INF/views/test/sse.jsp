<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>sse.jsp</title>
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
	
	div.test button {
		width: 183px;
	}
	
	button.test-btn {
		width: 183px;
	}
	
	input::-webkit-input-placeholder {
    	font-size: 1.25rem !important;
	}
}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>sse.jsp</p>
	<div class="test mb-5 px-5">
		<button type="button" class="btn btn-primary btn-lg test-btn mr-4" id="streamBtn">stream</button>
		<button type="button" class="btn btn-primary btn-lg test-btn mr-4">subscribe</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	const url = "${contextPath}/sse/stream";
	let eventSource;
	
	$(function() {
		$("#streamBtn").on("click", function() {
			eventSource = new EventSource(url);
			
			eventSource.onopen = (event) => {
				console.log("## event connection");
				console.log(event);
			}
			
			eventSource.onmessage = (event) => {
				console.log("## event recieved");
				console.log(event);
				//console.log(event.data);
			};
			
			eventSource.onerror = (error) => {
				console.log("## event error");
				console.log(error);
				eventSource.close();
			};
		});
	});
</script>
</body>
</html>