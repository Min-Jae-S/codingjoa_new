<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>tx-props.jsp</title>
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
	
	.sub-p {
		font-size: 25px;
		padding-top: 0;
		text-align: left;
	}
	
	div.test {
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
	
	div.test button {
		width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-4 px-0">
	<div class="d-flex justify-content-between">
		<div class="btn-group btn-group-toggle" data-toggle="buttons">
			<label class="btn btn-secondary btn-sm active">
				<input type="radio" name="options" value="rollback" checked>ROLLBACK
			</label> 
			<label class="btn btn-secondary btn-sm">
				<input type="radio" name="options" value="propagation">PROPAGATION
			</label>
			<label class="btn btn-secondary btn-sm">
				<input type="radio" name="options" value="isolation">ISOLATION
			</label>
			<label class="btn btn-secondary btn-sm">
				<input type="radio" name="options" value="time-out-read-only">TIME OUT & READ ONLY
			</label>
		</div>
	</div>
	<p class="mb-4">tx-props.jsp</p>
	<div class="btn-group d-flex justify-content-end">
		<button class="btn btn-sm btn-secondary" onclick="selectAll()">SELECT TEST1</button>
		<button class="btn btn-sm btn-secondary" onclick="selectAll2()">SELECT TEST2</button>
		<button class="btn btn-sm btn-secondary" onclick="removeAll()">DELETE TEST1</button>
		<button class="btn btn-sm btn-secondary" onclick="removeAll2()">DELETE TEST2</button>
	</div>
	<div class="parent-div rollback">
		<p class="sub-p mt-4 pl-4 mb-4">- Rollback</p>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest1()">CATCH</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest2()">NO CATCH</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="rollbackTest3()">ROLLBACK_FOR Ex</button>
			<button class="btn btn-primary mx-3" onclick="rollbackTest4()">ROLLBACK_FOR SQLEx</button>
			<button class="btn btn-primary mx-3" onclick="rollbackTest5()">NOT ROLLBACK_FOR SQLEx</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest6()">CHECKED E</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="rollbackTest7()">UNCHECKED E</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
	</div>
	<div class="parent-div propagation d-none">
		<p class="sub-p mt-4 pl-4 mb-4">- Propagation</p>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="propagationTest1()">REQUIRED &rArr; REQUIRED<br>INNER Exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest2(true)">REQUIRED &rArr; RE_NEW<br>INNER Exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest2(false)">REQUIRED &rArr; RE_NEW<br>NO Exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest3()">REQUIRED &rArr; RE_NEW<br>OUTER Exception</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="propagationTest4()">MANDATORY</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest5()">REQUIRED &rArr; NESTED<br>INNER Exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest6()">REQUIRED &rArr; NESTED<br>NO Exception</button>
			<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
	</div>
	<div class="parent-div isolation d-none">
		<p class="sub-p mt-4 pl-4 mb-4">- Isolation Level</p>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest2()">READ_COMMITTED</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest3()">SERIALIZABLE</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest1()">DEFAULT</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-lg btn-warning mx-3" onclick="findNumbers()">FIND NUMBERS</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="insertRandomNumber()">INSERT NUMBER</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="removeNumbers()">DELETE NUMBERS</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-lg btn-success mx-3" onclick="resumeReadCommitted()">RESUME<br>READ_COMMITTED</button>
			<button class="btn btn-lg btn-success mx-3" onclick="resumeSerializable()">RESUME<br>SERIALIZABLE</button>
			<button class="btn btn-lg btn-success mx-3 invisible" onclick="#">#</button>
		</div>
	</div>
	<div class="parent-div time-out-read-only d-none">
		<p class="sub-p mt-4 pl-4 mb-2">- Time out</p>
		<div class="test d-flex justify-content-center">
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
	</div>
	<div class="parent-div time-out-read-only d-none">
		<p class="sub-p mt-4 pl-4 mb-2">- Read Only</p>
		<div class="test d-flex justify-content-center">
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		$("input[type=radio][name=options]").change(function() {
			$(".parent-div").addClass("d-none");
			$("." + $(this).val()).removeClass("d-none");
		});
	});
	
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
				console.log(jqXHR.responseJSON);
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
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function selectAll2() {
		console.log("## selectAll2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/select-all2",
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
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function removeAll2() {
		console.log("## removeAll2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx/remove-all2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	// rollback test
	function rollbackTest1() {
		console.log("## rollbackTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest2() {
		console.log("## rollbackTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test2",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function rollbackTest3() {
		console.log("## rollbackTest3 - rollback for Exception");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest4() {
		console.log("## rollbackTest4 - rollback for SQLException");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest5() {
		console.log("## rollbackTest5 - no rollback for SQLException");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test5",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest6() {
		console.log("## rollbackTest6 - rollback for checked exception");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test6",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function rollbackTest7() {
		console.log("## rollbackTest7 - rollback for unchecked exception");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/rollback/test7",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	// propagation test
	function propagationTest1() {
		console.log("## propagationTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest2(innerException) {
		console.log("## propagationTest2");
		console.log("> innerException = %s", innerException);
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test2/" + innerException,
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest3() {
		console.log("## propagationTest3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test3",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest4() {
		console.log("## propagationTest4");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test4",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function propagationTest5() {
		console.log("## propagationTest5");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test5",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function propagationTest6() {
		console.log("## propagationTest6");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test6",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	// isolation test
	function findNumbers() {
		console.log("## findNumbers");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/numbers",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> result = no records");
				} else {
					console.log("> result = %s", result);
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function insertRandomNumber() {
		console.log("## insertRandomNumber");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/new",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> result = no records");
				} else {
					console.log("> result = %s", result);
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function removeNumbers() {
		console.log("## removeNumbers");
		$.ajax({
			type : "DELETE",
			url : "${contextPath}/test/tx-props/isolation/numbers",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result == "") {
					console.log("> result = deleted records clearly");
				} else {
					console.log("> result = records still remain");
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function resumeReadCommitted() {
		console.log("## resumeReadCommitted");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/resume/read-committed",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function resumeSerializable() {
		console.log("## resumeSerializable");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/resume/serializable",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function isolationTest1() {
		console.log("## isolationTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/default",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function isolationTest1() {
		console.log("## isolationTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/default",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function isolationTest2() {
		console.log("## isolationTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/read-committed",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
	
	function isolationTest3() {
		console.log("## isolationTest3");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/serializable",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log("> result = %s", result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}
</script>
</body>
</html>