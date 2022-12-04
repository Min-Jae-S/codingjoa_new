<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<!DOCTYPE html>
<html>
<head>
<title>글쓰기</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<style>
	select.form-control {
		font-size: 0.9rem;
	}
	
	.form-group button {
		font-size: 0.9rem;
	} 
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>



<div class="container board-write-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<h5 class="font-weight-bold">게시판 글쓰기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<form>
					<div class="form-row">
						<div class="form-group col-md-8">
							<select class="form-control">
								<c:forEach var="category" items="${categoryList}">
									<option value="${category.categoryCode}" 
									${category.categoryCode eq categoryCode ? "selected" : ""}>${category.categoryName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="form-group col-md-2">
							<button class="btn btn-primary btn-block">등록</button>
						</div>
						<div class="form-group col-md-2">
							<button type="reset" class="btn btn-secondary btn-block">취소</button>
						</div>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="" placeholder="제목을 입력하세요.">
					</div>
					<div class="form-group">
						<!--  
							CKeditor...
						-->
					</div>
				</form>
			</div>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

</body>
</html>