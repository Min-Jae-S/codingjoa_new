<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>test-view.jsp</title>
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
</style>
<style>
	div.test2 {
		padding-left: 1.3rem;
		padding-right: 1.3rem;
	}
		
	.input-group input {
		border-right: none;
	}
	
	.input-group input::placeholder {
		font-size: 1rem;
		color: #adb5bd;
	}

	.input-group-prepend {
		width: 60%;
	}
	
	.input-group-text:first-child {
		color: #212529;
		width: 35%;
		border-right: none;
	} 

	.input-group-text:nth-child(2) {
		color: #212529;
		width: 5%;
		border-left: none;
		border-right: none;
		margin: 0 !important;
	} 

	.input-group-text:last-child {
		color: #212529;
		width: 60%;
		border-left: none;
		border-right: none;
		margin: 0 !important;
	}
	
	img {
		width: 140px;
		height: 140px;
	}
</style>
</head>
<body>
<c:import url="/WEB-INF/views/include/top-menu.jsp"/>
<div class="container my-5">
	<p>test-view.jsp</p>
	<div class="d-none justify-content-center mt-5 test">
		<button class="btn btn-danger btn-lg mx-3" onclick="test1()">test1</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test2()">test2</button>
		<button class="btn btn-danger btn-lg mx-3" onclick="test3()">test3</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test4()">test4</button>
		<button class="btn btn-primary btn-lg mx-3" onclick="test5()">test5</button>
	</div>
	<div class="d-none justify-content-center mt-5 test">
		<button class="btn btn-warning btn-lg mx-3" onclick="methodArgumentException()">method-argumentEx</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="expectedException1()">expectedEx1</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="expectedException2()">expectedEx2</button>
		<button class="btn btn-warning btn-lg mx-3" onclick="testException()">textEx</button>
		<!-- <button class="btn btn-secondary btn-lg" onclick="colored_console()">console</button> -->
	</div>
	<div class="d-none justify-content-center mt-5 test">
		<form class="d-none" id="testForm" method="POST" action="${contextPath}/test/test-form">
			<input type="hidden" name="foo[]" value="1">
			<input type="hidden" name="foo[]" value="2">
			<input type="hidden" name="foo[]" value="3">
		</form>
		<button class="btn btn-lg mx-3" onclick="submit()">submit</button>
		<button class="btn btn-lg mx-3" onclick="serialize()">serialize</button>
		<button class="btn btn-lg mx-3" onclick="serializeArray()">serializeArray</button>
		<button class="btn btn-lg mx-3" onclick="serializeObject()">serializeObject</button>
	</div>
	<div class="d-none justify-content-center mt-5 test">
		<div class="d-flex flex-column px-5" style="border-right: 2px black solid;">
			<%-- <image class="mb-3 align-self-center border" id="testImage" src="${contextPath}/resources/images/img_profile.png"> --%>
			<button class="btn btn-success btn-lg" onclick="testUrlResource1()">testUrlResource1</button>
		</div>
		<div class="px-5" style="border-right: 2px black solid;">
			<%-- <image class="border" src="${contextPath}/test/test-url-resource2"> --%>
		</div>
		<div class="px-5">
			<button class="btn btn-success btn-lg" onclick="testUrlResource2()">testUrlResource2</button>
		</div>
	</div>
	<div class="d-flex justify-content-center mt-5 test">
		<button class="btn btn-lg btn-link mx-3 border" onclick="userDetails()">userDetails</button>
		<button class="btn btn-lg btn-link mx-3 border" onclick="memberDetails()">memberDetails</button>
		<button class="btn btn-lg btn-primary mx-3" id="testUploadBtn">testUpload</button>
		<input class="d-none" type="file" id="uploadFile"/>
		<button class="btn btn-lg mx-3 invisible" onclick="#">#</button>
	</div>
	<div class="row test2">
		<div class="col-sm-12">
			<div class="mt-5 mx-3 pt-3">
				<div class="input-group mb-4">
					<div class="input-group-prepend">
						<span class="input-group-text">STAR</span>
						<span class="input-group-text"></span>
		   				<span class="input-group-text"></span>
					</div>
					<input type="text" class="form-control" placeholder="starCount">
					<div class="input-group-append">
		   				<button class="btn btn-warning" id="testStarBtn">TEST</button>
					</div>
				</div>
				<div class="input-group mb-4">
					<div class="input-group-prepend">
		   				<span class="input-group-text">Get Board Image</span>
		   				<span class="input-group-text">:</span>
		   				<span class="input-group-text">/api/board/images/{imageName}</span>
					</div>
		 				<input type="text" class="form-control" placeholder="boardImageName">
					<div class="input-group-append">
		   				<button class="btn btn-warning" id="testGetBoardImageBtn">TEST</button>
					</div>
				</div>
				<div class="input-group mb-4">
					<div class="input-group-prepend">
						<span class="input-group-text">Get Member Image</span>
						<span class="input-group-text">:</span>
		   				<span class="input-group-text">/api/member/images/{imageName}</span>
					</div>
					<input type="text" class="form-control" placeholder="memberImageName">
					<div class="input-group-append">
		   				<button class="btn btn-warning" id="testGetMemberImageBtn">TEST</button>
					</div>
				</div>
				<div class="d-flex pt-3">
					<div class="mr-5">
						<img src="${contextPath}/api/board/images/aa.jpg">
					</div>
					<div class="mr-5 d-flex flex-column">
						<img class="mb-1" id="testBoardImage">
						<span class="align-self-center text-secondary">#testBoardImage</span>
					</div>
					<div class="mr-5 d-flex flex-column">
						<img class="mb-1" id="testMemberImage">
						<span class="align-self-center text-secondary">#testMemberImage</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>
<script>
	$(document).ready(function() {
		$("input").on("keydown", function(e) {
			if (e.keyCode == 13) {
				$(this).closest("div.input-group").find("button").click();
			}
		});
		
		$("#testBoardImage, #testMemberImage").on("load", function() {
			console.log("%c> IMAGE LOADING SUCCESS", "color:green");
			
			let imageURL = $(this).attr("src");
			console.log("> revoke imageURL = %s", imageURL);
			URL.revokeObjectURL(imageURL);
		});

		$("#testBoardImage, #testMemberImage").on("error", function() {
			console.log("%c> IMAGE LOADING FAILURE", "color:red");
		});

		$("#testStarBtn").on("click", function() {
			let starCount = $(this).closest("div.input-group").find("input").val();
			let url = "${contextPath}/test/test-star/" + starCount;
			$.ajax({
				type : "GET",
				url : url,
				success : function(result) {
					alert(result);
				},
				error : function(jqXHR) {
					console.log(jqXHR);
				}
			});
		});
		
		$("#testGetBoardImageBtn").on("click", function() {
			console.log("## testGetBoardImageBtn click");
			
			let boardImageName = $(this).closest("div.input-group").find("input").val();
			let url = "${contextPath}/api/board/images/" + boardImageName;
			console.log("> url = %s", url); 
			
			$.ajax({
				type : "GET",
				url : url,
				//cache: false, // http://localhost:8888/codingjoa/api/board/images/?_=1692932178224 
				//xhrFields: { responseType: 'blob'},
				xhr : function() {
					let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState == XMLHttpRequest.HEADERS_RECEIVED) {
                            if (xhr.status == 200) {
                                xhr.responseType = "blob";
                            } 
                        }
                       
                    };
                    return xhr;
				},
				success : function(result, status, jqXHR) {
					console.log("%c> SUCCESS", "color:green");
					console.log(result);
					
					//const blobSupported = new Blob(["ä"]).size === 2;
					//console.log("> blobSupported = %s", blobSupported);
					
					//let blob = new Blob([result], { type: 'image/jpeg' });
					//console.log(blob);
					
					let boardImageUrl = URL.createObjectURL(result);
			    	$("#testBoardImage").attr("src", boardImageUrl);
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					let errorResponse = JSON.parse(jqXHR.responseText);
					console.log(JSON.stringify(errorResponse, null, 2));
					alert(errorResponse.message);
				}
			});		 
		});

		$("#testGetMemberImageBtn").on("click", function() {
			console.log("## testGetMemberImageBtn click");
			
			let memberImageName = $(this).closest("div.input-group").find("input").val();
			let url = "${contextPath}/api/member/images/" + memberImageName;
			console.log("> url = %s", url);
			
			$.ajax({
				type : "GET",
				url : url,
				xhr : function() {
					let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function() {
                        if (xhr.readyState == XMLHttpRequest.HEADERS_RECEIVED) {
                            if (xhr.status == 200) {
                                xhr.responseType = "blob";
                            } 
                        }
                    };
                    return xhr;
				},
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(result);
					
			        let memberImageUrl = URL.createObjectURL(result);
			        $("#testMemberImage").attr("src", memberImageUrl);
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					let errorResponse = JSON.parse(jqXHR.responseText);
					console.log(JSON.stringify(errorResponse, null, 2));
					alert(errorResponse.message);
				}
			});	
			
		});
		
		$("#testUploadBtn").on("click", function() {
			console.log("## testUploadBtn click");
			$("#uploadFile").click();
		});
		
		$("#uploadFile").on("change", function() {
			console.log("## uploadFile open");
			let formData = new FormData();
			formData.append("file", this.files[0]);
			$(this).val("");
			
			$.ajax({
				type : "POST",
				url : "${contextPath}/test/test-upload",
				processData: false,
			    contentType: false,
				data : formData,
				dataType : "json",
				success : function(result) {
					console.log("%c> SUCCESS", "color:green");
					console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					console.log("%c> ERROR", "color:red");
					console.log(jqXHR);
				}
			});
		});
		
		$("#testForm").submit(function(e) {
			e.preventDefault();
			const formData = $(this).serializeObject();
			console.log("## formData");
			console.log(formData)
			
			const jsonData = JSON.stringify(formData);
			console.log("## jsonData");
			console.log(jsonData);
		});
		
		function submit() {
			$("form#testForm").submit();	
		}
	});
	
	function colored_console() {
		console.log("%c## SUCCESS","color:green;background-color:#dcedc8");
		console.log("%c## ERROR","color:red;background-color:#ffe6e6");
	}
	
	function parseError(jqXHR) {
		try {
			let errorResponse = JSON.parse(jqXHR.responseText);
			console.log(JSON.stringify(errorResponse, null, 2));
			return errorResponse;
		} catch(e) {
			alert("## Parsing Error");
			return null;
		}
	}
	
	function proccessError(jqXHR) {
		try {
			let errorResponse = JSON.parse(jqXHR.responseText);
			console.log(JSON.stringify(errorResponse, null, 2));
			
			let details = errorResponse.details;
			if (details.length > 0) {
				$.each(details, function(index, item) {
					//console.log("> %s / %s", item.field, item.message);	
				});
			} else {
				alert(errorResponse.message);
			}
		} catch(e) {
			alert("## Parsing Error");
			console.log(jqXHR);
		}
	}

	function test1() { // errorResponse(errorMap=null, errorMeesage=OK)
		let url = "${contextPath}/test/test1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test2() { // errorResonse(errorMap=OK, errorMeesage=null)
		let url = "${contextPath}/test/test2";
		console.log("## url = %s", url);
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify({
				param1 : "",
				param2 : -1,
				param3 : 9999
			}),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test3() { // no errorResponse
		let url = "${contextPath}/test/aaaa";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test4() {
		let url = "${contextPath}/test/test4";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			//dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}

	function test5() {
		let url = "${contextPath}/test/test5";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS","color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR","color:red");
				proccessError(jqXHR);
			}
		});
	}
	
	function methodArgumentException() {
		let url = "${contextPath}/test/method-argument-exception";
		console.log("## url = %s", url);
		$.ajax({
			type : "POST",
			url : url,
			/* data : JSON.stringify({
				param1 : "",	// String, @NotEmpty
				param2 : "cc",	// int, @Positive
				param3 : "dd",	// int, @BoardCategoryCode
			}), */
			data : JSON.stringify({
				param1 : "",	// String, 	@NotEmpty
				param2 : -1,	// int, 	@Positive
				param3 : 999,	// int, 	@BoardCategoryCode
				param4 : 1		// int, 	TestValidator(between 4 and 10);
			}),
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				proccessError(jqXHR);
			}
		});
	}

	function expectedException1() {
		let url = "${contextPath}/test/expected-exception1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				proccessError(jqXHR);
			}
		});
	}

	function expectedException2() {
		let url = "${contextPath}/test/expected-exception2";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				proccessError(jqXHR);
			}
		});
	}
	
	function testException() {
		let url = "${contextPath}/test/test-exception";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				proccessError(jqXHR);
			}
		});
	}
	
	function serialize() {
		console.log("## serialize");
		let FormData = $("#testForm").serialize();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}

	function serializeArray() {
		console.log("## serializeArray");
		let FormData = $("#testForm").serializeArray();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}
	
	function serializeObject() {
		console.log("## serializeObject");
		let FormData = $("#testForm").serializeObject();
		console.log(FormData);
		console.log(JSON.stringify(FormData, null, 2));
	}
	
	function testUrlResource1() {
		let url = "${contextPath}/test/test-url-resource1";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
				$("#testImage").attr("src", result.data);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				proccessError(jqXHR);
			}
		});
	}

	function testUrlResource2() {
		let url = "${contextPath}/test/test-url-resource2";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(result.data);
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}
	
	function userDetails() {
		let url = "${contextPath}/test/user-details";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}
	
	function memberDetails() {
		let url = "${contextPath}/api/member/details";
		console.log("## url = %s", url);
		$.ajax({
			type : "GET",
			url : url,
			success : function(result) {
				console.log("%c## SUCCESS", "color:blue");
				console.log(JSON.stringify(result, null, 2));
			},
			error : function(jqXHR) {
				console.log("%c## ERROR", "color:red");
				console.log(jqXHR);
			}
		});
	}
</script>
</body>
</html>