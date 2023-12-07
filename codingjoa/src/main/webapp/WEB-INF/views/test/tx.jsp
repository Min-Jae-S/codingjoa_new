<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>tx.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
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
	
	div.test button {
		width: 230px;
	}
	
	.btn {
		font-size: 1.2rem !important;
		padding: 0.375rem 0.5rem !important;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-4 px-0">
	<p>tx.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-warning mx-3" onclick="datasources()">datasources</button>
		<button class="btn btn-warning mx-3" onclick="managers()">tx-managers</button>
		<button class="btn btn-warning mx-3" onclick="factory()">factory</button>
		<button class="btn btn-warning mx-3" onclick="template()">template</button>
		<button class="btn btn-warning mx-3" onclick="syncManager()">sync-manager</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-outline-primary mx-3" onclick="test1()">doSomething1</button>
		<button class="btn btn-outline-primary mx-3" onclick="test2()">doSomething2</button>
		<button class="btn btn-outline-primary mx-3" onclick="test3()">doSomething3</button>
		<button class="btn btn-outline-primary mx-3" onclick="test4()">doSomething4</button>
		<button class="btn btn-outline-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-primary mx-3" onclick="selectAll()">selectAll</button>
		<button class="btn btn-primary mx-3" onclick="update()">update</button>
		<button class="btn btn-primary mx-3" onclick="remove()">delete</button>
		<button class="btn btn-primary mx-3" onclick="removeAll()">deleteAll</button>
		<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-none justify-content-center mt-5">
		<button class="btn btn-primary mx-3" onclick="insertNoTx()">insert no tx</button>
		<button class="btn btn-primary mx-3" onclick="insertTx()">insert tx</button>
		<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-secondary mx-3" onclick="invoke()">invoke</button>
		<button class="btn btn-secondary mx-3" onclick="invokeNoTx()">invoke no tx</button>
		<button class="btn btn-secondary mx-3" onclick="invokeTx()">invoke tx</button>
		<button class="btn btn-secondary mx-3 invisible" onclick="payment()">payment</button>
		<button class="btn btn-secondary mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-secondary mx-3" onclick="invokeSqlSession()">sqlSession</button>
		<button class="btn btn-secondary mx-3" onclick="invokeSqlSessionTemplate()">template</button>
		<button class="btn btn-secondary mx-3" onclick="invokeMapper()">mapper</button>
		<button class="btn btn-secondary mx-3 invisible" onclick="#">#</button>
		<button class="btn btn-secondary mx-3 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function datasources() {
		console.log("## datasources");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/datasources",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function managers() {
		console.log("## managers");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/managers",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function factory() {
		console.log("## factory");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/factory",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function template() {
		console.log("## template");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/template",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function syncManager() {
		console.log("## syncManager");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/sync-manager",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function test2() {
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function test3() {
		console.log("## test3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function test4() {
		console.log("## test4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function selectAll() {
		console.log("## selectAll");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/select-all",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> result = no records");
				} else {
					console.log("> result = ");
					console.log(JSON.stringify(result, null, 2));
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function insertNoTx() {
		console.log("## insertNoTx");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/insert-no-tx",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function insertTx() {
		console.log("## insertTx");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/insert-tx",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function update() {
		console.log("## update");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/update",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function remove() {
		console.log("## remove");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/remove",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function removeAll() {
		console.log("## removeAll");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/remove-all",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function invoke() {
		console.log("## invoke");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function invokeNoTx() {
		console.log("## invokeNoTx");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke-no-tx",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function invokeTx() {
		console.log("## invokeTx");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke-tx",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function payment() {
		console.log("## payment");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/payment",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function invokeSqlSession() {
		console.log("## invokeSqlSession");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke/sqlSession",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function invokeSqlSessionTemplate() {
		console.log("## invokeSqlSessionTemplate");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke/sqlSessionTemplate",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}

	function invokeMapper() {
		console.log("## invokeMapper");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/invoke/mapper",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
</script>
</body>
</html>