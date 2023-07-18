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

<div class="container find-password-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5 class="font-weight-bold">비밀번호 찾기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<p class="title">
					<span>코딩조아(Codingjoa) 계정의 아이디와 등록한 이메일을 입력해주세요.</span><br/>
					<span>입력하신 정보와 연계된 계정이 있다면 비밀번호 재설정을 위한 링크가 이메일을 통해 전송됩니다.</span><br/>
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
						<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일 입력" />
					</dd>
				</dl>
				<div class="pt-3">
					<button type="button" class="btn btn-primary btn-block" id="findPasswordBtn">확인</button>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#findPasswordBtn").on("click", function() {
			let obj = {
				memberId : $("#memberId").val(),
				memberEmail : $("#memberEmail").val()
			};
			
			memberService.findPassword(obj, function(result) {
				$(".error, .success").remove();
				$("#editEmail").after("<dd class='success'>" + result.message + "</dd>");
			});
		});
		
		$("#memberId, #memberEmail").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#findPasswordBtn").click();
			}
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