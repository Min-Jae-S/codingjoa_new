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
	
	.resume-read-committed,
	.resume-serializable,
	.resume-default {
		font-size: 0.8rem;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-4 px-0">
	<div class="d-flex justify-content-between ml-4 mb-3">
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
				<input type="radio" name="options" value="time-out-read-only">TIMEOUT & READONLY
			</label>
		</div>
	</div>
	<div class="btn-group d-flex ml-4 mb-3">
		<button class="btn btn-secondary btn-sm" onclick="selectAll()">SELECT TEST1</button>
		<button class="btn btn-secondary btn-sm" onclick="selectAll2()">SELECT TEST2</button>
		<button class="btn btn-secondary btn-sm" onclick="removeAll()">DELETE TEST1</button>
		<button class="btn btn-secondary btn-sm" onclick="removeAll2()">DELETE TEST2</button>
	</div>
	<p class="mb-4">tx-props.jsp</p>
	<div class="parent-div rollback">
		<p class="sub-p mt-4 pl-4 mb-4">- rollback</p>
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
		<p class="sub-p mt-4 pl-4 mb-4">- propagation</p>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="propagationTest1()">REQUIRED &rArr; REQUIRED<br>inner exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest7()">REQUIRED &rArr; REQUIRED<br>outer exception</button>
			<button class="btn btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="propagationTest2(true)">REQUIRED &rArr; RE_NEW<br>inner exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest3()">REQUIRED &rArr; RE_NEW<br>outer exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest2(false)">REQUIRED &rArr; RE_NEW<br>no exception</button>
		</div>
		<div class="test d-flex justify-content-center mb-4">
			<button class="btn btn-primary mx-3" onclick="propagationTest4()">MANDATORY</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest5()">REQUIRED &rArr; NESTED<br>inner exception</button>
			<button class="btn btn-primary mx-3" onclick="propagationTest6()">REQUIRED &rArr; NESTED<br>no exception</button>
		</div>
	</div>
	<div class="parent-div isolation d-none">
		<p class="sub-p mt-4 pl-4 mb-4">- isolation Level</p>
		<div class="test d-flex justify-content-center mb-5">
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest1()">READ_COMMITTED</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest2()">SERIALIZABLE</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="isolationTest3()">DEFAULT</button>
		</div>
		<div class="test d-flex justify-content-center mb-2">
			<button class="btn btn-lg btn-warning mx-3" onclick="findNumbers()">FIND NUMBERS</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="findCurrentNumber()">FIND CURRENT</button>
			<button class="btn btn-lg btn-warning mx-3 invisible" onclick="#">#</button>
		</div>
		<div class="test d-flex justify-content-center mb-5">
			<button class="btn btn-lg btn-warning mx-3" onclick="insertRandomNumber()">INSERT NUMBER</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="updateCurrentNumber()">UPDATE CURRENT</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="removeNumbers()">DELETE NUMBERS</button>
		</div>
		<div class="test d-flex justify-content-center mb-5">
			<div class="d-flex flex-column">
				<button class="btn btn-lg btn-success mx-3 mb-2" onclick="resumeReadCommitted()">RESUME<br>READ_COMMITTED</button>
				<div class="resume-read-committed px-3 d-flex justify-content-around">
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeReadCommittedRadioOptions" id="inlineRadio1" value="U" checked>
					  <label class="form-check-label" for="inlineRadio1">NON-REPEATABLE</label>
					</div>
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeReadCommittedRadioOptions" id="inlineRadio2" value="I">
					  <label class="form-check-label" for="inlineRadio2">PHANTOM</label>
					</div>
				</div>
			</div>
			<div class="d-flex flex-column">
				<button class="btn btn-lg btn-success mx-3 mb-2" onclick="resumeSerializable()">RESUME<br>SERIALIZABLE</button>
				<div class="resume-serializable px-3 d-flex justify-content-around">
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeSerializableRadioOptions" id="inlineRadio3" value="U" checked>
					  <label class="form-check-label" for="inlineRadio3">NON-REPEATABLE</label>
					</div>
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeSerializableRadioOptions" id="inlineRadio4" value="I">
					  <label class="form-check-label" for="inlineRadio4">PHANTOM</label>
					</div>
				</div>
			</div>
			<div class="d-flex flex-column">
				<button class="btn btn-lg btn-success mx-3 mb-2" onclick="resumeDefault()">RESUME<br>DEFAULT</button>
				<div class="resume-default px-3 d-flex justify-content-around">
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeDefaultRadioOptions" id="inlineRadio5" value="U" checked>
					  <label class="form-check-label" for="inlineRadio5">NON-REPEATABLE</label>
					</div>
					<div class="form-check form-check-inline mr-0">
					  <input class="form-check-input" type="radio" name="resumeDefaultRadioOptions" id="inlineRadio6" value="I">
					  <label class="form-check-label" for="inlineRadio6">PHANTOM</label>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="parent-div time-out-read-only mb-5 d-none">
		<p class="sub-p mt-4 pl-4 mb-4">- timeout</p>
		<div class="test d-flex justify-content-center mb-2">
			<button class="btn btn-lg btn-primary mx-3" onclick="timeoutTest1()">delay by DB</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="timeoutTest2()">delay by thread</button>
			<button class="btn btn-lg btn-primary mx-3 invisible" onclick="#">#</button>
		</div>
		<div class="test d-flex justify-content-center mb-2">
			<button class="btn btn-lg btn-warning mx-3" onclick="findNumbers()">FIND NUMBERS</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="insertRandomNumber()">INSERT NUMBER</button>
			<button class="btn btn-lg btn-warning mx-3" onclick="removeNumbers()">DELETE NUMBERS</button>
		</div>
	</div>
	<div class="parent-div time-out-read-only mb-5 d-none">
		<p class="sub-p mt-4 pl-4 mb-4">- readOnly</p>
		<div class="test d-flex justify-content-center">
			<button class="btn btn-lg btn-primary mx-3" onclick="readOnlyTest1()">test1</button>
			<button class="btn btn-lg btn-primary mx-3" onclick="checkAutoCommit()">check AutoCommit<br>without tx</button>
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
	
	function propagationTest7() {
		console.log("## propagationTest7");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/propagation/test7",
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
		console.log("> inner exception = %s", innerException);
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
				if (result.length > 0) {
					console.log("> result = %s", result);
				} else {
					console.log("> result = no records");
				}
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(jqXHR.responseJSON);
			}
		});		
	}

	function findCurrentNumber() {
		console.log("## findCurrentNumber");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/current-number",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				if (result === "") {
					console.log("> result = no record");
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
				if (result === "") {
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

	function updateCurrentNumber() {
		console.log("## updateCurrentNumber");
		$.ajax({
			type : "POST",
			url : "${contextPath}/test/tx-props/isolation/current-number",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
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
				if (result.length > 0) {
					console.log("> result = records still remain");
				} else {
					console.log("> result = deleted records clearly");
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
		let option = $("input:radio[name='resumeReadCommittedRadioOptions']:checked").val();
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/resume/read-committed/" + option,
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
		let option = $("input:radio[name='resumeSerializableRadioOptions']:checked").val();
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/resume/serializable/" + option,
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

	function resumeDefault() {
		console.log("## resumeDefault");
		let option = $("input:radio[name='resumeDefaultRadioOptions']:checked").val();
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/isolation/resume/default/" + option,
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
		console.log("## isolationTest1 ( read-committed )");
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

	function isolationTest2() {
		console.log("## isolationTest2 ( serializable )");
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
	
	function isolationTest3() {
		console.log("## isolationTest3 ( default )");
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
	
	function timeoutTest1() {
		console.log("## timeoutTest1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/timeout/test1",
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

	function timeoutTest2() {
		console.log("## timeoutTest2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/timeout/test2",
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

	function checkAutoCommit() {
		console.log("## checkAutoCommit");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/tx-props/check-autocommit",
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