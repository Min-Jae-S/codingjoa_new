<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>Codingjoa : 글쓰기</title>
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
	
	.write-wrap {
		width: 820px;
		margin: 0 auto;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="write-wrap">
		<h4 class="font-weight-bold mb-3">게시판 글쓰기</h4>
		<div class="pt-4 border-top border-dark">
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
			<button class="btn btn-warning mr-2" type="button" id="testGetDataBtn">getData</button>
			<button class="btn btn-warning mr-2" type="button" id="testJsoupBtn">Jsoup</button>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let writeEditor;
	createEditor("#boardContent")
		.then(editor => {
			const $file = $("span.ck-file-dialog-button").find("input[type='file']");
			$file.attr("accept", "*/*").attr("multiple", false);
			editor.model.document.on('change:data', () => {
				let boardContent = editor.getData();
				$("#boardContent").val(boardContent);
			});
			writeEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		$("#resetBtn").on("click", function() {
			$("#writeBoardDto").trigger("reset"); 
			writeEditor.setData("");
		});
		
		$("#writeBtn").on("click", function(e) {
			e.preventDefault();
			console.log("## remove hidden boardImages input");
			$("input[name='boardImages']").remove();

			let $form = $("#writeBoardDto");
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
			
			// TreeWalker instance
			// Position iterator class. It allows to iterate forward and backward over the document.
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
			if (!confirm("게시글을 등록하시겠습니까?")) {
				return;
			}
			
			$form.submit();
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
		
		// testGetData
		$("#testGetDataBtn").on("click", function() {
			console.log("> '%s'", writeEditor.getData());
		});
		
		// testJsoup
		$("#testJsoupBtn").on("click", function() {
			$("input[name='boardImages']").remove();
			let $form = $("#writeBoardDto");
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
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
			console.log("## check formData");
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