<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>회원가입 | Codingjoa</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="${contextPath}/resources/js/main.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	.join-wrap {
		min-width: 620px;
		margin: 0 auto;
	}
	
	.join-wrap .form-check-input {
		margin-top: .15rem !important;
	}
	
	.form-control:disabled {
		background-color: #f2f2f2;
	}

	span.error, span.success {
		display: inline-block;
		padding-top: 7px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container join-container">
	<div class="join-wrap">
		<div class="title-wrap">
			<h3 class="font-weight-bold">회원 가입</h3>
		</div>
		<div class="card rounded-xl">
			<div class="card-body p-5">
				<form:form action="${contextPath}/join" method="POST" modelAttribute="joinDto">
					<div class="form-group mb-4">
						<form:label path="email" class="font-weight-bold">이메일</form:label>
						<div class="input-group mb-3">
							<form:input path="email" class="form-control rounded-md" placeholder="이메일을 입력해주세요"/>
							<div class="input-group-append">
								<button type="button" class="btn btn-outline-secondary btn-sm rounded-md" id="sendAuthCodeBtn">인증코드 받기</button>
							</div>
						</div>
						<div class="input-group">
							<form:input path="authCode" class="form-control rounded-md" placeholder="인증코드를 입력해주세요"/>
						</div>
						<form:errors path="email" cssClass="error"/>
						<form:errors path="authCode" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="nickname" class="font-weight-bold">닉네임</form:label>
						<div class="input-group">
							<form:input path="nickname" class="form-control rounded-md" placeholder="닉네임을 입력해주세요"/>
						</div>
						<form:errors path="nickname" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="password" class="font-weight-bold">비밀번호</form:label>
						<div class="input-group">
							<form:password path="password" class="form-control rounded-md" placeholder="비밀번호를 입력해주세요" showPassword="true"/>
						</div>
						<form:errors path="password" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="confirmPassword" class="font-weight-bold">비밀번호 확인</form:label>
						<div class="input-group">
							<form:password path="confirmPassword" class="form-control rounded-md" placeholder="비밀번호 확인을 입력해주세요" showPassword="true"/>
						</div>
						<form:errors path="confirmPassword" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<div class="form-check small mb-1">
							<label class="form-check-label">
								<form:checkbox class="form-check-input" path="agree"/>
								<span>마케팅 활용 및 광고 수신 동의 (선택)</span>
							</label>
						</div>
						<div class="form-check small">
							<label class="form-check-label">
								<input class="form-check-input" type="checkbox" id="agreeJoin">
								<span><a href="#">이용약관</a> 및 <a href="#">개인정보 처리방침</a> 동의</span>
							</label>
						</div>
					</div>
					<div class="mb-3">
						<form:button class="btn btn-primary btn-block rounded-md" id="joinBtn" disabled="true">회원가입</form:button>
					</div>
				</form:form>
				<p class="text-center small mb-0">
					<span class="mr-1">이미 회원이신가요?</span>
					<span><a href="${contextPath}/login">로그인 하기</a></span>
				</p>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#sendAuthCodeBtn").on("click", function() {
			let obj = {
				email : $("#email").val()
			};
			
			mainService.sendAuthCodeForJoin(obj, function(result) {
				//$("#authCode").closest("div").after("<span class='success'>" + result.message + "</span>");
				alert(result.message);
				$("#authCode").val("");
				$("#authCode").focus();
			});
		});
		
		$("#email").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#sendAuthCodeBtn").click();
			}
		});
		
		$("#agreeJoin").on("change", function() {
			let agreed = $(this).prop("checked");
			$("#joinBtn").prop("disabled", !agreed);
		});
	});
</script>
</body>
</html>