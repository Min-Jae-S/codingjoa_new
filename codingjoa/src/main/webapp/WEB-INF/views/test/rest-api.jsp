<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>rest-api.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="${contextPath}/resources/js/utils.js"></script>
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
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
	
	.test-btn, div.input-group {
		width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>rest-api.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-primary test-btn mx-3" onclick="test1()">test1</button>
		<button class="btn btn-primary test-btn mx-3" onclick="test2()">test2</button>
		<button class="btn btn-primary test-btn mx-3" onclick="test3()">test3</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-outline-secondary test-btn" onclick="getMapping()">GET</button>
		</div>
		<div class="d-flex flex-column mx-3">
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">id</span>
	  			</div>
				<input type="text" class="form-control text-center" id="getMapping2Id">
				<div class="input-group-append">
    				<button class="btn btn-outline-secondary" onclick="getMapping2()">GET2</button>
  				</div>
			</div>
		</div>
		<div class="d-flex flex-column mx-3">
			<button class="btn btn-outline-secondary test-btn" onclick="postMapping()">POST</button>
		</div>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column mx-3">
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">id</span>
	  			</div>
				<input type="text" class="form-control text-center" id="putMappingId">
				<div class="input-group-append">
    				<button class="btn btn-outline-secondary" onclick="putMappingId()">PUT</button>
  				</div>
			</div>
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">name</span>
	  			</div>
				<input type="text" class="form-control text-center" id="putMappingName">
			</div>
			<div class="input-group">
				<div class="input-group-prepend">
	    			<span class="input-group-text">age</span>
	  			</div>
				<input type="text" class="form-control text-center" id="putMappingAge">
			</div>
		</div>
		<div class="d-flex flex-column mx-3">
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">id</span>
	  			</div>
				<input type="text" class="form-control text-center" id="patchMappingId">
				<div class="input-group-append">
    				<button class="btn btn-outline-secondary" onclick="patchMapping()">PATCH</button>
  				</div>
			</div>
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">name</span>
	  			</div>
				<input type="text" class="form-control text-center" id="patchMappingName">
			</div>
			<div class="input-group">
				<div class="input-group-prepend">
	    			<span class="input-group-text">age</span>
	  			</div>
				<input type="text" class="form-control text-center" id="patchMappingAge">
			</div>
		</div>
		<div class="d-flex flex-column mx-3">
			<div class="input-group mb-2">
				<div class="input-group-prepend">
	    			<span class="input-group-text">id</span>
	  			</div>
				<input type="text" class="form-control text-center" id="deleteMappingId">
				<div class="input-group-append">
    				<button class="btn btn-outline-secondary" onclick="deleteMapping()">DELETE</button>
  				</div>
			</div>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function checkJSON(str) {
		console.log("## checkJSON");
		try {
			var json = JSON.parse(str);
			return (typeof json === 'object');
		} catch (e) {
			return false;
		}
	}

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
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
			url : "${contextPath}/test/rest-api/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
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
			url : "${contextPath}/test/rest-api/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function getMapping() {
		console.log("## getMapping");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test-members",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result != "") {
					console.log(JSON.stringify(result, null, 2));
				} else {
					console.log("> No members");
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function getMapping2() {
		console.log("## getMapping2");
		console.log("\t> id = %s", $("#getMapping2Id").val());
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/rest-api/test-members/" + $("#getMapping2Id").val(),
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result != "") {
					console.log(JSON.stringify(result, null, 2));
				} else {
					console.log("> No member");
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function postMapping() {
		console.log("## postMapping");
		$.ajax({
			type : "POST",
			url : "${contextPath}/test/rest-api/test-members",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function putMapping() {
		console.log("## putMapping");
		console.log("\t> id = %s", $("#putMappingId").val());
		console.log("\t> name = %s", $("#putMappingName").val());
		console.log("\t> age = %s", $("#putMappingAge").val());
		
		let sendData = {
			name : $("#putMappingName").val(),
			age : $("#putMappingAge").val()
		};

		$.ajax({
			type : "PUT",
			url : "${contextPath}/test/rest-api/test-members/" + $("#putMappingId").val(),
			data : JSON.stringify(sendData),
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function patchMapping() {
		console.log("## patchMapping");
		console.log("\t> id = %s", $("#patchMappingId").val());
		console.log("\t> name = %s", $("#patchMappingName").val());
		console.log("\t> age = %s", $("#patchMappingAge").val());
		
		let sendData = {
			name : $("#patchMappingName").val(),
			age : $("#patchMappingAge").val()
		};
		
		$.ajax({
			type : "PATCH",
			url : "${contextPath}/test/rest-api/test-members/" + $("#patchMappingId").val(),
			data : JSON.stringify(sendData),
			contentType : "application/json; charset=utf-8",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}

	function deleteMapping() {
		console.log("## deleteMapping");
		console.log("\t> id = %s", $("#deleteMappingId").val());
		$.ajax({
			type : "DELETE",
			url : "${contextPath}/test/rest-api/test-members/" + $("#deleteMappingId").val(),
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				parseError(jqXHR);
			}
		});
	}
</script>
</body>
</html>