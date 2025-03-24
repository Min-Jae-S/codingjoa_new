function parseError(jqXHR) {
	console.log("## parseError");
	try {
		let errorResponse = JSON.parse(jqXHR.responseText);
		console.log(JSON.stringify(errorResponse, null, 2));
		return errorResponse;
	} catch(e) {
		console.log("## failed to parse errorResponse:", e);
		return {
			status : jqXHR.status,
			message : "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.",
			details : [],
			timestamp : new Date().toISOString().slice(0, 19)
		};
	}
}

function handleLoginError(errorResponse) {
	console.log("## handleLoginError");
	let message = errorResponse.message.replace(/\.\s/g, ".<br>"); // replace(/\.\s*/g, ".<br>");
	$(".email_pw_wrap").after("<div class='error'><p>" + message + "</p></div>");
}

// handleUserError, handleMainError
function handleValidationError(errorResponse) {
	console.log("## handleValidationError");
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			$("#" + item.field).closest("dd").after("<dd id='" + item.field + ".errors' class='error'>" + item.message + "</dd>");
		});
		return;
	}
	
	let message = errorResponse.message.replace(/\.\s/g, ".\n");
	alert(message);
}

// handleImageError, handleCommentError, handleLikeError
function handleError(errorResponse) {
	console.log("## handleError");
	let message;
	let details = errorResponse.details;
	
	if (details.length > 0) {
		message = details[0].message;
	} else {
		message = errorResponse.message;
	}
	
	message = message.replace(/\.\s/g, ".\n");
	console.log("\t > handled message = %s", message);
	
	alert(message);
}


