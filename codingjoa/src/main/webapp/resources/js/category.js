let categoryService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function getCategoryListByParent(category, callback) {
		console.log("## getCategoryListByParent");
		let url = contextPath + "/api/category/" + category;
		console.log("> URL = '%s'", url);
		
		$.getJSON(url, function(result) {
			console.log("%c> SUCCESS", "color:green");
			console.log(JSON.stringify(result, null, 2));
			callback(result);
		})
		.fail(function(jqXHR, textStatus, error) {
			console.log("%c> ERROR", "color:red");
			let errorResponse = parseError(jqXHR);
			if (errorResponse != null) {
				alert(errorResponse.message);
			} else {
				alert("## parsing error");
			}
		})
	}
	
	return {
		getCategoryListByParent:getCategoryListByParent
	};
	
})();