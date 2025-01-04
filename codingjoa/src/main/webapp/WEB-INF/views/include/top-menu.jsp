<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- navbar -->
<nav class="navbar navbar-custom navbar-expand-sm">
	<div class="container-fluid px-5">
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}">Codingjoa</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown category mx-2" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link">
							<c:out value="${parentCategory.categoryName}"/>
						</a>
						<div class="dropdown-menu">
							<!-- categories -->
							<!-- 
							<button class="dropdown-item" type="button" data-path="/?boardCategoryCode=4">공지 게시판</button>
							<button class="dropdown-item" type="button" data-path="/?boardCategoryCode=5">질문 게시판</button>
							<button class="dropdown-item" type="button" data-path="/?boardCategoryCode=6">자유 게시판</button> 
							-->
						</div>
					</li>
				</c:forEach>
				<li class="nav-item dropdown test mx-2">
					<a href="#" class="nav-link">TEST</a>
					<div class="dropdown-menu">
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/member/account'">/member/account</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/admin'">/admin</button>
						<button class="dropdown-item" type="button" onclick="adminApi('${contextPath}/api/admin')">/api/admin</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/test/ws'">/test/ws</button>
					</div>
				</li>
			</ul>
			<ul class="navbar-nav ml-auto">
				<sec:authentication property="principal" var="principal"/>
				<c:if test="${empty principal}">
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/login?continue=${loginCurrentUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/member/join" class="nav-link rounded-md">회원가입</a>
					</li>
				</c:if>
				<sec:authorize access="isAnonymous()">
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/login?continue=${loginCurrentUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/account" class="nav-link nav-member-profile">
							<c:choose>
								<c:when test="${not empty principal.imageUrl}">
									<img class="nav-member-image" id="navMemberImage" src="${principal.imageUrl}">
								</c:when>
								<c:otherwise>
									<img class="nav-member-image"" id="navMemberImage" src="${contextPath}/resources/images/img_profile.png">
								</c:otherwise>
							</c:choose>
							<span class="font-weight-bold text-body" id="navMemberNickname">
								<c:out value="${principal.nickname}"/>
							</span>
						</a>
					</li>
					<li class="nav-item">
						<span class="nav-link" style="pointer-events: none;">|</span>
					</li>
					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<li class="nav-item mx-2">
							<a href="${contextPath}/admin" class="nav-link">관리자 페이지</a>
						</li>
					</sec:authorize>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/account" class="nav-link">계정 관리</a>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/logout?continue=${logoutCurrentUrl}" class="nav-link">로그아웃</a>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>

<script src="${contextPath}/resources/js/category.js"></script>
<script src="${contextPath}/resources/js/html-creator.js"></script>
<script>
	$(function() {
		let timer, delay = 200;
		let $dropdowns = $("li.category div.dropdown-menu");
		
		$("li.category").on("mouseenter", function() {
			clearTimeout(timer);
			
			let category = $(this).data("category");
			let $dropdown = $(this).find("div.dropdown-menu");
			timer = setTimeout(function() {
				categoryService.getCategoryListByParent(category, function(result) {
					let categoryList = result.data;
					let categoryMenuHtml = createCategoryMenuHtml(categoryList);
					if (categoryMenuHtml != "") {
						$dropdown.html(categoryMenuHtml).addClass("show");
					}
				});
			}, delay);
		});
		
		$("li.category").on("mouseleave", function() {
			clearTimeout(timer);
			$dropdowns.removeClass("show").empty();
		});

		
		$(document).on("click", "li.category button.dropdown-item", function() {
			let parentPath = $(this).closest("li.dropdown").data("path");
			location.href = "${contextPath}" + parentPath + $(this).data("path");
		});
		
		$("li.test").on("mouseenter", function() {
			$(this).find("div.dropdown-menu").addClass("show");
		});

		$("li.test").on("mouseleave", function() {
			$(this).find("div.dropdown-menu").removeClass("show");
		});
		
	});
	
	function adminApi(url) {
		console.log("## adminApi");
		console.log("> URL = '%s'", url);
		
		$.getJSON(url, function(result) {
			console.log("%c> SUCCESS", "color:green");
			console.log(JSON.stringify(result, null, 2));
		})
		.fail(function(jqXHR, textStatus, error) {
			console.log("%c> ERROR", "color:red");
			let errorResponse = JSON.parse(jqXHR.responseText);
			console.log(JSON.stringify(errorResponse, null, 2));
		});
	}
</script>