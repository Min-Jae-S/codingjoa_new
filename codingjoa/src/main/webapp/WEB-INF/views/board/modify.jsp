<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>게시글 수정 | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<!-- ckeditor -->
<script src="${contextPath}/resources/ckeditor5/plugins/upload-adapter.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<script src="${contextPath}/resources/js/editor.js"></script>
<style>
	input::placeholder {
		font-size: 1rem !important
	}
	
	span.error {
		display: inline-block;
		padding: .5rem;
	}
	
	.ck-toolbar {
		border-radius: .75rem .75rem 0 0 !important;
	}
	
	.ck-editor__editable {
		min-height: 350px;
		padding-left: 1rem !important;
		padding-right: 1rem !important;
		border-radius: 0 0 .75rem .75rem !important;
	}
	
	.ck-editor__editable p {
		margin-bottom: .5rem;
	}
	
	.ck-button__label {
		font-size: 1rem !important;
	}
	
	.modify-wrap {
		min-width: 820px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="modify-wrap">
		<h3 class="font-weight-bold mb-3">게시글 수정</h3>
		<div class="pt-4 border-top border-dark">
			<form:form action="${contextPath}/board/modify" method="POST" modelAttribute="modifyBoardDto">
				<form:hidden path="boardIdx"/>
				<div class="d-flex justify-content-between mb-3">
					<div class="w-75">
						<form:select class="custom-select rounded-md" path="boardCategoryCode">
							<form:options items="${boardCategoryList}" itemValue="categoryCode" itemLabel="categoryName"/>
						</form:select>
						<form:errors path="boardCategoryCode" cssClass="error"/>
					</div>
					<div class="w-10">
						<form:button class="btn btn-primary btn-block rounded-md" id="modifyBtn">수정</form:button>
					</div>
					<div class="w-10">
						<button type="button" class="btn btn-secondary btn-block rounded-md" id="resetBtn">취소</button>
					</div>
				</div>
				<div class="form-group">
					<form:input path="boardTitle" class="form-control rounded-md" placeholder="제목을 입력하세요."/>
					<form:errors path="boardTitle" class="error"/>
				</div>
				<div class="form-group">
					<form:textarea path="boardContent" class="d-none"/>
					<form:errors path="boardContent" class="error"/>
				</div>
			</form:form>
		</div>
		
		<!-- test -->
		<div class="mt-4 d-none">
			<button class="btn btn-warning mr-2" type="button" id="testGetDataBtn">getData</button>
			<button class="btn btn-warning mr-2" type="button" id="testJsoupBtn">Jsoup</button>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let modifyEditor, originalContent;
	const $form = $("#modifyBoardDto");
	
	createEditor("#boardContent")
		.then(editor => {
			const $file = $("span.ck-file-dialog-button").find("input[type='file']");
			$file.attr("accept", "*/*").attr("multiple", false);
			editor.model.document.on('change:data', () => {
				let boardContent = editor.getData();
				$("#boardContent").val(boardContent);
			});
			originalContent = editor.getData();
			modifyEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		$("#resetBtn").on("click", function() {
			$form.trigger("reset");
			modifyEditor.setData(originalContent);
		});
		
		$("#modifyBtn").on("click", function(e) {
			e.preventDefault();
			console.log("## initialize boardImages");
			$("input[name='boardImages']").remove();

			// TreeWalker instance
			// Position iterator class. It allows to iterate forward and backward over the document.
			const range = modifyEditor.model.createRangeIn(modifyEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { 
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			 	// imageBlock & imageInlne
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
			    // add boardImages
			    let boardImageIdx = value.item.getAttribute("dataIdx");
			    console.log("## add boardImages, boardImageIdx = %s", boardImageIdx);
			    $("<input/>", { type: "hidden", name: "boardImages", value: boardImageIdx }).appendTo($form);
			}
			
			console.log("## check formData");
			console.log(JSON.stringify($form.serializeObject(), null, 2));
			if (!confirm("게시글을 수정하시겠습니까?")) {
				return;
			}
			
			$form.submit();
		});
		
		// testGetData
		$("#testGetDataBtn").on("click", function() {
			console.log("> '%s'", modifyEditor.getData());
		});
		
		// testJsoup
		$("#testJsoupBtn").on("click", function() {
			$("input[name='boardImages']").remove();
			
			const range = modifyEditor.model.createRangeIn(modifyEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { 
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
			    let boardImageIdx = value.item.getAttribute("dataIdx");
			    $("<input/>", { type: "hidden", name: "boardImages", value: boardImageIdx }).appendTo($form);
			}
			
			let formData = $form.serializeObject();
			console.log("## Check formData");
			console.log(JSON.stringify(formData, null, 2));
			
			$.ajax({
				type : "POST",
				url : "${contextPath}/test/test-jsoup",
				data : JSON.stringify(formData),
				contentType : "application/json;charset=utf-8",
				dataType : "json",
				success : function(result) {
					console.log("%c## SUCCESS", "color:blue");
					console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					console.log("%c## ERROR", "color:red");
					console.log(jqXHR);
				}
			});
		});
	});
</script>
</body>
</html>