<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>TEST</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align:center; 
		margin-top:200px; 
		font-size: 100px;
		font-weight: bold;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div>
	<p>TEST</p>
	<div class="d-flex justify-content-center">
		<button class="btn btn-primary mx-2" type="button" onclick="sample1()">sample1</button>
		<button class="btn btn-warning mx-2" type="button" onclick="sample2()">sample2</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function sample1() {
		console.log('## sample1');
		console.log('## return ResponseEntity.ok(new Sample("a", "b", "c"))');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/sample1",
			dataType : "json",
			success : function(result) {
				console.log("## success");
				console.log(result);
				console.log("## isJSON = %s", isJSON(result));
			},
			error : function(jqXHR) {
				console.log("## error");
				console.log(jqXHR);
			}
		});
	}

	function sample2() {
		console.log('## sample2');
		console.log('## return new Sample("a", "b", "c")');
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/sample2",
			dataType : "json",
			success : function(result) {
				console.log("## success");
				console.log(result);
				console.log("## isJSON = %s", isJSON(result));
			},
			error : function(jqXHR) {
				console.log("## error");
				console.log(jqXHR);
			}
		});
	}
	
	function isJSON(data) {
		try {
			JSON.parse(data);
			return true;
		} catch(e) {
			return false;
		}
	}
</script>
</body>
</html>