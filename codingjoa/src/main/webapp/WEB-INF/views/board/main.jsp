<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>${category.categoryName}</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
	.table {
		border-spacing: 0px;
		border-collapse: separate;
	}

	.table thead th {
		vertical-align: middle;
		border-top: 1px solid black;
		border-bottom: 1px solid #dee2e6;
	}

	.table td {
		vertical-align: middle;
		border-top: none;
		border-bottom: 1px solid #dee2e6;
	}
	
	/* .table tr:last-child td {
		border-bottom: none !important;
	} */
</style>
</head>
<body>
	
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-main-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<h5 class="font-weight-bold">${category.categoryName}</h5>
			<div class="pt-3">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center d-none d-md-table-cell">번호</th>
							<th class="text-center d-none d-md-table-cell w-50">제목</th>
							<th class="text-center d-none d-md-table-cell">작성자</th>
							<th class="text-center d-none d-md-table-cell">작성일</th>
							<th class="text-center d-none d-md-table-cell">조회</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var='boardDetailsDto' items="${boardDetailsList}">
							<tr>
								<td class="text-center d-none d-md-table-cell">${boardDetailsDto.boardIdx}</td>
								<td><a href='${contextPath}/board/read?boardIdx=${boardDetailsDto.boardIdx}'>${boardDetailsDto.boardTitle}</a></td>
								<td class="text-center d-none d-md-table-cell">${boardDetailsDto.memberId}</td>
								<td class="text-center d-none d-md-table-cell">
									<fmt:formatDate value="${boardDetailsDto.regdate}" type="date"/>
								</td>
								<td class="text-center d-none d-md-table-cell">${boardDetailsDto.boardViews}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<a class="btn btn-primary" href="${contextPath}/board/write?categoryCode=${category.categoryCode}">글쓰기</a>
		</div>		
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		
	});
</script>	
</body>
</html>