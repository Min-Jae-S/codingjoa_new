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
<title><c:out value="${category.categoryName}"/></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
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
	
	.table td:not(:first-child) {
		color: #757575;
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
	
	.board_title, .board_title:hover {
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
</style>
</head>
<body>
	
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<h4 class="font-weight-bold mb-4"><c:out value="${category.categoryName}"/></h4>
			<div class="pt-3">
	        	<form:form class="form-inline" action="${contextPath}/board/" method="GET" modelAttribute="boardCri">
	        		<input type="hidden" name="boardCategoryCode" value="${category.categoryCode}">
				  	<form:select path="type" class="custom-select custom-select-sm mr-2">
				  		<form:options items="${typeMap}"/>
				  	</form:select>
					<div class="input-group">
						<form:input path="keyword" class="form-control form-control-sm" placeholder="검색어를 입력해주세요"/>
					  	<div class="input-group-append">
					  		<form:button class="btn btn-outline-secondary btn-sm">검색</form:button>
					  	</div>
	        		</div>
	        		<form:select path="recordCnt" class="custom-select custom-select-sm ml-auto">
						<form:options items="${recordCntMap}"/>
	        		</form:select>
				</form:form>
			</div>
			<div class="pt-3 mb-3">
				<table class="table">
					<thead>
						<tr>
							<th class="d-md-table-cell">번호</th>
							<th class="d-md-table-cell w-40">제목</th>
							<th class="d-md-table-cell">작성자</th>
							<th class="d-md-table-cell">작성일</th>
							<th class="d-md-table-cell">조회</th>
							<th class="d-md-table-cell">좋아요</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${not empty board}">
								<c:forEach var='boardDetails' items="${board}">
									<tr>
										<td class="d-md-table-cell"><c:out value="${boardDetails.boardIdx}"/></td>
										<td class="d-md-table-cell text-left">
											<a class="board_title" href="${contextPath}/board/read?boardIdx=${boardDetails.boardIdx}&
												${boardCri.getQueryString()}">
												<c:out value="${boardDetails.boardTitle}"/>
											</a>
											<c:if test="${boardDetails.commentCnt > 0}">
												<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
											</c:if>
										</td>
										<td class="d-md-table-cell"><c:out value="${boardDetails.memberId}"/></td>
										<td class="d-md-table-cell">
											<fmt:formatDate value="${boardDetails.regdate}" type="date"/>
										</td>
										<td class="d-md-table-cell"><c:out value="${boardDetails.boardViews}"/></td>
										<td class="d-md-table-cell">
											<sec:authorize access="isAnonymous()">
												<i class="fa-regular fa-heart"></i>
											</sec:authorize>
											<sec:authorize access="isAuthenticated()">
												<sec:authentication property="principal" var="principal"/>
												<c:choose>
													<c:when test="${principal.isMyBoardLikes(boardDetails.boardIdx)}">
														<i class="text-danger fa-solid fa-heart"></i>
													</c:when>
													<c:otherwise>
														<i class="fa-regular fa-heart"></i>
													</c:otherwise>
												</c:choose>
											</sec:authorize>
											<span class="board-likes-cnt"><c:out value="${boardDetails.boardLikesCnt}"/></span>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="6">
										<div class="no-board py-5">등록된 게시글이 없습니다.</div>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<div class="mb-3" style="height: 38px;">
				<a class="btn btn-primary" 
					href="${contextPath}/board/write?boardCategoryCode=${category.categoryCode}">글쓰기</a>
			</div>
			<div class="pt-3">
				<ul class="pagination justify-content-center">
					<c:if test="${pagination.prev}">
						<li class="page-item">
							<a class="page-link" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
								${boardCri.getQueryString(pagination.prevPage)}"><i class="fa-solid fa-chevron-left"></i></a>
						</li>
					</c:if>
					<c:forEach var="item" begin="${pagination.startPage}" end="${pagination.endPage}">
						<li class="page-item ${item eq pagination.page ? 'active' : ''}">
							<a class="page-link" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
								${boardCri.getQueryString(item)}">${item}</a>
						</li>
					</c:forEach>
					<c:if test="${pagination.next}">
						<li class="page-item">
							<a class="page-link" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
								${boardCri.getQueryString(pagination.nextPage)}"><i class="fa-solid fa-chevron-right"></i></a>
						</li>
					</c:if>
				</ul>
			</div>
		</div>		
		<div class="col-sm-1"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#recordCnt").on("change", function() {
			let url = new URL(location.href);
			url.searchParams.set("page", "1");
			url.searchParams.set("recordCnt", $(this).val());
			location.href = url;
		});
	});
</script>	
</body>
</html>