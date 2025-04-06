function parseError(jqXHR) {
	console.log("## parseError");
	try {
		return JSON.parse(jqXHR.responseText);
	} catch(e) {
		console.log("\t > failed to parse errorResponse:", e);
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
	console.log(JSON.stringify(errorResponse, null, 2));
	
	let message = errorResponse.message.replace(/\.\s/g, ".<br>"); // replace(/\.\s*/g, ".<br>");
	console.log("> handled message = %s", message);
	
	$(".email_pw_wrap").after("<div class='error'><p>" + message + "</p></div>");
}

// handleUserError, handleMainError
function handleValidationError(errorResponse) {
	console.log("## handleValidationError");
	console.log(JSON.stringify(errorResponse, null, 2));
	
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			$(`#${item.field}`).closest("dd").after(`<dd id='${item.field}.errors' class='error'>${item.message}</dd>`);
		});
		return;
	}
	
	let message = errorResponse.message.replace(/\.\s/g, ".\n");
	console.log("> handled message = %s", message);
	
	alert(message);
}

function handleAdminValidationError(errorResponse) {
	console.log("## handleAdminValidationError");
	console.log(JSON.stringify(errorResponse, null, 2));
	
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			$(`input[name='${item.field}']`).closest("div")
				.after(`<span id='${item.field}.errors' class='error'>${item.message}</span>`);
		});
		return;
	}
	
	let message = errorResponse.message.replace(/\.\s/g, ".\n");
	console.log("> handled message = %s", message);
	
	alert(message);
}

// handleImageError, handleCommentError, handleLikeError
function handleError(errorResponse) {
	console.log("## handleError");
	console.log(JSON.stringify(errorResponse, null, 2));
	
	let message;
	let details = errorResponse.details;
	
	if (details.length > 0) {
		message = details[0].message;
	} else {
		message = errorResponse.message;
	}
	
	message = message.replace(/\.\s/g, ".\n");
	console.log("> handled message = %s", message);
	
	alert(message);
}


