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
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/authentication.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	.info-member {
		overflow: hidden;
		font-size: 85%;
    	font-weight: 400;
    	margin-bottom: 3rem;
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
	
	.login-wrap .card {
		border-radius: 16px;
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
	}
	
	.social-login-title {
		font-size: 85%;
    	font-weight: 400;
    	color: grey;
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
	
	/* .social-login-btn {
		width: 52px;
		height: 52px;
		border-radius: 8px;
		border: none;
		background-color: transparent;
		background-size: contain;
		background-repeat: no-repeat;
	} */
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container login-container">
	<div class="login-wrap">
		<div class="card">
			<div class="card-body p-5">
				<form id="loginForm">
					<div class="id_pw_wrap">
						<div class="form-group mb-4">
							<label class="font-weight-bold" for="memberEmail" >이메일</label>
							<input class="form-control" type="text" name="memberEmail" id="memberEmail" placeholder="이메일 입력"/>
						</div>
						<div class="form-group mb-4">
							<label class="font-weight-bold" for="memberPassword">비밀번호</label>
							<input class="form-control" type="password" name="memberPassword" id="memberPassword" placeholder="비밀번호 입력" autocomplete="off"/>
						</div>
					</div>
					<div class="form-group pt-4 mb-4">
						<button type="submit" class="btn btn-primary btn-block">로그인</button>
					</div>
				</form>
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
				<div class="social-login">
					<hr class="social-login-line">
					<span class="social-login-title">SNS 계정 로그인</span>
					<div class="social-login-body">
						<a href="${contextPath}/login/kakao?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/kakao.png">
						</a>
						<a href="${contextPath}/login/naver?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/naver.png">
						</a>
						<a href="${contextPath}/login/google?continue=${continueUrl}">
							<img class="social-login-btn" src="${contextPath}/resources/images/google.png">
						</a>
						<a href="#">
							<img class="social-login-btn" src="${contextPath}/resources/images/github.png">
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
		const continueUrl = "<c:out value='${continueUrl}'/>";
		$("#loginForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				memberEmail : $("#memberEmail").val(),
				memberPassword : $("#memberPassword").val(),
			};
			
			authenticationService.login(formData, continueUrl, function(result) {
				setTimeout(function() {
					alert(result.message);
					location.href = result.data.continueUrl;
				}, 50);
			});
		});
	})
</script>
</body>

</html>