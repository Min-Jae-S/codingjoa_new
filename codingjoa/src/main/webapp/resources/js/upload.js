console.log("## Upload service ready - upload.js");

let uploadService = (function() {
	const contextPath = getContextPath();

	function uploadProfileImage(data, callback) {
		console.log("## Upload Profile Image");
		let url = contextPath + "/api/upload/profile-image";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			processData: false,
		    contentType: false,
			data : data,
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
					handleUploadError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	return {
		uploadProfileImage:uploadProfileImage
	};
	
})();