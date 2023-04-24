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
<title><c:out value="${category.categoryName}"/></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/comment.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
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
	
	.ck .ck-widget.ck-widget_selected, 
	.ck .ck-widget.ck-widget_selected:hover { 
		outline: none; 
	}
	
	.ck.ck-widget__selection-handle { 
		display: none; 
	}
	
	.header-group { 
		border-bottom: 1px solid rgba(0,0,0,.125); 
	}
	
	.comment-group { 
		border-top: 1px solid rgba(0,0,0,.125);
	}

	.board-category {
		color: #007bff;
		font-weight: bold;
		font-size: 0.9rem;
		text-decoration: none !important;
	}
	
	.board-category:after {
		content: ">";
		margin-left: 0.25rem;
	}
	
	.board-utils {
		float: right;
		cursor: pointer;
	}
	
	.category .dropdown-item,
	.comment-area-footer .dropdown-item {
		font-size: 0.875rem;
	}
	
	.title {
		color: black;
		font-weight: bold;
	}
	
	.category .dropdown-menu,
	.comment-area-footer .dropdown-menu {
		padding-top :0;
		padding-bottom: 0;
	}

	.category .dropdown-header,
	.comment-area-footer .dropdown-header {
		color: black; 
		font-weight: bold;
		border-bottom: 1px solid #e9ecef;
	}
	
	.header-group .header-meta {
		font-size: 14px;
		color: #979797;
		/* color: #757575; */
	}
	
	.header-group .header-meta .ml-auto {
		color: black;
	}
	
	.comment-regdate {
		font-size: 13px;
		color: #979797;
	}
	
	.comment-regdate::before {
		content: "(";
	}

	.comment-regdate::after {
		content: ")";
	}
	
	.comment-input {
		margin-left: 0.5rem;
		margin-right: 0.5rem;
		padding: 1.3rem 1.3rem 1rem 1.3rem;
		height: 100%;
	}
	
	.comment-edit {
		margin: 0;
		padding: 1.3rem 1.3rem 1rem 1.3rem;
		height: 100%;
	}
	
	.comment-input textarea,
	.comment-edit textarea {
		border: none;
		width: 100%;
		font-size: 15px;
		resize: none;
		overflow: hidden;
		margin: 0;
		padding: 0;
		max-height: 150px;
	}
	
	.comment-input .btn,
	.comment-edit .btn {
		font-size: 0.85rem;
		box-shadow: none !important;
	}
	
	.comment-input div,
	.comment-edit div {
		text-align: right;
	}
	
	.comment-input textarea:focus,
	.comment-edit textarea:focus { 
		outline: none; 
	}
	
	.comment-input textarea::placeholder { 
		color: #868e96; 
	}
	
	.comment-input textarea:focus::placeholder { 
		color: #ced4da; 
	}
	
	.comment-cnt {
		font-size: 1.3rem;
		font-weight: bold;
	}
	
	.comment-list { 
		font-size: 15px; 
	}
	
	.comment-list .list-group-item {
		padding: 1.25rem 0.5rem;
	}
	
	.comment-info { 
		margin-bottom: 1rem; 
	}
	
	.comment-writer {
		color: #495057;
		font-weight: bold;
		margin-right: 0.25rem;
	}
	
	.deleted-comment .comment-content {
		color: #868e96;
	}
	
	.fa-heart {
		color: red;
	}
	
	.comment-area {
		display: flex;
	}
	
	.comment-area .comment-area-footer {
		display: flex;
		flex-direction: column;
		margin-left: auto;
		/* padding-right: 1.3rem; */
	}
	
	.comment-utils {
		float: right;
		cursor: pointer;
	}
	
	.comment-likes {
		margin-top: auto;
		color: #007acf;
	}
	
	.textarea-border {
		border: 1px solid #868e96;
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
				<div class="header-group mb-4">
					<div class="category dropright mb-2">
						<a class="board-category" 
							href="${contextPath}/board/main?boardCategoryCode=${category.categoryCode}">
							<c:out value="${category.categoryName}"/>
						</a>
						<sec:authorize access="isAuthenticated()">
							<sec:authentication property="principal.member" var="member"/>
							<c:if test="${member.memberIdx eq boardDetails.boardWriterIdx}">
								<span class="board-utils" data-toggle="dropdown" data-offset="0,10">
									<i class="fa-solid fa-ellipsis-vertical"></i>
								</span>
								<div class="dropdown-menu">
									<h6 class="dropdown-header">게시글 관리</h6>
									<a class="dropdown-item" 
										href="${contextPath}/board/modify?boardIdx=${boardDetails.boardIdx}">수정하기
									</a>
							      	<a class="dropdown-item" id="deleteBoardLink"
							      		href="${contextPath}/board/deleteProc?boardIdx=${boardDetails.boardIdx}">삭제하기
							     	</a>
							    </div>
							</c:if>
						</sec:authorize>
					</div>
					<h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/></h3>
					<div class="header-meta d-flex mb-2">
						<span class="mr-3"><c:out value="${boardDetails.memberId}"/></span>
						<span class="mr-3">
							<fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy.MM.dd. HH:mm"/>
						</span>
						<span class="mr-1">조회</span>
						<span><c:out value="${boardDetails.boardViews}"/></span>
						<div class="ml-auto">
							<span><i class="fa-regular fa-comment-dots mr-1"></i>댓글</span>
							<span class="mr-3"><c:out value="${boardDetails.commentCnt}"/></span>
							<span><i class="fa-regular fa-heart mr-1"></i>좋아요</span>
							<span><c:out value="${boardDetails.boardLikesCnt}"/></span>
						</div>
					</div>
				</div>
				<div class="content-group mb-4">
					<div id="boardContent">
						<!-- ${boardDetails.boardContent} -->
					</div>
				</div>
				<div class="comment-group pt-4">
					<div class="comment-cnt mb-3">
						<span>댓글</span>
						<c:if test="${boardDetails.commentCnt > 0}">
							<span><c:out value="${boardDetails.commentCnt}"/></span>
						</c:if>
					</div>
					<div class="input-group">
						<div class="comment-input form-control">
							<sec:authorize access="isAuthenticated()">
								<p class="font-weight-bold mb-2">
									<sec:authentication property="principal.member.memberId" />
								</p>
							</sec:authorize>
							<textarea id="commentContent" placeholder="댓글을 남겨보세요" rows="1"></textarea>
							<div class="mt-2">
								<button class="btn btn-sm" id="writeCommentBtn">등록</button>
							</div>
						</div>
					</div>
					<div class="comment-list">
						<!-- comment -->
					</div>
				</div>
			</div>
			<div class="card-bottom">
				<a class="btn btn-secondary" href="${contextPath}/board/main?boardCategoryCode=${category.categoryCode}&
					${boardCri.getQueryString()}">목록</a>
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
				attributeExtender,
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
			console.log("## ReadEditor initialize");
			const toolbarElement = editor.ui.view.toolbar.element;
			toolbarElement.style.display = "none";
			editor.enableReadOnlyMode("#boardContent");
			//editor.setData('${boardDetails.boardContent}');
			readEditor = editor;
		})
		.catch(error => {
			console.error(error);
		});
	
	function makeCommentHtml(list) {
		let boardWriterIdx = "<c:out value='${boardDetails.boardWriterIdx}'/>";
		let html = "<ul class='list-group list-group-flush mt-3'>";
		
		$.each(list, function(index, commentDetails) {
			if (!commentDetails.commentUse) {
				html += "<li class='list-group-item deleted-comment' comment-idx='" + commentDetails.commentIdx + "'>";
				html += "<div class='comment-area'>";
				html += "<div class='comment-area-header'>";
				html += "<div class='comment-info'>";
				html += "<span class='comment-writer'>삭제된 댓글</span>";
				html += "</div>";
				html += "<div class='comment-content'>";
				html += "<span>삭제된 댓글입니다.</span>";
				html += "</div>";
				html += "</div>";
				html += "</div>";
				html += "</li>";
				return true;
			}
			
			html += "<li class='list-group-item' comment-idx='" + commentDetails.commentIdx + "'>";
			html += "<div class='comment-area'>";
			html += "<div class='comment-area-header'>";
			html += "<div class='comment-info'>";
			html += "<span class='comment-writer'>" + commentDetails.memberId + "</span>";
			if (commentDetails.commentWriterIdx == boardWriterIdx) {
				html += "<span class='badge badge-pill badge-primary mr-1'>작성자</span>"
			}
			html += "<span class='comment-regdate'>" + commentDetails.regdate + "</span>";
			html += "<span class='comment-moddate d-none'>" + commentDetails.moddate + "</span>";
			html += "</div>";
			html += "<div class='comment-content'>";
			html += "<span>" + commentDetails.commentContent.replace(/(?:\r\n|\r|\n)/g, "<br>"); + "</span>";
			html += "</div>";
			html += "</div>";
			html += "<div class='comment-area-footer'>";
			html += "<div class='dropright'>";
			html += "<span class='comment-utils' data-toggle='dropdown' data-offset='0,10'>";
			html += "<i class='fa-solid fa-ellipsis-vertical'></i>";
			html += "</span>";
			html += "<div class='dropdown-menu'>";
			html += "<h6 class='dropdown-header'>댓글 관리</h6>";
			html += "<button class='dropdown-item' name='showEditCommentBtn'>수정하기</button>";
			html += "<button class='dropdown-item' name='deleteCommentBtn'>삭제하기</button>";
			html += "</div>";
			html += "</div>";
			html += "<div class='comment-likes'>";
			html += "<span><i class='fa-regular fa-thumbs-up mr-1'></i>" + commentDetails.commentLikes + "</span>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</li>";
		});
		html += "</ul>";
		
		return html;
	}
	
	function makeEditCommentHtml(commentDetails) {
		let html = "<div class='input-group'>";
		html += "<div class='comment-edit form-control'>";
		html += "<p class='font-weight-bold mb-2'>" + commentDetails.memberId + "</p>";
		html += "<textarea rows='1'>" + commentDetails.commentContent + "</textarea>";
		html += "<div class='mt-2'>";
		html += "<button class='btn btn-outline-primary btn-sm mr-2' name='modifyCommentBtn'>수정</button>";
		html += "<button class='btn btn-outline-secondary btn-sm' name='closeEditCommentBtn'>취소</button>";
		html += "</div>";		
		html += "</div>";			
		html += "</div>";			
		
		return html;
	}
	
	$(function() {
		let boardIdx = "<c:out value='${boardDetails.boardIdx}'/>";
		let boardCategoryCode = "<c:out value='${boardDetails.boardCategoryCode}'/>";
		let commentListURL = "${contextPath}/boards/" + boardIdx + "/comments";
		
		commentService.getCommentList(commentListURL , function(result) {
			let commentList = result.data;
			if (commentList.length == 0) return;
			
			let html = makeCommentHtml(commentList);
			$(".comment-list").html(html);
		});
		
		$("#deleteBoardLink").on("click", function() {
			return confirm("게시글을 삭제하시겠습니까?");
		});
		
		$("#commentContent").on({
			"focus":function() { $(this).closest("div").addClass("textarea-border"); },
			"blur" :function() { $(this).closest("div").removeClass("textarea-border"); },
			"input":function() {
				$(this).height("auto");
				$(this).height($(this).prop("scrollHeight") + "px");
				
				if ($(this).val() != "") {
					$(this).closest("div").find("button").addClass("btn-primary");
				} else {
					$(this).closest("div").find("button").removeClass("btn-primary");
				}
			}
		});
		
		$(document).on("focus", ".comment-edit textarea", function() {
			console.log("focus");
			$(this).closest("div").addClass("textarea-border");
		});
		
		$(document).on("blur", ".comment-edit textarea", function() {
			console.log("blur");
			$(this).closest("div").removeClass("textarea-border");
		});
		
		$(document).on("input", ".comment-edit textarea", function() {
			console.log("input");
			$(this).height("auto");
			$(this).height($(this).prop("scrollHeight") + "px");
			
			if ($(this).val() != "") {
				$(this).closest("div").find("button[name='modifyCommentBtn']").addClass("btn-outline-primary");
			} else {
				$(this).closest("div").find("button[name='modifyCommentBtn']").removeClass("btn-outline-primary");
			}
		});
		
		$("#writeCommentBtn").on("click", function() {
			let comment = {
				commentBoardIdx : boardIdx,
				boardCategoryCode : boardCategoryCode,
				commentContent : $("#commentContent").val(),
			};
			
			commentService.writeComment("${contextPath}/comments", comment, function(result) {
				alert(result.message);
				$("#commentContent").val("");
				
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length == 0) return;
					
					let html = makeCommentHtml(commentList);
					$(".comment-list").html(html);
				});
			});
		});
		
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.attr("comment-idx");
			
			commentService.getComment("${contextPath}/comments/" + commentIdx, function(result) {
				let html = makeEditCommentHtml(result.data);
				$li.find("div.comment-area").addClass("d-none").after(html);
				
				/* let commentDetails = result.data;
				if (!commentDetails.commentUse) {
					alert("삭제된 댓글은 수정할 수 없습니다.");
				} */
			});
		});

		$(document).on("click", "button[name=closeEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			$li.find("div.comment-area").removeClass("d-none")
				.next("div.input-group").remove();
		});

		$(document).on("click", "button[name=modifyCommentBtn]", function() {
			let comment = {
				commentIdx : $(this).closest("li").attr("comment-idx"),
				commentBoardIdx : boardIdx,
				boardCategoryCode : boardCategoryCode,
				commentContent : $("#commentContent").val(),
			};
			
			commentService.modifyComment(url, comment, function(result) {
				alert(result.message);
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length == 0) return;

					let html = makeCommentHtml(commentList);
					$(".comment-list").html(html);
				});
			});
		});
		
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) return;
			
			let commentIdx = $(this).closest("li").attr("comment-idx");
			commentService.deleteComment("${contextPath}/comments/" + commentIdx, function(result) {
				alert(result.message);
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length == 0) return;

					let html = makeCommentHtml(commentList);
					$(".comment-list").html(html);
				});
			});
		});
		
	});
</script>
</body>
</html>