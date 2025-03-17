
function parseError(jqXHR) {
	try {
		let errorResponse = JSON.parse(jqXHR.responseText);
		console.log(JSON.stringify(errorResponse, null, 2));
		return errorResponse;
	} catch(e) {
		console.log(e);
		return null;
	}
}

function handleUserError(errorResponse) {
	console.log("## handleUserError");
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			$("#" + item.field).closest("dd").after("<dd id='" + item.field + ".errors' class='error'>" + item.message + "</dd>");
		});
		return;
	}
	
	let message = errorResponse.message;
	if (message != "") {
		alert(message);
	}
}

function handleCommentError(errorResponse) {
	console.log("## handleCommentError");
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
		return;
	} 
	
	let message = errorResponse.message;
	if (message != "") {
		alert(message);
	}
	
}

function handleLikeError(errorResponse) {
	console.log("## handleLikeError");
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
		return;
	}
	
	let message = errorResponse.message;
	if (message != "") {
		alert(message);
	}
}

function handleImageError(errorResponse) {
	console.log("## handleImageError");
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
		return;
	}
	
	let message = errorResponse.message;
	console.log("\t > original message = %s", message);
	
	if (message != "") {
		message = message.replace(/\\n/gi, "\n");
		console.log("\t > handled message = %s", message);
		alert(message);
	}
}

function handleLoginError(errorResponse) {
	console.log("## handleLoginError");
	let message = errorResponse.message;
	console.log("\t > original message = %s", message);
	
	if (message != "") {
		message = message.replace(/\.\s*/g, ".<br>");
		console.log("\t > handled message = %s", message);
		$(".email_pw_wrap").after("<div class='error'><p>" + message + "</p></div>");
	}
}
