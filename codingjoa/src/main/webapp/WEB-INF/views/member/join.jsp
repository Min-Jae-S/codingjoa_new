<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa : 회원가입</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<!-- <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js"></script> -->
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	.form-control:read-only {
 		background-color: white;
	}
	
	.form-control:disabled {
		background-color: #f2f2f2;
	}

	span.error, span.success {
		display: inline-block;
		padding-top: 7px;
	}
	
	input[name="memberZipcode"], 
	input[name="memberAddr"] {
		cursor: pointer;
	}
	
	.join-container .card-body {
		padding: 3.5rem;
	}
	
	.join-wrap {
		width: 620px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container join-container">
	<div class="join-wrap">
		<div class="card shadow">
			<div class="card-body">
				<form:form action="${contextPath}/member/joinProc" method="POST" modelAttribute="joinDto">
					<div class="form-group mb-4">
						<form:label path="memberId" class="font-weight-bold">아이디</form:label>
						<div class="input-group">
							<form:input path="memberId" class="form-control" placeholder="아이디 입력"/>
						</div>
						<form:errors path="memberId" cssClass="error" />
					</div>
					<div class="form-group mb-4">
						<form:label path="memberPassword" class="font-weight-bold">비밀번호</form:label>
						<form:password path="memberPassword" class="form-control" placeholder="비밀번호 입력" showPassword="true"/>
						<form:errors path="memberPassword" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="confirmPassword" class="font-weight-bold">비밀번호 확인</form:label>
						<form:password path="confirmPassword" class="form-control" placeholder="비밀번호 확인 입력" showPassword="true"/>
						<form:errors path="confirmPassword" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:label path="memberEmail" class="font-weight-bold">이메일</form:label>
						<div class="input-group mb-3">
							<form:input path="memberEmail" class="form-control" placeholder="이메일 입력"/>
							<div class="input-group-append">
								<button type="button" class="btn btn-outline-secondary btn-sm" id="sendAuthCodeBtn">인증코드 받기</button>
							</div>
						</div>
						<div class="input-group">
							<form:input path="authCode" class="form-control" placeholder="인증코드를 입력하세요."/>
						</div>
						<form:errors path="memberEmail" cssClass="error"/>
						<form:errors path="authCode" cssClass="error"/>
					</div>
					<div class="form-group">
						<form:label path="memberZipcode" class="font-weight-bold">주소</form:label>
					    <div class="input-group w-50">
					    	<form:input path="memberZipcode" class="form-control" readonly="true" placeholder="우편번호 입력"/>
							<div class="input-group-append">
								<button type="button" class="btn btn-outline-secondary btn-sm" id="searchAddrBtn">주소 찾기</button>
							</div>
						</div>
						<form:errors path="memberZipcode" cssClass="error"/>
					</div>
					<div class="form-group">
						<form:input path="memberAddr" class="form-control" readonly="true" placeholder="기본주소 입력"/>
						<form:errors path="memberAddr" cssClass="error"/>
					</div>
					<div class="form-group mb-4">
						<form:input path="memberAddrDetail" class="form-control" placeholder="상세주소 입력"/>
						<form:errors path="memberAddrDetail" cssClass="error"/>
					</div>
					<div class="form-check small mb-1">
						<label class="form-check-label">
							<form:checkbox class="form-check-input" path="memberAgree"/> 이메일 광고 수신에 동의합니다. (선택)
						</label>
					</div>
					<div class="form-check small mb-1">
						<label class="form-check-label">
							<input class="form-check-input" type="checkbox" id="agreeJoinCheck">
							<a href="#">이용약관</a> 및 <a href="#">개인정보 처리방침</a>에 동의합니다.
						</label>
					</div>
					<div class="form-group pt-4">
						<form:button class="btn btn-primary btn-block mb-4" id="joinBtn" disabled="true">회원가입</form:button>
					</div>
				</form:form>
				<p class="text-center small mb-0">
					<span>이미 회원이신가요?</span>
					<span><a href="${contextPath}/member/login">로그인 하기</a></span>
				</p>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	$(function() {
		$("#sendAuthCodeBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val()
			};
			
			memberService.sendAuthCodeForJoin(obj, function(result) {
				$("#authCode").closest("div").after("<span class='success'>" + result.message + "</span>");
				$("#authCode").val("");
				$("#authCode").focus();
			});
		});
		
		$("#memberEmail").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$("#sendAuthCodeBtn").click();
			}
		});
		
		$("#agreeJoinCheck").on("change", function() {
			if ($(this).is(":checked")) {
				$("#joinBtn").attr("disabled", false);
			} else {
				$("#joinBtn").attr("disabled", true);
			}
		});
		
		$("#searchAddrBtn, #memberZipcode, #memberAddr").on("click", function() {
			execPostcode();
		});
	});
	
    function execPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                let addr = ''; // 주소 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('memberZipcode').value = data.zonecode;
                document.getElementById("memberAddr").value = addr;
                document.getElementById("memberAddrDetail").value = '';
               
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("memberAddrDetail").focus();
            }
        }).open();
    }
</script>
</body>
</html>