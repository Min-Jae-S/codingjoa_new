<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>글쓰기</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<style>
	select.form-control {
		font-size: 0.9rem;
	}
	
	.form-group button {
		font-size: 0.9rem;
	}
	
	span.error {
		display: inline-block;
		padding-top: 7px;
	}
	
	.ck-editor__editable[role="textbox"] {
		min-height: 300px;
		font-size: 14px;
		padding-left: 0.75rem;
		padding-right: 0.75rem;
	}
    
    .ck-content .image {
		max-width: 80%;
		margin: 20px auto;
	}
	
	.ck-placeholder {
		font-size: 14px;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-write-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<h5 class="font-weight-bold">게시판 글쓰기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<form:form action="${contextPath}/board/writeProc" method="POST" modelAttribute="writeBoardDto">
					<div class="form-row">
						<div class="form-group col-md-8">
							<form:select class="form-control" path="boardCategoryCode">
								<c:forEach var="category" items="${categoryList}">
									<option value="${category.categoryCode}" ${category.categoryCode eq writeBoardDto.boardCategoryCode ? "selected" : ""}>
										${category.categoryName}
									</option>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-2">
							<form:button class="btn btn-primary btn-block">등록</form:button>
						</div>
						<div class="form-group col-md-2">
							<button type="button" class="btn btn-secondary btn-block" id="resetBtn">취소</button>
						</div>
					</div>
					<div class="form-group">
						<form:input path="boardTitle" class="form-control" placeholder="제목을 입력하세요."/>
						<form:errors path="boardTitle" cssClass="error"/>
					</div>
					<div class="form-group">
						<form:textarea path="boardContent"/>
						<form:errors path="boardContent" cssClass="error"/>
					</div>
				</form:form>
			</div>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let CKEditor;
	
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			extraPlugins: [UploadAdapterPlugin],
			fontFamily: {
				options: ["defalut", "Arial", "궁서체", "바탕", "돋움"],
				supportAllValues: true
			},
			fontSize: {
				options: [ 10, 12, "default", 16, 18, 20, 22 ],
				supportAllValues: true
			},
			placeholder: "내용을 입력하세요."
		})
		.then(editor => {
			CKEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});

	$(function() {
		$("#resetBtn").on("click", function() {
			$("form")[0].reset();
			CKEditor.setData("");
		});
	});
	
	function UploadAdapterPlugin(editor) {
	    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
	        return new UploadAdapter(loader);
	    };
	}
	
	class UploadAdapter {
	    constructor(loader) {
	        this.loader = loader;
	    }

	    upload() {
	        return this.loader.file.then(file => new Promise((resolve, reject) => {
	            this._initRequest();
	            this._initListeners(resolve, reject, file);
	            this._sendRequest(file);
	        }));
	    }

	    _initRequest() {
	        const xhr = this.xhr = new XMLHttpRequest();
	        
	     	// Note that your request may look different. It is up to you and your editor
	        // integration to choose the right communication channel. This example uses
	        // a POST request with JSON as a data structure but your configuration
	        // could be different.
	        xhr.open('POST', '${contextPath}/board/uploadImage', true);
	        xhr.responseType = 'json';
	    }

	    _initListeners(resolve, reject, file) {
	        const xhr = this.xhr;
	        const loader = this.loader;
	        const genericErrorText = '파일을 업로드 할 수 없습니다.';
	
	        xhr.addEventListener('error', () => reject(genericErrorText));
	        xhr.addEventListener('abort', () => reject());
	        xhr.addEventListener('load', () => {
	            const response = xhr.response;
	            console.log("xhr.response : " + response);
	            
	         	// This example assumes the XHR server's "response" object will come with
	            // an "error" which has its own "message" that can be passed to reject() in the upload promise.
	            // Your integration may handle upload errors in a different way so make sure
	            // it is done properly. The reject() function must be called when the upload fails.
	            if(!response || response.error) {
	                return reject(response && response.error ? response.error.message : defaultErrorMessage);
	            }
				
	         	// If the upload is successful, resolve the upload promise with an object containing
	            // at least the "default" URL, pointing to the image on the server.
	            // This URL will be used to display the image in the content. Learn more in the
	            // UploadAdapter#upload documentation.
	            resolve({
	                default: response.url
	            });
	        });
	    }

	    _sendRequest(file) {
	        const data = new FormData();
	        data.append('upload', file);
	        
	     	// Important note: This is the right place to implement security mechanisms
	        // like authentication and CSRF protection. For instance, you can use
	        // XMLHttpRequest.setRequestHeader() to set the request headers containing
	        // the CSRF token generated earlier by your application.

	        // Send the request.
	        this.xhr.send(data);
	    }
	}
</script>

</body>
</html>