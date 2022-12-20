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
				<button class="btn btn-info btn-block" id="getDataBtn">myEditor.getData()</button>
				<button class="btn btn-warning btn-block" id="setDataBtn">myEditor.setData()</button>
			</div>
		</div>
		<div class="col-sm-2"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	let myEditor;
	
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			extraPlugins: [UploadAdapterPlugin],
			allowedContent: true,
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
			console.log("## Editor initialize");
			myEditor = editor;
			
			// https://github.com/ckeditor/ckeditor5/issues/5204
			console.log("## Allow custom attribute");
			myEditor.model.schema.extend("imageBlock", { allowAttributes: "dataIdx" });
			myEditor.model.schema.extend("imageInline", { allowAttributes: "dataIdx" });
			
			// view-to-model converter
			console.log("## Register view-to-model converter");
			myEditor.conversion.for("upcast").attributeToAttribute({
	            view: "data-idx",
	            model: "dataIdx"
	        });

			// model-to-view converter
			console.log("## Register model-to-view converter(data downcast)");
			myEditor.conversion.for("dataDowncast").add(dispatcher => {
				dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => { 
					const modelElement = data.item;
					const name = modelElement.name;
	            	
					// convert imageBlock, imageInline only
	            	if (!name.startsWith("image")) { 
	            		return;
	            	}
	            	
	            	const viewWriter = conversionApi.writer;
	            	
	            	// figure: imgaeblock, span: imageinline
	                const viewElement = conversionApi.mapper.toViewElement(modelElement); 
	                const img = name === "imageBlock" ? viewElement.getChild(0) : viewElement;
	                		
	                if (data.attributeNewValue !== null) {
		                viewWriter.setAttribute("data-idx", data.attributeNewValue, img);
	                } else {
	                	viewWriter.removeAttribute("data-idx", viewElement.getChild(0));
	                }
				});
			})
			
			// model-to-view converter
			// https://stackoverflow.com/questions/56402202/ckeditor5-create-element-image-with-attributes
			// https://gitlab-new.bap.jp/chinhnc2/ckeditor5/-/blob/690049ec7b8e95ba840ab1c882b5680f3a3d1dc4/packages/ckeditor5-engine/docs/framework/guides/deep-dive/conversion-preserving-custom-content.md
			console.log("## Register model-to-view converter(editing downcast)");
			myEditor.conversion.for("editingDowncast").add(dispatcher => { // downcastDispatcher
	            dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => {
	            	const modelElement = data.item;
	            	
	            	// convert imageBlock, imageInline only
	            	if (!modelElement.name.startsWith("image")) { 
	            		return;
	            	}
	            	
	                const viewWriter = conversionApi.writer;
	                
	             	// figure: imgaeblock, span: imageinline
	                const viewElement = conversionApi.mapper.toViewElement(modelElement); 
	                const img = viewElement.getChild(0);
	                
	                if (data.attributeNewValue !== null) {
	                	viewWriter.setAttribute("data-idx", data.attributeNewValue, img);
	                } else {
	                	viewWriter.removeAttribute("data-idx", img);
	                }
	            });
	        });
			
			console.log("## Register event listener(uloadComplete)");
			myEditor.plugins.get("ImageUploadEditing").on("uploadComplete", (evt, {data, imageElement}) => {
				// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadediting-ImageUploadEditing.html#event-uploadComplete
				myEditor.model.change(writer => {
					evt.stop();
					writer.setAttribute("src", "${contextPath}" + data.url, imageElement);
					writer.setAttribute("dataIdx", data.idx, imageElement);
				});
			});
		})
		.catch(error => {
			console.error(error);
		});
	
	function UploadAdapterPlugin(editor) {
	    editor.plugins.get("FileRepository").createUploadAdapter = (loader) => {
	        return new UploadAdapter(loader);
	    };
	}
	
	$(function() {
		alert('${writeBoardDto.boardContent}');
		
		// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadui-ImageUploadUI.html
		$("input[type='file']").removeAttr("accept"); /*.removeAttr("multiple");*/
		
		$("#getDataBtn").on("click", function() {
			console.log(myEditor.getData());
		});

		$("#setDataBtn").on("click", function() {
			editorData = myEditor.getData();
			myEditor.setData(editorData);
			
			console.log(myEditor.getData());
		});

		$("#resetBtn").on("click", function() {
			//$("form")[0].reset();
			$("#writeBoardDto").trigger("reset");
			myEditor.setData("");
		});
		
		$("#writeBtn").on("click", function(e) {
			e.preventDefault();
			let form = $("#writeBoardDto");
			let images = $(".ck-content .image, .ck-content .image-inline").find("img");
			
			$.each(images, function(index, item) {
				let input = $("<input>").attr("type", "hidden").attr("name", "uploadIdxList");
				let idx = $(this).data("idx");
				
				input.val(idx);
				form.append(input);
			});
			
			form.submit();
		});
	});
</script>

</body>
</html>