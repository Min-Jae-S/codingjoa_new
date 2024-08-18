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
				let errorResponse = parseError(jqXHR);
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
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

	function getMemberDetails(callback) {
		console.log("## getMemberDetails");
		let url = contextPath + "/api/member/details";
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
	
	function updatePassword(obj, callback) {
		console.log("## updatePassword");
		let url = contextPath + "/api/member/password";
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
				$(".error").remove();
				console.log(JSON.stringify(result, null, 2));
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
		getMemberInfo:getMemberInfo,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		getMemberDetails:getMemberDetails,
		confirmPassword:confirmPassword,
		updatePassword:updatePassword,
		findAccount:findAccount,
		findPassword:findPassword,
		resetPassword:resetPassword
	};
	
})();