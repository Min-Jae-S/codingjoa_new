<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />    
<script>
	alert("게시글이 등록되었습니다.");
	location.href = "${contextPath}/board/read?boardIdx=${writeBoardDto.boardIdx}";
</script>