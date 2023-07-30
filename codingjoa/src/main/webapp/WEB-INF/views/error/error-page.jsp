<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>ERROR PAGE</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="error-container">
	<p class="font-weight-bold text-center bg-danger text-white mb-5" style="font-size: 80px;">ERROR PAGE</p>
	<div class="text-center">
		<c:choose>
			<c:when test="${fn:length(errorResponse.details > 0)}">
				<c:forEach items="${errorResponse.details}" var="item">
					<h2 class="font-weight-bold"><c:out value="${item.message}"/></h2>
				</c:forEach>
			</c:when>
			<c:when test="${fn:length(errorResponse.details == 0)}">
				<h2 class="font-weight-bold"><c:out value="${errorResponse.message}"/></h2>
			</c:when>
			<c:otherwise>
				<h2 class="font-weight-bold">오류가 발생하였습니다.</h2>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>