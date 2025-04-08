<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<title>ADMIN | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
<link href="${contextPath}/resources/sb/css/styles.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/sb/js/scripts.js"></script>
<script src="${contextPath}/resources/sb/js/datatables.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="${contextPath}/resources/js/admin.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script src="${contextPath}/resources/js/html-creator.js"></script>
<script src="${contextPath}/resources/js/page-router.js"></script>
<style>
	.admin-content-container .card {
		min-width: 1020px;
		margin: 0 auto;
		padding: 3rem;
		justify-content: center;
	}
	
	.admin-content-container .card * {
		font-size: 15px;
	}
	
	.table thead th {
    	text-align: center;
   	 	vertical-align: middle;
    	border-top: 1px solid black;
    	border-bottom: 1px solid #dee2e6;
    	padding: 0.75rem;
    	white-space: nowrap;
    }
    
    .table tbody td {
    	text-align: center;
    	vertical-align: middle;
    	border-top: none;
    	border-bottom: 1px solid #dee2e6;
    	padding: 0.75rem;
    	height: 75px;
    	white-space: nowrap;
	}

	.table tbody tr:has(button[aria-expanded="true"]) {
    	background-color: #e7f1ff;
	}
	
	.table .created-at {
		font-size: 90%;
	}
	
	.table .updated-at {
		font-size: 90%;
		color: #969691;
	}
	
	.table .linked-at{
		font-size: 90%;
	}
	
	.table.boards-table .email {
		font-size: 90%;
		color: #969691; 
	}
	
	.table .form-check {
		margin-bottom: 0;
	}
	
	.table .form-check .form-check-input {
		float: none;
	}
	
	.form-wrap {
		margin-bottom: 1.5rem;
	}
	
	.table-wrap {
		margin-bottom: 1.5rem;
	}
	
	.board-pagination, .user-pagination {
		display: flex;   
		justify-content: center;
	}
	
	.board-pagination .pagination,
	.user-pagination .pagination {
		margin-bottom: 0;
	}
	
	.page-link {
		padding: .5rem .75rem;
	}
	
	.no-results {
		display: flex;
		flex-direction: column;
		justify-content: center;
		min-height: 250px;
		font-size: 16px !important;
	}
	
	.welcome {
		font-size: 50px;
		font-weight: bold;
	}
	
	#contentContainer .error-wrap {
		margin: auto;
		padding-bottom: 2rem;
	}
	
	#contentContainer .error-code {
		color: #dc3545 !important;
		font-weight: bold;
		font-size: 9.5rem;
		text-align: center;
		line-height: 1;
	}
	
	#contentContainer .error-message {
		font-weight: bold;
		font-size: 1.75rem; /* h3 */
		text-align: center;
	}
	
	#adminBoardsSearchForm input[name='keyword'] {
		height: 38px;
	}
	
	#adminBoardsSearchForm select[name='sort'], #adminBoardsSearchForm select[name='recordCnt'] {
		width: 95px;
		height: 38px;
		margin: 0;
	}
	
	#adminBoardsSearchForm select[name='type'] {
		width: 110px;
		height: 38px;
	}
	
	#adminBoardsSearchForm .categories.dropdown {
		margin-right: 1rem;
	}
	
	#adminBoardsSearchForm .categories.dropdown button.custom-select {
		width: 110px;
		height: 38px;
		text-align: left;
		background-image: url(/codingjoa/resources/images/down-triangle-solid.svg);
		/* background-image: url(../resources/images/down-triangle.svg); */
	}
	
	#adminBoardsSearchForm .categories.dropdown button.dropdown-toggle::after {
		display: none !important;
	}
	
	#adminBoardsSearchForm .categories.dropdown .dropdown-menu {
		padding: 0;
		border-radius: 0.5rem;
	}
	
	#adminBoardsSearchForm .categories.dropdown .dropdown-menu li {
		display: flex;
		align-items: center;
		height: 30px;
		margin: 0;
		color: #495057;
	}
	
	#adminBoardsSearchForm .categories.dropdown .dropdown-menu li:first-child {
		border-radius: .5rem .5rem 0 0;
	}

	#adminBoardsSearchForm .categories.dropdown .dropdown-menu li:last-child {
		border-radius: 0 0 .5rem .5rem;
	}
	
	#adminBoardsSearchForm .categories.dropdown .dropdown-menu li:hover {
		background-color: #0a58ca;
		color: #fff;
	}
	
	#adminBoardsSearchForm .categories.dropdown .form-check .form-check-input {
		margin-left: -0.5em;
		margin-right: 0.5em;
	}

	#adminBoardsSearchForm .categories.dropdown .form-check .form-check-input:checked {
		background-image: url(/codingjoa/resources/images/checked.svg);
		background-color: #fff;
	}
	
	#adminBoardsSearchForm .selected-categories {
		display: flex;
		align-items: center;
		column-gap: 10px;
	}
	
	#adminBoardsSearchForm .selected-categories .badge.category-badge {
		/* color: rgb(0, 196, 113); */
		/* background-color: rgb(229, 249, 241); */
		color: #fff;
		background-color: rgba(108, 117, 125, 1);
		border-radius: 0.5rem;
		line-height: 0;
		font-weight: 600;
		font-size: 95%;
	}
	
	#adminBoardsSearchForm .selected-categories .category-badge-btn {
		width: 12px;
		height: 12px;
		border: 0;
		background-color: transparent;
		padding-left: 0;
		padding-right: 0;
		margin-left: 4px;
		background-image: url(/codingjoa/resources/images/remove.svg);
		background-size: contain;
		background-repeat: no-repeat;
		background-position: center;
	}
	
	img.provider {
		width: 40px;
		height: 40px;
	}
	
	span.provider {
		font-weight: bold;
	}
	
	span.provider {
		font-weight: bold;
	}
	
	.modify-forms-wrap {
		padding: 3rem 6rem;
	}
	
	.modify-forms-wrap form {
		width: 100%; 
		text-align: left;
	}
	
	.modify-forms-wrap > div {
		display: none;
	}
	
	.modify-forms-wrap > div.active {
		display: block;	
	}
	
	.modify-forms-wrap form .form-group {
		margin-bottom: 2.5rem;
	}
	
	.registration-form-wrap form .form-group {
		margin-bottom: 2.5rem;
	}
	
	input[name="zipcode"], input[name="addr"] {
		cursor: pointer;
	}

	input[name="zipcode"][readonly], input[name="addr"][readonly] {
		background-color: #fff;
	}
	
	.form-menu button.nav-link {
		color: #858994;
		border-top-left-radius: 0.5rem !important;
	    border-top-right-radius: 0.5rem !important;
	}
	
	.form-menu button.nav-link.active {
		color: black;
		font-weight: 600;
	}

	.form-menu button.nav-link:hover {
		color: black;
	}

	.form-menu button.nav-link:hover:not(.active) {
		border-color: transparent;
	}
	
	.registration-form-wrap {
		padding: 2rem;
	}
	
	.modal-body {
		padding: 2rem 0;
	}
	
	.modal-backdrop {
		width: 100%; 	/* 100vw -> 100% */
		height: 100%;	/* 100vh -> 100% */
	}
	
}
</style>
</head>
<body class="sb-nav-fixed">
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
		<!-- Navbar Brand-->
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}">Codingjoa</a>
		</div>
		
		<!-- Sidebar Toggle-->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-lg-0" id="sidebarToggle" href="#!">
			<i class="fas fa-bars"></i>
		</button>
		
		<!-- Navbar-->
		<ul class="navbar-nav ms-auto me-2"> <!-- ms-md-0 me-lg-4 -->
			<sec:authentication property="principal" var="principal"/>
			<li class="nav-item dropdown member-menu">
				<a class="nav-link dropdown-toggle nav-member-profile" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown">
					<img class="nav-member-image" src="${empty principal.imagePath ? 
						contextPath += '/resources/images/img_profile.png' : contextPath += principal.imagePath}">
					<span class="font-weight-bold">
						<c:out value="${principal.nickname}"/>
					</span>
				</a>
				<ul class="dropdown-menu dropdown-menu-end">
					<li>
						<div class="dropdown-item">
							<img class="nav-member-image" src="${empty principal.imagePath ? 
								contextPath += '/resources/images/img_profile.png' : contextPath += principal.imagePath}">
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
						<a href="${contextPath}/logout?continue=${logoutContinueUrl}" class="dropdown-item logout">로그아웃</a>
					</li>
				</ul>
			</li>
		</ul>
	</nav>
	
	<!-- Sidenav -->
	<div id="layoutSidenav">
		<!-- Sidenav_nav -->
		<div id="layoutSidenav_nav">
			<nav class="sb-sidenav accordion" id="sidenavAccordion">
				<div class="sb-sidenav-menu">
					<div class="nav">
						<div class="sb-sidenav-menu-heading">Users</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseUserMenu" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-user-gear fa-fw pl-1"></i>
							</div>
							<span>회원 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseUserMenu">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/users" class="nav-link" aria-pressed="false">회원 통합 관리</a>
							</nav>
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/users/register" class="nav-link" aria-pressed="false">회원 등록</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Contents</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseContentMenu" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-columns fa-fw"></i>
							</div>
							<span>콘텐츠 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseContentMenu">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/boards" class="nav-link" aria-pressed="false">게시글 관리</a>
								<a href="${contextPath}/admin/comments" class="nav-link" aria-pressed="false">댓글 관리</a>
							</nav>
						</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseNotificationMenu" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-book-open fa-fw"></i>
							</div>
							<span>공지사항 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseNotificationMenu">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/notifications1" class="nav-link" aria-pressed="false">공지사항1</a>
								<a href="${contextPath}/admin/notifications2" class="nav-link" aria-pressed="false">공지사항2</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Systems</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseSystemMenu" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fa-solid fa-list fa-fw"></i>
							</div>
							<span>시스템 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseSystemMenu"> <!-- data-bs-parent="#sidenavAccordion" -->
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/categories" class="nav-link" aria-pressed="false">카테고리 관리</a>
								<a href="${contextPath}/admin/menues" class="nav-link" aria-pressed="false">메뉴 관리</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Analystics</div>
						<a href="${contextPath}/admin/charts" class="nav-link" aria-pressed="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-chart-area fa-fw"></i>
							</div>
							<span>Charts</span>
						</a>
						<a href="${contextPath}/admin/tables" class="nav-link" aria-pressed="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-table fa-fw"></i>
							</div>
							<span>Tables</span> 
						</a>
					</div>
				</div>
			</nav>
		</div> <!-- /Sidenav_nav -->
		
		<!-- Sidenav_conent -->
		<div id="layoutSidenav_content">
			<main>
				<div class="admin-content-container" id="contentContainer">
					<!------------------------>
					<!----    content     ---->
					<!------------------------>
				</div>
			</main>
		</div> <!-- /Sidenav_conent -->
	</div> <!-- /Sidenav -->
</body>
	
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	$(function() {
		const $contentContainer = $("#contentContainer");
		const pageRouter = new PageRouter();
		
		pageRouter.setErrorHandler(function() {
			let errorPageHtml = createErrorPageHtml();
			$contentContainer.html(errorPageHtml);
		});
		
		pageRouter.addRouter("${contextPath}/admin", function() {
			let welcomePageHtml = createWelcomePageHtml();
			$contentContainer.html(welcomePageHtml);
		});
		
		pageRouter.addRouter("${contextPath}/admin/boards", function(params) {
			adminService.getPagedBoards(params, function(result) {
				localStorage.setItem("adminBoardOptions", JSON.stringify(result.data.options))
				let boardsPageHtml = createBoardsPageHtml(result);
				$contentContainer.html(boardsPageHtml);
			});
		});

		pageRouter.addRouter("${contextPath}/admin/boards/", function(params) {
			adminService.getPagedBoardsBySearch(params, function(result) {
				let boardsTableHtml = createBoardsTableHtml(result.data.pagedBoards);
				let paginationHtml = createPaginationHtml(result.data.pagination);
				$(".table-wrap").html(boardsTableHtml);
				$(".board-pagination").html(paginationHtml);
			});
		});

		pageRouter.addRouter("${contextPath}/admin/users", function(params) {
			adminService.getPagedUsers(params, function(result) {
				let usersPageHtml = createUsersPageHtml(result);
				$contentContainer.html(usersPageHtml);
			});
		});

		pageRouter.addRouter("${contextPath}/admin/users/", function(params) {
			adminService.getPagedUsersBySearch(params, function(result) {
				let usersTableHtml = createUsersTableHtml(result.data.pagedUsers);
				let paginationHtml = createPaginationHtml(result.data.pagination);
				$(".table-wrap").html(usersTableHtml);
				$(".user-pagination").html(paginationHtml);
			});
		});

		pageRouter.addRouter("${contextPath}/admin/users/register", function() {
			let userRegistrationPage = createUserRegistrationPageHtml();
			$contentContainer.html(userRegistrationPage);
		});

		// 1. exclude empty values, null, and undefined
		// 2. convert comma-separated values(categories) into an array
		function transformParams(URLSerarchParams) {
			return Object.fromEntries(
				Array.from(URLSerarchParams)
					.filter(([key, value]) => value != null && value.trim() != "")
					.map(([key, value]) => [key, value.includes(",") ? value.split(",") : value])
				);
		}
		
		function initPage() {
			let params = new URLSearchParams(window.location.search);
			// route(routingPath, pushStatePath, params = {}, pushState = true) 
			pageRouter.route(window.location.pathname, null, transformParams(params), false);
		}
		
		console.log("## initializing page, routing to URL:", window.location.pathname);
		initPage();
		
		$("#sidenavAccordion a.nav-link").on("click", function(e) {
			e.preventDefault();
			$("#sidenavAccordion a.nav-link").attr("aria-pressed", false);
			$(this).attr("aria-pressed", true);
			
			pageRouter.route($(this).attr("href"), null);
			
			if (!window.matchMedia("(min-width: 992px)").matches) {
				$("#sidebarToggle").trigger("click");
			}
		});
		
		$(document).on("change", "#toggleAllUsers", function() {
			$("input[type='checkbox'][name='userIds']").prop("checked", this.checked);
			
			let anyChecked = $("input[type='checkbox'][name='userIds']:checked").length > 0;
			$("#deleteUsersBtn").prop("disabled", !anyChecked);
		});
		
		$(document).on("change", "input[type='checkbox'][name='userIds']", function() {
			let totalCnt =  $("input[type='checkbox'][name='userIds']").length;
			let checkedCnt = $("input[type='checkbox'][name='userIds']:checked").length;
			$("#toggleAllUsers").prop("checked", totalCnt > 0 && totalCnt == checkedCnt);
			
			let anyChecked = checkedCnt > 0;
			$("#deleteUsersBtn").prop("disabled", !anyChecked);
		});
	
		$(document).on("change", "#toggleAllBoards", function() {
			$("input[type='checkbox'][name='boardIds']").prop("checked", this.checked);
			
			let anyChecked = $("input[type='checkbox'][name='boardIds']:checked").length > 0;
			$("#deleteBoardsBtn").prop("disabled", !anyChecked);
		});
		
		$(document).on("change", "input[type='checkbox'][name='boardIds']", function() {
			let totalCnt =  $("input[type='checkbox'][name='boardIds']").length;
			let checkedCnt = $("input[type='checkbox'][name='boardIds']:checked").length;
			$("#toggleAllBoards").prop("checked", totalCnt > 0 && totalCnt == checkedCnt);
			
			let anyChecked = checkedCnt > 0;
			$("#deleteBoardsBtn").prop("disabled", !anyChecked);
		});
		
		// click delete user button
		$(document).on("click", "#deleteUsersBtn", function() {
			let userIds = $("input[type='checkbox'][name='userIds']:checked")
				.get()
				.map(el => el.value);
		
			if (!userIds.length) {
				alert("삭제할 유저를 선택해주세요.");
				return;
			}
			
			if (!confirm(`총 \${userIds.length}명의 유저를 삭제하시겠습니까?`)) {
				return;
			}
		
			adminService.deleteUsers(userIds, function(result) {
				alert(result.message);
				let params = new URLSearchParams(window.location.search);
				params.delete("page");
				
				pageRouter.route("${contextPath}/admin/users/", "${contextPath}/admin/users", transformParams(params));
			});
		});

		// click delete board button
		$(document).on("click", "#deleteBoardsBtn", function() {
			let boardIds = $("input[type='checkbox'][name='boardIds']:checked")
				.get()
				.map(el => el.value);
		
			if (!boardIds.length) {
				alert("삭제할 게시물을 선택해주세요.");
				return;
			}
			
			if (!confirm(`총 \${boardIds.length}개의 게시글을 삭제하시겠습니까?`)) {
				return;
			}
		
			adminService.deleteBoards(boardIds, function(result) {
				alert(result.message);
				let params = new URLSearchParams(window.location.search);
				params.delete("page");
				
				pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
			});
		});
		
		// submit user search
		$(document).on("submit", "#adminUsersSearchForm", function(e) {
			e.preventDefault();
			let formData = $(this).serializeObject();
			let params = new URLSearchParams(formData);
			
			pageRouter.route("${contextPath}/admin/users/", "${contextPath}/admin/users", transformParams(params));
		});
		
		// submit board search
		$(document).on("submit", "#adminBoardsSearchForm", function(e) {
			e.preventDefault();
			let formData = $(this).serializeObject();
			let params = new URLSearchParams(formData);
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// click user pagination
		$(document).on("click", ".user-pagination button.page-link", function() {
			let params = new URLSearchParams(window.location.search);
			params.set("page", $(this).data("page"));
			
			pageRouter.route("${contextPath}/admin/users/", "${contextPath}/admin/users", transformParams(params));
		});
		
		// click board pagination
		$(document).on("click", ".board-pagination button.page-link", function() {
			let params = new URLSearchParams(window.location.search);
			params.set("page", $(this).data("page"));
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// change user recordCnt, sort
		$(document).on("change", "#adminUsersSearchForm select[name='recordCnt'], #adminUsersSearchForm select[name='sort']", function() {
			let params = new URLSearchParams(window.location.search);
			params.set($(this).attr("name"), $(this).val());
			params.delete("page");
			
			pageRouter.route("${contextPath}/admin/users/", "${contextPath}/admin/users", transformParams(params));
		});
		
		// change board recordCnt, sort
		$(document).on("change", "#adminBoardsSearchForm select[name='recordCnt'], #adminBoardsSearchForm select[name='sort']", function() {
			let params = new URLSearchParams(window.location.search);
			params.set($(this).attr("name"), $(this).val());
			params.delete("page");
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// change board categories
		$(document).on("change", "#adminBoardsSearchForm input[name='categories']", function() {
			let categoryId = $(this).val();
			
			if ($(this).prop("checked")) {
				let categoryBadgeHtml = createCategoryBadgeHtml(categoryId);
				$(".selected-categories").append(categoryBadgeHtml);
			} else {
				let $categoryBadge = $(".selected-categories")
					.find(`button[data-category-id='\${categoryId}']`)
					.closest(".category-badge");
				$categoryBadge.remove();
			}
			
			let categoryIds = $("input[name='categories']:checked").map((index, el) => el.value).get();
			let params = new URLSearchParams(window.location.search);
			params.set("categories", categoryIds);
			params.delete("page");
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// click category badge button
		$(document).on("click", "#adminBoardsSearchForm button[name='categoryBadgeBtn']", function() {
			let categoryId = $(this).data("category-id");
			let $categoryCheck = $(`input[name='categories'][value='\${categoryId}']`);
			$categoryCheck.prop("checked", false).trigger("change");
			
			$(this).closest(".category-badge").remove();
		});
		
		$(document).on("click", "button[name='searchAddrBtn'], input[name='zipcode'], input[name='addr']", function() {
			execPostcode();
		});
		
		$(document).on("focus", "input[name='zipcode'], input[name='addr']", function() {
			$(this).blur();
		});
		
		// open modal
		$(document).on("click", "button[name='openUserEditModal']", function() {
			let userId = $(this).data("user-id");
			adminService.getAdminUser(userId, function(result) {
				let adminUser = result.data;
				let modalHtml = createUserEditModalHtml(adminUser);
				$("body").append(modalHtml);
				$("#userEditModal").modal("show");
			});
		});
		
		$(document).on("hide.bs.modal", "#userEditModal", function() {
			console.log("## hide.bs.modal: removing focus from all buttons and inputs");
			$(this).find("button, input").each(function() {
				$(this).blur();
			});
		});
		
		$(document).on("hidden.bs.modal", "#userEditModal", function() {
			console.log("## hidden.bs.modal: modal disposed and removed from DOM");
			$(this).modal("dispose");
			$(this).remove();
			// search again
		});
		
		// click form-menu button 
		$(document).on("click", "#userEditModal .form-menu button.nav-link", function() {
			$("#userEditModal").find(".modify-forms-wrap > div").removeClass("active");
			
			let targetName = $(this).data("target");
			console.log("## activate target:", targetName)
			
			let $target = $("#userEditModal").find(`div[name='\${targetName}']`);
			$target.addClass("active");
			$target.find("form").trigger("reset");
			
			$("#userEditModal .form-menu button.nav-link").removeClass("active");
			$(this).addClass("active");
		}); 
		
		// submit userNicknameForm 
		$(document).on("submit", "#userEditModal form[name='userNicknameForm']", function(e) {
			e.preventDefault();
			let $nickname = $(this).find("input[name='nickname']");
			let userId = $(this).closest(".modal").data("user-id");
			let formData = $(this).serializeObject();
			
			adminService.updateNickname(userId, formData, function(result) {
				alert(result.message);
				adminService.getAdminUser(userId, function(result) {
					let adminUser = result.data;
					$nickname.val(adminUser.nickname).prop("defaultValue", adminUser.nickname);
				});
			});
		});
		
		// submit userEmailForm 
		$(document).on("submit", "#userEditModal form[name='userEmailForm']", function(e) {
			e.preventDefault();
			let $email = $(this).find("input[name='email']");
			let userId = $(this).closest(".modal").data("user-id");
			let formData = $(this).serializeObject();
			
			adminService.updateEmail(userId, formData, function(result) {
				alert(result.message);
				adminService.getAdminUser(userId, function(result) {
					let adminUser = result.data;
					$email.val(adminUser.email).prop("defaultValue", adminUser.email);
				});
			});
		});
		
		// submit userAddrForm 
		$(document).on("submit", "#userEditModal form[name='userAddrForm']", function(e) {
			e.preventDefault();
			let $zipcode = $(this).find("input[name='zipcode']");
			let $addr = $(this).find("input[name='addr']");
			let $addrDetail = $(this).find("input[name='addrDetail']");
			let userId = $(this).closest(".modal").data("user-id");
			let formData = $(this).serializeObject();
			
			adminService.updateAddr(userId, formData, function(result) {
				alert(result.message);
				adminService.getAdminUser(userId, function(result) {
					let adminUser = result.data;
					$zipcode.val(adminUser.zipcode).prop("defaultValue", adminUser.zipcode);
					$addr.val(adminUser.addr).prop("defaultValue", adminUser.addr);
					$addrDetail.val(adminUser.addrDetail).prop("defaultValue", adminUser.addrDetail);
				});
			});
		});

		// submit userAgreeForm 
		$(document).on("submit", "#userEditModal form[name='userAgreeForm']", function(e) {
			e.preventDefault();
			let $agree = $(this).find("input[name='agree']");
			let userId = $(this).closest(".modal").data("user-id");
			let formData = {
				agree : $(this).find("input[name='agree']").prop("checked")
			};
			
			adminService.updateAgree(userId, formData, function(result) {
				alert(result.message);
				adminService.getAdminUser(userId, function(result) {
					let adminUser = result.data;
					$agree.prop("checked", adminUser.agree).prop("defaultChecked", adminUser.agree);
				});
			});
		});

		// submit userPasswordForm
		$(document).on("submit", "#userEditModal form[name='userPasswordForm']", function(e) {
			e.preventDefault();
			let $form = $(this);
			let userId = $(this).closest(".modal").data("user-id");
			let formData = $(this).serializeObject();
			
			adminService.updatePassword(userId, formData, function(result) {
				alert(result.message);
				$form[0].reset();
			});
		});
		
		// submit userAuthForm
		$(document).on("submit", "#userEditModal form[name='userAuthForm']", function(e) {
			e.preventDefault();
			let $adminCheckBox = $(this).find("input[name='roles'][value='ROLE_ADMIN']");
			let userId = $(this).closest(".modal").data("user-id");
			let roles = $(this).find("input[name='roles']:checked")
				.get()
				.map(el => el.value);
			let formData = { "roles" : roles };
			
			adminService.updateAuth(userId, formData, function(result) {
				alert(result.message);
				adminService.getAdminUser(userId, function(result) {
					let adminUser = result.data;
					let hasAdminRole = adminUser.roles.includes("ROLE_ADMIN");
					$adminCheckBox.prop("checked", hasAdminRole).prop("defaultChecked", hasAdminRole);
				});
			});
		});
		
		// submit userRegistrationForm
		$(document).on("submit", "#userRegistrationForm", function(e) {
			e.preventDefault();
			let $form = $(this);
			let formData = $(this).serializeObject();
			let roles = $(this).find("input[name='roles']:checked")
				.get()
				.map(el => el.value);
			formData.roles = roles;
			
			adminService.registerUser(formData, function(result) {
				alert(result.message);
				$form.trigger("reset");
			});
		});
		
		// reset userRegistrationForm 
		$(document).on("reset", "#userRegistrationForm", function() {
			$(this).find(".error").remove();
		});
		
	});
	
	function execPostcode() {
	    new daum.Postcode({
	        oncomplete: function(data) {
	            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
	            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	            let addr = ''; // 주소 변수
	
	            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	                addr = data.roadAddress;
	            } else { // 사용자가 지번 주소를 선택했을 경우(J)
	                addr = data.jibunAddress;
	            }
	
	            // 우편번호와 주소 정보를 해당 필드에 넣는다.
	            document.querySelector('.modal.show input[name="zipcode"]').value = data.zonecode;
	            document.querySelector('.modal.show input[name="addr"]').value = addr;
	            
	            // 커서를 상세주소 필드로 이동한다.
	            document.querySelector('.modal.show input[name="addrDetail"]').focus();
	        }
	    }).open();
	}
</script>
</html>