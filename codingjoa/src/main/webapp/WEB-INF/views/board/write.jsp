<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>게시글 작성 | Codingjoa</title>
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
<!-- test -->
<script src="${contextPath}/resources/js/attrchange.js"></script>
<script src="${contextPath}/resources/js/attrchange_ext.js"></script>
<style>
	.write-wrap {
		min-width: 820px;
		margin: 0 auto;
	}
	
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
		margin-bottom: .5em;
	}
	
	.ck-button__label {
		font-size: 0.9rem !important;
	}
	
	.ck-tooltip {
    	display: none !important;
	}
}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="write-wrap">
		<h3 class="font-weight-bold mb-3">게시글 작성</h3>
		<div class="pt-4 border-top border-dark">
			<form:form action="${contextPath}/board/write" method="POST" modelAttribute="writeBoardDto">
				<div class="d-flex justify-content-between mb-3">
					<div class="w-75">
						<form:select class="custom-select rounded-md" path="categoryCode">
							<form:options items="${boardCategoryList}" itemValue="code" itemLabel="name"/>
						</form:select>
						<form:errors path="categoryCode" cssClass="error"/>
					</div>
					<div class="w-10">
						<form:button class="btn btn-primary btn-block rounded-md" id="writeBoardBtn">등록</form:button>
					</div>
					<div class="w-10">
						<button type="reset" class="btn btn-secondary btn-block rounded-md" id="resetBoardBtn">취소</button>
					</div>
				</div>
				<div class="form-group">
					<form:input path="title" class="form-control rounded-md" placeholder="제목을 입력하세요."/>
					<form:errors path="title" class="error"/>
				</div>
				<div class="form-group">
					<form:textarea path="content"/>
					<form:errors path="content" class="error"/>
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
	let writeEditor;
	const $form = $("#writeBoardDto");
	
	createEditor("#content")
		.then(editor => {
			const $file = $("span.ck-file-dialog-button").find("input[type='file']");
			$file.attr("accept", "*/*").attr("multiple", false);
			editor.model.document.on('change:data', () => {
				let content = editor.getData();
				$("#content").val(content);
			});
			//editor.ui.view.toolbar.items.map( item => console.log(item) );
			writeEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		$("#resetBoardBtn").on("click", function(e) {
			e.preventDefault();
			$form.trigger("reset"); 
			writeEditor.setData("");
		});
		
		$("#writeBoardBtn").on("click", function(e) {
			e.preventDefault();
			console.log("## initialize images");
			$("input[name='images']").remove();

			// TreeWalker instance
			// Position iterator class. It allows to iterate forward and backward over the document.
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { 
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			 	// imageBlock & imageInlne
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
				// add boardImages
			    let imageId = value.item.getAttribute("dataId");
				console.log("## add image, id = %s", imageId);
			    $("<input/>", { type: "hidden", name: "images", value: imageId }).appendTo($form);
			}
			
			console.log("## check form data");
			console.log(JSON.stringify($form.serializeObject(), null, 2));
			if (!confirm("게시글을 등록하시겠습니까?")) {
				return;
			}
			
			$form.submit();
		});
		
		// attrchange.js, attrchange_ext.js 
		$(".navbar-custom").attrchange({
			trackValues: true,
			callback: function(e) {
				console.log("attributeName	: " + e.attributeName);
				console.log("oldValue		: " + e.oldValue);
				console.log("newValue		: " + e.newValue);
			}
		});
		
		// testGetData
		$("#testGetDataBtn").on("click", function() {
			console.log("> '%s'", writeEditor.getData());
		});
		
		// testJsoup
		$("#testJsoupBtn").on("click", function() {
			$("input[name='images']").remove();

			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
			for (const value of range.getWalker({ ignoreElementEnd: true })) { 
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
			    let imageId = value.item.getAttribute("dataId");
			    $("<input/>", { type: "hidden", name: "images", value: imageId }).appendTo($form);
			}
			
			console.log("## check form data");
			console.log(JSON.stringify($form.serializeObject(), null, 2));
			
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