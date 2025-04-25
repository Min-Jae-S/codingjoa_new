<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>로그인 | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<style>
	.login-wrap {
		min-width: 500px;
		margin: 0 auto;
	}
	
	.info-user {
		overflow: hidden;
		font-size: 90%;
    	font-weight: 400;
    	margin-bottom: 2.2rem;
	}
	
	.info-user a {
		text-decoration-line: none;
		color: #495057;
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
	
	.social-login {
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	
	.social-login-line {
		position: relative;
    	bottom: -8px;
    	margin: 0;
    	width: 100%;
    	border: 0;
    	border-top: 1px solid rgba(0, 0, 0, .125);
	}
	
	.social-login-title {
		font-size: 90%;
    	font-weight: 400;
    	/* color: #495057; */
    	color: #6c757d; /* .text-muted */
    	z-index: 1;
    	background-color: white;
    	line-height: 16px;
    	padding-left: 1rem;
    	padding-right: 1rem;
    	margin-bottom: 2.2rem;
	}
	
	.social-login-body {
		display: flex;
		justify-content: center;
		column-gap: 30px;
	}
	
	.social-login-btn {
		width: 52px;
		height: 52px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container login-container">
	<div class="login-wrap">
		<div class="card rounded-xl">
			<div class="card-body p-5">
				<form id="loginForm">
					<div class="email_pw_wrap">
						<div class="form-group mb-4">
							<label class="font-weight-bold" for="email" >이메일</label>
							<input class="form-control rounded-md" type="text" name="email" id="email" placeholder="이메일을 입력해주세요."/>
						</div>
						<div class="form-group mb-4">
							<label class="font-weight-bold" for="password">비밀번호</label>
							<input class="form-control rounded-md" type="password" name="password" id="password" placeholder="비밀번호를 입력해주세요." autocomplete="off"/>
						</div>
					</div>
					<div class="form-group pt-3 mb-3">
						<button type="submit" class="btn btn-primary btn-block rounded-md">로그인</button>
					</div>
				</form>
				<div class="info-user">
					<a class="link-join" href="${contextPath}/auth/join">회원가입</a>
					<ul class="link-ul">
						<li class="link-li">
							<a href="${contextPath}/auth/password/find">비밀번호 찾기</a>
						</li>
					</ul>
				</div>
				<div class="social-login">
					<hr class="social-login-line">
					<span class="social-login-title">SNS 계정 로그인</span>
					<div class="social-login-body">
						<a href="${contextPath}/auth/login/kakao?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/provider/kakao.png">
						</a>
						<a href="${contextPath}/auth/login/naver?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/provider/naver.png">
						</a>
						<a href="${contextPath}/auth/login/google?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/provider/google.png">
						</a>
						<a href="#">
							<img class="social-login-btn" src="${contextPath}/resources/images/provider/github.png">
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		const encodedContinueUrl = "${continueUrl}";
		console.log("## resolved continueUrl from Controller: '%s'", encodedContinueUrl);
		
		$("#loginForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				email : $("#email").val(),
				password : $("#password").val(),
			};
			
			authService.login(formData, function(result) {
				localStorage.setItem("ACCESS_TOKEN", result.data.accessToken);
				alert(result.message);
				location.href = decodeURIComponent(encodedContinueUrl);
			});
		});
	})
</script>
</body>

</html>