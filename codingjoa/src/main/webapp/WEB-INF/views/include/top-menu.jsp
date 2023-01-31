<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%-- <c:set var="principal" value="${SPRING_SECURITY_CONTEXT.authentication.principal}" /> --%>
<!-- 상단 메뉴 -->
<nav class="navbar navbar-custom navbar-expand-md">
	<div class="container-fluid pl-5 pr-5">
		<a class="navbar-brand font-weight-bold" href="${contextPath}">Codingjoa</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown mx-2" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link">${parentCategory.categoryName}</a>
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
							<span class="font-weight-bold"><sec:authentication property="principal.member.memberId"/></span>
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
		var timer, delay=100;
		
		$(".dropdown").on("mouseenter", function() {
			var parent_category = $(this).data("category");
			var a_tag = $(this).find("a");
			a_tag.css("color", "black").css("font-weight", "bold");
			
			timer = setTimeout(function() {
				$.getJSON("${contextPath}/category/" + parent_category, function(data) {
					if (data.length == 0) return;

					var html = "<div class='dropdown-menu show'>";
					$.each(data, function(i, value) {
						html += "<button class='dropdown-item' type='button' data-path='";
						html += (data[i].categoryCode == data[i].categoryPath) ? 
									"/main?boardCategoryCode=" + data[i].categoryCode : data[i].categoryPath;
						html += "'>" + data[i].categoryName + "</button>";
					});
					html += "</div>";
					
					a_tag.after(html);
				});
			}, delay);
		});
			
		$(".dropdown").on("mouseleave", function() {
			$(".dropdown-menu").remove();
			$(this).find("a").css("color", "grey").css("font-weight", "400");
			clearTimeout(timer);
		});
		
		$(document).on("mouseenter", "button.dropdown-item", function() {
			$(this).css("color", "black")
				   .css("font-weight", "bold")
				   .css("background-color", "transparent");
		});

		$(document).on("mouseleave", "button.dropdown-item", function() {
			$(this).css("color", "grey").css("font-weight", "400");
		});
		
		$(document).on("click", "button.dropdown-item", function() {
			var parent_path = $(this).closest(".dropdown").data("path");
			location.href = "${contextPath}" + parent_path + $(this).data("path");
		}); 
	});
</script>