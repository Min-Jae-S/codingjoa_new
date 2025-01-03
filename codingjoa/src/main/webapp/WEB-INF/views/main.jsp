<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	#configHeader a {
	    color: #9e9e9e;
	}

	#configHeader a.active {
	    color: #495057;
	}
	
	#configBody {
		padding: 2.5rem;
	}
	
	#configBody p {
		font-size: 0.9rem;
		margin-bottom: 0.8rem;
	}
	
	#configBody p:last-child {
		margin-bottom: 0;
	}
	
	.home-wrap {
		width: 920px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container main-container">
	<div class="home-wrap">
		<div class="card">
			<div class="card-header" id="configHeader">
				<ul class="nav nav-tabs card-header-tabs">
					<li class="nav-item">
						<a class="nav-link active" href="${contextPath}/config/filters">Filter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/handler-mappings">HandlerMapping &amp; Handler</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/interceptors">Interceptor</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/handler-adapters">HandlerAdapter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/argument-resolvers">ArgumentResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/validators">Validator</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/return-value-handlers">ReturnValueHandler</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/view-resolvers">ViewResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/message-converters">HttpMessageConverter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/exception-resolvers">ExceptionResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/object-mappers">ObjectMapper</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/config/beans">Beans</a>
					</li>
				</ul>
			</div>
			<div class="card-body" id="configBody">
				<!-- cofing info -->
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#configHeader a").on("click", function (e) {
			e.preventDefault();
			$("#configBody").empty();
			$("#configHeader a").removeClass("active");
			$(this).addClass("active");
			
			$.ajax({
				type : "GET",
				url : $(this).attr("href"),
				dataType : "json",
				success : function(result) {
					console.log(JSON.stringify(result, null, 2));
					let configHtml = createConfigHtml(result.data);
					$("#configBody").html(configHtml);
				},
				error : function(jqXHR) {
					console.log(JSON.stringify(jqXHR, null, 2));
					let errorResponse = JSON.parse(jqXHR.responseText);
					alert(errorResponse.errorMessage);
				}
			});
		});
	});
	
	function createConfigHtml(data) {
		let html = "";
		if (data instanceof Array) { // Array.isArray()
			$.each(data, function(index, item) {
				if (typeof item == "string") {
					html += "<p class='card-text'>";
					html += "<i class='fa-solid fa-asterisk mr-2'></i>" + item + "</p>";
				} else {
					$.each(item, function(key, value) {
						html += "<p class='card-text'>";
						html += "<i class='fa-solid fa-asterisk mr-2'></i>" +  key + "</p>";
						$.each(value, function(index, item) {
							html += "<p class='card-text'>";
							html += "<i class='fa-solid fa-caret-right ml-4 mr-2'></i>" + item + "</p>";
						});
					});
				}
			});	
		} else {
			$.each(data, function(key, value) {
				html += "<p class='card-text'>";
				html += "<i class='fa-solid fa-asterisk mr-2'></i>" +  key + "</p>";
				html += "<p class='card-text'>";
				html += "<i class='fa-solid fa-caret-right ml-4 mr-2'></i>" + value + "</p>";
			});
		} 
		
		return html;
	}
</script>

</body>
</html>