<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 재설정</title>
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
		margin-bottom: 5px;
		font-size: 14px;
		font-weight: bold;
	}
	
	p.description {
		margin-bottom: 40px;
		font-size: 12px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container reset-password-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5 class="font-weight-bold">비밀번호 재설정</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<p class="title">새로운 비밀번호를 입력해주세요.</p>
				<p class="description"> 
					<span>- 비밀번호는 8-16자 영문자, 숫자, 특수문자를 사용하세요.</span><br/>
					<span>- 보안 정책에 따라 현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.</span>
				</p>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>새로운 비밀번호</dt>
					<dd class="input-group">
						<input type="password" id="memberPassword" name="memberPassword" placeholder="새로운 비밀번호 입력"/>
					</dd>
				</dl>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>비밀번호 확인</dt>
					<dd class="input-group">
						<input type="password" id="confirmPassword" name="confirmPassword" placeholder="비밀번호 확인 입력"/>
					</dd>
				</dl>
				<div class="pt-3">
					<button type="button" class="btn btn-primary btn-block" id="resetPasswordBtn">확인</button>
				</div>				
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#resetPasswordBtn").on("click", function() {
			let obj = {
				memberPassword : $("#memberPassword").val(),
				confirmPassword : $("#confirmPassword").val(),
				type : "RESET_PASSWORD"
			};
			
			resetPassword("${contextPath}/member/resetPassword", obj);
		});
		
		$("input").on("focus", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #868e96");
		});

		$("input").on("blur", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #dee2e6");
		});
	});
	
	function resetPassword(url, obj) {
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
				location.href = "${contextPath}/member/login";
			},
			error : function(jqXHR) {
				console.log(jqXHR);
				//$("#memberPassword\\.errors, #confirmPassword\\.errors").remove();
				$(".error").remove();
				
				if(jqXHR.status == 422) {
					let errorMap = JSON.parse(jqXHR.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
</script>
</body>
</html>