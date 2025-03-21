<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="defaultImageUrl" value="${pageContext.request.contextPath}/resources/images/img_profile.png"/>
<!DOCTYPE html>
<html>
<head>
<title>ADMIN | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
    }
    
    .table tbody td {
    	text-align: center;
    	vertical-align: middle;
    	border-top: none;
    	border-bottom: 1px solid #dee2e6;
    	padding: 0.75rem;
	}
	
	.table .created-at {
		/* font-size: 90%; */
	}
	
	.table .updated-at {
		/* font-size: 90%; */
		color: #969691;
	}
	
	.table .email {
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
	
	.board-pagination {
		display: flex;   
		justify-content: center;
	}
	
	.board-pagination .pagination {
		margin-bottom: 0;
	}
	
	.page-link {
		padding: .5rem .75rem;
	}
	
	.no-board {
		display: flex;
		flex-direction: column;
		justify-content: center;
		min-height: 250px;
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
	
	#adminBoardsForm #keyword {
		height: 38px;
	}
	
	#adminBoardsForm #sort, #adminBoardsForm #recordCnt {
		width: 95px;
		height: 38px;
		margin: 0;
	}
	
	#adminBoardsForm #type {
		width: 110px;
		height: 38px;
	}
	
	#adminBoardsForm .categories.dropdown {
		margin-right: 1rem;
	}
	
	#adminBoardsForm .categories.dropdown button.custom-select {
		width: 110px;
		height: 38px;
		text-align: left;
		background-image: url(/codingjoa/resources/images/down-triangle-solid.svg);
		/* background-image: url(../resources/images/down-triangle.svg); */
	}
	
	#adminBoardsForm .categories.dropdown button.dropdown-toggle::after {
		display: none !important;
	}
	
	#adminBoardsForm .categories.dropdown .dropdown-menu {
		padding: 0;
		border-radius: 0.5rem;
	}
	
	#adminBoardsForm .categories.dropdown .dropdown-menu li {
		display: flex;
		align-items: center;
		height: 30px;
		margin: 0;
		color: #495057;
	}
	
	#adminBoardsForm .categories.dropdown .dropdown-menu li:first-child {
		border-radius: .5rem .5rem 0 0;
	}

	#adminBoardsForm .categories.dropdown .dropdown-menu li:last-child {
		border-radius: 0 0 .5rem .5rem;
	}
	
	#adminBoardsForm .categories.dropdown .dropdown-menu li:hover {
		background-color: #0a58ca;
		color: #fff;
	}
	
	#adminBoardsForm .categories.dropdown .form-check .form-check-input {
		margin-left: -0.5em;
		margin-right: 0.5em;
	}

	#adminBoardsForm .categories.dropdown .form-check .form-check-input:checked {
		background-image: url(/codingjoa/resources/images/checked.svg);
		background-color: #fff;
	}
	
	#adminBoardsForm .selected-categories {
		display: flex;
		align-items: center;
		column-gap: 10px;
	}
	
	#adminBoardsForm .selected-categories .badge.category-badge {
		/* color: rgb(0, 196, 113); */
		/* background-color: rgb(229, 249, 241); */
		color: #fff;
		background-color: rgba(108, 117, 125, 1);
		border-radius: 0.5rem;
		line-height: 0;
		font-weight: 600;
		font-size: 95%;
	}
	
	#adminBoardsForm .selected-categories .category-badge-btn {
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

}
</style>
</head>
<body class="sb-nav-fixed">
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
		<!-- Navbar Brand-->
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}/admin">Codingjoa</a>
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
					<img class="nav-member-image" 
						src="${empty principal.imageUrl ? defaultImageUrl : principal.imageUrl}">
					<span class="font-weight-bold">
						<c:out value="${principal.nickname}"/>
					</span>
				</a>
				<ul class="dropdown-menu dropdown-menu-end">
					<li>
						<div class="dropdown-item">
							<img class="nav-member-image" 
								src="${empty principal.imageUrl ? defaultImageUrl : principal.imageUrl}">
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
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseMembers" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-user-gear fa-fw pl-1"></i>
							</div>
							<span>회원 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseMembers">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/members" class="nav-link" aria-pressed="false">회원 정보 관리</a>
								<a href="${contextPath}/admin/members/role" class="nav-link" aria-pressed="false">권한 관리</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Contents</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseContents" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-columns fa-fw"></i>
							</div>
							<span>콘텐츠 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseContents">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/boards" class="nav-link" aria-pressed="false">게시글 관리</a>
								<a href="${contextPath}/admin/comments" class="nav-link" aria-pressed="false">댓글 관리</a>
							</nav>
						</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseNotifications" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fas fa-book-open fa-fw"></i>
							</div>
							<span>공지사항 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseNotifications">
							<nav class="sb-sidenav-menu-nested nav">
								<a href="${contextPath}/admin/notifications1" class="nav-link" aria-pressed="false">공지사항1</a>
								<a href="${contextPath}/admin/notifications2" class="nav-link" aria-pressed="false">공지사항2</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Systems</div>
						<button type="button" class="nav-link collapsed" data-bs-toggle="collapse" data-bs-target="#collapseSystems" aria-expanded="false">
							<div class="sb-nav-link-icon">
								<i class="fa-solid fa-list fa-fw"></i>
							</div>
							<span>시스템 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-up fa-fw"></i>
							</div>
						</button>
						<div class="collapse" id="collapseSystems"> <!-- data-bs-parent="#sidenavAccordion" -->
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
		
		// click delete board btn
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
		
		// click search
		$(document).on("submit", "#adminBoardsForm", function(e) {
			e.preventDefault();
			let formData = $(this).serializeObject();
			let params = new URLSearchParams(formData);
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// click pagination
		$(document).on("click", ".board-pagination button.page-link", function() {
			console.log("## pagination clicked...");
			let params = new URLSearchParams(window.location.search);
			params.set("page", $(this).data("page"));
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// change recordCnt, sort
		$(document).on("change", "#recordCnt, #sort", function() {
			let params = new URLSearchParams(window.location.search);
			params.set($(this).attr("name"), $(this).val());
			params.delete("page");
			
			pageRouter.route("${contextPath}/admin/boards/", "${contextPath}/admin/boards", transformParams(params));
		});
		
		// change categories
		$(document).on("change", "input[name='categories']", function() {
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
		
		// click category badge btn
		$(document).on("click", "button[name='categoryBadgeBtn']", function() {
			let categoryId = $(this).data("category-id");
			let $categoryCheck = $(`input[name='categories'][value='\${categoryId}']`);
			$categoryCheck.prop("checked", false).trigger("change");
			
			$(this).closest(".category-badge").remove();
		});
		
	});
</script>
</html>