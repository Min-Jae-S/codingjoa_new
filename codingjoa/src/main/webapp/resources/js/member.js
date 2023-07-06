console.log("## Member service ready - member.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let MemberService = (function() {

	const contextPath = getContextPath();

	function checkEmail(obj, callback) {
		console.log("## Check Email");
		let url = contextPath + "/api/member/info/check-email";
		console.log("> url = '%s'", url);
		console.log("> obj = %s", JSON.stringify(obj, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(JSON.stringify(errorResponse, null, 2));
				$("#memberEmail\\.errors, #authCode\\.errors, .success").remove();
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						$("#" + errorField).closest("dd")
							.after("<dd id='" + errorField + ".errors' class='error'>" + errorMessage + "</dd>");
					});
				} else {
					alert(errorResponse.errorMessage);
				}
			}
		});
	}

	return {
		checkEmail:checkEmail
	};
	
})();