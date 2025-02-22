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
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<!-- data-toggle, data-target >> data-bs-toggle, data-bs-target -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script> --> 
<!-- <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script> -->
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
		width: 1020px;
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
		margin-bottom: 2.5rem;
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
			<button type="button" class="btn btn-sm btn-info mx-3" id="adminBoardCriBtn">adminBoardCri</button>
			<a class="nav-link dropdown-toggle nav-member-profile" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown">
				<img class="nav-member-image" 
					src="${empty principal.imageUrl ? '../resources/images/img_profile.png' : principal.imageUrl}">
				<span class="font-weight-bold">
					<c:out value="${principal.nickname}"/>
				</span>
			</a>
				<ul class="dropdown-menu dropdown-menu-end">
					<li>
						<div class="dropdown-item">
							<img class="nav-member-image" 
								src="${empty principal.imageUrl ? '../resources/images/img_profile.png' : principal.imageUrl}">
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
								<a href="${contextPath}/admin/comments" class="nav-link" aria-pressed="false">권한 관리</a>
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
								<a href="${contextPath}/admin/boards"  class="nav-link" aria-pressed="false">게시글 관리</a>
								<a class="nav-link"  aria-pressed="false">댓글 관리</a>
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
								<a href="${contextPath}/admin/test1" class="nav-link" aria-pressed="false">공지사항1</a>
								<a href="${contextPath}/admin/test2" class="nav-link" aria-pressed="false">공지사항2</a>
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
								<a href="${contextPath}/admin/menus" class="nav-link" aria-pressed="false">메뉴 관리</a>
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
			
			<!-- footer -->
			<%-- <c:import url="/WEB-INF/views/include/bottom-menu.jsp"/> --%>
			
		</div> <!-- /Sidenav_conent -->
	</div> <!-- /Sidenav -->
<script>
	$(function() {
		const $contentContainer = $("#contentContainer");
		let adminBoardCri = null;
		
		const pageRouter = new PageRouter();
		pageRouter.addRoute("/admin/boards", function() {
			adminService.getPagedBoards({}, function(result) {
				if (!window.matchMedia("(min-width: 992px)").matches) {
					$("#sidebarToggle").trigger("click");
				}
				
				adminBoardCri = result.data.adminBoardCri;
				let boardsPage = createBoardsPageHtml(result);
				$contentContainer.html(boardsPage);
		});
		
		$("#adminBoardCriBtn").on("click", function() {
			console.log("## adminBoardCri");
			console.log(JSON.stringify(adminBoardCri, null, 2));
		});
		
		$(document).on("click", "#sidenavAccordion a.nav-link", function(e) {
			e.preventDefault();
			$("#sidenavAccordion a.nav-link").attr("aria-pressed", false);
			$(this).attr("aria-pressed", true);
			
			let url = $(this).attr("href");
			if (url) {
				history.pushState({ page : url }, "", url);
				pageRouter.navigate(url);
			}
			
			//let url = $(this).attr("href");
			//history.pushState({ page : url }, "", url); // history.pushState(state, title, url)

			/* adminService.getPagedBoards({}, function(result) {
				if (!window.matchMedia("(min-width: 992px)").matches) {
					$("#sidebarToggle").trigger("click");
				}
				
				adminBoardCri = result.data.adminBoardCri;
				let boardsPage = createBoardsPageHtml(result);
				$contentContainer.html(boardsPage);
			}); */
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
		
		// delete boards
		$(document).on("click", "#deleteBoardsBtn", function() {
			let boardIds = $("input[type='checkbox'][name='boardIds']:checked")
				.get()
				.map(element => element.value);
		
			if (!boardIds.length) {
				alert("삭제할 게시물을 선택해주세요.");
				return;
			}
			
			if (!confirm(boardIds.length + "개의 게시글을 삭제하시겠습니까?")) {
				return;
			}
		
			adminService.deleteBoards(boardIds, function(result) {
				alert(result.message);
				adminService.getPagedBoards({ ...adminBoardCri, page: 1 }, function(result) {
					adminBoardCri = result.data.adminBoardCri;
					let boardsPage = createBoardsPageHtml(result);
					$contentContainer.html(boardsPage);
				});
			});
		});
		
		// click search
		$(document).on("submit", "#adminBoardsForm", function(e) {
			e.preventDefault();
			adminService.getPagedBoards($(this).serializeObject(), function(result) {
				adminBoardCri = result.data.adminBoardCri;
				let boardsPage = createBoardsPageHtml(result);
				$contentContainer.html(boardsPage);
			});
		});
		
		// click pagination
		$(document).on("click", ".board-pagination .page-link", function() {
			adminService.getPagedBoards({ ...adminBoardCri, page: $(this).data("page") }, function(result) {
				adminBoardCri = result.data.adminBoardCri;
				let boardsPage = createBoardsPageHtml(result);
				$contentContainer.html(boardsPage);
			});
		});
		
		// change recordCnt
		$(document).on("change", "#recordCnt", function() {
			adminService.getPagedBoards({ ...adminBoardCri, page: 1 , recordCnt: $(this).val() }, function(result) {
				adminBoardCri = result.data.adminBoardCri;
				let boardsPage = createBoardsPageHtml(result);
				$contentContainer.html(boardsPage);
			});
		});
		
	});
</script>
</html>