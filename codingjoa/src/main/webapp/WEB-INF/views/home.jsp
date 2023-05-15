<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa HOME</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<style>
	#configHeader a {
	    color: #495057;
	}

	#configBody {
		min-height: 350px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp" />

<div class="container home-container">
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<div class="card">
				<div class="card-header" id="configHeader">
					<ul class="nav nav-tabs card-header-tabs">
						<li class="nav-item">
							<a class="nav-link active" href="#">HOME</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${contextPaht}/config/filters">Filters</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${contextPaht}/config/converters">Coverters</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${contextPaht}/config/message-converters">Message Converters</a>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="${contextPaht}/config/exception-resolvers">Exception Resolvers</a>
						</li>
					</ul>
				</div>
				<div class="card-body p-5" id="configBody">
					<h5 class="card-title">Special title treatment</h5>
					<p class="card-text">With supporting text below as a natural lead-in to additional content.</p>
					<a href="#" class="btn btn-primary">Go somewhere</a>
				</div>
			</div>
		</div>
		<div class="col-sm-1"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#configHeader a").on("click", function (e) {
			e.preventDefault();
			$("#configHeader a").removeClass("active");
			$(this).addClass("active");
			
			$.ajax({
				type : "POST",
				url : url,
				dataType : "json",
				success : function(result) {
					console.log("success");
					//console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					consoel.log("error");
					//let errorResponse = JSON.parse(jqXHR.responseText);
					//console.log(JSON.stringify(errorResponse, null, 2));
				}
			});
		});
	});
</script>

</body>
</html>