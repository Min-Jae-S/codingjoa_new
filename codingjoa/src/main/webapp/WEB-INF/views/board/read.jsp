<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>${boardDetails.boardTitle}</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<%-- <script src="${contextPath}/resources/ckeditor5/build/upload-adapter.js"></script> --%>
<style>
	.custom-select {
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
		font-size: 0.9rem;
		padding-left: 0.75rem;
		padding-right: 0.75rem;
	}
    
    /*
    .ck-content .image {
		max-width: 80%;
		margin: 20px auto;
	}
	*/
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<div class="card">
				<div class="card-header">
					<p>제목: ${boardDetails.boardTitle}</p>
					<p>작성자: ${boardDetails.memberId}</p>
					<p>조회수: ${boardDetails.boardViews}</p>
					<p>작성일: <fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy. MM. dd. HH:mm"/></p>
					<p>변경일: <fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy. MM. dd. HH:mm"/></p>
				</div>
				<div class="card-body" id="boardContent">
					${boardDetails.boardContent}
				</div>
			</div>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let readEditor;
	
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			extraPlugins: [
				extendAttribute,
				viewToModelConverter, 
				modelToViewEditingConverter
			],
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
			}
		})
		.then(editor => {
			editor.enableReadOnlyMode("#boardContent");
			const toolbarElement = editor.ui.view.toolbar.element;
			toolbarElement.style.display = "none";
			readEditor = editor;
			console.log("## readEditor initialize");
		})
		.catch(error => {
			console.error(error);
		});
	
	// https://github.com/ckeditor/ckeditor5/issues/5204
	function extendAttribute(editor) {
		console.log("## Allow custom attribute ==> blockObject, inlineOjbect");
		editor.model.schema.extend("$blockObject", { allowAttributes: "dataIdx" });
		editor.model.schema.extend("$inlineObject", { allowAttributes: "dataIdx" });
	}
	
	// view-to-model converter(upcast)
	function viewToModelConverter(editor) {
		console.log("## Register view-to-model converter ==> upcast");
		editor.conversion.for("upcast").attributeToAttribute({
            view: "data-idx",
            model: "dataIdx"
        });
	}
	
	// model-to-view converter(editing downcast)
	// https://stackoverflow.com/questions/56402202/ckeditor5-create-element-image-with-attributes
	// https://gitlab-new.bap.jp/chinhnc2/ckeditor5/-/blob/690049ec7b8e95ba840ab1c882b5680f3a3d1dc4/packages/ckeditor5-engine/docs/framework/guides/deep-dive/conversion-preserving-custom-content.md
	function modelToViewEditingConverter(editor) {
		console.log("## Register model-to-view converter ==> Editing downcast");
		
		editor.conversion.for("editingDowncast").add(dispatcher => { // downcastDispatcher
            dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => {
            	console.log("## Editing downcast");
            	const modelElement = data.item;
            	
            	if (!conversionApi.consumable.consume(modelElement, evt.name)) {
                	return;
            	}
            	
                const viewWriter = conversionApi.writer;
                const imageContainer = conversionApi.mapper.toViewElement(modelElement);
                const imageElement = imageContainer.getChild(0);
                //console.log("modelElement	: " + modelElement.name);
                //console.log("imageContainer	: " + imageContainer.name);
                //console.log("imageElement	: " + imageElement.name);
                
                if (data.attributeNewValue !== null) {
                	viewWriter.setAttribute("data-idx", data.attributeNewValue, imageElement);
                } else {
                	viewWriter.removeAttribute("data-idx", imageElement);
                }
            });
        });
	}
	
	$(function() {
		
	});
</script>

</body>
</html>