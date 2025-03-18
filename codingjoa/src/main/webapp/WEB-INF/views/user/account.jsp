<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>계정 관리 | Codingjoa</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/user.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<style>
	.form-wrap input[type="text"], .form-wrap input[type="password"] {
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
		/* overflow: hidden; */
	}
	
	div.show-wrap {
		/* overflow: hidden; */
	}
	
	div.show-wrap dd {
		padding: 13px 0 5px 0;
		margin-right: 5px;
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
	
	input[name="zipcode"], input[name="addr"] {
		cursor: pointer;
	}
	
	dl.form-group {
		margin-bottom: 0;
	}
	
	.user-thumb-image {
		width: 85px;
		height: 85px;
		border: 1px solid #dee2e6 !important;
		border-radius: 1rem;
	}	
	
	.user-image-wrap {
		position: relative;
	}
	
	.user-image-btn {
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
	
	.user-image-icon {
		display: inline-block;
		width: 35px;
		height: 35px;
		border: 1px solid white;
		border-radius: 50%;
		background-image: url(../resources/images/img_camera.png);
		background-size: contain;
    	background-repeat: round;
	}
	
	.account-wrap {
		min-width: 620px;
		margin: 0 auto;
	}
	
	.profile-wrap, .security-wrap {
		border: 1px solid rgba(0, 0, 0, .125);
   	 	border-radius: 16px;
   	 	padding: 2.5rem;
   	 	margin-top: 2rem;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container account-container">
	<div class="account-wrap">
		<div class="title-wrap">
			<h3 class="font-weight-bold">계정 관리</h3>
		</div>
		<div class="profile-wrap">
			<h5 class="mb-3 font-weight-bold">계정 정보</h5>
			<div class="mb-5 d-flex">
				<div class="user-image-wrap mr-4">
					<img class="user-thumb-image" id="userThumbImage">
					<button type="button" class="user-image-btn" id="userImageBtn">
						<span class="user-image-icon"></span>
						<form id="imageForm">
							<input type="file" id="userImage" name="userImage"/>
						</form>
					</button>
				</div>
				<div class="w-100 pt-2">
					<dl class="form-group">
						<dt><i class="fa-solid fa-check mr-2"></i>닉네임</dt>
						<div class="show-wrap">
							<dd class="input-group" id="showNickname">
								<div>
									<span class="inner-text">
										<!-- nickname -->
									</span>
								</div>
								<button type="button" class="btn btn-outline-primary btn-sm">수정</button>
							</dd>
						</div>
						<div class="form-wrap d-none">
							<form id="nicknameForm">
								<dd class="input-group">
									<input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력해주세요"/>
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
								<span class="inner-text">
									<!-- email -->
								</span>
							</div>
							<button type="button" class="btn btn-outline-primary btn-sm">수정</button>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="emailForm">
							<dd class="input-group">
								<input type="text" id="email" name="email" placeholder="이메일을 입력해주세요"/>
								<div>
									<button type="button" class="btn btn-warning btn-sm" id="sendAuthCodeBtn">인증코드 받기</button>
									<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
									<button type="reset" class="btn btn-outline-secondary btn-sm">취소</button>
								</div>
							</dd>
							<dd class="input-group" id="editAuthCode">
								<input type="text" id="authCode" name="authCode" placeholder="인증코드를 입력해주세요"/>
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
								<span class="inner-text text-danger">* 주소를 등록해주세요</span>
							</div>
							<button type="button" class="btn btn-outline-primary btn-sm">등록</button>
						</dd>
						<dd class="input-group d-none" id="showAddr">
							<span class="inner-text">
								<!-- addr -->
							</span>
						</dd>
						<dd class="input-group d-none" id="showAddrDetail">
							<span class="inner-text">
								<!-- addrDetail -->
							</span>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="addrForm">
							<dd class="input-group">
								<input type="text" id="zipcode" name="zipcode" placeholder="우편번호를 등록해주세요" readonly/>
								<div>
									<button type="button" class="btn btn-warning btn-sm" id="searchAddrBtn">주소 찾기</button>
									<button type="submit" class="btn btn-outline-primary btn-sm">확인</button>
									<button type="reset" class="btn btn-outline-secondary btn-sm">취소</button>
								</div>
							</dd>
							<dd class="input-group">
								<input type="text" id="addr" name="addr" placeholder="기본주소를 등록해주세요" readonly/>
							</dd>
							<dd class="input-group">
								<input type="text" id="addrDetail" name="addrDetail" placeholder="상세주소를 등록해주세요"/>
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
									<span class="inner-text">이메일 광고 수신에 동의합니다</span>
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
										<input type="checkbox" class="form-check-input" id="agree" name="agree"/>
										<span class="inner-text">이메일 광고 수신에 동의합니다</span>
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
		<div class="security-wrap">
			<h5 class="mb-4 font-weight-bold">계정 보안</h5>
			<div>
				<dl class="form-group">
					<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>
					<div class="show-wrap">
						<dd class="input-group" id="showPassword">
							<div>
								<span class="inner-text text-danger">* 비밀번호를 등록해주세요</span>
							</div>
							<button class="btn btn-outline-primary btn-sm">등록</button>
						</dd>
					</div>
					<div class="form-wrap d-none">
						<form id="passwordSaveForm">
							<dd class="input-group">
								<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요"/>
								<div>
									<button class="btn btn-outline-primary btn-sm" type="submit">확인</button>
									<button class="btn btn-outline-secondary btn-sm" type="reset">취소</button>
								</div>
							</dd>
							<dd class="input-group">
								<input type="password" id="confirmPassword" name="confirmPassword" placeholder="확인 비밀번호를 입력해주세요"/>
							</dd>
						</form>
					</div>
				</dl>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
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
	            document.getElementById('zipcode').value = data.zonecode;
	            document.getElementById('addr').value = addr;
	            document.getElementById('addrDetail').value = '';
	            
	            // 우편번호와 주소에 해당하는 에러메세지를 제거한다.
	            let zipcodeErrorElement = document.getElementById('zipcode.errors');
	            let addrErrorElement = document.getElementById('addr.errors');
	            
	            if (zipcodeErrorElement != null) {
	            	zipcodeErrorElement.remove();
	            }
	            
	            if (addrErrorElement != null) {
	            	addrErrorElement.remove();
	            }
	
	            // 커서를 상세주소 필드로 이동한다.
	            document.getElementById("addrDetail").focus();
	        }
	    }).open();
	}
	
	$(function() {
		userService.getAccount(function(result) {
			// destructuring assignment (구조 분해 할당)
			let { imagePath, nickname, email, zipcode, addr, addrDetail, agree, hasPassword} = result.data;
			
			$("#userThumbImage").attr("src", imagePath ? imagePath : "${contextPath}/resources/images/img_profile.png");
			
			$("#nickname").attr("value", nickname);
			$("#showNickname span").text(nickname);
			
			$("#email").attr("value", email);
			$("#showEmail span").text(email);
			
			if (zipcode) {
				$("#zipcode").attr("value", zipcode);
				$("#showZipcode span").text(zipcode);
			}
			
			if (addr) {
				$("#addr").attr("value", addr);
				$("#showAddr span").text(addr);
			}
			
			if (addrDetail) {
				$("#addrDetail").attr("value", addrDetail);
				$("#showAddrDetail span").text(addrDetail);
			}
			
			if (zipcode && addr && addrDetail) {
				$("#showZipcode button").html("수정");
				$("#showAddr, #showAddrDetail").removeClass("d-none");
			}
			
			$("#agree").attr("checked", agree);
			$("#showAgree input[type='checkbox']").attr("checked", agree);
			
			//$("div.security-wrap").html(hasPassword ? createPasswordChangeForm() : createPasswordSaveForm());
			if (hasPassword) {
				$("div.security-wrap").html(createPasswordChangeForm());
			}
		});
		
		$("#userImageBtn").on("click", function() {
			$("#userImage").click();
		});
		
		// prevent stack overflow (Uncaught RangeError: Maximum call stack size exceeded)
		// since #userImagee(file) is a child element of #userImageBtn, event propagation occurs
		$("#userImage").on("click", function(e) {
			e.stopPropagation();
		});
		
		$("#userImage").on("change", function() {
			// jQuery object --> javaScript DOM object 
			// let $userImage = $(this)[0];
			let formData = new FormData();
			formData.append("file", this.files[0]);
			
			// initialize the file input, but not with null
			//this.value = "";
			$("#imageForm")[0].reset();

			userService.saveImageWithUpload(formData, function(result) {
				alert(result.message);
				userService.getAccount(function(result) {
					$("#navbardDropdown .nav-user-image, #userThumbImage").attr("src", result.data.imagePath);
				});
			});
		});
		
		$("#nicknameForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				nickname : $("#nickname").val()
			};
			
			userService.updateNickname(formData, function(result) {
				alert(result.message);
				userService.getAccount(function(result) {
					let account = result.data;
					$("#nickname").attr("value", account.nickname);
					$("#showNickname span").text(account.nickname);
					$("#navUserNickname").text(account.nickname);
					$("#nicknameForm button[type='reset']").click();
				});
			});
		});
		
		$("#sendAuthCodeBtn").on("click", function() {
			let formData = {
				email : $("#email").val(),
			};
			
			userService.sendAuthCodeForEmailUpdate(formData, function(result) {
				$("#authCode").closest("dd").after("<dd class='success'>" + result.message + "</dd>");
				$("#authCode").val("");
				$("#authCode").focus();
			});
		});
		
		$("#email").on("keydown", function(e) {
			if (e.keyCode == 13) {
				e.preventDefault();
				$("#sendAuthCodeBtn").click();
			}
		});

		$("#emailForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				email : $("#email").val(),
				authCode : $("#authCode").val()
			};
			
			userService.updateEmail(formData, function(result) {
				alert(result.message);
				userService.getAccount(function(result) {
					let account = result.data;
					$("#email").attr("value", account.email);
					$("#showEmail span").text(account.email);
					$("#emailForm button[type='reset']").click();
				});
			});
		});
		
		$("#addrForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				zipcode : $("#zipcode").val(),
				addr : $("#addr").val(),
				addrDetail : $("#addrDetail").val()
			};
			
			userService.updateAddr(formData, function(result) {
				alert(result.message);
				userService.getAccount(function(result) {
					let account = result.data;
					$("#zipcode").attr("value", account.zipcode);
					$("#addr").attr("value", account.addr);
					$("#addrDetail").attr("value", account.addrDetail);
					$("#showZipcode span").text(account.zipcode);
					$("#showAddr span").text(account.addr);
					$("#showAddrDetail span").text(account.addrDetail);
					$("#showZipcode button").html("수정");
					$("#showAddr, #showAddrDetail").removeClass("d-none");
					$("#addrForm button[type='reset']").click();
				});
			});
		});

		$("#agreeForm").on("submit", function(e) {
			e.preventDefault();
			let formData = {
				agree : $("#agree").prop("checked")	
			};
			
			userService.updateAgree(formData, function(result) {
				alert(result.message);
				userService.getAccount(function(result) {
					let account = result.data;
					$("#agree").attr("checked", account.agree);
					$("#showAgree input[type='checkbox']").attr("checked", account.agree);
					$("#agreeForm button[type='reset']").click();
				});
			});
		});

		$(document).on("submit", "#passwordChangeForm", function(e) {
			e.preventDefault();
			let formData = {
				currentPassword : $("#currentPassword").val(),
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			userService.updatePassword(formData, function(result) {
				alert(result.message);
				$("#passwordChangeForm button[type='reset']").click();
			});
		});

		$(document).on("submit", "#passwordSaveForm", function(e) {
			e.preventDefault();
			let formData = {
				newPassword : $("#newPassword").val(),
				confirmPassword : $("#confirmPassword").val()
			};
			
			userService.savePassword(formData, function(result) {
				alert(result.message);
				$("div.security-wrap").html(createPasswordChangeForm());
			});
		});
		
		$("#searchAddrBtn, #zipcode, #addr").on("click", function() {
			execPostcode();
		});
		
		$("#zipcode, #addr").on("focus", function() {
			$(this).blur();
		});
		
		$(document).on("click", "dd[id^='show'] button", function() {
			let $dl = $(this).closest("dl");
			$dl.find("div.show-wrap").addClass("d-none");
			$dl.find("div.form-wrap").removeClass("d-none");
		});

		$(document).on("click", "button[type='reset']", function() {
			let $dl = $(this).closest("dl");
			$dl.find(".error, .success").remove();
			$dl.find("div.show-wrap").removeClass("d-none");
			$dl.find("div.form-wrap").addClass("d-none");
		});
	});
</script>
</body>
</html>