<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>batch-quartz.jsp</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
	
	.options {
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
	
	.flow-options {
		display: flex;
		flex-direction: column;
		visibility: hidden;
	}
	
	.flow-options.active {
		visibility: visible;
	}
	
	.form-check {
		padding-left: 1.5em;
	}

	.form-check.form-switch {
		padding-left: 2.5em;
	}
	
	.form-check-label {
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
		<button class="btn btn-lg btn-warning" onclick="configChunkJob()">config<br>(ChunkJob)</button>
		<button class="btn btn-lg btn-warning" onclick="configBoardImagesCleanupJob()">config<br>(BoardImagesCleanupJob)</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-warning" onclick="triggerNoHandler('json')">trigger NoHandler<br>(dataType: json)</button>
		<button class="btn btn-lg btn-warning" onclick="triggerNoHandler('text/html')">trigger NoHandler<br>(dataType: text/html)</button>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary" id="runJobBtn">run Job</button>
		<div class="options">
			<div class="form-check">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio1" value="unregisterdJob" checked>
				<label class="form-check-label" for="jobRadio1">unregisterd job</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio2" value="multiStepsJob">
				<label class="form-check-label" for="jobRadio2">multiStepsJob</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="radio" name="jobNameOptions" id="jobRadio3" value="flowJob"> 
				<label class="form-check-label mr-5" for="jobRadio3">flowJob</label>
			</div>
		</div>
		<div class="flow-options">
			<form>
				<div class="form-check">
					<input class="form-check-input" type="radio" name="flowStatus" id="flowStatusRadio1" value="true" checked>
					<label class="form-check-label" for="flowStatusRadio1">success</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="radio" name="flowStatus" id="flowStatusRadio2" value="false">
					<label class="form-check-label" for="flowStatusRadio2">failure</label>
				</div>
				<div class="form-check">
					<input class="form-check-input" type="radio" name="flowStatus" id="flowStatusRadio3" value="">
					<label class="form-check-label" for="flowStatusRadio3">empty</label>
				</div>
			</form>
		</div>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary" onclick="runTaskletJob()">run TaskletJob</button>
		<button class="btn btn-lg btn-primary" id="runChunkJobBtn">run ChunkJob</button>
		<form id="chunkJobForm">
			<div class="form-check form-switch">
				<input class="form-check-input" type="checkbox" role="switch" id="useStepScope" name="useStepScope">
				<label class="form-check-label" for="useStepScope">use @StepScope</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="checkbox" name="lastNames" id="check1" value="kim"> 
				<label class="form-check-label" for="check1">kim</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="checkbox" name="lastNames" id="check2" value="lee"> 
				<label class="form-check-label" for="check2">lee</label>
			</div>
			<div class="form-check">
				<input class="form-check-input" type="checkbox" name="lastNames" id="check3" value="park"> 
				<label class="form-check-label" for="check3">park</label>
			</div>
		</form>
	</div>
	<div class="test d-flex mt-5">
		<button class="btn btn-lg btn-primary" id="runMyBatisJobBtn">run MyBatisJob</button>
		<button class="btn btn-lg btn-primary" id="runBoardImagesCleanupJobBtn">run BoardImagesCleanupJob</button>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(function() {
		$("#runJobBtn").on("click", function() {
			const jobName = $("input[name='jobNameOptions']:checked").val();
			console.log("## runJob, %s", jobName)
			
			let formData = null;
			if ($("#jobRadio3").prop("checked")) {
				formData = $(".flow-options form").serializeObject();
			}
			console.log("\t > formData:", formData);
			
			$.ajax({
				type : "GET",
				url : `${contextPath}/test/batch-quartz/jobs/\${jobName}/run`,
				data : formData,
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
			if ($(this).attr("id") == "jobRadio3") {
				$(".flow-options").addClass("active")
			} else {
				$(".flow-options").removeClass("active");
				$(".flow-options form").trigger("reset");
			}
		});
		
		$("#runChunkJobBtn").on("click", function() {
			console.log("## runChunkJob");
			
			let formData = $("#chunkJobForm").serializeObject();
			console.log("\t > formData:", formData);
			
			$.ajax({
				type : "GET",
				url : "${contextPath}/test/batch-quartz/chunk-job/run",
				data : formData,
				dataType: "json",
				traditional : true,
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

		$("#runMyBatisJobBtn").on("click", function() {
			console.log("## runMyBatisJob");
			$.ajax({
				type : "GET",
				url : "${contextPath}/test/batch-quartz/mybatis-job/run",
				dataType: "json",
				traditional : true,
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

		$("#runBoardImagesCleanupJobBtn").on("click", function() {
			console.log("## runBoardImagesCleanupJob");
			$.ajax({
				type : "GET",
				url : "${contextPath}/test/batch-quartz/board-images-cleanup-job/run",
				dataType: "json",
				traditional : true,
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
	
	function configChunkJob() {
		console.log("## configChunkJob");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/chunk-job/config",
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

	function configBoardImagesCleanupJob() {
		console.log("## configBoardImagesCleanupJob");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/board-images-cleanup-job/config",
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

	function triggerNoHandler(dataType) {
		console.log("## triggerNoHandler");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/no-handler",
			dataType: `\${dataType}`,
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

	function runTaskletJob() {
		console.log("## runTaskletJob");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/batch-quartz/tasklet-job/run",
			dataType : "json",
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