<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
	let message = "<c:out value='${message}'/>";
	let redirectUrl = "<c:out value='${redirectUrl}'/>";
	
	if (message != "") alert(message);
	location.href = redirectUrl;
</script>