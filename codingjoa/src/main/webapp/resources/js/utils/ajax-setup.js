$.ajaxSetup({
	beforeSend: function(xhr, settings) {
		console.log("%c> [GLOBAL] BEFORE SEND", "color:blue");
		const token = localStorage.getItem("ACCESS_TOKEN");
		if (token) {
			xhr.setRequestHeader("Authorization", `Bearer ${token}`);
		}
		
		console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
	}
});