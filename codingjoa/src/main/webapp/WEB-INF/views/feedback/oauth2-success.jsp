<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
	console.log("## oauth2-success.jsp");
	
	localStorage.setItem("ACCESS_TOKEN", "${response.data.accessToken}");
	
	const message = "${response.message}";
	console.log("\t > message = %s", message);
	alert(message);
	
	const continueUrl = "${continueUrl}";
	console.log("\t > resovled continueUrl from OAuth2LoginSuccessHandler: '%s'", continueUrl);
	
	location.href = continueUrl;
</script>