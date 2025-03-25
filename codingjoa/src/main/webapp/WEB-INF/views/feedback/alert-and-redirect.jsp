<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
	console.log("## alert-and-redirect.jsp");
	
	let message = "<c:out value='${message}'/>";
	if (message) {
		message = message.replace(/\.\s/g, ".\n");
		console.log("\t > handled message = %s", message);
		
		alert(message);
	}
	
	let continueUrl = "<c:out value='${continueUrl}'/>";
	console.log("\t > continueUrl = '%s'", continueUrl);
	
	location.href = continueUrl;
</script>