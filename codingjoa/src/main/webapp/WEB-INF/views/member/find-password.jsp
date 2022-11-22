<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 찾기</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	input[type="text"] {
		border: none;
		width: 100%;
		padding: 3px 0 3px 7px;
	}
	
	input[type="text"]:focus {
		outline: none;
	}
	
	dt {
		font-size: 14px;
		font-weight: bold;
	}
	
	dd.error, dd.success {
		padding-left: 7px;
	}
	
	dd.input-group {
		padding: 10px 0 5px 0;
		border-bottom: 1px solid #dee2e6;
	}

	#editEmail > div {
		display: flex;
		flex: 1 1 auto;
		align-items: center;
	}
	
	p.title {
		margin-bottom: 40px;
		font-size: 14px;
		font-weight: bold;
	}
	
	h5 {
		font-weight: bold;
	}
	
	.btn-sm {
		font-size: 0.75rem;
		border-radius: 0.5rem;
		align-self: center;
		margin-left: 5px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container find-password-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5>비밀번호 찾기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<p class="title">
					<span>비밀번호 재설정을 위해 사용자 인증이 필요합니다.</span><br/>
					<span>코딩조아(Codingjoa) 계정의 아이디와 등록한 이메일로 인증을 진행해주세요.</span><br/>
					<span>등록한 이메일 주소와 입력한 이메일 주소가 같아야, 인증코드를 받을 수 있습니다.</span>
				</p>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>아이디</dt>
					<dd class="input-group" id="editId">
						<input type="text" id="memberId" name="memberId" placeholder="아이디 입력"/>
					</dd>
				</dl>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<dd class="input-group" id="editEmail">
						<div>
							<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일 입력" />
						</div>
						<button class="btn btn-warning btn-sm" id="sendAuthEmailBtn">인증코드 받기</button>
					</dd>
					<dd class="input-group" id="editAuthCode">
						<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력하세요.">
					</dd>
				</dl>
				<div class="pt-3">
					<button type="button" class="btn btn-primary btn-block" id="findPasswordBtn">비밀번호 찾기</button>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#sendAuthEmailBtn").on("click", function() {
			sendAuthEmail();
		});
		
		$("#findPasswordBtn").on("click", function() {
			findPassword();
		});
		
		$("input").on("focus", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #868e96");
		});
	
		$("input").on("blur", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #dee2e6");
		});
	});
	
	function sendAuthEmail() {
		var obj = {
			memberId : $("#memberId").val(),
			memberEmail : $("#memberEmail").val(),
			type : "BEFORE_FIND_PASSWORD"
		};
		
		$.ajax({
			type : "POST",
			url : "${contextPath}/member/sendAuthEmail",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				//$("#memberId\\.errors, #memberEmail\\.errors, #authCode\\.errors, .success").remove();
				$(".error, .success").remove();
				$("#authCode").closest("dd").after("<dd class='success'>" + result.message + "</dd>");
				$("#authCode").val("");
				$("#authCode").focus();
			},
			error : function(e) {
				console.log(e.responseText);
				//$("#memberId\\.errors, #memberEmail\\.errors, #authCode\\.errors, .success").remove();
				$(".error, .success").remove();
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
	function findAccount() {
		var obj = {
			memberEmail : $("#memberEmail").val(),
			authCode : $("#authCode").val(),
			type : "FIND_PASSWORD"		
		};
		
		$.ajax({
			type : "POST",
			url : "${contextPath}/member/findAccount",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
				location.href = "${contextPath}/member/findAccountResult";
			},
			error : function(e) {
				console.log(e.responseText);
				//$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				$(".error, .success").remove();
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					
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