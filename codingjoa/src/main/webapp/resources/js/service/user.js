let userService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getAccount(callback) {
		console.log("## getAccount");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/users/me`,
			dataType : "json",
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
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/users/me/auth-code/send`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function updateNickname(obj, callback) {
		console.log("## updateNickname");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/users/me/nickname`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#nickname\\.errors").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#nickname\\.errors").remove();
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function updateEmail(obj, callback) {
		console.log("## updateEmail");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/users/me/email`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#email\\.errors, #authCode\\.errors, .success").remove();
				handleValidationError(parseError(jqXHR));
			}
		});
	}

	function updateAddr(obj, callback) {
		console.log("## updateAddr");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/users/me/address`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#zipcode\\.errors, #addr\\.errors, #addrDetail\\.errors").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#zipcode\\.errors, #addr\\.errors, #addrDetail\\.errors").remove();
				handleValidationError(parseError(jqXHR));
			}
		});
	}

	function updateAgree(obj, callback) {
		console.log("## updateAgree");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/users/me/agree`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				$("#agree\\.errors").remove();
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				$("#agree\\.errors").remove();
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function saveImageWithUpload(formData, callback) {
		console.log("## saveImageWithUpload");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/users/me/image`,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function updatePassword(obj, callback) {
		console.log("## updatePassword");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/users/me/password`,
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
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function savePassword(obj, callback) {
		console.log("## savePassword");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/users/me/password`,
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