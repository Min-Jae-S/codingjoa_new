function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let imageService = (function() {
	const contextPath = getContextPath();

	function uploadMemberImage(formData, callback) {
		console.log("## uploadMemberImage");
		let url = contextPath + "/api/member/image";
		console.log("> URL = '%s'", url);
		
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
					alert("## parsing error");
				}
			}
		});
	}

	return { 
		uploadMemberImage:uploadMemberImage 
	};
	
})();