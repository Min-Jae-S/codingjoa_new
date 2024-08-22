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
	.form-wrap input[type="text"], 
	.form-wrap input[type="password"] {
		border: none;
		flex-grow: 1;
		padding: 5px 0 5px 7px;
		font-size: 14px;
	}
	
	.form-wrap .form-check {
		flex-grow: 1;
	}
	
	input[type="text"]:focus, input[type="password"]:focus, input[type="checkbox"]:focus {
		outline: none;
	}
	
	.inner-text {
		border-radius: 0.5rem;
		padding: 5px 0 5px 7px;
		font-size: 14px;
	}
	
	label {
		border-radius: 0.5rem;
		padding-left: 7px;
	}
	
	.inner-text:not(.form-wrap .inner-text), .label-disabled {
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
	
	div.form-wrap dd.input-group {
		padding: 13px 0 5px 0;
		border-bottom: 1px solid #868e96;
	}
	
	div.form-wrap form {
		width: 100%;
		overflow: hidden;
	}
	
	div.show-wrap {
		overflow: hidden;
	}
	
	div.show-wrap dd {
		padding: 13px 0 5px 0;
		border-bottom: 1px solid #dee2e6;
	}
	
	div.show-wrap dd > div, .inner-text, label {
		display: flex;
		flex: 1 1 auto;
		align-items: center;
	}
	
	#imageForm {
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
						<form id="imageForm">
							<input type="file" id="memberImage" name="memberImage"/>
						</form>
					</button>
				</div>
				<div class="w-100 pt-2">
					<dl class="form-group">
						<dt><i class="fa-solid fa-check mr-2"></i>닉네임</dt>
						<div class="show-wrap">
							<dd class="input-group" id="showNickname">
								<div>
									<span class="inner-text"></span>
								</div>
								<button type="button" class="btn btn-outline-primary btn-sm">수정</button>
							</dd>
						</div>
						<div class="form-wrap d-none">
							<form id="nicknameForm">
								<dd class="input-group">
									<input type="text" id="memberNickname" name="memberNickname" placeholder="닉네임을 입력해주세요."/>
									<div>
										<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
										<button type="reset" class="btn btn-outline-secondary btn-sm">취소</button>
									</div>
								</dd>
							</form>
						</div>
					</dl>
				</div>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일</dt>
					<div class="show-wrap">
						<dd class="input-group" id="showEmail">
							<div>
								<span class="inner-text"></span>
							</div>
							<button type="button" class="btn btn-outline-primary btn-sm">수정</button>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="emailForm">
							<dd class="input-group">
								<input type="text" id="memberEmail" name="memberEmail" placeholder="이메일을 입력해주세요."/>
								<div>
									<button type="button" class="btn btn-warning btn-sm" id="sendAuthCodeBtn">인증코드 받기</button>
									<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
									<button type="reset" class="btn btn-outline-secondary btn-sm">취소</button>
								</div>
							</dd>
							<dd class="input-group" id="editAuthCode">
								<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력해주세요."/>
							</dd>
						</form>
					</div>
				</dl>
			</div>
			<div class="mb-5">
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>주소</dt>
					<div class="show-wrap">
						<dd class="input-group" id="showZipcode">
							<div>
								<span class="inner-text"></span>
							</div>
							<button type="button" class="btn btn-outline-primary btn-sm"></button>
						</dd>
						<dd class="input-group" id="showAddr">
							<span class="inner-text"></span>
						</dd>
						<dd class="input-group" id="showAddrDetail">
							<span class="inner-text"></span>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="addrForm">
							<dd class="input-group">
								<input type="text" id="memberZipcode" name="memberZipcode" placeholder="우편번호를 등록해주세요." readonly/>
								<div>
									<button type="button" class="btn btn-warning btn-sm" id="searchAddrBtn">주소 찾기</button>
									<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
									<button type="reset" class="btn btn-outline-secondary btn-sm">취소</button>
								</div>
							</dd>
							<dd class="input-group">
								<input type="text" id="memberAddr" name="memberAddr" placeholder="기본주소를 등록해주세요." readonly/>
							</dd>
							<dd class="input-group">
								<input type="text" id="memberAddrDetail" name="memberAddrDetail" placeholder="상세주소를 등록해주세요."/>
							</dd>
						</form>
					</div>
				</dl>
			</div>
			<div>
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>이메일 수신</dt>
					<div class="show-wrap">
						<dd class="input-group" id="showAgree">
							<div class="form-check form-check-inline mr-0">
								<label class="form-check-label label-disabled">
									<input type="checkbox" class="form-check-input" disabled/>
									<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
								</label>
							</div>
							<button type="button" class="btn btn-outline-primary btn-sm">수정</button>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="agreeForm">
							<dd class="input-group">
								<div class="form-check form-check-inline">
									<label class="form-check-label">
										<input type="checkbox" class="form-check-input" id="memberAgree" name="memberAgree"/>
										<span class="inner-text">이메일 광고 수신에 동의합니다.</span>
									</label>
								</div>
								<div>
									<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
									<button type="reset" class="btn btn-outline-secondary btn-sm" >취소</button>
								</div>
							</dd>
						</form>
					</div>
				</dl>
			</div>
		</div>
		<div class="mt-4 p-4 security-wrap">
			<!--------------------------------------------------------------------------------->
			<!----   paswordForm by createPasswordSaveForm(), createPasswordChangeForm()   ---->
			<!--------------------------------------------------------------------------------->
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
		
		if (memberInfo.memberZipcode != "") {
			$("#memberZipcode").attr("value", memberInfo.memberZipcode);
			$("#showZipcode span").text(memberInfo.memberZipcode);
			$("#showZipcode button").html("수정");
		} else {
			$("#showZipcode span").text("주소를 등록해주세요.");
			$("#showZipcode button").html("등록");
			$("#showAddr, #showAddrDetail").addClass("d-none");
		}
		
		if (memberInfo.memberAddr != "") {
			$("#memberAddr").attr("value", memberInfo.memberAddr);
			$("#showAddr span").text(memberInfo.memberAddr);
		}
		
		if (memberInfo.memberAddrDetail != "") {
			$("#memberAddrDetail").attr("value", memberInfo.memberAddrDetail);
			$("#showAddrDetail span").text(memberInfo.memberAddrDetail);
		}
		
		if (memberInfo.memberAgree) {
			$("#memberAgree").attr("checked", "checked");
			$("#showAgree input[type='checkbox']").attr("checked", "checked");
		} else {
			$("#memberAgree").removeAttr("checked");
			$("#showAgree input[type='checkbox']").removeAttr("checked");
		}
		
		if (memberInfo.hasPassword) {
			$("div.security-wrap").html(createPasswordChangeForm());
		} else {
			$("div.security-wrap").html(createPasswordSaveForm());
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
			$("#imageForm")[0].reset();

			memberService.updateMemberImage(formData, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberThumbImage, #navMemberImage").attr("src", result.data.memberImageUrl);
				});
			});
		});
		
		$("#nicknameForm").on("submit", function(e) {
			e.preventDefault();
			let obj = {
				memberNickname : $("#memberNickname").val()
			};
			
			memberService.updateNickname(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberNickname").attr("value", result.data.memberNickname);
					$("#showNickname span").text(result.data.memberNickname);
					$("#navMemberNickname").text(result.data.memberNickname);
					$("#nicknameForm button[type='reset']").click();
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
		
		$("#memberEmail").on("keydown", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$("#sendAuthCodeBtn").click();
			}
		});

		$("#emailForm").on("submit", function(e) {
			e.preventDefault();
			let obj = {
				memberEmail : $("#memberEmail").val(),
				authCode : $("#authCode").val()
			};
			
			memberService.updateEmail(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					$("#memberEmail").attr("value", result.data.memberEmail);
					$("#showEmail span").text(result.data.memberEmail);
					$("#emailForm button[type='reset']").click();
				});
			});
		});
		
		$("#addrForm").on("submit", function(e) {
			e.preventDefault();
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
					$("#showZipcode button").html("수정");
					$("#showAddr, #showAddrDetail").removeClass("d-none");
					$("#addrForm button[type='reset']").click();
				});
			});
		});

		$("#agreeForm").on("submit", function(e) {
			e.preventDefault();
			let obj = {
				memberAgree : $("#memberAgree").prop("checked")	
			};
			
			memberService.updateAgree(obj, function(result) {
				alert(result.message);
				memberService.getMemberInfo(function(result) {
					if (result.data.memberAgree) {
						$("#memberAgree").attr("checked", "checked");
						$("#showAgree input[type='checkbox']").attr("checked", "checked");
					} else {
						$("#memberAgree").removeAttr("checked");
						$("#showAgree input[type='checkbox']").removeAttr("checked");
					}
					$("#agreeForm button[type='reset']").click();
				});
			});
		});

		$(document).on("submit", "#passwordChangeForm", function(e) {
			e.preventDefault();
			let obj = {
				currentPassword : $("#currentPassword").val(),
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			memberService.updatePassword(obj, function(result) {
				alert(result.message);
				$("#passwordChangeForm button[type='reset']").click();
			});
		});

		$(document).on("submit", "#passwordSaveForm", function(e) {
			e.preventDefault();
			let obj = {
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			memberService.savePassword(obj, function(result) {
				alert(result.message);
				$("div.security-wrap").html(createPasswordChangeForm());
			});
		});
		
		$(document).on("click", "#searchAddrBtn, #memberZipcode, #memberAddr", function() {
			execPostcode();
		});
		
		$(document).on("click", "dd[id^='show'] button", function() {
			let $dl = $(this).closest("dl");
			$dl.find("div.show-wrap").addClass("d-none"); 		// $dl.find("div.show-wrap").css("display", "none"); 
			$dl.find("div.form-wrap").removeClass("d-none");	// $dl.find("div.form-wrap").css("display", "flex");
		});

		$(document).on("click", "button[type='reset']", function() {
			let $dl = $(this).closest("dl");
			$dl.find(".error, .success").remove();
			$dl.find("div.show-wrap").removeClass("d-none"); 	// $dl.find("div.show-wrap").css("display", "flex");
			$dl.find("div.form-wrap").addClass("d-none"); 		// $dl.find("div.form-wrap").css("display", "none");
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
    
    function createPasswordChangeForm() {
    	let html = '<h5 class="mb-4 font-weight-bold">계정 보안</h5>';
    	html += '<div>';
    	html += '<dl class="form-group">';
    	html += '<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>';
    	html += '<div class="show-wrap">';
    	html += '<dd class="input-group" id="showPassword">';
    	html += '<div>';
    	html += '<span class="inner-text">********</span>';
    	html += '</div>';
    	html += '<button class="btn btn-outline-primary btn-sm">수정</button>';
    	html += '</dd>';
    	html += '</div>';
    	html += '<div class="form-wrap d-none">';
    	html += '<form id="passwordChangeForm">';
    	html += '<dd class="input-group">';
    	html += '<input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호를 입력해주세요."/>';
    	html += '<div>';
    	html += '<button class="btn btn-outline-primary btn-sm" type="submit">확인</button>';
    	html += '<button class="btn btn-outline-secondary btn-sm" type="reset">취소</button>';
    	html += '</div>';
    	html += '</dd>';
    	html += '<dd class="input-group">';
    	html += '<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요."/>';
    	html += '</dd>';
    	html += '<dd class="input-group">';
    	html += '<input type="password" id="confirmPassword" name="confirmPassword" placeholder="확인 비밀번호를 입력해주세요."/>';
    	html += '</dd>';
    	html += '</form>';
    	html += '</div>';
    	html += '</dl>';
    	html += '</div>';
    	return html;
    }
    
    function createPasswordSaveForm() {
    	let html = '<h5 class="mb-4 font-weight-bold">계정 보안</h5>';
    	html += '<div>';
    	html += '<dl class="form-group">';
    	html += '<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>';
    	html += '<div class="show-wrap">';
    	html += '<dd class="input-group" id="showPassword">';
    	html += '<div>';
    	html += '<span class="inner-text">비밀번호를 등록해주세요.</span>';
    	html += '</div>';
    	html += '<button class="btn btn-outline-primary btn-sm">등록</button>';
    	html += '</dd>';
    	html += '</div>';
    	html += '<div class="form-wrap d-none">';
    	html += '<form id="passwordSaveForm">';
    	html += '<dd class="input-group">';
    	html += '<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요."/>';
    	html += '<div>';
    	html += '<button class="btn btn-outline-primary btn-sm" type="submit">확인</button>';
    	html += '<button class="btn btn-outline-secondary btn-sm" type="reset">취소</button>';
    	html += '</div>';
    	html += '</dd>';
    	html += '<dd class="input-group">';
    	html += '<input type="password" id="confirmPassword" name="confirmPassword" placeholder="확인 비밀번호를 입력해주세요."/>';
    	html += '</dd>';
    	html += '</form>';
    	html += '</div>';
    	html += '</dl>';
    	html += '</div>';
		return html;
    }
</script>
</body>
</html>