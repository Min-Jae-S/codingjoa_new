<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<style>
	.main-wrap {
		width: 860px;
		min-width: 860px;
		margin: 0 auto;
	}
	
	.table-container {
		padding-top: .5rem;
	}
	
	.table-container:not(:last-child) {
		margin-bottom: 6rem;
	}

	.table {
		border-spacing: 0px;
		border-collapse: separate;
	}

	.table thead th {
		text-align: center;
		vertical-align: middle;
		border-top: 1px solid black;
		border-bottom: 1px solid #dee2e6;
	}

	.table tbody td {
		text-align: center;
		vertical-align: middle;
		border-top: none;
		border-bottom: 1px solid #dee2e6;
	}
	
	.table tbody td:not(:first-child) {
		color: #757575;
	}
	
	.board-title, .board-title:hover {
		color: black;
	}
	
	.comment-cnt {
		color: red;
	}
	
	.comment-cnt:before {
		content: "(";
	}

	.comment-cnt:after {
		content: ")";
	}
	
	.no-results {
		display: flex;
	    flex-direction: column;
    	justify-content: center;
    	min-height: 208px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container main-container">
	<div class="main-wrap">
		<!-- boards -->
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>