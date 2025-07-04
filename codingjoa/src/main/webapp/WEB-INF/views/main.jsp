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
		<c:forEach var="board" items="${boards}" varStatus="status">
			<h3 class="font-weight-bold"><c:out value="${boardCategories[status.index].name}"/></h3>
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
											<span><c:out value="${boardDetails.id}"/></span>
										</td>
										<td class="d-md-table-cell text-left">
											<a class="board-title" href="${contextPath}/board/read?id=${boardDetails.id}">
												<c:out value="${boardDetails.title}"/><!--
										 --></a>
											<c:if test="${boardDetails.commentCount > 0}">
												<span class="comment-cnt"><c:out value="${boardDetails.commentCount}"/></span>
											</c:if>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.writerNickname}"/></span>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.createdAt}"/></span>
										</td>
										<td class="d-md-table-cell">
											<span><c:out value="${boardDetails.viewCount}"/></span>
										</td>
										<td class="d-md-table-cell">
											<i class="fa-heart fa-fw ${boardDetails.liked ? 'fa-solid text-danger' : 'fa-regular'}"></i>
											<span><c:out value="${boardDetails.likeCount}"/></span>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="6">
										<div class="no-results">일치하는 결과가 없습니다.</div>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<div>
					<a href="${contextPath}/board/?categoryCode=${boardCategories[status.index].code}" class="btn btn-primary rounded-md">
						게시판 가기
					</a>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>