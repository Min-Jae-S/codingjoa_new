<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>ADMIN | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/sb/css/styles.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<!-- data-toggle, data-target >> data-bs-toggle, data-bs-target -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script> --> 
<!-- <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script> -->
<script src="${contextPath}/resources/sb/js/scripts.js"></script>
<script src="${contextPath}/resources/sb/js/datatables.js"></script>

<script src="${contextPath}/resources/js/admin.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script src="${contextPath}/resources/js/html-creator.js"></script>
<style>
	.admin-content-container .card {
		min-height: 600px;
		min-width: 1150px;
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
	
	.table .email, .table .updated-at {
		color: #969691;
		font-size: 90%;
	}
	
	.table .created-at {
		font-size: 90%;
	}
	
	.table .form-check {
		margin-bottom: 0;
	}
	
	.table .form-check .form-check-input {
		float: none;
	}
	
	.table-content {
		margin-bottom: 2rem;
	} 
	
	.table-footer {
		position: relative;
	}
	
	.table-footer .board-pagination {
		position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
	}
	
	.table-footer .board-pagination .pagination {
		margin-bottom: 0;
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
						<button type="button" class="nav-link" aria-pressed="false" data-url="${contextPath}/api/admin/members">
							<div class="sb-nav-link-icon">
								<i class="fas fa-user-gear"></i>
							</div>
							<span>사용자 관리</span>
						</button>
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
						<div class="collapse" id="collapseContents" data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="${contextPath}/api/admin/boards">게시글 관리</a>
								<a class="nav-link" href="${contextPath}/api/admin/comments">댓글 관리</a>
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
						<div class="collapse" id="collapseNotifications" data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="${contextPath}/api/admin/test1">test1</a>
								<a class="nav-link" href="${contextPath}/api/admin/test2">test2</a>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Systems</div>
						<button type="button" class="nav-link" aria-pressed="false" data-url="${contextPath}/api/admin/categories">
							<div class="sb-nav-link-icon">
								<i class="fa-solid fa-list fa-fw"></i>
							</div>
							<span>카테고리 관리</span>
						</button>
						<div class="sb-sidenav-menu-heading">Analystics</div>
						<button type="button" class="nav-link" aria-pressed="false" data-url="${contextPath}/api/admin/charts">
							<div class="sb-nav-link-icon">
								<i class="fas fa-chart-area fa-fw"></i>
							</div>
							<span>Charts</span>
						</button> 
						<button type="button" class="nav-link" aria-pressed="false" data-url="${contextPath}/api/admin/tables">
							<div class="sb-nav-link-icon">
								<i class="fas fa-table fa-fw"></i>
							</div>
							<span>Tables</span> 
						</button>
					</div>
				</div>
			</nav>
		</div> <!-- /Sidenav_nav -->
		
		<!-- Sidenav_conent -->
		<div id="layoutSidenav_content">
			<main>
				<div class="container-fluid admin-content-container" id="contentContainer">
					<!-- <div class="admin-content-wrap" id="contentWrapDiv"></div> -->
					<!------------------------>
					<!----    contents    ---->
					<!------------------------>
				</div>
			</main>
			
			<!-- footer -->
			<%-- <c:import url="/WEB-INF/views/include/bottom-menu.jsp"/> --%>
			
		</div> <!-- /Sidenav_conent -->
	</div> <!-- /Sidenav -->
<script>
	$(function() {
		const $collapsibleBtns = $('#sidenavAccordion button[data-bs-toggle="collapse"]');
		const $nonCollapsibleBtns = $('#sidenavAccordion button:not([data-bs-toggle="collapse"])');

		const $contentWrapDiv = $('#contentWrapDiv');
		const $contentContainer = $('#contentContainer');
		
		$collapsibleBtns.on("click", function() {
			$("#sidenavAccordion *[aria-pressed='true']").removeAttr("aria-pressed");
		});
		
		$nonCollapsibleBtns.on("click", function() {
			$("#sidenavAccordion *[aria-pressed='true']").removeAttr("aria-pressed");
			$(this).attr("aria-pressed", "true");
			
			$collapsibleBtns.each(function() {
				let target = $(this).attr("data-bs-target") || $(this).attr("href");
				let collapseInstance = bootstrap.Collapse.getInstance($(target)[0]);
				if (collapseInstance) {
					collapseInstance.hide();
				}
			});
			
			$.ajax({
				type : "GET",
				url : $(this).data("url"),
				dataType : "json",
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null, 2));
					
					if (!window.matchMedia("(min-width: 992px)").matches) { // mediaQuery
						$("#sidebarToggle").trigger("click");
					}
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					parseError(jqXHR);
				}
			});
		});
		
		$(document).on("click", "#sidenavAccordion .collapse a.nav-link", function(e) {
			e.preventDefault();
			$("#sidenavAccordion *[aria-pressed='true']").removeAttr("aria-pressed");
			$(this).attr("aria-pressed", "true");
			
			adminService.getPagedBoards(1, 10, function(result) {
				if (!window.matchMedia("(min-width: 992px)").matches) {
					$("#sidebarToggle").trigger("click");
				}
				
				let pagedBoardsHtml = createdPagedBoardsHtml(result);
				$contentContainer.html(pagedBoardsHtml);
			});
		});
	
		$(document).on("change", "#toggleAllBoards", function() {
			console.log("## toggleAllBoards");
			$("input[type='checkbox'][name='boardIds']").prop("checked", this.checked);
			
			let anyChecked = $("input[type='checkbox'][name='boardIds']:checked").length > 0;
			$("#deleteBoardsForm button[type='submit']").prop("disabled", !anyChecked);
		});

		$(document).on("change", "input[type='checkbox'][name='boardIds']", function() {
			console.log("## boardIds chk, idx = %s", $(this).val());
			let totalCnt =  $("input[type='checkbox'][name='boardIds']").length;
			let checkedCnt = $("input[type='checkbox'][name='boardIds']:checked").length;
			$("#toggleAllBoards").prop("checked", totalCnt > 0 && totalCnt == checkedCnt);
			
			let anyChecked = checkedCnt > 0;
			$("#deleteBoardsForm button[type='submit']").prop("disabled", !anyChecked);
		});
		
		$(document).on("submit", "#deleteBoardsForm", function(e) {
			e.preventDefault();
			let boardIds = $("input[type='checkbox'][name='boardIds']:checked")
				.get()
				.map(element => $(element).val());
			console.log(boardIds);
			
			adminService.deleteBoards(boardIds, function(result) {
				alert(result.message);
			});
		});
		
	});
</script>
		=
</html>