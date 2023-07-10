console.log("## Member service ready - member.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let memberService = (function() {

	const contextPath = getContextPath();

	function sendAuthEmail(obj, callback) {
		console.log("## Send Auth Email");
		let url = contextPath + "/api/member/send/auth-email";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$("#memberZipcode\\.errors, #memberAddr\\.errors, #memberAddrDetail\\.errors").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$("#memberAgree\\.errors").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$(".error").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$(".error").remove();
				
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
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
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				$(".error, .success").remove();
				
				if (jqXHR.status == 422) {
					let errorMap = JSON.parse(jqXHR.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
				}
			}
		});
	}

	return {
		sendAuthEmail:sendAuthEmail,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		getCurrentMember:getCurrentMember,
		checkPassword:checkPassword,
		updatePassword:updatePassword,
		findAccount:findAccount
	};
	
})();