<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<sec:authentication property="principal" var="principal"/>
<!DOCTYPE html>
<html>
<head>
<title>계정 관리 | Codingjoa</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/member.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	input[type="text"], input[type="password"] {
		border: none;
		width: 100%;
		padding: 5px 0 5px 7px;
		font-size: 14px;
	}
	
	input[type="text"]:focus, input[type="password"]:focus {
		outline: none;
	}
	
	.inner-text {
		border-radius: 0.5rem;
		padding: 5px 0 5px 7px;
		/* padding: 3px 0 3px 7px; */
		font-size: 14px;
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
	
	dd.input-group > form,
	#showNickname > div, 
	#showEmail > div, 
	#showZipcode > div, 
	#showAgree > div,
	#showPassword > div,
	.inner-text, label {
		display: flex;
		flex: 1 1 auto;
		align-items: center;
	}
	
	#editNickname, #editEmail, #editAuthCode, #editZipcode, #editAddr, #editAddrDetail, #editAgree, #editPassword {
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
		background-image: url(/codingjoa/resources/images/img_camera3.png);
		background-size: contain;
	}
	
	.account-wrap {
		width: 620px;
		margin: 0 auto;
	}
	
	.profile-wrap, .security-wrap {
		border: 1px solid #d7e2eb;
   	 	border-radius: 16px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container account-container">
	<div class="account-wrap">
		<div class="border-bottom border-dark">
			<h4 class="font-weight-bold">계정 관리</h4>
		</div>
		<div class="mt-4 p-4 profile-wrap">
			<h5 class="mb-3 font-weight-bold">계정 정보</h5>
			<div class="mb-5 d-flex">
				<div class="wrap-member-image mr-4">
					<img class="member-thumb-image" id="memberThumbImage">
					<button type="button" class="member-image-btn" id="updateMemberImageBtn">
						<span class="member-image-icon"></span>
						<form id="memberImageForm">
							<input type="file" id="memberImage" name="memberImage">
						</form>
					</button>
				</div>
				<div class="w-100 pt-2">
					<dl class="form-group">
						<dt><i class="fa-solid fa-check mr-2"></i>닉네임</dt>
						<dd class="input-group" id="showNickname">
							<div>
								<span class="inner-text"></span>
							</div>
							<button class="btn btn-outline-primary btn-sm" id="showNicknameBtn">수정</button>
						</dd>
						<!-- d-none(#editNickname) -->
						<dd class="input-group" id="editNickname">
							<form>
								<input type="text" id="memberNickname" name="memberNickname" placeholder="닉네임을 입력해주세요."/>
							</form>
							<div>
								<button class="btn btn-outline-primary btn-sm" type="button" id="updateNicknameBtn">확인</button>
								<button class="btn btn-outline-secondary btn-sm" type="button" id="resetNicknameBtn">취소</button>
							</div>
						</dd>
					</dl>
				</div>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<dd class="input-group" id="showEmail">
						<div>
							<span class="inner-text"></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showEmailBtn">수정</button>
					</dd>
					<!-- d-none(#editEmail) -->
					<dd class="input-group" id="editEmail">
						<form>
							<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일을 입력해주세요."/>
						</form>
						<div>
							<button class="btn btn-warning btn-sm" type="button" id="sendAuthCodeBtn">인증코드 받기</button>
							<button class="btn btn-outline-primary btn-sm" type="button" id="updateEmailBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" type="button" id="resetEmailBtn">취소</button>
						</div>
					</dd>
					<dd class="input-group" id="editAuthCode">
						<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력해주세요.">
					</dd>
				</dl>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>주소</dt>
					<dd class="input-group" id="showZipcode">
						<div>
							<span class="inner-text"></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" type="button" id="showAddrBtn"></button>
					</dd>
					<dd class="input-group" id="showAddr">
						<span class="inner-text"></span>
					</dd>
					<dd class="input-group" id="showAddrDetail">
						<span class="inner-text"></span>
					</dd>
					<!-- d-none(#editZipcode) -->
					<dd class="input-group" id="editZipcode">
						<form>
							<input type="text" id="memberZipcode" name="memberZipcode" placeholder="우편번호를 등록해주세요." readonly/>
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
							<input type="text" id="memberAddr" name="memberAddr" placeholder="기본주소를 등록해주세요." readonly/>
						</form>
					</dd>
					<!-- d-none(#editAddrDetail) -->
					<dd class="input-group" id="editAddrDetail">
						<form>
							<input type="text" id="memberAddrDetail" name="memberAddrDetail" placeholder="상세주소를 등록해주세요."/>
						</form>
					</dd>
				</dl>
			</div>
			<div>
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일 수신</dt>
					<dd class="input-group" id="showAgree">
						<div class="form-check form-check-inline mr-0">
							<label class="form-check-label label-disabled">
								<input class="form-check-input" type="checkbox" disabled/>
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
									<input class="form-check-input" type="checkbox" id="memberAgree" name="memberAgree"/>
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
		<div class="mt-4 p-4 security-wrap">
			<h5 class="mb-4 font-weight-bold">계정 보안</h5>
			<div>
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>
					<dd class="input-group" id="showPassword">
						<div>
							<span class="inner-text"></span>
						</div>
						<button class="btn btn-outline-primary btn-sm" id="showPasswordBtn"></button>
					</dd>
					<!-- d-none(#editPassword) -->
					<dd class="input-group" id="editPassword">
						<form>
							<input type="password" id="memberPassword" name="memberPassword" placeholder="********"/>
						</form>
						<div>
							<button class="btn btn-outline-primary btn-sm" type="button" id="updatePasswordBtn">확인</button>
							<button class="btn btn-outline-secondary btn-sm" type="button" id="resetPasswordBtn">취소</button>
						</div>
					</dd>
				</dl>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	const defaultMemberImageUrl = "${contextPath}/resources/images/img_profile.png";

	memberService.getMemberInfo(function(result) {
		let memberInfo = result.data;
		if (memberInfo.memberImageUrl != "") {
			$("#memberThumbImage").attr("src", memberInfo.memberImageUrl);
		} else {
			$("#memberThumbImage").attr("src", defaultMemberImageUrl);
		}
		
		$("#memberNickname").attr("value", memberInfo.memberNickname);
		$("#showNickname span").text(memberInfo.memberNickname);
		
		$("#memberEmail").attr("value", memberInfo.memberEmail);
		$("#showEmail span").text(memberInfo.memberEmail);
		
		$("#memberZipcode").attr("value", memberInfo.memberZipcode);
		$("#memberAddr").attr("value", memberInfo.memberAddr);
		$("#memberAddrDetail").attr("value", memberInfo.memberAddrDetail);
		
		if (memberInfo.memberZipcode != "") {
			$("#showZipcode span").text(memberInfo.memberZipcode);
			$("#showAddrBtn").html("수정");
		} else {
			$("#showZipcode span").text("우편번호를 등록해주세요.");
			$("#showAddrBtn").html("등록");
		}
		
		if (memberInfo.memberAddr != "") {
			$("#showAddr span").text(memberInfo.memberAddr);
		} else {
			$("#showAddr span").text("기본주소를 등록해주세요.");
		}
	
		if (memberInfo.memberAddrDetail != "") {
			$("#showAddrDetail span").text(memberInfo.memberAddrDetail);
		} else {
			$("#showAddrDetail span").text("상세주소를 등록해주세요.");
		}
		
		$("#showAgree input[type='checkbox']").prop("checked", memberInfo.memberAgree);
		$("#memberAgree").prop("checked", memberInfo.memberAgree);
		
		if (memberInfo.hasPassword == true) {
			$("#showPassword span").text("********");
			$("#showPasswordBtn").html("수정");
		} else {
			$("#showPassword span").text("비밀번호를 등록해주세요.");
			$("#showPasswordBtn").html("등록");
		};
	});

	$(function() {
		$("#updateMemberImageBtn").on("click", function() {
			$("#memberImage").click();
		});
		
		// prevent stack overflow (Uncaught RangeError: Maximum call stack size exceeded)
		// since #memberImage(file) is a child element of #updateMemberImageBtn, event propagation occurs
		$("#memberImage").on("click", function(e) {
			e.stopPropagation();
		})
		
		$("#memberImage").on("change", function() {
			// jQuery object --> javaScript DOM object 
			// let $memberImage = $(this)[0];
			const formData = new FormData();
			formData.append("file", this.files[0]);
			
			// initialize the file input, but not with null
			//this.value = "";
			$("#memberImageForm")[0].reset();

			memberService.updateMemberImage(formData, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberThumbImage, #navMemberImage").attr("src", result.data.memberImageUrl);
				});
			});
		});
		
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
		
		$("#updateNicknameBtn").on("click", function() {
			let obj = {
				memberNickname : $("#memberNickname").val()
			};
			
			memberService.updateNickname(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberNickname").attr("value", result.data.memberNickname);
					$("#showNickname span").text(result.data.memberNickname);
					$("#navMemberNickname").text(result.data.memberNickname);
					$("#resetNicknameBtn").click();
				});
			});
		});
		
		$(document).on("keydown", "#memberNickname", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$("#updateNicknameBtn").click();
			}
		});

		$("#updateEmailBtn").on("click", function() {
			let obj = {
				memberEmail : $("#memberEmail").val(),
				authCode : $("#authCode").val()
			};
			
			memberService.updateEmail(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberEmail").attr("value", result.data.memberEmail);
					$("#showEmail span").text(result.data.memberEmail);
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
				e.preventDefault();
				$("#updateEmailBtn").click();
			}
		});
		
		$("#updateAddrBtn").on("click", function() {
			let obj = {
				memberZipcode : $("#memberZipcode").val(),
				memberAddr : $("#memberAddr").val(),
				memberAddrDetail : $("#memberAddrDetail").val()
			};
			
			memberService.updateAddr(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberZipcode").attr("value", result.data.memberZipcode);
					$("#showZipcode span").text(result.data.memberZipcode);
					$("#memberAddr").attr("value", result.data.memberAddr);
					$("#showAddr span").text(result.data.memberAddr);
					$("#memberAddrDetail").attr("value", result.data.memberAddrDetail);
					$("#showAddrDetail span").text(result.data.memberAddrDetail);
					$("#showAddrBtn").html("수정");
					$("#resetAddrBtn").click();
				});
			});
		});
		
		$(document).on("click", "#searchAddrBtn, #memberZipcode, #memberAddr", function() {
			execPostcode();
		});
		
		$(document).on("keydown", "#memberAddrDetail", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$("#updateAddrBtn").click();
			}
		});

		$("#updateAgreeBtn").on("click", function() {
			let obj = {
				memberAgree : $("#memberAgree").prop("checked")	
			};
			
			memberService.updateAgree(obj, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					$("#memberAgree").prop("checked", result.data.memberAgree);
					$("#showAgree input[type='checkbox']").prop("checked", result.data.memberAgree);
					$("#resetAgreeBtn").click();
				});
			});
		});

		$("#updatePasswordBtn").on("click", function() {
			let obj = {
				currentPassword : $("#currentPassword").val(),
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			memberService.updatePassword(obj, function(result) {
				alert(result.message);
				memberService.getMemberDetails(function(result) {
					$("#resetPasswordBtn").click();
				});
			});
		});
		
		$("#showNicknameBtn").on("click", function() {
			$("#showNickname").css("display", "none");
			$("#editNickname, #editNickname > div").css("display", "flex");
		});
		
		$("#resetNicknameBtn").on("click", function() {
			$(this).closest("dl").find(".error").remove(); // $("#memberNickname\\.errors").remove();
			$("#showNickname").css("display", "flex");
			$("#editNickname, #editNickname > div").css("display", "none");
			$("#editNickname").find("form")[0].reset();
		});
		
		$("#showEmailBtn").on("click", function() {
			$("#showEmail").css("display", "none");
			$("#editEmail, #editEmail > div, #editAuthCode").css("display", "flex");
		});
		
		$("#resetEmailBtn").on("click", function() {
			$(this).closest("dl").find(".error, .success").remove(); // $("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
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
			$(this).closest("dl").find(".error").remove(); // $("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
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
			$(this).closest("dl").find(".error").remove(); // $("#memberAgree\\.errors").remove();
			$("#showAgree").css("display", "flex");
			$("#editAgree, #editAgree > div").css("display", "none");
			$("#editAgree").find("form")[0].reset();
		});
		
		$("#showPasswordBtn").on("click", function() {
			$("#showPassword").css("display", "none");
			$("#editPassword, #editPassword > div").css("display", "flex");
		});
		
		$("#resetPasswordBtn").on("click", function() {
			$(this).closest("dl").find(".error").remove(); // $("#showPassword\\.errors").remove();
			$("#showPassword").css("display", "flex");
			$("#editPassword, #editPassword > div").css("display", "none");
			$("#editPassword").find("form")[0].reset();
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
                document.getElementById('memberAddr').value = addr;
                document.getElementById('memberAddrDetail').value = '';
                
                // 우편번호와 주소에 해당하는 에러메세지를 제거한다.
                let zipcodeErrorElement = document.getElementById('memberZipcode.errors');
                let addrErrorElement = document.getElementById('memberAddr.errors');
                
                if (zipcodeErrorElement != null) {
                	zipcodeErrorElement.remove();
                }
                
                if (addrErrorElement != null) {
                	addrErrorElement.remove();
                }

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("memberAddrDetail").focus();
            }
        }).open();
    }
</script>
</body>
</html>