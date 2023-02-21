<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title><c:out value="${boardName}"/></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<script src="${contextPath}/resources/js/reply.js"></script>
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
	
	.ck.ck-editor__main>.ck-editor__editable {
		border: none !important;
		box-shadow: none !important;
	}
	
	.header-group {
		border-bottom: 1px solid rgba(0,0,0,.125);
	}
	
	.header-group .category a {
		color: #007bff;
		font-weight: bold;
		font-size: 0.9rem;
		text-decoration: none;
	}
	
	.header-group .category a::after {
		content: ">";
		margin-left: 0.25rem;
	}
	
	.title {
		color: black;
		font-weight: bold;
	}
	
	.header-group .header-meta {
	    font-size: 0.9rem;
    	color: #757575;
	}
	
	.reply-input textarea {
		min-height: 6.125rem;
		resize: none;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<div class="card p-4 mb-3">
				<div class="header-group mb-3">
					<div class="category mb-2">
						<a href="${contextPath}/board/main?boardCategoryCode=${cri.boardCategoryCode}"><c:out value="${boardName}"/></a>
					</div>
					<h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/></h3>
					<div class="header-meta d-flex mb-2">
						<span class="author mr-4"><c:out value="${boardDetails.memberId}"/></span>
						<span class="date"><fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy. MM. dd. HH:mm"/></span>
						<span class="views ml-auto">조회 <c:out value="${boardDetails.boardViews}"/></span>
					</div>
				</div>
				<div class="content-group">
					<div id="boardContent">
						<c:out value="${boardDetails.boardContent}" escapeXml="false"/>
					</div>
				</div>
			</div>
			<div class="mb-3">
				<sec:authorize access="isAuthenticated()">
					<sec:authentication property="principal.member.memberIdx" var="memberIdx"/>
					<c:if test="${memberIdx eq boardDetails.boardWriterIdx}">
						<a class="btn btn-primary mr-1" href="${contextPath}/board/modify?boardIdx=${boardDetails.boardIdx}">수정</a>
						<a class="btn btn-warning mr-1" href="${contextPath}/board/deleteProc?boardIdx=${boardDetails.boardIdx}"
							onclick="return confirm('게시글을 삭제하시겠습니까?');">삭제</a>
					</c:if>
				</sec:authorize>
				<a class="btn btn-secondary" href="${contextPath}/board/main${cri.getQueryString()}">목록</a>
			</div>
			<div class="reply">
				<div class="reply-input">
					<div class="input-group">
						<textarea class="form-control" id="replyContent" name="replyContent"></textarea>
						<div class="input-group-append">
							<button class="btn btn-outline-primary" id="writeReplyBtn">등록</button>
						</div>
					</div>
				</div>
				<div class="reply-list">
				
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
			console.log("## readEditor initialize");
			const toolbarElement = editor.ui.view.toolbar.element;
			toolbarElement.style.display = "none";
			editor.enableReadOnlyMode("#boardContent");
			readEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	/* replyService.getReplyList("${contextPath}/reply/board/${boardDetails.boardIdx}", 
			function(list) {
		console.log(list);
	}); */
	
	
	
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
</script>

<script>
	$(function() {
		$("#writeReplyBtn").on("click", function() {
			let reply = {
				replyBoardIdx : "${boardDetails.boardIdx}",
				replyContent : $("#replyContent").val(),
			};
			
			replyService.writeReply("${contextPath}/reply", reply, function(result) {
				console.log(result);
			});
		});
	});
</script>

</body>
</html>