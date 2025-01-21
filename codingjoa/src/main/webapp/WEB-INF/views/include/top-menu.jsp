<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!-- navbar -->
<nav class="navbar navbar-custom navbar-expand-sm bg-light">
	<div class="container-fluid px-5">
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}">Codingjoa</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav categories">
				<c:forEach var="parentCategory" items="${parentCategoryList}">
					<li class="nav-item dropdown category" data-category="${parentCategory.categoryCode}" data-path="${parentCategory.categoryPath}">
						<a href="${contextPath}${parentCategory.categoryPath}" class="nav-link"><c:out value="${parentCategory.categoryName}"/></a>
						<div class="dropdown-menu">
							<!-- categories -->
							<!-- <button class="dropdown-item" type="button" data-path="/?boardCategoryCode=4">공지 게시판</button>
							<button class="dropdown-item" type="button" data-path="/?boardCategoryCode=5">질문 게시판</button>
							<button class="dropdown-item" type="button" data-path="/?boardCategoryCode=6">자유 게시판</button>  -->
						</div>
					</li>
				</c:forEach>
				<li class="nav-item dropdown test">
					<a href="#" class="nav-link">TEST</a>
					<div class="dropdown-menu">
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/login'">login</button>
						<button class="dropdown-item" type="button" onclick="mvcLogin()">mvc login</button>
						<button class="dropdown-item" type="button" onclick="invalidLogin()">invalid login</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/logout'">logout</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/member/account'">account</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/error'">error</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/admin'">admin</button>
						<button class="dropdown-item" type="button" onclick="adminApi()">/api/admin</button>
						<button class="dropdown-item" type="button" onclick="savedRequestApi()">/api/saved-request</button>
						<button class="dropdown-item" type="button" onclick="location.href='${contextPath}/test/ws'">/test/ws</button>
					</div>
				</li>
			</ul>
			<ul class="navbar-nav member-utils">
				<sec:authentication property="principal" var="principal"/>
				<c:if test="${empty principal}">
					<li class="nav-item">
						<a href="${contextPath}/login?continue=${loginContinueUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
				</c:if>
				<sec:authorize access="isAnonymous()">
					<li class="nav-item">
						<a href="${contextPath}/login?continue=${loginContinueUrl}" class="nav-link">로그인</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<button href="${contextPath}/notifications" class="notification">
							<span></span>
						</button>
					</li>
					<li class="nav-item dropdown member-menu">
						<a class="nav-link dropdown-toggle nav-member-profile" id="navbarDropdown" href="#" role="button" data-toggle="dropdown">
							<img class="nav-member-image" 
								src ="${empty principal.imageUrl ? contextPath.concat('/resources/images/img_profile.png') : principal.imageUrl}">
							<span class="font-weight-bold">
								<c:out value="${principal.nickname}"/>
							</span>
						</a>
						<ul class="dropdown-menu dropdown-menu-end">
							<li>
								<div class="dropdown-item">
									<img class="nav-member-image" 
										src ="${empty principal.imageUrl ? contextPath.concat('/resources/images/img_profile.png') : principal.imageUrl}">
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
								<a href="${contextPath}/member/message" class="dropdown-item message">메시지</a>
							</li>
							<hr class="dropdown-divider">
							<li>
								<a href="${contextPath}/member/account" class="dropdown-item account">계정 관리</a>
								<a href="${contextPath}/logout?continue=${logoutContinueUrl}" class="dropdown-item logout">로그아웃</a>
							</li>
						</ul>
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
		let timer; 
		let delay = 200; // 0.2s
		let $dropdowns = $(".category .dropdown-menu");
		
		$(".category").on("mouseenter", function() {
			clearTimeout(timer);
			$dropdowns.removeClass("show").empty();
			$(this).addClass("active");
			
			let category = $(this).data("category");
			let $dropdown = $(this).find(".dropdown-menu");
			
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
		
		$(".category").on("mouseleave", function() {
			clearTimeout(timer);
			$(this).removeClass("active");
			$dropdowns.removeClass("show").empty();
		});
		
		$(document).on("click", ".category .dropdown-item", function() {
			let parentPath = $(this).closest(".dropdown").data("path");
			location.href = "${contextPath}" + parentPath + $(this).data("path");
		});
		
		$(".test").on("mouseenter", function() {
			$dropdowns.removeClass("show").empty();
			$(this).addClass("active");
			$(this).find(".dropdown-menu").addClass("show");
		});

		$(".test").on("mouseleave", function() {
			$(this).removeClass("active");
			$(this).find(".dropdown-menu").removeClass("show");
		});
		
		$("button.notification").on("click", function() {
			location.href = "${contextPath}/notifications";
		});
	});
	
	function adminApi() {
		console.log("## adminApi");
		
		let url = "${contextPath}/api/admin";
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

	function savedRequestApi() {
		console.log("## savedRequestApi");
		
		let url = "${contextPath}/api/saved-request";
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
	
	function mvcLogin() {
		const $form = $("<form>", {
            method: "POST",
            action: "${contextPath}/api/login"
        });
		
		//$form.append($('<input>', { type: 'hidden', name: 'username', value: 'user123' }));
		
		// login:195 Form submission canceled because the form is not connected
		$form.appendTo(document.body).submit();
		$form.remove();
	}

	function invalidLogin() {
		let obj = {
			"param1" : "aa",
			"param2" : "bb"
		};
		
		$.ajax({
			type : "POST",
			url : "${contextPath}/api/login",
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(JSON.stringify(errorResponse, null, 2));
			}
		});
	}
</script>