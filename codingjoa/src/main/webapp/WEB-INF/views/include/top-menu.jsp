<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- navbar -->
<nav class="navbar navbar-custom navbar-expand-sm">
	<div class="container-fluid">
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}">Codingjoa</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav categories">
				<c:forEach var="category" items="${parentCategories}">
					<li class="nav-item dropdown category" data-code="${category.code}" data-path="${category.path}">
						<a href="${contextPath}${category.path}" class="nav-link"><c:out value="${category.name}"/></a>
						<div class="dropdown-menu">
							<!-- categories -->
							<!-- <a href="/codingjoa/board/?categoryCode=4" class="dropdown-item">공지게시판</a> -->
							<!-- <a href="/codingjoa/board/?categoryCode=5" class="dropdown-item">질문게시판</a> -->
							<!-- <a href="/codingjoa/board/?categoryCode=6" class="dropdown-item">자유게시판</a> -->
						</div>
					</li>
				</c:forEach>
			</ul>
			<ul class="navbar-nav member-utils">
				<sec:authentication property="principal" var="principal"/>
				<c:if test="${empty principal}">
					<li class="nav-item">
						<a href="${empty currentUrl ? contextPath += '/auth/login' : contextPath += '/auth/login?continue=' += currentUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/auth/join" class="nav-link">회원가입</a>
					</li>
				</c:if>
				<sec:authorize access="isAnonymous()">
					<li class="nav-item">
						<a href="${empty currentUrl ? contextPath += '/auth/login' : contextPath += '/auth/login?continue=' += currentUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/auth/join" class="nav-link">회원가입</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<button class="notification"></button>
					</li>
					<li class="nav-item dropdown member-menu">
						<a href="#" class="nav-link dropdown-toggle nav-member-profile" id="navbarDropdown" role="button" data-bs-toggle="dropdown" data-bs-auto-close="outside">
							<img class="nav-member-image" src="${empty principal.imagePath ? contextPath += '/resources/images/img_profile.png' : contextPath += principal.imagePath}">
							<span class="nickname">
								<c:out value="${principal.nickname}"/>
							</span>
						</a>
						<ul class="dropdown-menu dropdown-menu-end">
							<li>
								<div class="dropdown-item">
									<img class="nav-member-image" src="${empty principal.imagePath ? contextPath += '/resources/images/img_profile.png' : contextPath += principal.imagePath}">
									<div class="nickname-email-box">
										<span class="nickname">
											<c:out value="${principal.nickname}"/>
										</span>
										<span class="email">
											<c:out value="${principal.email}"/>
										</span>
									</div>
								</div>
							</li>			
							<hr class="dropdown-divider">
							<sec:authorize access="hasRole('ROLE_ADMIN')">
								<li>
									<a href="${contextPath}/admin" class="dropdown-item admin">관리자</a>
								</li>
								<hr class="dropdown-divider">
							</sec:authorize>
							<li>
								<a href="${contextPath}/user/messages" class="dropdown-item message">메시지</a>
							</li>
							<hr class="dropdown-divider">
							<li>
								<a href="${contextPath}/user/account" class="dropdown-item account">계정 관리</a>
								<a href="#" class="dropdown-item logout">로그아웃</a>
							</li>
						</ul>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>

<script src="${contextPath}/resources/js/utils/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/utils/ajax-setup.js"></script>
<script src="${contextPath}/resources/js/utils/html-creator.js"></script>
<script src="${contextPath}/resources/js/utils/handle-errors.js"></script>
<script src="${contextPath}/resources/js/service/category.js"></script>
<script src="${contextPath}/resources/js/service/auth.js"></script>
<script>
	$(function() {
		const encodedCurrentUrl = "${currentUrl}";
		console.log("## resolved currentUrl from TopMenuInterceptor: '%s'", encodedCurrentUrl);

		let timer; 
		let delay = 200; // 0.2s
		let $dropdowns = $(".category .dropdown-menu");
		
		$(".category").on("mouseenter", function() {
			clearTimeout(timer);
			$dropdowns.removeClass("show").empty();
			$(this).addClass("active");
			
			let parentCode = $(this).data("code");
			let parentPath = $(this).data("path");
			let $dropdown = $(this).find(".dropdown-menu");
			
			timer = setTimeout(function() {
				categoryService.getCategoriesByParent(parentCode, function(result) {
					let categories = result.data;
					let categoryMenuHtml = createCategoryMenuHtml(categories, parentPath);
					if (categoryMenuHtml) {
						$dropdown.html(categoryMenuHtml).addClass("show");
					}
				});
			}, delay);
		});
		
		$(".category").on("mouseleave", function() {
			clearTimeout(timer);
			$(this).removeClass("active");
			$dropdowns.removeClass("show").empty();
		});
		
		$(".logout").on("click", function(e) {
			e.preventDefault();
			authService.logout(function(result) {
				localStorage.removeItem("ACCESS_TOKEN");
				alert(result.message);
				location.href = "${contextPath}";
			});
		});
		
		$(".notification").on("click", function() {
			location.href = "${contextPath}/user/notifications";
		});
	});
</script>