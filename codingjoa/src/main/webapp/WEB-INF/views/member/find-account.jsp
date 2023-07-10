<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>계정 찾기</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
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

<div class="container find-account-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5 class="font-weight-bold">계정 찾기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<p class="title">
					<span>코딩조아(Codingjoa) 계정에 등록한 이메일로 인증을 진행해 주세요.</span><br/>
					<span>등록한 이메일 주소와 입력한 이메일 주소가 같아야, 인증코드를 받을 수 있습니다.</span>
				</p>
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<dd class="input-group" id="editEmail">
						<div>
							<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일 입력" />
						</div>
						<button class="btn btn-warning btn-sm" id="checkEmailBtn">인증코드 받기</button>
					</dd>
					<dd class="input-group" id="editAuthCode">
						<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력하세요.">
					</dd>
				</dl>
				<div class="pt-3">
					<button type="button" class="btn btn-primary btn-block" id="findAccountBtn">계정 찾기</button>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#checkEmailBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val(),
				type : "BEFORE_FIND_ACCOUNT"		
			};
			
			memberService.checkEmail(obj, function(result) {
				$(".error, .success").remove();
				$("#authCode").closest("dd").after("<dd class='success'>" + result.message + "</dd>");
				$("#authCode").val("");
				$("#authCode").focus();
			});
		});
		
		$("#findAccountBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val(),
				authCode : $("#authCode").val(),
				type : "FIND_ACCOUNT"		
			};
			
			memberService.findAccount(obj, function(result) {
				alert(result.message);
				location.href = "${contextPath}/member/help/findAccountResult";
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