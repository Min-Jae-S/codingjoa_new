<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script> -->
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script> -->
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="${contextPath}/resources/js/service/config.js"></script>
<style>
	.main-wrap {
		width: 860px;
		min-width: 860px;
		margin: 0 auto;
	}
	
	.card {
		width: 860px;
		min-width: 860px;
	}

	.card-header a {
	    color: #858994;
	    font-size: .95rem;
	    padding: .4rem .6rem;
	    border-top-left-radius: 0.5rem !important;
	    border-top-right-radius: 0.5rem !important;
	}
	
	.card-header a:hover:not(.active) {
		border-color: transparent;
	}
	
	.card-header a:hover {
		color: black;
	}
	
	.card-header a.active {
		color: black;
		font-weight: 600;
	}
	
	.card-body {
		padding: 2.5rem;
		height: 380px;
		overflow-y: auto;
	}
	
	.card-body p {
		font-size: 0.9rem;
		margin-bottom: 0.8rem;
	}
	
	.card-body p:last-child {
		margin-bottom: 0;
	}
	
	.card-header-tabs {
		margin: 0;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container main-container">
	<div class="main-wrap">
		<div class="card rounded-md">
			<div class="card-header">
				<ul class="nav nav-tabs card-header-tabs config-menu">
					<li class="nav-item">
						<a class="nav-link active" href="${contextPath}/api/config/filters">Filter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/handler-mappings">HandlerMapping &amp; Handler</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/interceptors">Interceptor</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/handler-adapters">HandlerAdapter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/argument-resolvers">ArgumentResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/validators">Validator</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/return-value-handlers">ReturnValueHandler</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/view-resolvers">ViewResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/message-converters">HttpMessageConverter</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/exception-resolvers">ExceptionResolver</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/object-mappers">ObjectMapper</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="${contextPath}/api/config/beans">Beans</a>
					</li>
				</ul>
			</div>
			<div class="card-body">
				<!------------------------>
				<!----     config     ---->
				<!------------------------>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		initMainPage();
		
		$(".config-menu a").on("click", function(e) {
			e.preventDefault();
			$(".card-body").empty();
			$(".config-menu a").removeClass("active");
			$(this).addClass("active");
			
			let url = $(this).attr("href");
			configService.getConfig(url, function(result) {
				let configHtml = createConfigHtml(result.data);
				$(".card-body").html(configHtml);
			});
		});
	});
	
	function initMainPage() {
		console.log("## initiate main page");
		let url = $(".config-menu a.active").attr("href");
		configService.getConfig(url, function(result) {
			let configHtml = createConfigHtml(result.data);
			$(".card-body").html(configHtml);
		});
	}
</script>

</body>
</html>