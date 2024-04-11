<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa : 로그인</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
	.info-member {
		overflow: hidden;
		font-size: 85%;
    	font-weight: 400;
	}
	
	.info-member a {
		text-decoration-line: none;
		/*color: #495057;*/
		color: grey;
	}

	.link-join {
		float: left;
	}
	
	.link-ul {
		float: right;
		list-style: none;
    	padding: 0;
    	margin: 0;
	}
	
	.link-ul li {
		display: inline-block;
	}

	.link-li+.link-li::before {
		content: "|";
		margin-right: 10px;
		margin-left: 10px;
		/*color: #adb5bd;*/
		color: grey;
	}
	
	.login-wrap {
		width: 500px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container login-container">
	<div class="login-wrap">
		<div class="card shadow">
			<div class="card-body p-5">
				<form:form action="${contextPath}/member/loginProc" method="POST" modelAttribute="loginDto">
					<div class="form-group mb-4">
						<form:label path="memberId" class="font-weight-bold">아이디</form:label>
						<form:input path="memberId" class="form-control" placeholder="아이디 입력"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="memberPassword" class="font-weight-bold">비밀번호</form:label>
						<form:password path="memberPassword" class="form-control" placeholder="비밀번호 입력" showPassword="true" autocomplete="off"/>
					</div>
					<c:if test="${not empty errorResponse}">
						<div class="error">
							<!-- 아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.<br>입력한 내용을 다시 확인해주세요. -->
							<c:out value="${errorResponse.message}" escapeXml="false"/>
						</div>
						<div class="error d-none">
							<fmt:parseDate value="${errorResponse.timestamp}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both"/>
							<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>
						</div>
					</c:if>
					<div class="form-group pt-4 mb-4">
						<form:button class="btn btn-primary btn-block">로그인</form:button>
					</div>
				</form:form>
				<div class="info-member">
					<a class="link-join" href="${contextPath}/member/join">회원가입</a>
					<ul class="link-ul">
						<li class="link-li">
							<a href="${contextPath}/member/findAccount">계정 찾기</a>
						</li>
						<li class="link-li">
							<a href="${contextPath}/member/findPassword">비밀번호 찾기</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		// just for inspecting the error response
		<c:if test="${not empty errorResponse}">
			<fmt:parseDate value="${errorResponse.timestamp}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both"/>
			<fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="timestamp"/>
			let errorResponse = {
				status : <c:out value='${errorResponse.status}'/>,
				message : "<c:out value='${errorResponse.message}' escapeXml='false'/>",
				details : <c:out value='${errorResponse.details}'/>,
				timestamp : "<c:out value='${timestamp}'/>"
			};
			console.log(JSON.stringify(errorResponse, null, 2));
		</c:if>
	})
</script>
</body>

</html>