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
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/upload-adapter.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<!-- test -->
<script src="${contextPath}/resources/js/attrchange.js"></script>
<script src="${contextPath}/resources/js/attrchange_ext.js"></script>
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
			<h4 class="font-weight-bold mb-3">게시판 글쓰기</h4>
			<div class="pt-4" style="border-top: 1px solid black;">
				<form:form action="${contextPath}/board/writeProc" method="POST" modelAttribute="writeBoardDto">
					<div class="form-row">
						<div class="form-group col-md-8">
							<form:select class="custom-select" path="boardCategoryCode">
								<form:options items="${boardCategoryList}" itemValue="categoryCode" itemLabel="categoryName"/>
							</form:select>
							<form:errors path="boardCategoryCode" cssClass="error"/>
						</div>
						<div class="form-group col-md-2">
							<form:button class="btn btn-primary btn-block" id="writeBtn">등록</form:button>
						</div>
						<div class="form-group col-md-2">
							<button type="reset" class="btn btn-secondary btn-block" id="resetBtn">취소</button>
						</div>
					</div>
					<div class="form-group">
						<form:input path="boardTitle" class="form-control" placeholder="제목을 입력하세요."/>
						<form:errors path="boardTitle" class="error"/>
					</div>
					<div class="form-group">
						<form:textarea path="boardContent" class="d-none"/>
						<form:errors path="boardContent" class="error"/>
					</div>
				</form:form>
			</div>
			<!-- test -->
			<div class="mt-4">
				<button class="btn btn-warning mr-2" type="button" id="testGetDataBtn">Test getData</button>
				<button class="btn btn-warning mr-2" type="button" id="testJsoupBtn">Test Jsoup</button>
			</div>
		</div>
		<div class="col-sm-1"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let writeEditor;
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
					top: navbarHeight
				}
			},
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
			console.log("## WriteEditor initialize");
			writeEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadui-ImageUploadUI.html
		let $fileDialog = $("span.ck-file-dialog-button").find("input[type='file']");
		$fileDialog.attr("accept", "*/*").attr("multiple", false);
	
		$("#resetBtn").on("click", function() {
			$("#writeBoardDto").trigger("reset"); 
			writeEditor.setData("");
		});
		
		$("#writeBtn").on("click", function(e) {
			e.preventDefault();
			let $form = $("#writeBoardDto");
			let hasBoardImages = false;
			
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
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
			    let boardImageIdx = value.item.getAttribute("dataIdx");
			    $("<input/>", { type: "hidden", name: "boardImages", value: boardImageIdx }).appendTo($form);
			    hasBoardImages = true;
			}
			
			console.log("## Check form data");
			console.log(JSON.stringify($form.serializeObject(), null, 2));
			
			if (!confirm("게시글을 수정하시겠습니까?")) {
				$("input[name='boardImages']").remove();
				return;
			}
			
			$form.submit();
		});
		
		// test
		$("#testBtn").on("click", function() {
			/* console.log("===============================");
			console.log("## writeEditor.getData():");
			console.log(writeEditor.getData());
			console.log("## plainText:");
			console.log(viewToPlainText(writeEditor.editing.view.document.getRoot()));
			console.log("==============================="); */
			$(".navbar-custom").height("100");
		});
		
		// test (attrchange.js, attrchange_ext.js) 
		$(".navbar-custom").attrchange({
			trackValues: true,
			callback: function(e) {
				console.log("attributeName	: " + e.attributeName);
				console.log("oldValue		: " + e.oldValue);
				console.log("newValue		: " + e.newValue);
			}
		});
		
		// test getData
		$("#testGetDataBtn").on("click", function() {
			console.log("## testGetDataBtn click");
			console.log(writeEditor.getData());
		});
		
		// test jsoup
		$("#testJsoupBtn").on("click", function() {
			console.log("## testJsoupBtn click");
			let $form = $("#writeBoardDto");
			
			const range = modifyEditor.model.createRangeIn(modifyEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { // TreeWalker instance
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
			console.log(JSON.stringify(formData, null, 2));
			if(!confirm("진행하시겠습니까?")) {
				$("input[name='boardImages']").remove();
				return;
			}
			
			$.ajax({
				type : "POST",
				url : "${contextPath}/test/test-jsoup",
				data : JSON.stringify(formData),
				contentType : "application/json;charset=utf-8",
				dataType : "json",
				success : function(result) {
					console.log("%c## SUCCESS","color:blue");
					console.log(JSON.stringify(result, null, 2));
				},
				error : function(jqXHR) {
					console.log("%c## ERROR","color:red");
					console.log(jqXHR);
				}
			});
		});
	});
</script>

</body>
</html>