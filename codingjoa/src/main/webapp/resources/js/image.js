console.log("## Image service ready - image.js");

let imageService = (function() {
	const contextPath = getContextPath();

	function uploadProfileImage(formData, callback) {
		console.log("## Upload Profile Image");
		let url = contextPath + "/api/upload/profile-image";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
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
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleUploadError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	function getCurrentProfileImage(callback) {
		console.log("## Get Current Profile Image");
		let url = "${contextPath}/api/profile/current";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			//dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleMemberError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});
	}
	
	return {
		uploadProfileImage:uploadProfileImage,
		getCurrentProfileImage:getCurrentProfileImage
	};
	
})();