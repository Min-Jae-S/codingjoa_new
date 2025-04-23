<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<link href="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui-bundle.js"></script>
<script src="https://cdn.jsdelivr.net/npm/swagger-ui-dist/swagger-ui-standalone-preset.js"></script>

<div id="swagger-ui"></div>

<script>
	const ui = SwaggerUIBundle({
		urls: [
			{ url: "${contextPath}/v2/api-docs?group=all-apis", name: "전체 API" },
			{ url: "${contextPath}/v2/api-docs?group=public-apis", name: "Public API" },
			{ url: "${contextPath}/v2/api-docs?group=private-apis", name: "Private API" }
		],
		dom_id : '#swagger-ui',
		layout : "StandaloneLayout",
		presets : [ SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset ],
		requestInterceptor : function(request) {
			const token = localStorage.getItem("ACCESS_TOKEN");
			if (token) {
				request.headers["Authorization"] = `Bearer \${token}`;
			}
			return request;
		}
	});

	window.ui = ui;
</script>