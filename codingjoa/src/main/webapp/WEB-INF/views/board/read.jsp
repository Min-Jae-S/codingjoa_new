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
<title><c:out value="${boardDetails.boardTitle}"/> | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="${contextPath}/resources/js/jquery.serialize.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/comment.js"></script>
<script src="${contextPath}/resources/js/likes.js"></script>
<script src="${contextPath}/resources/js/handle-errors.js"></script>
<script src="${contextPath}/resources/js/html-creator.js"></script>
<!-- ckeditor -->
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<style>
	.read-wrap {
		min-width: 820px;
		margin: 0 auto;
	}

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
	
	.board-utils {
		display: flex;
	}

	.board-category {
		color: #007bff;
		font-weight: bold;
		font-size: 1rem;
		text-decoration: none !important;
	}
	
	.board-category:after {
		content: ">>";
		margin-right: 0.1rem;
	}
	
	.board-utils-btn {
		box-shadow: none !important;
		padding-top: 0 !important;
		padding-bottom: 0 !important;;
		margin: 0 !important;;
		vertical-align: top !important;;
		border: none !important;
	}
	
	.board-utils .dropdown-item,
	.comment-area-header .dropdown-item {
		font-size: 0.875rem;
	}
	
	.title {
		color: black;
		font-weight: bold;
	}
	
	.board-utils .dropdown-menu,
	.comment-area-header .dropdown-menu {
		padding-top :0;
		padding-bottom: 0;
	}

	.board-utils .dropdown-header,
	.comment-area-header .dropdown-header {
		color: black; 
		font-weight: bold;
		border-bottom: 1px solid #e9ecef;
	}
	
	.board-info {
		display: flex;
		margin-bottom: .5rem !important;
		font-size: 1rem;
	}
	
	.board-info a {
		color: black;
		text-decoration: none;
	}
	
	.board-info .board-info-left {
		display: flex;
		align-items: center;
		gap: 1.5rem;
	}
	
	.board-info .board-info-right {
		display: flex;
		margin-left: auto;
		align-items: center;
		gap: 1.5rem;
	}
	
	.comment-input {
		margin: 0;
		padding: 1.3rem 1.3rem 1rem 1.3rem;
		height: 100%;
	}

	.comment-edit {
		margin: 0;
		padding: 1rem;
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
		line-height: 180%;
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
	
	.comment-cnt-wrap {
		font-size: 1.3rem;
		font-weight: bold;
	}
	
	.comment-list li {
		padding: 28px 28px 28px 98px;
		background-color: #f1f5fc;
	}
	
	.comment-list li.deleted-comment {
		padding-left: 28px;
	}
	
	.comment-list .comment-thum {
		position: absolute;
		top: 29px;
		left: 28px;
		width: 48px;
		height: 48px;
		border-radius: 50%;
		background-repeat: no-repeat;
	}
	
	.comment-list .comment-thum img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
		border: 1px solid #dee2e6 !important
	}
	
	.comment-area-header { 
		margin-bottom: 0.5rem; 
	}
	
	.comment-writer {
		color: #495057;
		font-weight: bold;
	}
	
	.comment-content {
		line-height: 180%;
	}
	
	.comment-content p {
		margin-bottom: 0.5rem !important;
	}
	
	.deleted-comment .comment-content{
		color: #868e96;
	}
	
	.comment-area-header, 
	.comment-area-footer {
		display:flex;
	}
	
	.comment-info {
		display: flex;
		align-items: center;
		gap: 0.5rem;
	}
	
	.comment-utils-btn {
		box-shadow: none !important;
		padding-top: 0 !important;
		padding-bottom: 0 !important;;
		margin: 0 !important;;
		vertical-align: top !important;;
		border: none !important;
	}
	
	.textarea-border {
		border: 1px solid #868e96;
	}
	
	.dropright button {
		padding-right: 0;
	}
	
	.dropright button:disabled {
		opacity: 0.3 !important;
		/* cursor: not-allowed !important; */
	}
	
	.comment-group-footer {
		position: relative;
	}
	
	.comment-group-footer .comment-pagination {
		position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
	}
	
	.text-grey {
		color: #868e96;
	}
	
	button[name="commentLikesBtn"] i {
		font-size: 1.1rem;
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

<div class="container board-container" onselectstart="return false" ondragstart="return false">
	<div class="read-wrap">
		<div class="card rounded-md">
			<div class="header-group">
				<div class="board-utils mb-3">
					<a class="board-category" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}">
						<c:out value="${category.categoryName}"/>
					</a>
					<div class="dropright ml-auto">
						<button class="board-utils-btn btn" data-toggle="dropdown" data-offset="0,10" ${boardDetails.boardWriter ? '' : 'disabled'}>
							<i class="fa-solid fa-ellipsis-vertical"></i>
						</button>
						<div class="dropdown-menu">
							<h6 class="dropdown-header">게시글 관리</h6>
							<a class="dropdown-item" href="${contextPath}/board/modify?boardIdx=${boardDetails.boardIdx}">
								수정하기
							</a>
					      	<a class="dropdown-item" href="${contextPath}/board/delete?boardIdx=${boardDetails.boardIdx}" id="deleteBoardLink">
					      		삭제하기
					     	</a>
					     </div>
					</div>
				</div>
				<h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/></h3>
				<div class="board-info">
					<div class="board-info-left">
						<span><c:out value="${boardDetails.boardWriterNickname}"/></span>
						<span><c:out value="${boardDetails.fullCreatedAt}"/></span>
						<span>조회 <c:out value="${boardDetails.boardViews}"/></span>
					</div>
					<div class="board-info-right">
						<div>
							<i class="fa-regular fa-comment-dots"></i>
							<span>댓글</span>
							<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
						</div>
						<button class="btn border-0 p-0 shadow-none" type="button" id="boardLikesBtn">
							<i class="fa-heart ${boardDetails.boardLiked ? 'fa-solid text-danger' : 'fa-regular'}"/></i>
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
					<div class="comment-group-header">
						<div class="comment-cnt-wrap mb-3">
							<span>댓글</span>
							<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
						</div>
						<div class="comment-write-wrap">
							<form>
								<div class="input-group">
									<div class="comment-input form-control rounded-md">
										<sec:authorize access="isAuthenticated()">
											<p class="font-weight-bold mb-2">
												<sec:authentication property="principal.nickname"/>
											</p>
										</sec:authorize>
										<textarea name="commentContent" rows="1" placeholder="댓글을 남겨보세요"></textarea>
										<input type="hidden" name="boardIdx" value="${boardDetails.boardIdx}">
										<div class="mt-2">
											<button class="btn btn-outline-secondary rounded-md" type="submit">등록</button>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				<div class="comment-group-body mt-4">
					<div class="comment-list">
						<!------------------------>
						<!----    comments    ---->
						<!------------------------>
					</div>
				</div>
				<div class="comment-group-footer mt-4">
					<a class="btn btn-secondary rounded-md" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
						${boardCri.queryString}">목록
					</a>
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
					<span class="input-group-text">Get pagedComment</span>
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
				<button class="btn">Get pagedComment<span>:</span></button>
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
		<div class="test3 mt-5 d-none">
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
		
		<!-- form test -->
		<div class="test4">
			<form class="d-none">
				<input type="hidden" name="boardIdx" value="${boardDetails.boardIdx}"/>
				<input type="hidden" name="boardTitle" value="${boardDetails.boardTitle}"/>
			</form>
		</div>
		
		<!-- execute test -->
		<div class="mt-4 d-none">
			<button class="btn btn-warning" onclick="getFormData()">getFormData</button>
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
	
	function saveCommentsAsMap(pagedComment, commentMap) {
		commentMap.clear();
		if (pagedComment.length == 0) {
			return;
		}
		
		$.each(pagedComment, function(index, commentDetails) {
			if (commentDetails != "") {
				commentMap.set(commentDetails.commentIdx, commentDetails);
			}
		});
	}
	
	$(function() {
		const $commentListDiv = $("div.comment-list");
		const $commentPageDiv = $("div.comment-pagination");
		const boardIdx = "<c:out value='${boardDetails.boardIdx}'/>";
		let commentMap = new Map();
		let curCommentPage = 1;
		
		commentService.getPagedComment(boardIdx, curCommentPage, function(result) {
			let pagedComment = result.data.pagedComment;
			saveCommentsAsMap(pagedComment, commentMap);
			
			let pagedCommentHtml = createPagedCommentHtml(pagedComment);
			$commentListDiv.html(pagedCommentHtml);

			let pagination = result.data.pagination;
			let paginationHtml = createPaginationHtml(pagination);
			$commentPageDiv.html(paginationHtml);
			$(".comment-cnt").text(pagination.totalCnt);
		});
		
		$("#deleteBoardLink").on("click", function() {
			return confirm("게시글을 삭제하시겠습니까?");
		});
		
		$(document).on("focus", ".comment-group textarea", function() {
			$(this).closest("div").addClass("textarea-border");
		});
		
		$(document).on("blur", ".comment-group textarea", function() {
			$(this).closest("div").removeClass("textarea-border");
		});
		
		$(document).on("input", ".comment-group textarea", function() {
			$(this).height("auto");
			$(this).height($(this).prop("scrollHeight") + "px");
			
			let $submitBtn = $(this).closest("div").find("button[type='submit']");
			if ($(this).val() != "") {
				//$submitBtn.prop("disabled", false).removeClass().addClass("btn btn-sm btn-outline-primary");
				$submitBtn.removeClass().addClass("btn btn-sm btn-primary");
			} else {
				//$submitBtn.prop("disabled", true).removeClass().addClass("btn btn-sm btn-outline-secondary");
				$submitBtn.removeClass().addClass("btn btn-sm btn-outline-secondary");
			}
		});
		
		// show editComment form
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li = $(this).closest("li");
			let commentDetails = commentMap.get($li.data("idx"));
			let editCommentHtml = createEditCommentHtml(commentDetails);
			$li.html(editCommentHtml);
			$li.find("textarea").trigger("input").focus();
		});

		// close editComment form
		$(document).on("click", ".comment-edit-wrap button[type='button']", function() {
			let $li = $(this).closest("li");
			let commentDetails = commentMap.get($li.data("idx"));
			let commentHtml = createCommentHtml(commentDetails);
			$li.html(createCommentHtml(commentDetails));
		});
		
		// writeComment
		$(document).on("submit", ".comment-write-wrap form", function(e) {
			e.preventDefault();
			let $form = $(this);
			let comment = $form.serializeObject();
			
			commentService.writeComment(comment, function(result) {
				alert(result.message);
				commentService.getPagedComment(boardIdx, 1, function(result) {
					let pagedComment = result.data.pagedComment;
					saveCommentsAsMap(pagedComment, commentMap);
					
					let pagedCommentHtml = createPagedCommentHtml(pagedComment);
					$commentListDiv.html(pagedCommentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$commentPageDiv.html(paginationHtml);
					
					$(".comment-cnt").text(pagination.totalCnt);	
					$form.trigger("reset");
					$form.find("textarea").trigger("input");
				});
			});
		});

		// modifyComment
		$(document).on("submit", ".comment-edit-wrap form", function(e) {
			e.preventDefault();
			let comment = $(this).serializeObject();
			let commentIdx = $(this).closest("li").data("idx");
			
			commentService.modifyComment(commentIdx, comment, function(result) {
				alert(result.message);
				commentService.getPagedComment(boardIdx, curCommentPage, function(result) {
					let pagedComment = result.data.pagedComment;
					saveCommentsAsMap(pagedComment, commentMap);
					
					let pagedCommentHtml = createPagedCommentHtml(pagedComment);
					$commentListDiv.html(pagedCommentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$commentPageDiv.html(paginationHtml);
					
					$(".comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// deleteComment
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) {
				return;
			}
			
			let commentIdx = $(this).closest("li").data("idx");
			commentService.deleteComment(commentIdx, function(result) {
				alert(result.message);
				commentService.getPagedComment(boardIdx, curCommentPage, function(result) {
					let pagedComment = result.data.pagedComment;
					saveCommentsAsMap(pagedComment, commentMap);
					
					let pagedCommentHtml = createPagedCommentHtml(pagedComment);
					$commentListDiv.html(pagedCommentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = createPaginationHtml(pagination);
					$commentPageDiv.html(paginationHtml);
					
					$(".comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// pagination
		$(document).on("click", "a.page-link", function(e) {
			e.preventDefault();
			commentService.getPagedComment(boardIdx, $(this).data("page"), function(result) {
				let pagedComment = result.data.pagedComment;
				saveCommentsAsMap(pagedComment, commentMap);
				
				let pagedCommentHtml = createPagedCommentHtml(pagedComment);
				$commentListDiv.html(pagedCommentHtml);

				let pagination = result.data.pagination;
				let paginationHtml = createPaginationHtml(pagination);
				$commentPageDiv.html(paginationHtml);
				
				$(".comment-cnt").text(pagination.totalCnt);
				curCommentPage = pagination.page;
				console.log("## curCommentPage = %s", curCommentPage);
			});
		});
		
		// toggleBoardLikes
		$("#boardLikesBtn").on("click", function() {
			likesService.toggleBoardLikes(boardIdx, function(result) {
				alert(result.message);
				let boardLiked = result.data;
				let cssClass = (boardLiked) ? "fa-heart fa-solid text-danger" : "fa-heart fa-regular text-grey";
				$("#boardLikesBtn i").removeClass().addClass(cssClass);
				
				likesService.getBoardLikesCnt(boardIdx, function(result) {
					$(".board-likes-cnt").text(result.data);
				});
			});
		});
		
		// toggleCommentLikes
		$(document).on("click", "button[name=commentLikesBtn]", function() {
			let $li = $(this).closest("li");
			let commentIdx = $li.data("idx");
			
			likesService.toggleCommentLikes(commentIdx, function(result) {
				alert(result.message);
				let commentLiked = result.data;
				let cssClass = (commentLiked) ? "fa-regular fa-thumbs-up text-primary" : "fa-regular fa-thumbs-up text-grey";
				$li.find("button[name=commentLikesBtn] i").removeClass().addClass(cssClass);
				
				likesService.getCommentLikesCnt(commentIdx, function(result) {
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

		// getPagedComment
		$("#testGetCommentListBtn").on("click", function() {
			let $input = $(this).closest("div.input-group").find("input");
			let commentBoardIdx = $input.first().val();
			let page = $input.last().val();
			commentService.getPagedComment(commentBoardIdx, page, function(result) {
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
		
		// getPagedComment2
		$("button[name='commentListBtn']").on("click", function() {
			commentService.getPagedComment($(this).data("idx"), curCommentPage, function(result) {
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
	
	function getFormData() {
		let $form = $("div.test4").find("form");
		let boardIdx = $form.find("input[name='boardIdx']").val();
		let boardTitle = $form.find("input[name='boardTitle']").val();
		console.log("## boardIdx = %s", boardIdx);
		console.log("## boardTitle = %s", boardTitle);
	}
</script>
</body>
</html>