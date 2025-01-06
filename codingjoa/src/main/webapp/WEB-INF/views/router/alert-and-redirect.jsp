<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
	console.log("## alert-and-redirect.jsp");
	
	let message = "<c:out value='${message}'/>";
	let continueUrl = "<c:out value='${continueUrl}'/>";
	console.log("\t > message = %s", message);
	console.log("\t > continueUrl = '%s'", continueUrl);
	
	if (message != "") {
		alert(message);
	}
	
	location.href = continueUrl;
</script>