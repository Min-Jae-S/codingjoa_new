let userService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function sendAuthCodeForJoin(obj, callback) {
		console.log("## sendAuthCodeForJoin");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/join/auth`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
 			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					//handleUserError(errorResponse);
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
	
	function sendAuthCodeForEmailUpdate(obj, callback) {
		console.log("## sendAuthCodeForEmailUpdate");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/account/email/auth`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updateUserImage(formData, callback) {
		console.log("## updateUserImage");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/image`,
			processData: false,
		    contentType: false,
			data : formData,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updateNickname(obj, callback) {
		console.log("## updateNickname");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/nickname`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#nickname\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#nickname\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updateEmail(obj, callback) {
		console.log("## updateEmail");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/email`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}

	function updateAddr(obj, callback) {
		console.log("## updateAddr");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/address`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#zipcode\\.errors, #addr\\.errors, #addrDetail\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#zipcode\\.errors, #addr\\.errors, #addrDetail\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}

	function updateAgree(obj, callback) {
		console.log("## updateAgree");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/agree`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				$("#agree\\.errors").remove();
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#agree\\.errors").remove();
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function updatePassword(obj, callback) {
		console.log("## updatePassword");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/account/password`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function savePassword(obj, callback) {
		console.log("## saveePassword");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/account/password`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function getUserInfo(callback) {
		console.log("## getUserInfo");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/user/account`,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});	
	}
	
	function confirmPassword(obj, callback) {
		console.log("## confirmPassword");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/confirm/password`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function findAccount(obj, callback) {
		console.log("## findAccount");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/find/account"`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function findPassword(obj, callback) {
		console.log("## findPassword");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/find/password`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function resetPassword(key, obj, callback) {
		console.log("## resetPassword");
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/user/reset/password?key=${key}`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
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
					handleUserError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}

	return {
		sendAuthCodeForJoin:sendAuthCodeForJoin,
		sendAuthCodeForEmailUpdate:sendAuthCodeForEmailUpdate,
		updateUserImage:updateUserImage,
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