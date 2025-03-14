let categoryService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function getCategoriesByParent(code, callback) {
		console.log("## getCategoriesByParent");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/categories/parent/${code}`,
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
					alert("## Parsing Error");
				}
			}
		});
	}
	
	return {
		getCategoriesByParent:getCategoriesByParent
	};
	
})();