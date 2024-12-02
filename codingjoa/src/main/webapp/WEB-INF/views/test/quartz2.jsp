<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>quartz2.jsp</title>
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
		display: flex;
		column-gap: 30px;
	}
	
	div.test button {
		width: 183px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>quartz2.jsp</p>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-warning btn-lg" onclick="config()">config</button>
		<button class="btn btn-warning btn-lg" onclick="currentJobs()">current jobs</button>
		<button class="btn btn-warning btn-lg" onclick="clearScheduler()">clear</button>
		<button class="btn btn-warning btn-lg" onclick="start()">start</button>
	</div>
	<div class="test mt-5 mb-5 px-5">
		<button class="btn btn-warning btn-lg" onclick="getSampleData()">get samples</button>
		<button class="btn btn-warning btn-lg" onclick="deleteSampleData()">delete samples</button>
	</div>
	<div class="test mt-5 mb-5 px-5">
		<div class="d-flex flex-column">
			<button class="btn btn-primary btn-lg px-1 mb-2" onclick="schedule()">
				<span>schedule job</span>
			</button>
			<div class="px-3 d-flex justify-content-around">
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="job" id="jobA" value="a" checked>
				  <label class="form-check-label" for="jobA">JobA</label>
				</div>
				<div class="form-check form-check-inline mr-0">
				  <input class="form-check-input" type="radio" name="job" id="jobB" value="b">
				  <label class="form-check-label" for="jobB">JobB</label>
				</div>
			</div>
		</div>
		<div>
			<button class="btn btn-primary btn-lg" onclick="standby()">standby</button>
		</div>
		<div>
			<button class="btn btn-primary btn-lg" onclick="shutdown()">shutdown</button>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	function config() {
		console.log("## config");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/config",
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

	function currentJobs() {
		console.log("## currentJobs");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/current-jobs",
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

	function clearScheduler() {
		console.log("## clearScheduler");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/clear",
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
	
	function start() {
		console.log("## start");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/start",
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
	
	function getSampleData() {
		console.log("## getSampleData");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/sample-data",
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

	function deleteSampleData() {
		console.log("## deleteSampleData");
		$.ajax({
			type : "DELETE",
			url : "${contextPath}/test/quartz2/sample-data",
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

	function schedule() {
		console.log("## schedule");
		let jobType = $("input[name='job']:checked").val();
		
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/schedule/" + jobType,
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

	function standby() {
		console.log("## standby");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/standby",
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

	function shutdown() {
		console.log("## shutdown");
		$.ajax({
			type : "GET",
			url : "${contextPath}/test/quartz2/shutdown",
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
</script>
</body>
</html>