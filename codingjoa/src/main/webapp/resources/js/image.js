console.log("## Image service ready - image.js");

let imageService = (function() {
	const contextPath = getContextPath();

	function uploadMemberImage(formData, callback) {
		console.log("## Upload Member Image");
		let url = contextPath + "/api/upload/member-image";
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
	
	function getCurrentMemberImage(callback) {
		console.log("## Get Current Member Image");
		let url = contextPath + "/api/member/image/current";
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
		uploadMemberImage:uploadMemberImage,
		getCurrentMemberImage:getCurrentMemberImage
	};
	
})();