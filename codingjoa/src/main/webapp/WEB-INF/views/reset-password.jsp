<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 재설정 | Codingjoa</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="${contextPath}/resources/js/main.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	.reset-password-wrap {
		width: 540px;
		margin: 0 auto;
	}
	
	input[type="password"] {
		border: none;
		flex-grow: 1;
		padding: 3px 0 3px 7px;
		font-size: 14px;
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
		font-size: 13px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container reset-password-container">
	<div class="reset-password-wrap">
		<div class="card rounded-xl">
			<div class="card-body p-5">
				<div class="title-wrap mb-0">
					<h4 class="font-weight-bold">
						비밀번호 재설정
						<button type="button" class="btn btn-sm btn-primary py-0 float-right" id="createKeyBtn">REMOVE KEY</button>
						<button type="button" class="btn btn-sm btn-secondary py-0 float-right" id="removeKeyBtn">REMOVE KEY</button>
					</h4>
				</div>
				<div class="pt-3">
					<p class="title">새로운 비밀번호를 입력해주세요.</p>
					<p class="description"> 
						<span>- 비밀번호는 8-16자 영문자, 숫자, 특수문자를 사용하세요.</span><br/>
						<span>- 보안 정책에 따라 현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.</span>
					</p>
					<dl class="form-group mb-5">
						<dt><i class="fa-solid fa-check mr-2"></i>새로운 비밀번호</dt>
						<dd class="input-group">
							<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호 입력"/>
						</dd>
					</dl>
					<dl class="form-group mb-5">
						<dt><i class="fa-solid fa-check mr-2"></i>비밀번호 확인</dt>
						<dd class="input-group">
							<input type="password" id="confirmPassword" name="confirmPassword" placeholder="비밀번호 확인 입력"/>
						</dd>
					</dl>
					<div class="pt-3">
						<button type="button" class="btn btn-primary btn-block rounded-md" id="resetPasswordBtn">확인</button>
					</div>				
				</div>
			</div>
		</div>
	</div>
</div>
	
		<!-- <h5 class="font-weight-bold">
			비밀번호 재설정
			<button type="button" class="btn btn-sm btn-warning py-0 float-right d-none" id="removeKeyBtn">REMOVE KEY</button>
		</h5>
		<div class="pt-3 border-top border-dark">
			<p class="title">새로운 비밀번호를 입력해주세요.</p>
			<p class="description"> 
				<span>- 비밀번호는 8-16자 영문자, 숫자, 특수문자를 사용하세요.</span><br/>
				<span>- 보안 정책에 따라 현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.</span>
			</p>
			<dl class="form-group mb-5">
				<dt><i class="fa-solid fa-check mr-2"></i>새로운 비밀번호</dt>
				<dd class="input-group">
					<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호 입력"/>
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
		</div> -->
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#resetPasswordBtn").on("click", function() {
			let obj = {
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			mainService.resetPassword("${key}", obj, function(result) {
				setTimeout(function() {
					alert(result.message);
					location.href = "${contextPath}/login";
				}, 50);
			});
		});
		
		$("#newPassword, #confirmPassword").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#resetPasswordBtn").click();
			}
		});
		
		$("input").on("focus", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #868e96");
		});

		$("input").on("blur", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #dee2e6");
		});
		
		$("#createKeyBtn").on("click", function() {
			$.ajax({
				type : "GET",
				url : "${contextPath}/api/password/reset/key",
				dataType : "json",
				beforeSend : function(xhr, settings) {
					console.log("%c> BEFORE SEND", "color:blue");
					console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				},
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					console.log(jqXHR);
				}
			});
		});
		
		$("#removeKeyBtn").on("click", function() {
			$.ajax({
				type : "DELETE",
				url : "${contextPath}/api/password/reset/key?key=${key}",
				dataType : "json",
				beforeSend : function(xhr, settings) {
					console.log("%c> BEFORE SEND", "color:blue");
					console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				},
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					console.log(jqXHR);
				}
			});
		});
	});
</script>
</body>
</html>