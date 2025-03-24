let userService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getAccount(callback) {
		console.log("## getAccount");
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
				handleError(parseError(jqXHR));
			}
		});	
	}
	
	function sendAuthCodeForEmailUpdate(obj, callback) {
		console.log("## sendAuthCodeForEmailUpdate");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/account/email/auth-code/send`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				$("#email\\.errors, #authCode\\.errors, .success").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
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
				$("#nickname\\.errors").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
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
				$("#email\\.errors, #authCode\\.errors, .success").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
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
				$("#zipcode\\.errors, #addr\\.errors, #addrDetail\\.errors").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
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
				$("#agree\\.errors").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function saveImageWithUpload(formData, callback) {
		console.log("## saveImageWithUpload");
		$.ajax({
			type : "POST",
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
				handleError(parseError(jqXHR));
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
				$("#currentPassword\\.errors, #newPassword\\.errors, #confirmPassword\\.errors").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function savePassword(obj, callback) {
		console.log("## savePassword");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/user/account/password`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				$("#newPassword\\.errors, #confirmPassword\\.errors").remove();
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleValidationError(parseError(jqXHR));
			}
		});
	}

	return {
		getAccount:getAccount,
		sendAuthCodeForEmailUpdate:sendAuthCodeForEmailUpdate,
		updateNickname:updateNickname,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		saveImageWithUpload:saveImageWithUpload,
		updatePassword:updatePassword,
		savePassword:savePassword
	};
	
})();