<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- <c:set var="principal" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" /> --%>

<!-- navbar -->
<nav class="navbar navbar-custom navbar-expand-md">
	<div class="container-fluid px-5">
		<a class="navbar-brand font-weight-bold" href="${contextPath}">Codingjoa</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav mr-auto">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown mx-2 mt-1" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link">
							<c:out value="${parentCategory.categoryName}"/>
						</a>
					</li>
				</c:forEach>
			</ul>
			<ul class="navbar-nav ml-auto">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item mx-2 mt-1">
						<c:choose>
							<c:when test="${not empty loginMenuUrl}">
								<a href="${contextPath}/login?continue=${loginMenuUrl}" class="nav-link">로그인</a>
							</c:when>
							<c:otherwise>
								<a href="${contextPath}/login" class="nav-link">로그인</a>
							</c:otherwise>
						</c:choose>
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
						<span class="nav-link font-weight-bold text-body">
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
						<c:choose>
							<c:when test="${not empty currentUrl}">
								<a href="${contextPath}/logout?redirect=${currentUrl}" class="nav-link">로그아웃</a>
							</c:when>
							<c:otherwise>
								<a href="${contextPath}/logout" class="nav-link">로그아웃</a>
							</c:otherwise>
						</c:choose>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>

<script>
	$(function() {
		let timer;
		let showDelay = 300;
		
		$(".navbar-nav li.dropdown").on("mouseenter", function(e) {
			e.stopPropagation();
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
					if (categoryList.length == 0) {
						return;
					}

					let html = "<div class='dropdown-menu show'>";
					$.each(categoryList, function(i, value) {
						let categoryCode = categoryList[i].categoryCode;
						let categoryPath = categoryList[i].categoryPath;
						let categoryName = categoryList[i].categoryName;
						html += "<button class='dropdown-item' type='button' data-path='";
						html += (categoryCode == categoryPath) ? "/?boardCategoryCode=" + categoryCode : categoryPath;
						html += "'>" + categoryName + "</button>";
					});
					html += "</div>";
					$a.after(html);
				})
				.fail(function(jqXHR, textStatus, error) {
					console.log("%c> ERROR", "color:red");
					console.log(jqXHR);
				});
			}, showDelay);
		});
			
		$(".navbar-nav li.dropdown").on("mouseleave", function() {
			clearTimeout(timer);
			$(this).find("a").css("color", "grey").css("font-weight", "400");
			$(this).find(".dropdown-menu").remove();
		});

		$(document).on("mouseenter", ".navbar-nav button.dropdown-item", function() {
			$(this).css("color", "black").css("font-weight", "bold").css("background-color", "transparent");
		});

		$(document).on("mouseleave", ".navbar-nav button.dropdown-item", function() {
			$(this).css("color", "grey").css("font-weight", "400");
		});
		
		$(document).on("click", ".navbar-nav button.dropdown-item", function() {
			let parentPath = $(this).closest("li.dropdown").data("path");
			location.href = "${contextPath}" + parentPath + $(this).data("path");
		});
	});
</script>