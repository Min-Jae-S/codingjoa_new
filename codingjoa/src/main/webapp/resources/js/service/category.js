let categoryService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function getCategoriesByParent(code, callback) {
		console.log("## getCategoriesByParent");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/categories/parent/${code}`,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	return {
		getCategoriesByParent:getCategoriesByParent
	};
	
})();