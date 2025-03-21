let mainService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function sendAuthCodeForJoin(obj, callback) {
		console.log("## sendAuthCodeForJoin");
		console.log(JSON.stringify(obj));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/join/auth-code/send`,
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
					let details = errorResponse.details;
					if (details.length > 0) {
						$.each(details, function(index, item) {
							$("#authCode").closest("div").after(`<span class='error' id='${item.field}.errors'>${item.message}</span>`);
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

	function sendPasswordResetLink(obj, callback) {
		console.log("## sendPasswordResetLink");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/password/reset-link/send`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				$(".error").remove();
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
					handleMainError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function resetPassword(key, obj, callback) {
		console.log("## resetPassword");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/password/reset?key=${key}`,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
				$(".error").remove();
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
					handleMainError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	/*function confirmPassword(obj, callback) {
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
				alert("## parsing error");
			}
		}
	});
}*/

	return {
		sendAuthCodeForJoin:sendAuthCodeForJoin,
		sendPasswordResetLink:sendPasswordResetLink,
		resetPassword:resetPassword
	};
	
})();