<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/upload-adapter.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/viewtoplaintext.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<style>
	/* .custom-select, input#boardTitle.form-control {
		font-size: 0.9rem;
	}
	
	.form-group button {
		font-size: 0.9rem;
	} */
	
	input::placeholder {
		font-size: 1rem;
	}
	
	span.error {
		display: inline-block;
		padding-top: 7px;
	}
	
	.ck-editor__editable[role="textbox"] {
		min-height: 350px;
		/* font-size: 0.9rem; */
		padding-left: 0.75rem;
		padding-right: 0.75rem;
	}
	
	.ck-editor__editable[role="textbox"] p {
		margin-bottom: 5px;
	}
	
	.ck-placeholder {
		/* font-size: 0.9rem; */
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<h4 class="font-weight-bold mb-3">게시글 수정하기 (boardIdx = ${modifyBoardDto.boardIdx})</h4>
			<div class="pt-4" style="border-top: 1px solid black;">
				<form:form action="${contextPath}/board/modifyProc" method="POST" modelAttribute="modifyBoardDto">
					<form:hidden path="boardIdx"/>
					<div class="form-row">
						<div class="form-group col-md-8">
							<form:select class="custom-select" path="boardCategoryCode">
								<form:options items="${boardCategoryList}" itemValue="categoryCode" itemLabel="categoryName"/>
							</form:select>
							<form:errors path="boardCategoryCode" cssClass="error"/>
						</div>
						<div class="form-group col-md-2">
							<form:button class="btn btn-primary btn-block" id="modifyBtn">수정</form:button>
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
		<div class="col-sm-1"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let modifyEditor;
	let editorData;
	let navbarHeight = document.querySelector(".navbar-custom").clientHeight;
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			extraPlugins: [
				uploadAdapter, 
				uploadCompleteListener, 
				attributeExtender,
				viewToModelConverter, 
				modelToViewEditingConverter, 
				modelToViewDataConverter
			],
			ui: {
				viewportOffset: {
					//top: 100
					top: navbarHeight
				}
			},
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
			console.log("## ModifyEditor initialize");
			modifyEditor = editor;
			editorData = editor.getData();
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadui-ImageUploadUI.html
		$("input[type='file']").removeAttr("accept").removeAttr("multiple");
		
		$("#resetBtn").on("click", function() {
			$("#modifyBoardDto").trigger("reset");
			modifyEditor.setData(editorData);
		});
		
		$("#modifyBtn").on("click", function(e) {
			e.preventDefault();
			let $form = $("#modifyBoardDto");
			let $textArea = $("<textarea>").attr("style", "display:none;").attr("name", "boardContentText");
			
			// https://github.com/ckeditor/ckeditor5/blob/6bb68aa202/packages/ckeditor5-clipboard/src/utils/viewtoplaintext.ts#L23
			let boardContentText = viewToPlainText(modifyEditor.editing.view.document.getRoot());
			$textArea.val(boardContentText);
			
			// add boardContentText
			$form.append($textArea);
			
			const range = modifyEditor.model.createRangeIn(modifyEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { // TreeWalker instance
				// Position iterator class. It allows to iterate forward and backward over the document.
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			 	// imageBlock & imageInlne
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
				// add boardImages
			    let $input = $("<input>").attr("type", "hidden").attr("name", "boardImages");
			    $input.val(value.item.getAttribute("dataIdx"));
			    $form.append($input);
			}
			console.log("## Check form-data (+) boardContentText, boardImages");
			console.log(JSON.stringify($form.serializeObject(), null, 2));
			
			if (!confirm("게시글을 수정하시겠습니까?")) {
				$("textArea[name='boardContentText'], input[name='boardImages']").remove();
				console.log("## Check form-data (-) boardContentText, boardImages");
				console.log(JSON.stringify($form.serializeObject(), null, 2));
				return;
			}
			
			$form.submit();
		});
	});
</script>
</body>
</html>