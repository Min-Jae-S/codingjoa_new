<script>
	console.log("## oauth2-success.jsp");
	
	localStorage.setItem("ACCESS_TOKEN", "${response.data.accessToken}");
	
	const message = "${response.message}";
	if (message) {
		alert(message);
	}
	
	const continueUrl = "${continueUrl}";
	console.log("\t > resovled continueUrl from OAuth2LoginSuccessHandler: '%s'", continueUrl);
	
	location.href = continueUrl;
</script>