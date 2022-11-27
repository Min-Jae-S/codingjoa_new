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
		<div class="collapse navbar-collapse" id="navMenu">
			<ul class="navbar-nav">
				<li class="nav-item">
					<a href="${contextPath}" class="nav-link">HOME</a>
				</li>
				<li class="nav-item">
					<a href="#" class="nav-link">게시판</a>
				</li>
			</ul>
			
			<ul class="navbar-nav ml-auto">
				<sec:authorize access="isAnonymous()">
					<li class="nav-item">
						<a href="${contextPath}/member/join" class="nav-link">회원가입</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/member/login" class="nav-link">로그인</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<img src="${contextPath}/resources/image/person.png" style="width: 40px; padding: 0.25rem;">
					</li>
					<li class="nav-item">
						<a class="nav-link text-dark" href="${contextPath}/member/info">
							<span class="font-weight-bold">${principal.member.memberId}</span>
						</a>
					</li>
					<li class="nav-item">
						<span class="nav-link" style="pointer-events: none;">|</span>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/member/security" class="nav-link">계정 관리</a>
					</li>
					<li class="nav-item">
						<a href="${contextPath}/member/logout" class="nav-link">로그아웃</a>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>
