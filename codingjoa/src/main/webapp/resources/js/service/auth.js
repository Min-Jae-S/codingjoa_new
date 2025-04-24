let authService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function sendAuthCodeForJoin(obj, callback) {
		console.log("## sendAuthCodeForJoin");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/auth/join/auth-code/send`,
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
				let errorResponse = parseError(jqXHR);
				let details = errorResponse.details;
				if (details.length > 0) {
					$.each(details, function(index, item) {
						$("#authCode").closest("div").after(`<span class='error' id='${item.field}.errors'>${item.message}</span>`);
					});
				} else {
					alert(errorResponse.message);
				}
			}
		});
	}

	function sendPasswordResetLink(obj, callback) {
		console.log("## sendPasswordResetLink");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/auth/password/reset-link/send`,
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
				handleValidationError(parseError(jqXHR));
			}
		});
	}
	
	function resetPassword(obj, callback) {
		console.log("## resetPassword");
		console.log(JSON.stringify(obj, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/auth/password/reset`,
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
				handleValidationError(parseError(jqXHR));
			}
		});
	}

	function login(formData, callback) {
		console.log("## login");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/auth/login`,
			data : JSON.stringify(formData),
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
				handleLoginError(parseError(jqXHR));
			}
		});
	}
	
	function logout(callback) {
		console.log("## logout");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/auth/logout`,
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
	
	return { 
		sendAuthCodeForJoin:sendAuthCodeForJoin,
		sendPasswordResetLink:sendPasswordResetLink,
		resetPassword:resetPassword,
		login:login,
		logout:logout
	};
	
})();