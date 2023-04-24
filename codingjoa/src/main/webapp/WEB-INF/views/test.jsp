<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="${contextPath}/resources/js/test1.js"></script>
<script src="${contextPath}/resources/js/test2.js"></script>
<script src="${contextPath}/resources/js/editor-service.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<style type="text/css">
	.ck-editor__editable[role="textbox"] {
		min-height: 300px;
	}
</style>
</head>
<body>
	<h1 class="font-weight-bold text-center my-5">JS MODULE TEST</h1>
	<div class="container">
		<div class="row">
			<div class="col-sm-2"></div>
			<div class="col-sm-8">
				<form:form action="${contextPath}/board/test" method="POST">
					<div class="form-row">
						<div class="form-group">
							<form:textarea path="boardContent"/>
							<form:errors path="boardContent" cssClass="error"/>
						</div>
						<div class="form-group">
							<form:button class="btn btn-primary btn-block">등록</form:button>
						</div>
					</div>
				</form:form>
			</div>
			<div class="col-sm-2"></div>
		</div>
	</div>
</body>

<script>
	let writeEditor;
	
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			extraPlugins: [
				uploadAdapterPlugin, 
				uploadCompleteListener, 
				extendAttribute,
				viewToModelConverter, 
				modelToViewEditingConverter, 
				modelToViewDataConverter
			],
			// https://ckeditor.com/docs/ckeditor5/latest/features/general-html-support.html
			htmlSupport: { 
				allow: [
					{
						attributes: [
							{ key: "data-idx", value: true }
						]
					}
				]
			},
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
			console.log("## WriteEditor initialized");
			writeEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
</script>
</html>