<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="principal" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" />
<!-- 상단 메뉴 -->
<nav class="navbar navbar-custom navbar-expand-md">
	<div class="container-fluid pl-5 pr-5">
		<a class="navbar-brand font-weight-bold" href="${contextPath}">Codingjoa</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav">
				<c:forEach var="parentCategory" items="${parentCategory}">
					<li class="nav-item dropdown mx-2" data-category="${parentCategory.categoryCode}">
						<a href="#" class="nav-link">${parentCategory.categoryName}</a>
					</li>
				</c:forEach>
			</ul>
			
			<ul class="navbar-nav ml-auto">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/login" class="nav-link">로그인</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<img src="${contextPath}/resources/image/person.png" style="width: 40px; padding: 0.25rem;">
					</li>
					<li class="nav-item mr-2">
						<a class="nav-link text-dark" href="${contextPath}/member/info">
							<span class="font-weight-bold">${principal.member.memberId}</span>
						</a>
					</li>
					<li class="nav-item">
						<span class="nav-link" style="pointer-events: none;">|</span>
					</li>
					<li class="nav-item mx-2">
						<a href="${contextPath}/member/security" class="nav-link">계정 관리</a>
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
		$(".dropdown").on("mouseenter", function() {
			var category = $(this).data("category");
			var attached = $(this).find("a");
			
			attached.css("color", "black");
			attached.css("font-weight", "bold");

			$.getJSON("${contextPath}/category/" + category, function(data) {
				if(data.length != 0) {
					var html = "<div class='dropdown-menu show'>";
					
					$.each(data, function(index, value) {
						html += "<button class='dropdown-item' type='button'>";
						html += data[index].categoryName;
						html += "</button>";
					});
					
					html += "</div>";
					attached.after(html);
				}
			});
		});
		
		$(".dropdown").on("mouseleave", function() {
			var attached = $(this).find("a");
			attached.css("color", "grey");
			attached.css("font-weight", "400");
			$(this).find(".dropdown-menu").remove();
		}); 
		
		$(document).on("mouseenter", "button.dropdown-item", function() {
			$(this).css("color", "black");
			$(this).css("font-weight", "bold");
			$(this).css("background-color", "transparent");
		});

		$(document).on("mouseleave", "button.dropdown-item", function() {
			$(this).css("font-weight", "400");
			$(this).css("color", "grey");
		});
		
		
	});
</script>