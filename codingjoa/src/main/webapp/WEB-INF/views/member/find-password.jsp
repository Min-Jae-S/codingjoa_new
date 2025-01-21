<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 찾기 | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	input[type="text"] {
		border: none;
		width: 100%;
		/* padding: 3px 0 3px 7px; */
		padding: 0;
	}
	
	input[type="text"]:focus {
		outline: none;
	}
	
	dt {
		font-size: 14px;
		font-weight: bold;
	}
	
	dd.error, dd.success {
		/* padding-left: 7px; */
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
	
	.find-password-wrap {
		min-width: 540px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container find-password-container">
	<div class="find-password-wrap">
		<div class="card rounded-xl">
			<div class="card-body p-5">
				<div class="border-bottom border-dark pb-2">
					<h4 class="font-weight-bold">비밀번호 찾기</h4>
				</div>
				<div class="pt-3">
					<p class="title">
						<span>코딩조아(Codingjoa)에 가입한 이메일을 입력해주세요.</span><br/>
						<span>일치하는 계정이 있다면 비밀번호 재설정 링크가 전송됩니다.</span><br/>
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
							<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일을 입력 하세요"/>
						</dd>
					</dl>
					<div class="pt-3">
						<button type="button" class="btn btn-primary btn-block rounded-md" id="findPasswordBtn">비밀번호 재설정 메일 보내기</button>
					</div>
				</div>
			</div>
		</div>
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
				setTimeout(function() {
					alert(result.message);
				}, 50);
			});
		});
		
		$("#memberId, #memberEmail").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#findPasswordBtn").click();
			}
		});
		
		$("#memberId, #memberEmail").on("focus", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #868e96");
		});
	
		$("#memberId, #memberEmail").on("blur", function() {
			$(this).closest("dd").css("border-bottom", "1px solid #dee2e6");
		});
	});
</script>
</body>
</html>