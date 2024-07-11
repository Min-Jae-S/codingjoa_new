<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>jwt.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		padding-left: 1.5rem;
		padding-right: 1.5rem;
	}
	
	.test-btn, div.input-group {
		width: 230px;
	}
	
	div.form-check {
		font-size: 1rem;
		font-weight: 400;
	}
	
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>jwt.jsp</p>
	<div class="test d-flex justify-content-between mt-5 mb-5">
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test1()">test1</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test2()">test2</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test4()">test4</button>
	</div>
	<div class="test d-flex flex-column mb-5">
		<button class="btn btn-primary btn-lg mx-3 mb-2" onclick="test5()">
			<span>test5 : send header</span>
		</button>
		<div class="px-3 d-flex justify-content-around">
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio1" value="undefined" checked>
			  <label class="form-check-label" for="test5Radio1">no header</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio2" value="">
			  <label class="form-check-label" for="test5Radio2">Authorization: blank header</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test5Radio" id="test5Radio3" value="Authorization">
			  <label class="form-check-label" for="test5Radio3">Authorization</label>
			</div>
		</div>
	</div>
	<div class="test d-flex flex-column mb-5">
		<button class="btn btn-primary btn-lg mx-3 mb-2" onclick="test6()">
			<span>test6 : send JWT</span>
		</button>
		<div class="px-3 d-flex justify-content-around">
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test6Radio" id="test6Radio1" value="undefiend" checked>
			  <label class="form-check-label" for="test6Radio1">no header</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test6Radio" id="test6Radio2" value="">
			  <label class="form-check-label" for="test6Radio2">empty</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test6Radio" id="test6Radio3" value="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWoyMDIyOCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODg4OC9jb2Rpbmdqb2EiLCJpYXQiOjE3MjAyNDI3MTYsImV4cCI6MTcyMDI0NDUxNiwicm9sZSI6IlJPTEVfTUVNQkVSIn0.Z-7IHPTrEXLct_DqFRRdkuW8CukO-Q2uolqp_Zu-OKs">
			  <label class="form-check-label" for="test6Radio3">no Bearer</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test6Radio" id="test6Radio4" value="Bearer  eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWoyMDIyOCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODg4OC9jb2Rpbmdqb2EiLCJpYXQiOjE3MjAyNDI3MTYsImV4cCI6MTcyMDI0NDUxNiwicm9sZSI6IlJPTEVfTUVNQkVSIn0.Z-7IHPTrEXLct_DqFRRdkuW8CukO-Q2uolqp_Zu-OKs">
			  <label class="form-check-label" for="test6Radio4">Bearer + blank + JWT</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test6Radio" id="test6Radio5" value="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWoyMDIyOCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODg4OC9jb2Rpbmdqb2EiLCJpYXQiOjE3MjAyNDI3MTYsImV4cCI6MTcyMDI0NDUxNiwicm9sZSI6IlJPTEVfTUVNQkVSIn0.Z-7IHPTrEXLct_DqFRRdkuW8CukO-Q2uolqp_Zu-OKs">
			  <label class="form-check-label" for="test6Radio5">Bearer + JWT</label>
			</div>
		</div>
	</div>
	<div class="test d-flex justify-content-between mt-5 mb-5">
		<button class="btn btn-secondary btn-lg test-btn mx-3" onclick="getInMemoryJwt()">memory JWT</button>
		<button class="btn btn-warning btn-lg test-btn mx-3" onclick="createJwt()">create JWT</button>
		<button class="btn btn-warning btn-lg test-btn mx-3" onclick="sendJwt()">send JWT</button>
		<button class="btn btn-warning btn-lg test-btn mx-3" onclick="resetJwt()">reset JWT</button>
	</div>
	<div class="test d-flex flex-column mb-5">
		<button class="btn btn-warning btn-lg mx-3 mb-2" onclick="test7()">
			<span>test7 : requests requiring authentication </span>
		</button>
		<div class="px-3 d-flex justify-content-around">
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test7Radio" id="test7Radio1" checked>
			  <label class="form-check-label" for="test7Radio1">without JWT</label>
			</div>
			<div class="form-check form-check-inline mr-0">
			  <input class="form-check-input" type="radio" name="test7Radio" id="test7Radio2">
			  <label class="form-check-label" for="test7Radio2">with JWT</label>
			</div>
		</div>
	</div>
	<div class="test d-flex justify-content-between mt-5 mb-5">
		<button class="btn btn-primary btn-lg test-btn mx-3" onclick="test8()">test8</button>
		<button class="btn btn-primary btn-lg test-btn mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-primary btn-lg test-btn mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-primary btn-lg test-btn mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	let jwt = "";
	
	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test2() {
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function test3() {
		console.log("## test3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function test4() {
		console.log("## test4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test5() {
		console.log("## test5");
		let option = $("[name='test5Radio']:checked").val();
		console.log("> option = '%s'", option);
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test5",
			beforeSend : function(xhr) {
				if (option != "undefined") {
					xhr.setRequestHeader("authorization", option);
					//xhr.setRequestHeader("Authorization", "Bearer " + jwt);
				}
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function test6() {
		console.log("## test6");
		let option = $("[name='test6Radio']:checked").val();
		console.log("> option = '%s'", option);
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test6",
			beforeSend : function(xhr) {
				if (option != "undefiend") {
					xhr.setRequestHeader("Authorization", option);
				}
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function createJwt() {
		console.log("## createJwt");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/create-jwt",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				console.log("> JWT has been renewed")
				jwt = result.data.token;
				alertJwt(jwt);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function sendJwt() {
		console.log("## sendJwt");
		alertJwt(jwt);
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/send-jwt",
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Authorization", "Bearer " + jwt);
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function resetJwt() {
		jwt = "";
		alertJwt(jwt);
	}
	
	function getInMemoryJwt() {
		alertJwt(jwt);
	}
	
	function test7() {
		console.log("## test7");
		alertJwt(jwt);
		
		let checked = $("#test7Radio2").prop("checked");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test7",
			beforeSend : function(xhr) {
				if (checked) {
					xhr.setRequestHeader("Authorization", "Bearer " + jwt);
				}
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function test8() {
		console.log("## test8 : from sessionStorage");
		
		let accessToken = sessionStorage.getItem("accessToken")
		alertJwt(accessToken);
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jwt/test8",
			beforeSend : function(xhr) {
				if (accessToken != "") {
					xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
				}
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
	
	function alertJwt(jwt) {
		if (jwt == "") {
			alert("JWT is empty");
		} else {
			alert(jwt);
		}
	}
</script>
</body>
</html>