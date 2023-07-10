<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
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
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container login-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
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
							<div class="error">${errorResponse.errorMessage}</div>
							<div class="error d-none">${errorResponse.responseDateTime}</div>
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
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>

</html>