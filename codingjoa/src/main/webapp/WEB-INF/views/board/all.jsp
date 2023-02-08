<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>게시판</title>
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
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<c:forEach var="board" items="${boardContainer}">
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
							<c:choose>
								<c:when test="${not empty board}">
									<c:forEach var='boardDetails' items="${board}">
										<tr>
											<td class="d-md-table-cell"><c:out value="${boardDetails.boardIdx}"/></td>
											<td class="d-md-table-cell text-left">
												<a href='${contextPath}/board/read?boardCategoryCode=${boardDetails.boardCategoryCode}&boardIdx=${boardDetails.boardIdx}'><c:out value="${boardDetails.boardTitle}"/></a>
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
											<div class="no-board py-5">등록된 게시글이 없습니다.</div>
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</c:forEach>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>