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
<!-- data-toggle, data-target -->
<!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script> --> 
<!-- <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script> -->

<!-- data-bs-toggle, data-bs-target -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/sb/js/scripts.js"></script>
<script src="${contextPath}/resources/sb/js/datatables.js"></script>
<style>
	/* ... */
</style>
</head>
<body class="sb-nav-fixed">
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark px-5">
		<!-- Navbar Brand-->
		<div class="logo-wrap">
			<a class="navbar-brand" href="${contextPath}">Codingjoa</a>
		</div>
		
		<!-- Sidebar Toggle-->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!">
			<i class="fas fa-bars"></i>
		</button>
		
		<!-- Navbar-->
		<ul class="navbar-nav ms-auto me-3"> <!-- ms-md-0 me-lg-4 -->
			<sec:authentication property="principal" var="principal"/>
			<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown">
				<i class="fas fa-user-large fa-fw"></i>
			</a>
				<ul class="dropdown-menu dropdown-menu-end">
					<li>
						<div class="dropdown-item">
							<img class="nav-member-image" src="${principal.imageUrl}">
							<div>
								<span class="font-weight-bold text-body">
									<c:out value="${principal.nickname}"/>
								</span></br>
								<span class="text-muted">
									<c:out value="${principal.email}"/>
								</span>
							</div>
						</div>
					</li>			
					<li>
						<hr class="dropdown-divider">
					</li>
					<li class="pt-2">
						<a href="${contextPath}/member/account" class="dropdown-item my-info">
							<!-- <i class="fa-solid fa-user-large fa-fw mr-3"></i> -->계정 관리
						</a>
					</li>
					<li class="pb-2">
						<a href="${contextPath}/logout?continue=${logoutContinueUrl}" class="dropdown-item logout">
							<!-- <i class="fa-solid fa-arrow-right-from-bracket fa-fw mr-3"></i> -->로그아웃
						</a>
					</li>
				</ul>
			</li>
		</ul>
	</nav>
	
	<!-- Sidenav -->
	<div id="layoutSidenav">
		<!-- Sidenav_nav -->
		<div id="layoutSidenav_nav">
			<nav class="sb-sidenav accordion sb-sidenav-light" id="sidenavAccordion">
				<div class="sb-sidenav-menu">
					<div class="nav">
						<div class="sb-sidenav-menu-heading">Users</div>
						<a class="nav-link" href="index.html">
							<div class="sb-nav-link-icon">
								<i class="fas fa-tachometer-alt fa-fw"></i>
							</div>
							<span>사용자 관리</span>
						</a>
						<div class="sb-sidenav-menu-heading">Contents</div>
						<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts">
							<div class="sb-nav-link-icon">
								<i class="fas fa-columns fa-fw"></i>
							</div>
							<span>콘텐츠 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-down fa-fw"></i>
							</div>
						</a>
						<div class="collapse" id="collapseLayouts" data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav">
								<a class="nav-link" href="layout-static.html">Static Navigation</a>
								<a class="nav-link" href="layout-sidenav-light.html">Light Sidenav</a>
							</nav>
						</div>
						<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapsePages">
							<div class="sb-nav-link-icon">
								<i class="fas fa-book-open fa-fw"></i>
							</div>
							<span>공지사항 관리</span>
							<div class="sb-sidenav-collapse-arrow">
								<i class="fas fa-angle-down fa-fw"></i>
							</div>
						</a>
						<div class="collapse" id="collapsePages" data-bs-parent="#sidenavAccordion">
							<nav class="sb-sidenav-menu-nested nav accordion" id="sidenavAccordionPages">
								<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAuth">
									<span>Authentication</span>
									<div class="sb-sidenav-collapse-arrow">
										<i class="fas fa-angle-down fa-fw"></i>
									</div>
								</a>
								<div class="collapse" id="pagesCollapseAuth" data-bs-parent="#sidenavAccordionPages">
									<nav class="sb-sidenav-menu-nested nav">
										<a class="nav-link" href="login.html">Login</a> 
										<a class="nav-link" href="register.html">Register</a>
										<a class="nav-link" href="password.html">Forgot Password</a>
									</nav>
								</div>
								<a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#pagesCollapseError">
									<span>Error</span>
									<div class="sb-sidenav-collapse-arrow">
										<i class="fas fa-angle-down fa-fw"></i>
									</div>
								</a>
								<div class="collapse" id="pagesCollapseError" data-bs-parent="#sidenavAccordionPages">
									<nav class="sb-sidenav-menu-nested nav">
										<a class="nav-link" href="401.html">401 Page</a> 
										<a class="nav-link" href="404.html">404 Page</a> 
										<a class="nav-link" href="500.html">500 Page</a>
									</nav>
								</div>
							</nav>
						</div>
						<div class="sb-sidenav-menu-heading">Analystics</div>
						<a class="nav-link" href="charts.html">
							<div class="sb-nav-link-icon">
								<i class="fas fa-chart-area fa-fw"></i>
							</div>
							<span>Charts</span>
						</a> 
						<a class="nav-link" href="tables.html">
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
				<div class="container-fluid px-4">
					<h1 class="mt-4">Sidenav Light</h1>
					<ol class="breadcrumb mb-4">
						<li class="breadcrumb-item"><a href="index.html">Dashboard</a></li>
						<li class="breadcrumb-item active">Sidenav Light</li>
					</ol>
					<div class="card mb-4">
						<div class="card-body">
							This page is an example of using the light side navigation option. 
							By appending the <code>.sb-sidenav-light</code> class to the <code>.sb-sidenav</code> class, 
							the side navigation will take on a light color scheme. 
							The <code>.sb-sidenav-dark</code> is also available for a darker option.
						</div>
					</div>
				</div>
			</main>
			<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
		</div> <!-- /Sidenav_conent -->
	</div> <!-- /Sidenav -->
</body>
</html>