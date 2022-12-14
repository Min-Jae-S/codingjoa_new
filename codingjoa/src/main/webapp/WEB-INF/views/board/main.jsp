<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
		/* font-size: 0.8rem; */
	}
	
	/*
	.input-group input::placeholder {
		font-size: 0.8rem;
	}
	
	.input-group input[type="text"] {
		font-size: 0.8rem;
	}
	
	.input-group-append .btn-sm {
		font-size: 0.8rem;
	}
	
	.no-board {
		font-size: 0.8rem;
	}
	*/
</style>
</head>
<body>
	
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<h4 class="font-weight-bold mb-4">${category.categoryName}</h4>
			<div class="pt-3">
	        	<form class="form-inline" action="${contextPath}/board/main" method="GET">
	        		<input type="hidden" name="categoryCode" value="${category.categoryCode}">
					<select class="custom-select custom-select-sm mr-2" name="searchType">
				    	<option value="T" ${searchDto.searchType eq 'T' ? 'selected' : ''}>??????</option>
				    	<option value="C" ${searchDto.searchType eq 'C' ? 'selected' : ''}>??????</option>
				    	<option value="W" ${searchDto.searchType eq 'W' ? 'selected' : ''}>?????????</option>
				    	<option value="TC" ${searchDto.searchType eq 'TC' ? 'selected' : ''}>?????? + ??????</option>
				  	</select>
					<div class="input-group">
					  	<input type="text" class="form-control form-control-sm" name="searchKeyword" value="${searchDto.searchKeyword}" placeholder="???????????? ??????????????????">
					  	<div class="input-group-append">
					  		<button class="btn btn-outline-secondary btn-sm">??????</button>
					  	</div>
	        		</div>
	        		<select class="custom-select custom-select-sm ml-auto" name="recordPerPage">
				    	<option value="5" ${searchDto.recordPerPage eq 5 ? 'selected' : ''}>5??????</option>
				    	<option value="10" ${searchDto.recordPerPage eq 10 ? 'selected' : ''}>10??????</option>
				    	<option value="15" ${searchDto.recordPerPage eq 15 ? 'selected' : ''}>15??????</option>
				    	<option value="20" ${searchDto.recordPerPage eq 20 ? 'selected' : ''}>20??????</option>
				    	<option value="25" ${searchDto.recordPerPage eq 25 ? 'selected' : ''}>25??????</option>
				  	</select>
				</form>
			</div>
			<div class="pt-3 mb-3">
				<table class="table">
					<thead>
						<tr>
							<th class="d-md-table-cell">??????</th>
							<th class="d-md-table-cell w-50">??????</th>
							<th class="d-md-table-cell">?????????</th>
							<th class="d-md-table-cell">?????????</th>
							<th class="d-md-table-cell">??????</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty boardDetailsList}">
								<c:forEach var='boardDetails' items="${boardDetailsList}">
									<tr>
										<td class="d-md-table-cell"><c:out value="${boardDetails.boardIdx}"/></td>
										<td class="d-md-table-cell text-left">
											<a href='${contextPath}/board/read?boardIdx=${boardDetails.boardIdx}'><c:out value="${boardDetails.boardTitle}"/></a>
										</td>
										<td class="d-md-table-cell"><c:out value="${boardDetails.memberId}"/></td>
										<td class="d-md-table-cell">
											<fmt:formatDate value="${boardDetails.regdate}" type="date"/>
										</td>
										<td class="d-md-table-cell"><c:out value="${boardDetails.boardViews}"/></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="5">
										<div class="no-board py-5">????????? ???????????? ????????????.</div>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<div class="mb-3" style="height: 38px">
				<sec:authorize access="isAuthenticated()">
					<a class="btn btn-primary" href="${contextPath}/board/write?categoryCode=${category.categoryCode}">?????????</a>
				</sec:authorize>
			</div>
			<nav class="pt-3">
				<ul class="pagination justify-content-center">
					<li class="page-item disabled"><a class="page-link" href="#" tabindex="-1">Previous</a></li>
					<li class="page-item"><a class="page-link"  href="#">1</a></li>
					<li class="page-item active"><a class="page-link" href="#">2</a></li>
					<li class="page-item"><a class="page-link" href="#">3</a></li>
					<li class="page-item"><a class="page-link" href="#">Next</a></li>
				</ul>
			</nav>
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