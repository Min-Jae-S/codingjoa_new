<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>게시판 | Codingjoa</title>
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
	.boards-wrap {
		width: 760px;
		min-width: 620px;
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
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container boards-container">
	<div class="boards-wrap">
		<c:forEach var="board" items="${boardList}" varStatus="status">
			<h3 class="font-weight-bold"><c:out value="${boardCategoryList[status.index].categoryName}"/></h3>
			<div class="table-container">
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
								<c:forEach var="boardDetails" items="${board}">
									<tr>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.boardIdx}"/></span>
										</td>
										<td class="d-md-table-cell text-left">
											<a class="board-title" href="${contextPath}/board/read?boardIdx=${boardDetails.boardIdx}">
												<c:out value="${boardDetails.boardTitle}"/>
											</a>
											<c:if test="${boardDetails.commentCnt > 0}">
												<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
											</c:if>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.boardWriterNickname}"/></span>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.createdAt}"/></span>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.boardViews}"/></span>
										</td>
										<td class="d-md-table-cell">
											<i class="fa-heart fa-fw ${boardDetails.boardLiked ? 'fa-solid text-danger' : 'fa-regular'}"></i>
											<span><c:out value="${boardDetails.boardLikesCnt}"/></span>
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
				<c:if test="${not empty board}">
					<a class="btn btn-primary rounded-md" 
						href="${contextPath}/board/?boardCategoryCode=${boardCategoryList[status.index].categoryCode}">
						게시글 더보기
					</a>
				</c:if>
			</div>
		</c:forEach>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>