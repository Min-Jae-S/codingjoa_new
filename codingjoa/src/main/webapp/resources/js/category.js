function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let categoryService = (function() {
	const contextPath = getContextPath();

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