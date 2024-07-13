<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- <c:set var="principal" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" /> --%>
<sec:authentication property="principal" var="principal"/>

<!DOCTYPE html>
<html>
<head>
<title>Codingjoa : 계정 정보</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
<script src="${contextPath}/resources/js/image.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	input[type="text"] {
		border: none;
		width: 100%;
		padding: 5px 0 5px 7px;
		/* padding: 3px 0 3px 7px; */
	}
	
	input[type="text"]:focus {
		outline: none;
	}
	
	.inner-text {
		border-radius: 0.5rem;
		padding: 5px 0 5px 7px;
		/* padding: 3px 0 3px 7px; */
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
		padding: 13px 0 5px 0;
		/* padding: 10px 0 5px 0; */
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
	
	#memberImageForm {
		display: none;
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
	
	dl.form-group {
		margin-bottom: 0;
	}
	
	.member-thumb-image {
		width: 85px;
		height: 85px;
		border: 1px solid #dee2e6 !important;
		border-radius: 0.5rem;
		/* width: 65px;
		height: 65px;
		border-radius: 0.4rem; */
		/* border-radius: 50%!important; */
	}	
	
	.wrap-member-image {
		position: relative;
	}
	
	.member-image-btn {
		position: absolute;
		z-index: 99;
		top: 60px;
		left: 60px;
		padding: 0;
		margin: 0;
  		line-height: 0;
  		border: none !important;
  		outline: none !important;
  		background-color: transparent;
  		cursor: pointer;
	}
	
	.member-image-icon {
		display: inline-block;
		width: 35px;
		height: 35px;
		border: 1px solid white;
		border-radius: 50%;
		background-image: url('/codingjoa/resources/images/img_camera3.png');
		background-size: contain;
	}
	
	.info-wrap {
		width: 620px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container info-container">
	<div class="info-wrap">
		<h5 class="font-weight-bold">계정 정보</h5>
		<div class="pt-4 border-top border-dark">
			<div class="mb-5 d-flex">
				<div class="wrap-member-image mr-4">
					<c:choose>
						<c:when test="${not empty principal.memberImageName}">
							<img class="member-thumb-image" id="memberThumbImage" src="${contextPath}/api/member/images/${principal.memberImageName}">
						</c:when>
						<c:otherwise>
							<img class="member-thumb-image" id="memberThumbImage" src="${contextPath}/resources/images/img_profile.png">
						</c:otherwise>
					</c:choose>
					<button type="button" class="member-image-btn" id="uploadMemberImageBtn">
						<span class="member-image-icon"></span>
						<form id="memberImageForm">
							<input type="file" id="memberImage" name="memberImage">
						</form>
					</button>
				</div>
				<div class="w-100 pt-2">
					<dl class="form-group">
						<dt><i class="fa-solid fa-check mr-2"></i>아이디</dt>
						<dd class="input-group">
							<span class="inner-text"><c:out value="${principal.member.memberId}"/></span>
						</dd>
					</dl>
				</div>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<dd class="input-group" id="showEmail">
						<div>
							<span class="inner-text"><c:out value="${principal.member.memberEmail}"/></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showEmailBtn">수정</button>
					</dd>
					<!-- d-none(#editEmail) -->
					<dd class="input-group" id="editEmail">
						<form>
							<input type="text" id="memberEmail" name="memberEmail" value="${principal.member.memberEmail}"/>
						</form>
						<div>
							<button class="btn btn-warning btn-sm" type="button" id="sendAuthCodeBtn">인증코드 받기</button>
							<button class="btn btn-outline-primary btn-sm" type="button" id="updateEmailBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" type="button" id="resetEmailBtn">취소</button>
						</div>
					</dd>
					<dd class="input-group" id="editAuthCode">
						<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력하세요.">
					</dd>
				</dl>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>주소</dt>
					<dd class="input-group" id="showZipcode">
						<div>
							<span class="inner-text"><c:out value="${principal.member.memberZipcode}"/></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" type="button" id="showAddrBtn">수정</button>
					</dd>
					<dd class="input-group" id="showAddr">
						<span class="inner-text"><c:out value="${principal.member.memberAddr}"/></span>
					</dd>
					<dd class="input-group" id="showAddrDetail">
						<span class="inner-text"><c:out value="${principal.member.memberAddrDetail}"/></span>
					</dd>
					<!-- d-none(#editZipcode) -->
					<dd class="input-group" id="editZipcode">
						<form>
							<input type="text" id="memberZipcode" name="memberZipcode" value="${principal.member.memberZipcode}" readonly/> 
						</form>
						<div>
							<button class="btn btn-warning btn-sm" type="button" id="searchAddrBtn">주소 찾기</button>
							<button class="btn btn-outline-primary btn-sm" type="button" id="updateAddrBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" type="button" id="resetAddrBtn">취소</button>
						</div>
					</dd>
					<!-- d-none(#editAddr) -->
					<dd class="input-group" id="editAddr">
						<form>
							<input type="text" id="memberAddr" name="memberAddr" value="${principal.member.memberAddr}" readonly/>
						</form>
					</dd>
					<!-- d-none(#editAddrDetail) -->
					<dd class="input-group" id="editAddrDetail">
						<form>
							<input type="text" id="memberAddrDetail" name="memberAddrDetail" value="${principal.member.memberAddrDetail}"/>
						</form>
					</dd>
				</dl>
			</div>
			<div class="mb-5">
				<dl class="form-group mb-5">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일 수신</dt>
					<dd class="input-group" id="showAgree">
						<div class="form-check form-check-inline mr-0">
							<label class="form-check-label label-disabled">
								<input class="form-check-input" type="checkbox" ${principal.member.memberAgree == true ? 'checked' : ''} disabled/>
								<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
							</label>
						</div>
						<button class="btn btn-outline-primary btn-sm" type="button" id="showAgreeBtn">수정</button>
					</dd>
					<!-- d-none(#editAgree) -->
					<dd class="input-group" id="editAgree">
						<form>
							<div class="form-check form-check-inline">
								<label class="form-check-label">
									<input class="form-check-input" type="checkbox" id="memberAgree" name="memberAgree" 
										${principal.member.memberAgree == true ? 'checked' : ''}>
									<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
								</label>
							</div>
						</form>
						<div>
							<button class="btn btn-outline-primary btn-sm" type="button" id="updateAgreeBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" type="button" id="resetAgreeBtn">취소</button>
						</div>
					</dd>
				</dl>
			</div>
		</div>
		
		<!-- test -->
		<div class="input-group pt-5 d-none">
			<div class="input-group-prepend">
   				<span class="input-group-text">/api/member/images/{memberImageName}</span>
			</div>
			<input type="text" class="form-control" placeholder="memberImageName" style="border: 1px solid #ced4da;">
			<div class="input-group-append">
   				<button class="btn btn-warning" id="testGetMemberImageBtn" onclick="testGetMemberImage(this)">TEST</button>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	$(function() {
		$("#uploadMemberImageBtn").on("click", function() {
			$("#memberImage").click();
		});
		
		// prevent stack overflow (Uncaught RangeError: Maximum call stack size exceeded)
		// since #memberImage(file) is a child element of #uploadMemberImageBtn, event propagation occurs
		$("#memberImage").on("click", function(e) {
			e.stopPropagation();
		})
		
		// uploadMemberImage
		$("#memberImage").on("change", function() {
			// jQuery object --> javaScript DOM object 
			// let $memberImage = $(this)[0];
			const formData = new FormData();
			formData.append("file", this.files[0]);
			
			// initialize the file input, but not with null
			//this.value = "";
			$("#memberImageForm")[0].reset();

			imageService.uploadMemberImage(formData, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					let memberImageUrl = "${contextPath}/api/member/images/" + result.data.memberImageName;
					$("#memberThumbImage, #navMemberImage").attr("src", memberImageUrl);
				});
			});
		});
		
		$("#memberThumbImage, #navMemberImage").on("load", function() {
			console.log("%c## IMAGE LOADING SUCCESS", "color:green");
			console.log("> %s", this.id);
		});

		$("#memberThumbImage, #navMemberImage").on("error", function() {
			console.log("%c## IMAGE LOADING FAILURE", "color:red");
			console.log("> %s", this.id);
		});
		
		// sendAuthCode
		$("#sendAuthCodeBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val(),
			};
			
			memberService.sendAuthCodeForUpdate(obj, function(result) {
				$("#authCode").closest("dd").after("<dd class='success'>" + result.message + "</dd>");
				$("#authCode").val("");
				$("#authCode").focus();
			});
		});

		// updateEmail
		$("#updateEmailBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val(),
				authCode : $("#authCode").val()
			};
			
			memberService.updateEmail(obj, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					let currentMember = result.data.member;
					$("#editEmail").find("form")
						.html("<input type='text' id='memberEmail' name='memberEmail' value='" + currentMember.memberEmail + "'>");
					$("#showEmail").find("span").text(currentMember.memberEmail);
					$("#resetEmailBtn").click();
				});
			});
		});
		
		$(document).on("keydown", "#memberEmail", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$("#sendAuthCodeBtn").click();
			}
		});
		
		$(document).on("keydown", "#authCode", function(e) {
			if (e.keyCode == 13) {
				$("#updateEmailBtn").click();
			}
		});
		
		// updateAddr
		$("#updateAddrBtn").on("click", function() {
			let obj = {
				memberZipcode : $("#memberZipcode").val(),
				memberAddr : $("#memberAddr").val(),
				memberAddrDetail : $("#memberAddrDetail").val()
			};
			
			memberService.updateAddr(obj, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					let currentMember = result.data.member;
					$("#editZipcode").find("form")
						.html("<input type='text' id='memberZipcode' name='memberZipcode' value='" + currentMember.memberZipcode + "' readonly>");
					$("#editAddr").find("form")
						.html("<input type='text' id='memberAddr' name='memberAddr' value='" + currentMember.memberAddr + "' readonly>");
					$("#editAddrDetail").find("form")
						.html("<input type='text' id='memberAddrDetail' name='memberAddrDetail' value='" + currentMember.memberAddrDetail + "'>");
					$("#showZipcode").find("span").text(currentMember.memberZipcode);
					$("#showAddr").find("span").text(currentMember.memberAddr);
					$("#showAddrDetail").find("span").text(currentMember.memberAddrDetail);
					$("#resetAddrBtn").click();
				});
			});
		});
		
		$(document).on("click", "#searchAddrBtn, #memberZipcode, #memberAddr", function() {
			execPostcode();
		});
		
		$(document).on("keydown", "#memberAddrDetail", function(e) {
			if (e.keyCode == 13) {
				$("#updateAddrBtn").click();
			}
		});

		// updateAgree
		$("#updateAgreeBtn").on("click", function() {
			let obj = {
				memberAgree : $("#memberAgree").prop("checked")	
			};
			
			memberService.updateAgree(obj, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					let currentMember = result.data.member;
					let checkValue = (currentMember.memberAgree == true) ? "checked" : "";
					$("#editAgree").find("label")
						.html("<input class='form-check-input' type='checkbox' id='memberAgree' name='memberAgree' " + checkValue + ">" 
								+ "<span class='inner-text'>이메일 광고 수신에 동의합니다.<span/>");
					$("#showAgree").find("input").prop("checked", currentMember.memberAgree);
					$("#resetAgreeBtn").click();
				});
			});
		});
		
		$("#showEmailBtn").on("click", function() {
			$("#showEmail").css("display", "none");
			$("#editEmail, #editEmail > div, #editAuthCode").css("display", "flex");
		});
		
		$("#resetEmailBtn").on("click", function() {
			$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
			$("#showEmail").css("display", "flex");
			$("#editEmail, #editEmail > div, #editAuthCode").css("display", "none");
			
			$("#editEmail").find("form")[0].reset();
			$("#authCode").val("");
		});
		
		$("#showAddrBtn").on("click", function() {
			$("#showZipcode, #showAddr, #showAddrDetail").css("display", "none");
			$("#editZipcode, #editZipcode > div, #editAddr, #editAddrDetail").css("display", "flex");	
		});

		$("#resetAddrBtn").on("click", function() {
			$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
			$("#showZipcode, #showAddr, #showAddrDetail").css("display", "flex");
			$("#editZipcode, #editZipcode > div, #editAddr, #editAddrDetail").css("display", "none");
			
			$("#editZipcode").find("form")[0].reset();
			$("#editAddr").find("form")[0].reset();
			$("#editAddrDetail").find("form")[0].reset();
		});
		
		$("#showAgreeBtn").on("click", function() {
			$("#showAgree").css("display", "none");
			$("#editAgree, #editAgree > div").css("display", "flex");
		});
		
		$("#resetAgreeBtn").on("click", function() {
			$("#memberAgree\\.errors").remove();
			$("#showAgree").css("display", "flex");
			$("#editAgree, #editAgree > div").css("display", "none");
			$("#editAgree").find("form")[0].reset();
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
    
    // test
    function testGetMemberImage(target) {
    	let testMemberImageName = $(target).closest("div.input-group").find("input").val();
		imageService.getMemberImageResource(testMemberImageName, function(result) {
			let memberImageUrl = URL.createObjectURL(result);
			$("#memberThumbImage, #navMemberImage").attr("src", memberImageUrl);
		});
    }
</script>
</body>
</html>