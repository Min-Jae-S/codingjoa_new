function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let adminService = (function() {
	const contextPath = getContextPath();

	function deletBoards(obj, callback) {
		console.log("## deletBoards");
		let url = contextPath + "/api/admin/boards";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "DELETE",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				console.log(errorResponse);
			}
		});
	}

	return {
		deletBoards:deletBoards
	};
	
})();