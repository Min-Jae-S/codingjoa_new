<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title><c:out value="${category.categoryName}"/> | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
	.board-wrap {
		width: 760px;;
		min-width: 620px;
		margin: 0 auto;
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
	
	.custom-select {
		display: flex;
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
	
	.board-link {
		color: black;
	}
	
	.board-link:hover {
		color: black;
		text-decoration: none;
	}
	
	.board-pagination {
		display: flex;
		justify-content: center;
	}
	
	.board-pagination .pagination {
		margin-bottom: 0;
	}
	
	.no-board {
		display: flex;
    	flex-direction: column;
    	justify-content: center;
    	min-height: 250px;
    }
</style>
</head>
<body>
	
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="board-wrap">
		<h3 class="font-weight-bold mb-4">
			<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}" class="board-link">
				<c:out value="${category.categoryName}"/>
			</a>
		</h3>
		<div class="pt-3">
        	<form:form class="form-inline" action="${contextPath}/board/" method="GET" modelAttribute="boardCri" id="boardForm">
        		<input type="hidden" name="boardCategoryCode" value="${category.categoryCode}">
			  	<form:select path="type" class="custom-select mr-3 rounded-md">
			  		<form:options items="${options.typeOption}"/>
			  	</form:select>
				<div class="input-group">
					<form:input path="keyword" class="form-control rounded-md" placeholder="검색어를 입력해주세요"/>
				  	<div class="input-group-append">
				  		<form:button class="btn btn-outline-secondary rounded-md">검색</form:button>
				  	</div>
        		</div>
        		<form:select path="recordCnt" class="custom-select rounded-md ml-auto">
        			<form:options items="${options.recordCntOption}"/>
        		</form:select>
			</form:form>
		</div>
		<div class="pt-4 mb-3">
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
						<c:when test="${not empty pagedBoard}">
							<c:forEach var='boardDetails' items="${pagedBoard}">
								<tr>
									<td class="d-md-table-cell">
										<span><c:out value="${boardDetails.boardIdx}"/></span>
									</td>
									<td class="d-md-table-cell text-left">
										<a class="board-title" href="${contextPath}/board/read?boardIdx=${boardDetails.boardIdx}&${boardCri.queryParams}">
											<c:out value="${boardDetails.boardTitle}"/><!--
									 --></a>
									 	<c:if test="${boardDetails.commentCnt > 0}">
											<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
										</c:if>
									</td>
									<td class="d-md-table-cell">
										<span><c:out value="${boardDetails.writerNickname}"/></span>
									</td>
									<td class="d-md-table-cell">
										<span><c:out value="${boardDetails.createdAt}"/></span>
									</td>
									<td class="d-md-table-cell">
										<span><c:out value="${boardDetails.boardView}"/></span>
									</td>
									<td class="d-md-table-cell">
										<i class="fa-heart fa-fw ${boardDetails.liked ? 'fa-solid text-danger' : 'fa-regular'}"></i>
										<span class="board-likes-cnt"><c:out value="${boardDetails.likesCnt}"/></span>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6">
									<div class="no-board">등록된 게시글이 없습니다.</div>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
		<div class="mb-3">
			<a href="${contextPath}/board/write?boardCategoryCode=${category.categoryCode}" class="btn btn-primary rounded-md">글쓰기</a>
		</div>
		<c:if test="${not empty pagination}">
			<div class="board-pagination">
				<ul class="pagination">
					<c:if test="${pagination.first}">
						<li class="page-item">
							<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&${boardCri.getQueryParams(1)}"
								class="page-link"><i class="fa-solid fa-fw fa-angles-left"></i>
							</a>
						</li>
					</c:if>
					<c:if test="${pagination.prev}">
						<li class="page-item">
							<!-- fa-chevron-left  -->
							<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&${boardCri.getQueryParams(pagination.prevPage)}"
								class="page-link"><i class="fa-solid fa-fw fa-angle-left"></i>
							</a>
						</li>
					</c:if>
					<c:forEach var="item" begin="${pagination.startPage}" end="${pagination.endPage}">
						<li class="page-item ${pagination.page eq item ? 'active' : ''}">
							<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&${boardCri.getQueryParams(item)}" 
								class="page-link">${item}
							</a>
						</li>
					</c:forEach>
					<c:if test="${pagination.next}">
						<li class="page-item">
							<!-- fa-chevron-right -->
							<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&${boardCri.getQueryParams(pagination.nextPage)}"
								class="page-link"><i class="fa-solid fa-fw fa-angle-right"></i>
							</a>
						</li>
					</c:if>
					<c:if test="${pagination.last}">
						<li class="page-item">
							<a href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&${boardCri.getQueryParams(pagination.pageCnt)}"
								class="page-link"><i class="fa-solid fa-fw fa-angles-right"></i>
							</a>
						</li>
					</c:if>
				</ul>
			</div>
		</c:if>
	</div>	
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		$("#recordCnt").on("change", function() {
			let url = new URL(location.href);
			url.searchParams.set("recordCnt", $(this).val());
			url.searchParams.delete("page");
			
			Array.from(url.searchParams).forEach(([key, value]) => {
				if (value == null || value.trim() == "") {
					url.searchParams.delete(key);
				}
			});
			
			location.href = url;
		});
		
		$("#boardForm").on("submit", function(e) {
			e.preventDefault();
			
			$(this).find("input, select").each(function() {
				if ($(this).val() == null || $(this).val().trim() == "") {
					$(this).removeAttr("name");
				}
			});
			
			//$(this).submit();
			this.submit();
		});
	});
</script>	
</body>
</html>