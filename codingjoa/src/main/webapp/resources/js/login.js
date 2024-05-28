function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let loginService = (function() {
	const contextPath = getContextPath();

	function login(formData, callback) {
		console.log("## login");
		let url = contextPath + "/api/login";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(formData),
			contentType : "application/json; charset=utf-8",
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
					handleLoginError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}

	return { 
		login:login 
	};
	
})();