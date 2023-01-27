<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="principal" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" />
<!DOCTYPE html>
<html>
<head>
<title>계정 정보</title>
<meta charset="utf-8">
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
	
	.inner-text {
		border-radius: 0.5rem;
		padding: 3px 0 3px 7px;
	}
	
	label {
		border-radius: 0.5rem;
		padding-left: 7px;
	}
	
	.inner-text:not(#editAgree .inner-text), .label-disabled {
		color: #545454;
		background-color: #f7f7f7;
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
	
	dd.input-group > form, #showEmail > div, 
	#showZipcode > div, #showAgree > div, 
	.inner-text, label {
		display: flex;
		flex: 1 1 auto;
		align-items: center;
	}
	
	#editEmail, #editAuthCode, #editZipcode, #editAddr, #editAddrDetail, #editAgree {
		display: none;
		border-bottom: 1px solid #868e96;
	}
	
	.btn-sm {
		font-size: 0.75rem;
		border-radius: 0.5rem;
		align-self: center;
		margin-left: 5px;
	}
	
	input[name="memberZipcode"], input[name="memberAddr"] {
		cursor: pointer;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container info-container">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<h5 class="font-weight-bold">계정 정보</h5>
			<div class="pt-4" style="border-top: 1px solid black;">
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>아이디</dt>
					<dd class="input-group">
						<span class="inner-text"><c:out value="${principal.member.memberId}"/></span>
					</dd>
				</dl>
				
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<dd class="input-group" id="showEmail">
						<div>
							<span class="inner-text"><c:out value="${principal.member.memberEmail}"/></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showEmailBtn">수정</button>
					</dd>
					
					<!-- display: none; -->
					<dd class="input-group" id="editEmail">
						<form>
							<input type="text" id="memberEmail" name="memberEmail" value="${principal.member.memberEmail}"/>
						</form>
						<div>
							<button class="btn btn-warning btn-sm" id="sendAuthEmailBtn">인증코드 받기</button>
							<button class="btn btn-outline-primary btn-sm" id="readEmailBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" id="resetEmailBtn">취소</button>
						</div>
					</dd>
					<dd class="input-group" id="editAuthCode">
						<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력하세요.">
					</dd>
				</dl>
				
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>주소</dt>
					<dd class="input-group" id="showZipcode">
						<div>
							<span class="inner-text"><c:out value="${principal.member.memberZipcode}"/></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showAllAddrBtn">수정</button>
					</dd>
					<dd class="input-group" id="showAddr">
						<span class="inner-text"><c:out value="${principal.member.memberAddr}"/></span>
					</dd>
					<dd class="input-group" id="showAddrDetail">
						<span class="inner-text"><c:out value="${principal.member.memberAddrDetail}"/></span>
					</dd>
					
					<!-- display: none; -->
					<dd class="input-group" id="editZipcode">
						<form>
							<input type="text" id="memberZipcode" name="memberZipcode" value="${principal.member.memberZipcode}" readonly/>
						</form>
						<div>
							<button class="btn btn-warning btn-sm" id="searchAddrBtn">주소 찾기</button>
							<button class="btn btn-outline-primary btn-sm" id="readAllAddrBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" id="resetAllAddrBtn">취소</button>
						</div>
					</dd>
					
					<!-- display: none; -->
					<dd class="input-group" id="editAddr">
						<form>
							<input type="text" id="memberAddr" name="memberAddr" value="${principal.member.memberAddr}" readonly />
						</form>
					</dd>
					
					<!-- display: none; -->
					<dd class="input-group" id="editAddrDetail">
						<form>
							<input type="text" id="memberAddrDetail" name="memberAddrDetail" value="${principal.member.memberAddrDetail}" />
						</form>
					</dd>
				</dl>

				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일 수신</dt>
					<dd class="input-group" id="showAgree">
						<div class="form-check form-check-inline mr-0">
							<label class="form-check-label label-disabled">
								<input class="form-check-input" type="checkbox" ${principal.member.memberAgree == true ? 'checked' : ''} disabled />
								<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
							</label>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showAgreeBtn">수정</button>
					</dd>
					
					<!-- display: none; -->
					<dd class="input-group" id="editAgree">
						<form>
							<div class="form-check form-check-inline">
								<label class="form-check-label">
									<input class="form-check-input" type="checkbox" id="memberAgree" name="memberAgree" ${principal.member.memberAgree == true ? 'checked' : ''}>
									<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
								</label>
							</div>
						</form>
						<div>
							<button class="btn btn-outline-primary btn-sm" id="readAgreeBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" id="resetAgreeBtn">취소</button>
						</div>
					</dd>
				</dl>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	$(function() {
		$("#searchAddrBtn, #memberZipcode, #memberAddr").on("click", function() {
			execPostcode();
		});
		
		$("#sendAuthEmailBtn").on("click", function() {
			sendAuthEmail();
		});
		
		/* 이메일 - 확인 버튼 */
		$("#readEmailBtn").on("click", function() {
			updateEmail();
		});

		/* 주소 - 확인 버튼 */
		$("#readAllAddrBtn").on("click", function() {
			updateAddr();
		});

		/* 이메일 동의 - 확인 버튼 */
		$("#readAgreeBtn").on("click", function() {
			updateAgree();
		});
		
		/* 이메일 - 수정 버튼 */
		$("#showEmailBtn").on("click", function() {
			$("#showEmail").css("display", "none");
			$("#editEmail, #editEmail > div, #editAuthCode").css("display", "flex");
		});

		/* 이메일 - 취소 버튼 */
		$("#resetEmailBtn").on("click", function() {
			$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
			$("#showEmail").css("display", "flex");
			$("#editEmail, #editEmail > div, #editAuthCode").css("display", "none");
			
			$("#editEmail").find("form")[0].reset();
			$("#authCode").val("");
		});
		
		/* 주소 - 수정 버튼 */
		$("#showAllAddrBtn").on("click", function() {
			$("#showZipcode, #showAddr, #showAddrDetail").css("display", "none");
			$("#editZipcode, #editZipcode > div, #editAddr, #editAddrDetail").css("display", "flex");	
		});

		/* 주소 - 취소 버튼 */
		$("#resetAllAddrBtn").on("click", function() {
			$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
			$("#showZipcode, #showAddr, #showAddrDetail").css("display", "flex");
			$("#editZipcode, #editZipcode > div, #editAddr, #editAddrDetail").css("display", "none");
			
			$("#editZipcode").find("form")[0].reset();
			$("#editAddr").find("form")[0].reset();
			$("#editAddrDetail").find("form")[0].reset();
		});
		
		/* 이메일 동의 - 수정 버튼 */
		$("#showAgreeBtn").on("click", function() {
			$("#showAgree").css("display", "none");
			$("#editAgree, #editAgree > div").css("display", "flex");
		});
		
		/* 이메일 동의 - 취소 버튼 */
		$("#resetAgreeBtn").on("click", function() {
			$("#memberAgree\\.errors").remove();
			$("#showAgree").css("display", "flex");
			$("#editAgree, #editAgree > div").css("display", "none");
			$("#editAgree").find("form")[0].reset();
		});
		
	});
	
	function sendAuthEmail() {
		var obj = {
			memberEmail : $("#memberEmail").val(),
			type : "BEFORE_UPDATE_EMAIL"
		};
		
		$.ajax({
			type : "POST",
			url : "${contextPath}/member/sendAuthEmail",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				$("#authCode").closest("dd").after("<dd class='success'>" + result.message + "</dd>");
				$("#authCode").val("");
				$("#authCode").focus();
			},
			error : function(e) {
				console.log(e.responseText);
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
	function updateEmail() {
		var obj = {
			memberEmail : $("#memberEmail").val(),
			authCode : $("#authCode").val(),
			type : "UPDATE_EMAIL"
		};
		
		$.ajax({
			type : "PUT",
			url : "${contextPath}/member/updateEmail",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
				var member = result.data.member;
				$("#editEmail").find("form").html("<input type='text' id='memberEmail' name='memberEmail' value='" + member.memberEmail + "' readonly>");
				$("#showEmail").find("span").text(member.memberEmail);
				$("#resetEmailBtn").click();
			},
			error : function(e) {
				console.log(e.responseText);
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
	function updateAddr() {
		var obj = {
			memberZipcode : $("#memberZipcode").val(),
			memberAddr : $("#memberAddr").val(),
			memberAddrDetail : $("#memberAddrDetail").val() 	
		};
		
		$.ajax({
			type : "PUT",
			url : "${contextPath}/member/updateAddr",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
				var member = result.data.member;
				$("#editZipcode").find("form").html("<input type='text' id='memberZipcode' name='memberZipcode' value='" + member.memberZipcode + "' readonly>");
				$("#showZipcode").find("span").text(member.memberZipcode);
				$("#editAddr").find("form").html("<input type='text' id='memberAddr' name='memberAddr' value='" + member.memberAddr + "' readonly>");
				$("#showAddr").find("span").text(member.memberAddr);
				$("#editAddrDetail").find("form").html("<input type='text' id='memberAddrDetail' name='memberAddrDetail' value='" + member.memberAddrDetail + "'>");
				$("#showAddrDetail").find("span").text(member.memberAddrDetail);
				$("#resetAllAddrBtn").click();
			},
			error : function(e) {
				console.log(e.responseText);
				$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();

				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
	function updateAgree() {
		$.ajax({
			type : "PUT",
			url : "${contextPath}/member/updateAgree",
			data : JSON.stringify({
				memberAgree : $("#memberAgree").prop("checked")
			}),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
				var member = result.data.member;
				var check_value = (member.memberAgree == true) ? "checked" : "";
				$("#editAgree").find("label").html("<input class='form-check-input' type='checkbox' id='memberAgree' name='memberAgree' " + check_value + "><span class='inner-text'>이메일 광고 수신에 동의합니다.<span/>");
				$("#showAgree").find("input").prop("checked", member.memberAgree);
				$("#resetAgreeBtn").click();
			},
			error : function(e) {
				console.log(e.responseText);
				$("#memberAgree\\.errors").remove();
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd").after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				}
			}
		});
	}
	
    function execPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수

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