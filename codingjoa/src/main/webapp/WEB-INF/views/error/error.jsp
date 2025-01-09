<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>ERROR PAGE | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<style>
	.error-wrap {
		margin: 0 auto;
		padding-bottom: 4rem;
	}
	
	.error-code {
		color: #dc3545 !important;
		font-weight: bold;
		font-size: 9.5rem;
	}

	.error-field::before {
		content: "( "
	}

	.error-field::after {
		content: " )"
	}
</style>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container error-container">
	<div class="error-wrap">
		<div class="text-center">
			<h3 class="error-code">
				<span><c:out value="${errorResponse.status}"/></span>
			</h3>
		</div>
		<div class="text-center px-5">
			<c:choose>
				<c:when test="${fn:length(errorResponse.details) > 0}">
					<c:forEach items="${errorResponse.details}" var="errorDetails">
						<h3 class="font-weight-bold">
							<span class="error-field"><c:out value="${errorDetails.field}"/></span>
							<span><c:out value="${errorDetails.message}"/></span>
						</h3>
					</c:forEach>
				</c:when>
				<c:when test="${fn:length(errorResponse.details) == 0}">
					<h3 class="font-weight-bold">
						<span><c:out value="${errorResponse.message}"/></span>
					</h3>
				</c:when>
				<c:otherwise>
					<h3 class="font-weight-bold">
						<span>오류가 발생하였습니다.</span>
					</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>