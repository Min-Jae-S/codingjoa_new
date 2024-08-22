function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let memberService = (function() {
	const contextPath = getContextPath();

	function sendAuthCodeForJoin(obj, callback) {
		console.log("## sendAuthCodeForJoin");
		let url = contextPath + "/api/member/join/auth";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					//handleMemberError(errorResponse);
					let details = errorResponse.details;
					if (details.length > 0) {
						$.each(details, function(index, item) {
							$("#authCode").closest("div")
								.after("<span id='" + item.field + ".errors' class='error'>" + item.message + "</span>");
						});
					} else {
						alert(errorResponse.message);
					}
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function sendAuthCodeForUpdate(obj, callback) {
		console.log("## sendAuthCodeForUpdate");
		let url = contextPath + "/api/member/update-email/auth";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function updateMemberImage(formData, callback) {
		console.log("## updateMemberImage");
		let url = contextPath + "/api/member/account/image";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			processData: false,
		    contentType: false,
			data : formData,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleImageError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function updateNickname(obj, callback) {
		console.log("## updateNickname");
		let url = contextPath + "/api/member/account/nickname";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberNickname\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberNickname\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function updateEmail(obj, callback) {
		console.log("## updateEmail");
		let url = contextPath + "/api/member/account/email";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}

	function updateAddr(obj, callback) {
		console.log("## updateAddr");
		let url = contextPath + "/api/member/account/address";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}

	function updateAgree(obj, callback) {
		console.log("## updateAgree");
		let url = contextPath + "/api/member/account/agree";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#memberAgree\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#memberAgree\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function updatePassword(obj, callback) {
		console.log("## updatePassword");
		let url = contextPath + "/api/member/account/password";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#currentPassword\\.errors, #newPassword\\.errors, #confirmPassword\\.errors").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#currentPassword\\.errors, #newPassword\\.errors, #confirmPassword\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function savePassword(obj, callback) {
		console.log("## saveePassword");
		let url = contextPath + "/api/member/account/password";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#newPassword\\.errors, #confirmPassword\\.errors").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#newPassword\\.errors, #confirmPassword\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function getMemberInfo(callback) {
		console.log("## getMemberInfo");
		let url = contextPath + "/api/member/account";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});	
	}
	
	function confirmPassword(obj, callback) {
		console.log("## confirmPassword");
		let url = contextPath + "/api/member/confirm/password";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				$(".error").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function findAccount(obj, callback) {
		console.log("## findAccount");
		let url = contextPath + "/api/member/find/account";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$(".error").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function findPassword(obj, callback) {
		console.log("## findPassword");
		let url = contextPath + "/api/member/find/password";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$(".error").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function resetPassword(key, obj, callback) {
		console.log("## resetPassword");
		let url = contextPath + "/api/member/reset/password?key=" + key;
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "PUT",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$(".error").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}

	return {
		sendAuthCodeForJoin:sendAuthCodeForJoin,
		sendAuthCodeForUpdate:sendAuthCodeForUpdate,
		updateMemberImage:updateMemberImage,
		updateNickname:updateNickname,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		updatePassword:updatePassword,
		savePassword:savePassword,
		getMemberInfo:getMemberInfo,
		confirmPassword:confirmPassword,
		findAccount:findAccount,
		findPassword:findPassword,
		resetPassword:resetPassword
	};
	
})();