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
<script src="${contextPath}/resources/ckeditor5/build/upload-adapter.js"></script>
<style>
	.custom-select, input#boardTitle.form-control {
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
	
	.ck-placeholder {
		font-size: 0.9rem;
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
			<h5 class="font-weight-bold">게시판 글쓰기</h5>
			<div class="pt-3" style="border-top: 1px solid black;">
				<form:form action="${contextPath}/board/writeProc" method="POST" modelAttribute="writeBoardDto">
					<div class="form-row">
						<div class="form-group col-md-8">
							<form:select class="custom-select" path="boardCategoryCode">
								<c:forEach var="category" items="${categoryList}">
									<option value="${category.categoryCode}" ${category.categoryCode eq writeBoardDto.boardCategoryCode ? "selected" : ""}>
										${category.categoryName}
									</option>
								</c:forEach>
							</form:select>
							<form:errors path="boardCategoryCode" cssClass="error"/>
						</div>
						<div class="form-group col-md-2">
							<form:button class="btn btn-primary btn-block" id="writeBtn">등록</form:button>
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
				<button type="button" class="btn btn-warning btn-block" id="getDataBtn">getData() Button</button>
			</div>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

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
			writeEditor = editor;
			console.log("## writeEditor initialize");
		})
		.catch(error => {
			console.error(error);
		});
	
	function uploadAdapterPlugin(editor) {
		console.log("## Register upload adapter");
	    editor.plugins.get("FileRepository").createUploadAdapter = (loader) => {
	        return new UploadAdapter(loader);
	    };
	}
	
	// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadediting-ImageUploadEditing.html#event-uploadComplete
	function uploadCompleteListener(editor) {
		console.log("## Register event listener(uploadComplete)");
		
		editor.plugins.get("ImageUploadEditing").on("uploadComplete", (evt, {data, imageElement}) => {
			console.log("## Upload complete");
			editor.model.change(writer => {
				evt.stop();
				writer.setAttribute("src", "${contextPath}" + data.url, imageElement);
				writer.setAttribute("dataIdx", data.idx, imageElement);
			});
		});
	}
	
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
	
	// model-to-view converter(data downcast)
	function modelToViewDataConverter(editor) {
		console.log("## Register model-to-view converter ==> Data downcast");
		
		editor.conversion.for("dataDowncast").add(dispatcher => {
			dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => { 
				console.log("## Data downcast");
				const modelElement = data.item;
				
            	if (!conversionApi.consumable.consume(modelElement, evt.name)) {
                	return;
            	}
            	
            	const viewWriter = conversionApi.writer;
                const imageContainer = conversionApi.mapper.toViewElement(modelElement);
                const imageElement = (modelElement.name === "imageBlock") ? imageContainer.getChild(0) : imageContainer;
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
		// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadui-ImageUploadUI.html
		$("input[type='file']").removeAttr("accept"); /*.removeAttr("multiple");*/
		
		$("#getDataBtn").on("click", function() {
			console.log("writeEditor.getData ==>");
			console.log(writeEditor.getData());
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
			
			for (const value of range.getWalker({ ignoreElementEnd: true })) { // TreeWalker instance
				// Position iterator class. It allows to iterate forward and backward over the document.
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			 	// imageBlock, imageInlne
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			 	
			    console.log("dataIdx: " + value.item.getAttribute("dataIdx"));
			}
		});

		$("#resetBtn").on("click", function() {
			$("#writeBoardDto").trigger("reset"); //$("form")[0].reset();
			writeEditor.setData("");
		});
		
		$("#writeBtn").on("click", function(e) {
			e.preventDefault();
			
			let form = $("#writeBoardDto");
			const range = writeEditor.model.createRangeIn(writeEditor.model.document.getRoot());
			
			for (const value of range.getWalker({ ignoreElementEnd: true })) { // TreeWalker instance
				// Position iterator class. It allows to iterate forward and backward over the document.
			    if (!value.item.is("element")) {
			    	continue;
			    }
			    
			 	// imageBlock, imageInlne
			    if (!value.item.name.startsWith("image")) { 
			    	continue;
			    }
			    
			    let input = $("<input>").attr("type", "hidden").attr("name", "uploadIdxList");
			    let dataIdx = value.item.getAttribute("dataIdx");
			    
			    input.val(dataIdx);
				form.append(input);
			}
			
			form.submit();
		});
	});
</script>

</body>
</html>