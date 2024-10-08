console.log("## Member service ready - member.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

function parseError(jqXHR) {
	try {
		let errorResponse = JSON.parse(jqXHR.responseText);
		console.log(JSON.stringify(errorResponse, null, 2));
		return errorResponse;
	} catch(e) {
		alert("## Parsing Error");
		return null;
	}
}

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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#authCode").closest("div")
								.after("<span id='" + errorField + ".errors' class='error'>" + errorMessage + "</span>");
						});
					} else {
						$("#authCode").closest("div")
							.after("<span id='memberEmail.errors' class='error'>" + errorResponse.errorMessage + "</span>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#memberEmail").closest("dd")
							.after("<dd id='memberEmail.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						alert(errorResponse.errorMessage);
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						alert(errorResponse.errorMessage);
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$("#memberAgree\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						alert(errorResponse.errorMessage);
					}
				}
			}
		});
	}

	function getCurrentMember(callback) {
		console.log("## Get Current Member");
		let url = contextPath + "/api/member/current-member";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					alert(errorResponse.errorMessage);
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
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#memberPassword").closest("dd")
							.after("<dd id='memberPassword.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				$(".error").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#memberPassword").closest("dd")
							.after("<dd id='memberPassword.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#memberEmail").closest("dd")
							.after("<dd id='memberEmail.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#editId").after("<dd id='memberId.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
						$("#editEmail").after("<dd id='memberEmail.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let errorMap = errorResponse.errorMap;
					if (errorMap != null) {
						$.each(errorMap, function(errorField, errorMessage) {
							$("#" + errorField).closest("dd")
								.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
						});
					} else {
						$("#memberPassword").closest("dd")
							.after("<dd id='#memberPassword.errors' class='error'>" + errorResponse.errorMessage + "</dd>");
					}
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
		getCurrentMember:getCurrentMember,
		checkPassword:checkPassword,
		updatePassword:updatePassword,
		findAccount:findAccount,
		findPassword:findPassword,
		resetPassword:resetPassword
	};
	
})();