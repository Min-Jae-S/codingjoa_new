let categoryService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function getCategoryListByParent(category, callback) {
		console.log("## getCategoryListByParent");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/category/${category}`,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
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
					alert(errorResponse.message);
				} else {
					alert("## parsing error");
				}
			}
		});
		
//		$.getJSON(`${contextPath}/api/category/${category}`, function(result) {
//			console.log("%c> SUCCESS", "color:green");
//			console.log(JSON.stringify(result, null, 2));
//			callback(result);
//		})
//		.fail(function(jqXHR, textStatus, error) {
//			console.log("%c> ERROR", "color:red");
//			let errorResponse = parseError(jqXHR);
//			if (errorResponse != null) {
//				alert(errorResponse.message);
//			} else {
//				alert("## parsing error");
//			}
//		})
	}
	
	return {
		getCategoryListByParent:getCategoryListByParent
	};
	
})();