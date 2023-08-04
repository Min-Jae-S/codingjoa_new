<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- <c:set var="principal" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal}" /> --%>
<!-- 상단 메뉴 -->
<nav class="navbar navbar-custom navbar-expand-md">
	<div class="container-fluid px-5">
		<a class="navbar-brand font-weight-bold" href="${contextPath}">Codingjoa</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav mr-auto">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown mx-2" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link">
							<c:out value="${parentCategory.categoryName}"/>
						</a>
					</li>
				</c:forEach>
			</ul>
			
			<ul class="navbar-nav ml-auto">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/login" class="nav-link">로그인</a>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<img class="m-1 nav-profile-image" src="${contextPath}/resources/image/person.png">
					</li>
					<li class="nav-item mr-2">
						<a class="nav-link text-body" href="${contextPath}/member/account/info">
							<span class="font-weight-bold">
								<sec:authentication property="principal.member.memberId"/>
							</span>
						</a>
					</li>
					<li class="nav-item">
						<span class="nav-link" style="pointer-events: none;">|</span>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/account" class="nav-link">계정 관리</a>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/logout" class="nav-link">로그아웃</a>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>

<script>
	$(function() {
		let timer; 
		let delay = 100;
		
		$(".navbar-nav .dropdown").on("mouseenter", function() {
			let parent_category = $(this).data("category");
			let $a = $(this).find("a");
			$a.css("color", "black").css("font-weight", "bold");
			
			timer = setTimeout(function() {
				$.getJSON("${contextPath}/category/" + parent_category, function(data) {
					if (data.length == 0) return;

					let html = "<div class='dropdown-menu show'>";
					$.each(data, function(i, value) {
						html += "<button class='dropdown-item' type='button' data-path='";
						html += (data[i].categoryCode == data[i].categoryPath) ? 
									"/?boardCategoryCode=" + data[i].categoryCode : data[i].categoryPath;
						html += "'>" + data[i].categoryName + "</button>";
					});
					html += "</div>";
					$a.after(html);
				});
			}, delay);
		});
			
		$(".navbar-nav .dropdown").on("mouseleave", function() {
			$(this).find(".dropdown-menu").remove();
			$(this).find("a").css("color", "grey").css("font-weight", "400");
			clearTimeout(timer);
		});
		
		$(document).on("mouseenter", ".navbar-nav button.dropdown-item", function() {
			$(this).css("color", "black")
					.css("font-weight", "bold")
					.css("background-color", "transparent");
		});

		$(document).on("mouseleave", ".navbar-nav button.dropdown-item", function() {
			$(this).css("color", "grey").css("font-weight", "400");
		});
		
		$(document).on("click", ".navbar-nav button.dropdown-item", function() {
			let parent_path = $(this).closest(".dropdown").data("path");
			location.href = "${contextPath}" + parent_path + $(this).data("path");
		}); 
	});
</script>