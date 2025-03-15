let authenticationService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function login(formData, continueUrl, callback) {
		console.log("## login");
		console.log(formData);
		
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/login?continue=${continueUrl}`,
			data : JSON.stringify(formData),
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
					handleLoginError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	return { 
		login:login
	};
	
})();