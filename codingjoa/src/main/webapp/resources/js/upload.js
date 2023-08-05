console.log("## Upload service ready - upload.js");

let uploadService = (function() {
	const contextPath = getContextPath();

	function uploadProfileImage(obj, callback) {
		console.log("## Upload Profile Image");
		let url = contextPath + "/api/upload/profile-image";
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
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					let details = errorResponse.details;
					if (details.length > 0) {
						$.each(details, function(index, item) {
							// ...
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
	
	return {
		sendAuthCodeForJoin:sendAuthCodeForJoin,
	};
	
})();