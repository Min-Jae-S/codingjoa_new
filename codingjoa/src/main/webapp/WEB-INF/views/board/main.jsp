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
		text-align: center;
		vertical-align: middle;
		border-top: 1px solid black;
		border-bottom: 1px solid #dee2e6;
	}

	.table td {
		text-align: center;
		vertical-align: middle;
		border-top: none;
		border-bottom: 1px solid #dee2e6;
	}
	
	.custom-select {
		display: flex;
	}	
</style>
</head>
<body>
	
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container main-board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<h4 class="font-weight-bold mb-4">${category.categoryName}</h4>
			<div class="pt-3">
	        	<form class="form-inline">
					<select name="searchType" class="custom-select custom-select-sm mr-2">
				    	<option value="T">제목</option>
				    	<option value="C">내용</option>
				    	<option value="W">작성자</option>
				    	<option value="TC">제목 + 내용</option>
				  	</select>
					<div class="input-group">
					  	<input type="text" class="form-control form-control-sm" name="serachKeyword">
					  	<div class="input-group-append">
					  		<button class="btn btn-outline-secondary btn-sm" type="button">검색</button>
					  	</div>
	        		</div>
	        		<select name="recordPerPage" class="custom-select custom-select-sm ml-auto">
				    	<option value="5">5개씩</option>
				    	<option value="10">10개씩</option>
				    	<option value="15">15개씩</option>
				    	<option value="20">20개씩</option>
				  	</select>
				</form>
			</div>
			<div class="pt-3">
				<table class="table">
					<thead>
						<tr>
							<th class="d-md-table-cell">번호</th>
							<th class="d-md-table-cell w-50">제목</th>
							<th class="d-md-table-cell">작성자</th>
							<th class="d-md-table-cell">작성일</th>
							<th class="d-md-table-cell">조회</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var='boardDetails' items="${boardDetailsList}">
							<tr>
								<td class="d-md-table-cell">${boardDetails.boardIdx}</td>
								<td class="d-md-table-cell text-left">
									<a href='${contextPath}/board/read?boardIdx=${boardDetails.boardIdx}'>${boardDetails.boardTitle}</a>
								</td>
								<td class="d-md-table-cell">${boardDetails.memberId}</td>
								<td class="d-md-table-cell"><fmt:formatDate value="${boardDetails.regdate}" type="date"/></td>
								<td class="d-md-table-cell">${boardDetails.boardViews}</td>
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