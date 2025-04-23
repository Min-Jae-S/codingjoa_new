<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<title>ADMIN | REST API documentation</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui-bundle.js"></script>
<script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui-standalone-preset.js"></script>
</head>
<body>
	<div id="swagger-ui"></div>
</body>
<script>
	const ui = SwaggerUIBundle({
		urls: [
			{ url : "${contextPath}/v2/api-docs?group=all-apis", name: "전체 API" },
			{ url : "${contextPath}/v2/api-docs?group=public-apis", name: "Public API" },
			{ url : "${contextPath}/v2/api-docs?group=private-apis", name: "Private API" }
		],
		dom_id : '#swagger-ui',
		layout : "StandaloneLayout",
		presets : [ 
			SwaggerUIBundle.presets.apis, 
			SwaggerUIStandalonePreset 
		],
		requestInterceptor : function(request) {
			const token = localStorage.getItem("ACCESS_TOKEN");
			if (token && request.url.includes("/v2/api-docs")) {
				request.headers["Authorization"] = `Bearer \${token}`;
			}
			
			return request;
		}
	});

	window.ui = ui;
</script>
</html>