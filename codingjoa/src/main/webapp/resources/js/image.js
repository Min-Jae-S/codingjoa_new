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

	function getMemberImageResource(memberImageName, callback) {
		console.log("## Get Member Image Resource");
		let url = contextPath + "/api/member/images/" + memberImageName;
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			xhr : function() {
				let xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function() {
                    if (xhr.readyState == XMLHttpRequest.HEADERS_RECEIVED) {
                        if (xhr.status == 200) {
                            xhr.responseType = "blob";
                        } 
                    }
                };
                return xhr;
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(result);
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleImageError(errorResponse);
				} else {
					alert("## Parsing Error");
				}
			}
		});	
	}
	
	return {
		uploadMemberImage:uploadMemberImage,
		getMemberImageResource:getMemberImageResource
	};
	
})();