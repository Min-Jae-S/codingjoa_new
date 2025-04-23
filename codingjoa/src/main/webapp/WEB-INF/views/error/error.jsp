<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>ERROR | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<style>
	.error-wrap {
		width: 900px;
		margin: 0 auto;
		padding-bottom: 2rem;
	}
	
	.error-code {
		color: #dc3545 !important;
		font-weight: bold;
		font-size: 9.5rem;
		text-align: center;
		line-height: 1;
	}
	
	.error-message {
		font-weight: bold;
		font-size: 1.75rem; /* h3 */
		text-align: center;
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
		<p class="error-code"><c:out value="${errorResponse.status}"/></p>
		<c:choose>
			<c:when test="${fn:length(errorResponse.details) > 0}">
				<c:forEach items="${errorResponse.details}" var="errorDetails">
					<p class="error-message">
						<span class="error-field"><c:out value="${errorDetails.field}"/></span>
						<span><c:out value="${errorDetails.message}"/></span>
					</p>
				</c:forEach>
			</c:when>
			<c:when test="${fn:length(errorResponse.details) == 0}">
				<p class="error-message"><c:out value="${errorResponse.message}"/></p>
			</c:when>
			<c:otherwise>
				<p class="error-message">오류가 발생하였습니다.</p>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>