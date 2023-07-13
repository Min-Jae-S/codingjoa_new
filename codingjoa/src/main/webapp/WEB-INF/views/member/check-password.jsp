<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="principal" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" />
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 확인</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
<style>
	input[type="text"], input[type="password"] {
		border: none;
		width: 100%;
		padding: 3px 0 3px 7px;
	}
	
	input[type="password"]:focus {
		outline: none;
	}
	
	.inner-text {
		width: 100%;
		color: #545454;
		background-color: #f7f7f7;
		border-radius: 0.5rem;
		padding: 3px 0 3px 7px;
	}
	
	dt {
		font-size: 14px;
		font-weight: bold;
	}
	
	dd.error {
		padding-left: 7px;
	}
	
	dd.input-group {
		padding: 10px 0 5px 0;
		border-bottom: 1px solid #dee2e6;
	}
	
	p.title {
		margin-bottom: 40px;
		font-size: 14px;
		font-weight: bold;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container check-password-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5 class="font-weight-bold">비밀번호 확인</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<p class="title">비밀번호 재설정을 위해서 현재 비밀번호 확인을 진행합니다.</p>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>아이디</dt>
					<dd class="input-group">
						<span class="inner-text"><c:out value="${principal.member.memberId}"/></span>
					</dd>
				</dl>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>
					<dd class="input-group">
						<input type="password" id="memberPassword" name="memberPassword" placeholder="현재 비밀번호"/>
					</dd>
				</dl>
				<div class="pt-3">
					<button type="button" class="btn btn-primary btn-block" id="checkPasswordBtn">확인</button>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#checkPasswordBtn").on("click", function() {
			let obj = {
				memberPassword : $("#memberPassword").val(),
				//type : "BEFORE_UPDATE_PASSWORD"
				type : "CHECK_PASSWORD"
			};
			
			memberService.checkPassword(obj, function(result) {
				alert(result.message);
				location.href = "${contextPath}/member/account/updatePassword";
			});
		});
		
		$("input").on("focus", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #868e96");
		});

		$("input").on("blur", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #dee2e6");
		});
	});
</script>
</body>
</html>