console.log("## Member service ready - member.js");

let memberService = (function() {
	const contextPath = getContextPath();

	function sendAuthCodeForJoin(obj, callback) {
		console.log("## Send Auth Code : Join");
		let url = contextPath + "/api/member/join/auth";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function sendAuthCodeForUpdate(obj, callback) {
		console.log("## Send Auth Code : Update Email");
		let url = contextPath + "/api/member/update-email/auth";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updateEmail(obj, callback) {
		console.log("## Update Email");
		let url = contextPath + "/api/member/email";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}

	function updateAddr(obj, callback) {
		console.log("## Update Addr");
		let url = contextPath + "/api/member/address";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}

	function updateAgree(obj, callback) {
		console.log("## Update Agree");
		let url = contextPath + "/api/member/agree";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}

	function getMemberDetails(callback) {
		console.log("## Get Member Details");
		let url = contextPath + "/api/member/details";
		console.log("> url = '%s'", url);
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function checkPassword(obj, callback) {
		console.log("## Check Password");
		let url = contextPath + "/api/member/check/password";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updatePassword(obj, callback) {
		console.log("## Update Password");
		let url = contextPath + "/api/member/password";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function findAccount(obj, callback) {
		console.log("## Find Account");
		let url = contextPath + "/api/member/find/account";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function findPassword(obj, callback) {
		console.log("## Find Password");
		let url = contextPath + "/api/member/find/password";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function resetPassword(key, obj, callback) {
		console.log("## Reset Password");
		let url = contextPath + "/api/member/reset/password?key=" + key;
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
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
					alert("## Parsing Error");
				}
			}
		});
	}

	return {
		sendAuthCodeForJoin:sendAuthCodeForJoin,
		sendAuthCodeForUpdate:sendAuthCodeForUpdate,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		getMemberDetails:getMemberDetails,
		checkPassword:checkPassword,
		updatePassword:updatePassword,
		findAccount:findAccount,
		findPassword:findPassword,
		resetPassword:resetPassword
	};
	
})();