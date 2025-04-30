<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>batch-quartz.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/resources/css/common.css" rel="stylesheet">
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<style>
	p {
		text-align: center; 
		font-size: 50px;
		font-weight: bold;
		padding-top: 20px;
	}
	
	div.test {
		column-gap: 2rem; 

	}
	
	div.options {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}
	
	div.options #flowControl {
		display: none;
		margin-left: 2rem;
	}
	
	div.options #flowControl.active {
		display: flex;
		justify-content: center;
	}
	
	div.options .form-check-label {
  		white-space: nowrap;
	}
	
	div.test button {
		min-width: 230px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>batch-quartz.jsp</p>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-warning" onclick="config()">config</button>
		<button class="btn btn-lg btn-warning" onclick="test1()">test1<br>(dataType: json)</button>
		<button class="btn btn-lg btn-warning" onclick="test2()">test2<br>(no dataType)</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary" id="runJobBtn">run Job</button>
		<div class="options">
			<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio1" value="unregisterdJob" checked>
				<label class="form-check-label" for="jobRadio0">unregisterd job</label>
			</div>
			<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio2" value="multiStepsJob">
				<label class="form-check-label" for="jobRadio1">multiStepsJob</label>
			</div>
			<div class="form-check form-check-inline">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio3" value="flowJob"> 
				<label class="form-check-label" for="jobRadio2">flowJob</label>
				<div id="flowControl">
					<div class="form-check form-check-inline">
  						<input class="form-check-input" type="radio" name="flowStatusOptions" id="flowStatusRadio1" value="true" checked>
  						<label class="form-check-label" for="flowStatusRadio1">success</label>
					</div>
					<div class="form-check form-check-inline">
  						<input class="form-check-input" type="radio" name="flowStatusOptions" id="flowStatusRadio2" value="false">
  						<label class="form-check-label" for="flowStatusRadio2">failure</label>
					</div>
					<div class="form-check form-check-inline">
  						<input class="form-check-input" type="radio" name="flowStatusOptions" id="flowStatusRadio3" value="">
  						<label class="form-check-label" for="flowStatusRadio3">empty</label>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		$("#runJobBtn").on("click", function() {
			const jobName = $("input[name='jobNameOptions']:checked").val();
			console.log("## runJob, %s", jobName)
			
			let url;
			if (jobName == "multiStepsJob") {
				const flowStatus = $("input[name='flowStatusOptions']:checked").val();
				url = `${contextPath}/test/batch-quartz/job/\${jobName}/run?flow_status=\${flowStatus}`;
			} else {
				url = `${contextPath}/test/batch-quartz/job/\${jobName}/run`;
			}
			
			$.ajax({
				type : "GET",
				url : url,
				dataType: "json",
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null	, 2));
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					console.log(JSON.stringify(parseError(jqXHR), null, 2));
				}
			});	
		});
		
		$("input[name='jobNameOptions']").on("change", function() {
			if ($(this).attr("id") == "jobRadio2") {
				$("#flowControl").addClass("active")
			} else {
				$("#flowControl").removeClass("active");
			}
		});
	});

	function config() {
		console.log("## config");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/config",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function test1() {
		console.log("## test1");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/test1",
			dataType: "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

	function test2() {
		console.log("## test2");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/test1",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null	, 2));
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				console.log(JSON.stringify(parseError(jqXHR), null, 2));
			}
		});		
	}

</script>
</body>
</html>