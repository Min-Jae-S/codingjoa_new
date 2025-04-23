<script>
	console.log("## alert-and-redirect.jsp");
	
	const message = "${message}";
	if (message) {
		alert(message);
	}
	
	/* if (message) {
		message = message.replace(/\.\s/g, ".\n");
		console.log("\t > handled message = %s", message);
		alert(message);
	} */
	
	let continueUrl = "${continueUrl}";
	console.log("\t > continueUrl: '%s'", continueUrl);
	
	location.href = continueUrl;
</script>