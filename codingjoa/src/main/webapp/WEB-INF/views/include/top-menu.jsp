<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<%-- <c:set var="principal" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" /> --%>

<!-- navbar -->
<nav class="navbar navbar-custom navbar-expand-md">
	<div class="container-fluid px-5">
		<a class="navbar-brand font-weight-bold" href="${contextPath}">Codingjoa</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav mr-auto">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown category mx-2 mt-1" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link">
							<c:out value="${parentCategory.categoryName}"/>
						</a>
					</li>
				</c:forEach>
				<li class="nav-item dropdown test mx-2 mt-1">
					<a href="#" class="nav-link">TEST</a>
					<div class="dropdown-menu">
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/member/account'">/member/account</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/admin'">/admin</button>
						<button class="dropdown-item" type="button" onclick="adminApi('${contextPath}/api/admin')">/api/admin</button>
					</div>
				</li>
			</ul>
			<ul class="navbar-nav ml-auto">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/login?continue=${loginCurrentUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item mx-2 mt-1">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal" var="principal"/>
					<li class="nav-item mx-2">
						<c:choose>
							<c:when test="${not empty principal.imageUrl}">
								<img class="nav-member-image mr-1" id="navMemberImage" src="${principal.imageUrl}">
							</c:when>
							<c:otherwise>
								<img class="nav-member-image mr-1" id="navMemberImage" src="${contextPath}/resources/images/img_profile.png">
							</c:otherwise>
						</c:choose>
						<span class="nav-link font-weight-bold text-body" id="navMemberNickname">
							<c:out value="${principal.nickname}"/>
						</span>
					</li>
					<li class="nav-item">
						<span class="nav-link" style="pointer-events: none;">|</span>
					</li>
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

<script src="${contextPath}/resources/js/render.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script>
	$(function() {
		let timer, delay = 100;
		
		$("li.category").on("mouseenter", function(e) {
			e.stopPropagation();
			$("li.category .dropdown-menu").remove();
			
			let parentCategory = $(this).data("category");
			let $a = $(this).find("a");
			$a.css("color", "black").css("font-weight", "bold");
			
			timer = setTimeout(function() {
				console.log("## getCategoryListByParent");
				let url = "${contextPath}/api/category/" + parentCategory;
				console.log("> URL = '%s'", url);
				
				$.getJSON(url, function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null, 2));
					
					let categoryList = result.data;
					let categoryMenuHtml = createCategoryMenuHtml(categoryList);
					$a.after(categoryMenuHtml);
				})
				.fail(function(jqXHR, textStatus, error) {
					console.log("%c> ERROR", "color:red");
					parseError(jqXHR);
				});
			}, delay);
		});
		
		$("li.category").on("mouseleave", function() {
			clearTimeout(timer);
			$("li.category .dropdown-menu").remove();
			$(this).find("a").css("color", "grey").css("font-weight", "400");
		});

		$(document).on("mouseenter", "li.category button.dropdown-item", function() {
			$(this).css("color", "black").css("font-weight", "bold");
		});

		$(document).on("mouseleave", "li.category button.dropdown-item", function() {
			$(this).css("color", "grey").css("font-weight", "400");
		});
		
		$(document).on("click", "li.category button.dropdown-item", function() {
			let parentPath = $(this).closest("li.dropdown").data("path");
			location.href = "${contextPath}" + parentPath + $(this).data("path");
		});
		
		$("li.test").on("mouseenter", function(e) {
			e.stopPropagation();
			$(this).find("a").css("color", "black").css("font-weight", "bold");
			$dropdown = $(this).find("div.dropdown-menu");
			
			timer = setTimeout(function() {
				$dropdown.addClass("show");
			}, delay);
		});

		$("li.test").on("mouseleave", function(e) {
			clearTimeout(timer);
			$(this).find("a").css("color", "grey").css("font-weight", "400");
			$(this).find("div.dropdown-menu").removeClass("show");
		});
		
		$("li.test button.dropdown-item").on("mouseenter", function() {
			$(this).css("color", "black").css("font-weight", "bold");
		});

		$("li.test button.dropdown-item").on("mouseleave", function() {
			$(this).css("color", "grey").css("font-weight", "400");
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
			parseError(jqXHR);
		});
	}
</script>