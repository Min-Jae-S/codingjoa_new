<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>jdbc-tx.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
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
	
	.small {
		font-size: 0.95rem;
	}
	
	.commit-or-rollback {
		font-size: 0.9rem;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>jdbc-tx.jsp</p>
	<div class="test d-flex justify-content-center mt-5">
		<button class="btn btn-lg btn-secondary mx-3 px-1" onclick="findTestItems()">
			<span>Find Items</span>
		</button>
		<button class="btn btn-lg btn-secondary mx-3 px-1" onclick="deleteTestItems()">
			<span>Delete Items</span>
		</button>
		<button class="btn btn-lg btn-primary mx-3 px-1 invisible" onclick="#">#</button>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column">
			<button class="btn btn-lg btn-primary mx-3 px-1 mb-2" onclick="useTx()">
				<span>Tx Only</span><br>
			</button>
			<div class="commit-or-rollback px-3 d-flex justify-content-around">
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback0" id="commitRadio0" value="true" checked>
				  <label class="form-check-label" for="commitRadio0">COMMIT</label>
				</div>
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback0" id="rollbackRadio0" value="false">
				  <label class="form-check-label" for="rollbackRadio0">ROLLBACK</label>
				</div>
			</div>
		</div>
		<div class="d-flex flex-column">
			<button class="btn btn-lg btn-primary mx-3 px-1 mb-2" onclick="useTxSyncManager()">
				<span>TxSyncManager</span><br>
			</button>
			<div class="commit-or-rollback px-3 d-flex justify-content-around">
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback1" id="commitRadio1" value="true" checked>
				  <label class="form-check-label" for="commitRadio1">COMMIT</label>
				</div>
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback1" id="rollbackRadio1" value="false">
				  <label class="form-check-label" for="rollbackRadio1">ROLLBACK</label>
				</div>
			</div>
		</div>
		<div class="d-flex flex-column">
			<button class="btn btn-lg btn-primary mx-3 px-1 mb-2" onclick="useTxManager()">
				<span>TxManager</span><br>
			</button>
			<div class="commit-or-rollback px-3 d-flex justify-content-around">
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback2" id="commitRadio2" value="true" checked>
				  <label class="form-check-label" for="commitRadio2">COMMIT</label>
				</div>
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback2" id="rollbackRadio2" value="false">
				  <label class="form-check-label" for="rollbackRadio2">ROLLBACK</label>
				</div>
			</div>
		</div>
	</div>
	<div class="test d-flex justify-content-center mt-5">
		<div class="d-flex flex-column">
			<button class="btn btn-lg btn-warning mx-3 px-1 mb-2" onclick="useDeclarativeTx()">
				<span>Declarative Tx</span>
			</button>
			<div class="commit-or-rollback px-3 d-flex justify-content-around">
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback3" id="commitRadio3" value="true" checked>
				  <label class="form-check-label" for="commitRadio3">COMMIT</label>
				</div>
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="commitOrRollback3" id="rollbackRadio3" value="false">
				  <label class="form-check-label" for="rollbackRadio3">ROLLBACK</label>
				</div>
			</div>
		</div>
		<button class="btn btn-lg btn-warning mx-3 px-1 mb-2 invisible" onclick="#">#</button>
		<button class="btn btn-lg btn-warning mx-3 px-1 mb-2 invisible" onclick="#">#</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function findTestItems() {
		console.log("## findTestItems");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jdbc/test-items",
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> no records");
				} else {
					console.log(JSON.stringify(result, null, 2));
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR);
			}
		});		
	}
	
	function deleteTestItems() {
		console.log("## deleteTestItems");
		$.ajax({
			type : "DELETE",
			url : "${contextPath}/test/jdbc/test-items",
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
	
	function useTx() {
		console.log("## useTx");
		let option = $('input[name="commitOrRollback0"]:checked').val();
		if (option == 'true') {
			console.log("> will commit");
		} else {
			console.log("> will rollback")
		}
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jdbc-tx/tx/" + option,
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

	function useTxSyncManager() {
		console.log("## useTxSyncManager");
		let option = $('input[name="commitOrRollback1"]:checked').val();
		if (option == 'true') {
			console.log("> will commit");
		} else {
			console.log("> will rollback")
		}
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jdbc-tx/sync-manager/" + option,
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

	function useTxManager() {
		console.log("## useTxManager");
		let option = $('input[name="commitOrRollback2"]:checked').val();
		if (option == 'true') {
			console.log("> will commit");
		} else {
			console.log("> will rollback")
		}

		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jdbc-tx/manager/" + option,
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

	function useDeclarativeTx() {
		console.log("## useDeclarativeTx");
		let option = $('input[name="commitOrRollback3"]:checked').val();
		if (option == 'true') {
			console.log("> will commit");
		} else {
			console.log("> will rollback")
		}
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/jdbc-tx/declarative-tx/" + option,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(JSON.stringify(errorResponse, null, 2));
			}
		});		
	}
</script>
</body>
</html>