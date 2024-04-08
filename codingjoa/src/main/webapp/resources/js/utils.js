function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

function parseError(jqXHR) {
	try {
		let errorResponse = JSON.parse(jqXHR.responseText);
		console.log(JSON.stringify(errorResponse, null, 2));
		return errorResponse;
	} catch(e) {
		console.log("> not valid JSON");
		return null;
	}
}

function handleMemberError(errorResponse) {
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			$("#" + item.field).closest("dd")
				.after("<dd id='" + item.field + ".errors' class='error'>" + item.message + "</dd>");
		});
	} else {
		let message = errorResponse.message.replace(/\\n/gi,"\n");
		alert(message);
	}
}

function handleCommentError(errorResponse) {
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
	} else {
		let message = errorResponse.message.replace(/\\n/gi,"\n");
		alert(message);
	}
}

function handleLikesError(errorResponse) {
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
	} else {
		let message = errorResponse.message.replace(/\\n/gi,"\n");
		alert(message);
	}
}

function handleUploadError(errorResponse) {
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
	} else {
		let message = errorResponse.message.replace(/\\n/gi,"\n");
		alert(message);
	}
}

function handleImageError(errorResponse) {
	let details = errorResponse.details;
	if (details.length > 0) {
		$.each(details, function(index, item) {
			alert(item.message);
		});
	} else {
		let message = errorResponse.message.replace(/\\n/gi,"\n");
		alert(message);
	}
}
