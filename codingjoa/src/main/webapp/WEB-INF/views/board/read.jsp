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
<title>Codingjoa : <c:out value="${boardDetails.boardTitle}"/> (${boardDetails.boardIdx})</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/utils.js"></script>
<script src="${contextPath}/resources/js/comment.js"></script>
<script src="${contextPath}/resources/js/likes.js"></script>
<script src="${contextPath}/resources/js/render.js"></script>
<!-- ckeditor -->
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<style>
	.card {
		padding: 2.25rem;
	}
	
	.content-group {
		min-height: 350px;
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
		box-shadow: none !important;
		padding-top: 0;
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
		font-size: 1rem;
		/* color: #868e96; */
		/* color: #979797; */
		/* color: #757575; */
	}
	
	.header-group .header-meta a {
		color: black;
		text-decoration: none;
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
	
	.comment-input, .comment-edit {
		margin: 0;
		padding: 1.3rem 1.3rem 1rem 1.3rem;
		height: 100%;
	}
	
	.comment-input textarea,
	.comment-edit textarea {
		border: none;
		width: 100%;
		resize: none;
		overflow: hidden;
		margin: 0;
		padding: 0;
		max-height: 200px;
	}
	
	.comment-input .btn,
	.comment-edit .btn {
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
	
	.comment-body {
		font-size: 1.3rem;
		font-weight: bold;
	}
	
	.comment-list .list-group-item {
		padding: 1.25rem 0;
	}
	
	.comment-info { 
		margin-bottom: 1rem; 
	}
	
	.comment-writer {
		color: #495057;
		font-weight: bold;
		margin-right: 0.25rem;
	}
	
	.comment-content p {
		/* margin-bottom: 0; */
	}
	
	.deleted-comment .comment-content {
		color: #868e96;
	}
	
	.comment-area {
		display: flex;
	} 
	
	.comment-area .comment-area-header {
		display: flex;
		width: 90% !important;
		flex-direction: column;
	}
	
	.comment-area .comment-area-footer {
		display: flex;
		flex-direction: column;
		margin-left: auto;
	}
	
	.comment-utils {
		float: right;
		box-shadow: none !important;
		padding-top: 0;
	}
	
	.textarea-border {
		border: 1px solid #868e96;
	}
	
	.dropright button {
		padding-right: 0;
	}
	
	.comment-footer {
		position: relative;
	}
	
	.comment-footer .comment-pagination {
		position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
	}
	
	.text-grey {
		color: #868e96;
	}
	
	button[name="commentLikesBtn"] i {
		font-size: 1.2rem;
	}
	
	.ck-read-only {
   		--ck-widget-outline-thickness: 0;
   		border: none !important;
   		padding: 0 !important;
	}
	
	/* https://stackoverflow.com/questions/54272325/how-to-style-ckeditor-content-for-read-only-display */
	/* These styles hide weird "things" in CKeditor Viewer (read only mode) */
	/* 
	.ckeditor-viewer .ck.ck-editor__editable.ck-blurred .ck-widget.ck-widget_selected, 
	.ck.ck-editor__editable.ck-blurred .ck-widget.ck-widget_selected:hover {
		outline-width: 0;
	}
	
	.ckeditor-viewer .ck .ck-editor__nested-editable {
		border: 1px solid transparent;
		height: 0;
		margin: 0;
	}
	
	.ckeditor-viewer .ck-content .image>figcaption {
		background-color: transparent !important;
	}
	
	.ckeditor-viewer .ck .ck-widget__selection-handle {
		width: 0;
		height: 0;
		display: none;
	} 
	*/
	
	.read-wrap {
		width: 820px;
		margin: 0 auto;
	}
</style>

<!-- test css -->	
<style>	
	.test2 div.d-flex {
		justify-content: space-between;
	}
	
	.test2 button.test-item {
		width: 26%;
		text-align: left !important;
	}
	
	.test2 button:not(.test-item) {
		padding-left: 0;
		padding-right: 0;
		width: 18%;
		text-align: left !important;
		pointer-events: none;
	}

	.test2 button:not(.test-item) span {
		float: right;
	}
	
	.input-group input {
		border-right: none;
	}
	
	.input-group input::placeholder {
		font-size: 1rem;
		color: #adb5bd;
	}

	.input-group-prepend {
		width: 60%;
	}
	
	.input-group-text:first-child {
		color: #212529;
		width: 35%;
		border-right: none;
	} 

	.input-group-text:nth-child(2) {
		color: #212529;
		width: 5%;
		border-left: none;
		border-right: none;
		margin: 0 !important;
	} 

	.input-group-text:last-child {
		color: #212529;
		width: 60%;
		border-left: none;
		border-right: none;
		margin: 0 !important;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="read-wrap">
		<div class="card">
			<div class="header-group">
				<div class="category dropright mb-2">
					<a class="board-category" 
						href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}">
						<c:out value="${category.categoryName}"/>
					</a>
					<button class="board-utils btn" data-toggle="dropdown" data-offset="0,10">
						<i class="fa-solid fa-ellipsis-vertical"></i>
					</button>
					<div class="dropdown-menu">
						<h6 class="dropdown-header">게시글 관리</h6>
						<a class="dropdown-item" 
							href="${contextPath}/board/modify?boardIdx=${boardDetails.boardIdx}">수정하기
						</a>
				      	<a class="dropdown-item" id="deleteBoardLink"
				      		href="${contextPath}/board/deleteProc?boardIdx=${boardDetails.boardIdx}">삭제하기
				     	</a>
				     </div>
				</div>
				<h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/></h3>
				<div class="header-meta d-flex mb-2">
					<span class="mr-4"><c:out value="${boardDetails.memberId}"/></span>
					<span class="mr-4">
						<fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy.MM.dd. HH:mm"/>
					</span>
					<span class="mr-1">조회</span>
					<span><c:out value="${boardDetails.boardViews}"/></span>
					<div class="d-flex ml-auto">
						<a class="mr-4" href="#">
							<i class="fa-regular fa-comment-dots"></i>
							<span>댓글</span>
							<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
						</a>
						<button class="btn border-0 p-0 shadow-none" type="button" id="boardLikesBtn">
						<sec:authorize access="isAnonymous()">
							<i class="fa-regular fa-heart"></i>
						</sec:authorize>
						<sec:authorize access="isAuthenticated()">
							<sec:authentication property="principal" var="principal"/>
							<c:choose>
								<c:when test="${principal.isMyBoardLikes(boardDetails.boardIdx)}">
									<i class="text-danger fa-solid fa-heart"></i>
								</c:when>
								<c:otherwise>
									<i class="fa-regular fa-heart"></i>
								</c:otherwise>
							</c:choose>
						</sec:authorize>
							<span>좋아요</span>
							<span class="board-likes-cnt"><c:out value="${boardDetails.boardLikesCnt}"/></span>
						</button>
					</div>
				</div>
			</div>
			<div class="content-group py-4">
				<textarea class="d-none" id="boardContent"></textarea>
				<%-- <c:out value="${boardDetails.boardContent}" escapeXml="false"/> --%>
			</div>
			<div class="comment-group pt-4">
				<div class="comment-body mb-3">
					<span>댓글</span>
					<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
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
							<button class="btn btn-sm" type="button" id="writeCommentBtn" disabled>등록</button>
						</div>
					</div>
				</div>
				<div class="comment-list mt-4">
					<!------------------------>
					<!----    comments    ---->
					<!------------------------>
				</div>
				<div class="comment-footer mt-4">
					<a class="btn btn-secondary" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
						${boardCri.getQueryString()}">목록</a>
					<div class="comment-pagination">
						<!------------------------>
						<!-- comment pagination -->
						<!------------------------>
					</div>
				</div>
			</div>
		</div>
		
		<!-- comment test1 -->
		<div class="test1 mt-5 d-none">
			<div class="input-group mb-4">
				<div class="input-group-prepend">
    				<span class="input-group-text">Write comment</span>
    				<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments</span>
  				</div>
  				<input type="text" class="form-control" placeholder="boardIdx">
  				<input type="text" class="form-control" placeholder="content">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testWriteBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Get commentList</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/boards/{commentBoardIdx}/comments</span>
  				</div>
  				<input type="text" class="form-control" placeholder="boardIdx">
  				<input type="text" class="form-control" placeholder="page">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testGetCommentListBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Get modifyComment</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments/{commentIdx}</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testGetModifyCommentBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Modify comment</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments/{commentIdx}</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<input type="text" class="form-control" placeholder="content">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testModifyCommentBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Delete comment</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments/{commentIdx}</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testDeleteCommentBtn">TEST</button>
  				</div>
			</div>
		</div>
		
		<!-- comment test2 -->
		<div class="test2 mt-5 d-none">
			<div class="mb-4 d-flex">
				<button class="btn">Write comment<span>:</span></button>
				<button class="btn btn-warning test-item" name="writeBtn" data-idx="">/comments; idx=?</button>
				<button class="btn btn-warning test-item" name="writeBtn" data-idx="a">/comments; idx=a</button>				
				<button class="btn btn-warning test-item" name="writeBtn" data-idx="9999">/comments; idx=9999</button>
			</div>
			<div class="mb-4 d-flex">
				<button class="btn">Get commentList<span>:</span></button>
				<button class="btn btn-warning test-item" name="commentListBtn" data-idx="">/boards/?/comments</button>
				<button class="btn btn-warning test-item" name="commentListBtn" data-idx="a">/boards/a/comments</button>				
				<button class="btn btn-warning test-item" name="commentListBtn" data-idx="9999">/boards/9999/comments</button>
			</div>
			<div class="mb-4 d-flex">
				<button class="btn">Get modifyComment<span>:</span></button>
				<button class="btn btn-warning test-item" name="commentBtn" data-idx="">/comments/?</button>
				<button class="btn btn-warning test-item" name="commentBtn" data-idx="a">/comments/a</button>				
				<button class="btn btn-warning test-item" name="commentBtn" data-idx="9999">/comments/9999</button>
			</div>
			<div class="mb-4 d-flex">
				<button class="btn">Modify comment<span>:</span></button>	
				<button class="btn btn-warning test-item" name="patchBtn" data-idx="">/comments/?</button>					
				<button class="btn btn-warning test-item" name="patchBtn" data-idx="a">/comments/a</button>				
				<button class="btn btn-warning test-item" name="patchBtn" data-idx="9999">/comments/9999</button>					
			</div>
			<div class="mb-4 d-flex">
				<button class="btn">Delete comment<span>:</span></button>
				<button class="btn btn-warning test-item" name="deleteBtn" data-idx="">/comments/?</button>					
				<button class="btn btn-warning test-item" name="deleteBtn" data-idx="a">/comments/a</button>				
				<button class="btn btn-warning test-item" name="deleteBtn" data-idx="9999">/comments/9999</button>					
			</div>
		</div>
		
		<!-- likes test -->
		<div class="test3 mt-5">
			<div class="input-group mb-4">
				<div class="input-group-prepend">
    				<span class="input-group-text">Toggle boardLikes</span>
    				<span class="input-group-text">:</span>
    				<span class="input-group-text">/boards/{boardIdx}/likes</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testToggleBoardLikesBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Toggle commentLikes</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments/{commentIdx}/likes</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testToggleCommentLikesBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Get boardLikesCnt</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/boards/{boardIdx}/likes</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testGetBoardLikesCntBtn">TEST</button>
  				</div>
			</div>
			<div class="input-group mb-4">
				<div class="input-group-prepend">
					<span class="input-group-text">Get commentLikesCnt</span>
					<span class="input-group-text">:</span>
    				<span class="input-group-text">/comments/{commentIdx}/likes</span>
  				</div>
  				<input type="text" class="form-control" placeholder="idx">
  				<div class="input-group-append">
    				<button class="btn btn-warning" id="testGetCommentLikesCntBtn">TEST</button>
  				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	ClassicEditor
		.create(document.querySelector("#boardContent"), {
			toolbar: []
		})
		.then(editor => {
			const toolbarContainer = editor.ui.view.stickyPanel;
			editor.ui.view.top.remove(toolbarContainer);
			editor.enableReadOnlyMode("editor");
			const boardContent = '<c:out value="${boardDetails.boardContent}" escapeXml="false"/>';
			editor.setData(boardContent);
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		const boardIdx = "<c:out value='${boardDetails.boardIdx}'/>";
		const boardWriterIdx = "<c:out value='${boardDetails.boardWriterIdx}'/>";
		let curCommentPage = 1;
		
		// getCommentList
		commentService.getCommentList(boardIdx, curCommentPage, function(result) {
			let commentList = result.data.commentList;
			let myCommentLikes = result.data.myCommentLikes;
			let commentHtml = createCommentHtml(commentList, myCommentLikes, boardWriterIdx);
			$("div.comment-list").html(commentHtml);

			let pagination = result.data.pagination;
			let paginationHtml = createPaginationHtml(pagination);
			$("div.comment-pagination").html(paginationHtml);
			$("span.comment-cnt").text(pagination.totalCnt);
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
					$(this).closest("div").find("button")
						.attr("disabled", false).addClass("btn-outline-primary");
				} else {
					$(this).closest("div").find("button")
						.attr("disabled", true).removeClass("btn-outline-primary");
				}
			}
		});
		
		$(document).on("focus", ".comment-edit textarea", function() {
			$(this).closest("div").addClass("textarea-border");
		});
		
		$(document).on("blur", ".comment-edit textarea", function() {
			$(this).closest("div").removeClass("textarea-border");
		});
		
		$(document).on("input", ".comment-edit textarea", function() {
			$(this).height("auto");
			$(this).height($(this).prop("scrollHeight") + "px");
			
			let $modifyCommentBtn = $(this).closest("div").find("button[name='modifyCommentBtn']");
			if ($(this).val() != "") {
				$modifyCommentBtn.attr("disabled", false).addClass("btn-outline-primary");
			} else {
				$modifyCommentBtn.attr("disabled", true).removeClass("btn-outline-primary");
			}
		});
		
		// writeComment
		$("#writeCommentBtn").on("click", function() {
			let comment = {
				commentBoardIdx : boardIdx,
				commentContent : $("#commentContent").val(),
			};
			
			commentService.writeComment(comment, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, 1, function(result) {
					let commentList = result.data.commentList;
					let myCommentLikes = result.data.myCommentLikes;
					let commentHtml = createCommentHtml(commentList, myCommentLikes, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
					$("#commentContent").val("").trigger("input");
				});
			});
		});
		
		// getModifyComment
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.data("comment-idx");
			
			commentService.getModifyComment(commentIdx, function(result) {
				let commentDetails = result.data;
				let editCommentHtml = createEditCommentHtml(commentDetails);
				$li.find("div.comment-area").addClass("d-none").after(editCommentHtml);
				
				let $textarea = $li.find("div.comment-edit textarea");
				$textarea.height("auto");
				$textarea.height($textarea.prop("scrollHeight") + "px");
				$textarea.focus();
			});
		});

		$(document).on("click", "button[name=closeEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			$li.find("div.comment-area").removeClass("d-none").next("div.input-group").remove();
		});
		
		// modifyComment
		$(document).on("click", "button[name=modifyCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.data("comment-idx");
			let comment = {
				commentContent : $li.find("div.comment-edit textarea").val(),
			};
			
			commentService.modifyComment(commentIdx, comment, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, curCommentPage, function(result) {
					let commentList = result.data.commentList;
					let myCommentLikes = result.data.myCommentLikes;
					let commentHtml = createCommentHtml(commentList, myCommentLikes, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// deleteComment
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) {
				return;
			}
			
			let commentIdx = $(this).closest("li").data("comment-idx");
			commentService.deleteComment(commentIdx, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, curCommentPage, function(result) {
					let commentList = result.data.commentList;
					let myCommentLikes = result.data.myCommentLikes;
					let commentHtml = createCommentHtml(commentList, myCommentLikes, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// pagination
		$(document).on("click", "a.page-link", function(e) {
			e.preventDefault();
			commentService.getCommentList(boardIdx, $(this).data("page"), function(result) {
				let commentList = result.data.commentList;
				let myCommentLikes = result.data.myCommentLikes;
				let commentHtml = createCommentHtml(commentList, myCommentLikes, boardWriterIdx);
				$("div.comment-list").html(commentHtml);

				let pagination = result.data.pagination;
				let paginationHtml = createPaginationHtml(pagination);
				$("div.comment-pagination").html(paginationHtml);
				$("span.comment-cnt").text(pagination.totalCnt);
				
				curCommentPage = pagination.page;
				console.log("## current comment page = %s", curCommentPage);
			});
		});
		
		// toggleBoardLikes
		$("#boardLikesBtn").on("click", function() {
			likesService.toggleBoardLikes(boardIdx, function(result) {
				alert(result.message);
				let cssClass = (result.data == "ON") ? 
						"text-danger fa-solid fa-heart" : "text-grey fa-regular fa-heart";
				likesService.getBoardLikesCnt(boardIdx, function(result) {
					$("#boardLikesBtn i").removeClass().addClass(cssClass);
					$(".board-likes-cnt").text(result.data);
				});
			});
		});
		
		// toggleCommentLikes
		$(document).on("click", "button[name=commentLikesBtn]", function() {
			let $li = $(this).closest("li");
			let commentIdx = $li.data("comment-idx");
			likesService.toggleCommentLikes(commentIdx, function(result) {
				alert(result.message);
				let cssClass = (result.data == "ON") ? 
						"text-primary fa-regular fa-thumbs-up" : "text-grey fa-regular fa-thumbs-up";
				likesService.getCommentLikesCnt(commentIdx, function(result) {
					$li.find("button[name=commentLikesBtn] i").removeClass().addClass(cssClass);
					$li.find(".comment-likes-cnt").text(result.data);
				});
			});
		});
	});
</script>

<!-- test script -->
<script>
	$(function() {
		// writeComment	
		$("#testWriteBtn").on("click", function() {
			let $input = $(this).closest("div.input-group").find("input");
			let comment = {
				commentBoardIdx : $input.first().val(),
				commentContent : $input.last().val()
			};
			
			commentService.writeComment(comment, function(result) {
				alert(result.message);
			});
		});

		// getCommentList
		$("#testGetCommentListBtn").on("click", function() {
			let $input = $(this).closest("div.input-group").find("input");
			let commentBoardIdx = $input.first().val();
			let page = $input.last().val();
			commentService.getCommentList(commentBoardIdx, page, function(result) {
				// ...
			});
		});

		// getModifyComment
		$("#testGetModifyCommentBtn").on("click", function() {
			let commentIdx = $(this).closest("div.input-group").find("input").val();
			commentService.getModifyComment(commentIdx, function(result) {
				// ...
			});
		});
		
		// modifyComment
		$("#testModifyCommentBtn").on("click", function() {
			let $input = $(this).closest("div.input-group").find("input");
			let commentIdx = $input.first().val();
			let comment = {
				commentContent : $input.last().val()
			};
			
			commentService.modifyComment(commentIdx, comment, function(result) {
				alert(result.message);
			});
		});

		// deleteComment
		$("#testDeleteCommentBtn").on("click", function() {
			let commentIdx = $(this).closest("div.input-group").find("input").val();
			commentService.deleteComment(commentIdx, function(result) {
				alert(result.message);
			});
		});

		// writeComment2
		$("button[name='writeBtn']").on("click", function() {
			let comment = {
				commentBoardIdx : $(this).data("idx"),
				commentContent : "aa"
			};
			commentService.writeComment(comment, function(result) {
				alert(result.message);
			});
		});
		
		// getCommentList2
		$("button[name='commentListBtn']").on("click", function() {
			commentService.getCommentList($(this).data("idx"), curCommentPage, function(result) {
				alert(result.message);
			});
		});
		
		// getModifyComment2
		$("button[name='commentBtn']").on("click", function() {
			commentService.getModifyComment($(this).data("idx"), function(result) {
				alert(result.message);
			});
		});

		// modifyComment2
		$("button[name='patchBtn']").on("click", function() {
			let comment = {
				commentContent : "aa"
			};
			commentService.modifyComment($(this).data("idx"), comment, function(result) { 
				alert(result.message);
			});
		});
		
		// deleteComment2
		$("button[name='deleteBtn']").on("click", function() {
			commentService.deleteComment($(this).data("idx"), function(result) { 
				alert(result.message);
			});
		});
		
		// toggleBoardLikes
		$("#testToggleBoardLikesBtn").on("click", function() {
			let boardIdx = $(this).closest("div.input-group").find("input").val();
			likesService.toggleBoardLikes(boardIdx, function(result) {
				// ...
			});
		});
 		
		// toggleCommentLikes
		$("#testToggleCommentLikesBtn").on("click", function() {
			let commentIdx = $(this).closest("div.input-group").find("input").val();
			likesService.toggleCommentLikes(commentIdx, function(result) {
				// ...
			});
		});
		
		// getBoardLikesCnt
		$("#testGetBoardLikesCntBtn").on("click", function() {
			let boardIdx = $(this).closest("div.input-group").find("input").val();
			likesService.getBoardLikesCnt(boardIdx, function(result) {
				// ...
			});
		});

		// getCommentLikesCnt
		$("#testGetCommentLikesCntBtn").on("click", function() {
			let commentIdx = $(this).closest("div.input-group").find("input").val();
			likesService.getCommentLikesCnt(commentIdx, function(result) {
				// ...
			});
		});
	});
</script>
</body>
</html>