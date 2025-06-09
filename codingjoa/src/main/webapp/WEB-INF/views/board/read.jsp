<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title><c:out value="${boardDetails.title}"/> | Codingjoa</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" rel="stylesheet" >
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" >
<link href="${contextPath}/resources/fontawesome/css/all.css" rel="stylesheet">
<script src="${contextPath}/resources/fontawesome/js/all.js"></script>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${contextPath}/resources/js/service/comment.js"></script>
<script src="${contextPath}/resources/js/service/like.js"></script>
<!-- ckeditor -->
<script src="${contextPath}/resources/ckeditor5/build/ckeditor.js"></script>
<script src="${contextPath}/resources/ckeditor5/plugins/ckeditor-plugins.js"></script>
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
		margin-bottom: 0.5rem;
	}
	
	.title {
		color: black;
		font-weight: bold;
	}

	.board-category {
		color: #007bff;
		font-weight: bold;
		font-size: 1rem;
		text-decoration: none !important;
	}
	
	.board-category:hover {
		color: #007bff;
	}
	
	.board-category svg { /* .svg-inline--fa : vertical-align: -.125em; */
		vertical-align: -.15em;
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
		border-radius: 0.5rem;
	}

	.comment-edit {
		margin: 0;
		padding: 1rem;
		height: 100%;
		border-radius: 0.5rem;
	}
	
	.comment-input button,
	.comment-edit button {
		border-radius: 0.5rem;
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
	
	.comment-cnt-wrap {
		font-size: 1.3rem;
		font-weight: bold;
	}
	
	.comment-list > ul {
		border-radius: 0.5rem;
	}
	
	.comment-list > ul > li {
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
	
	.textarea-border {
		border: 1px solid #868e96;
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
	
	.comment-group-footer .comment-pagination .pagination {
		margin-bottom: 0;
	}
	
	.ck-read-only {
   		--ck-widget-outline-thickness: 0;
   		border: none !important;
   		padding: 0 !important;
	}
	
	.board-utils-btn, .comment-utils-btn {
		margin: 0;
		padding: 0;
		border: none;
		background: transparent;
		width: 20px;
		text-align: right;
	}
	
	.board-utils .dropdown-menu,
	.comment-area-header .dropdown-menu {
		margin: 0;
		padding: 0;
		text-align: center;
		width: 140px;
		border-radius: 0.5rem;
	}
	
	/* .board-utils .dropdown-menu[data-popper-placement="right-start"],
	.comment-area-header .dropdown-menu[data-popper-placement="right-start"] {
		transform: translate(1081px, 33px) !important;
	} */
	/* .board-utils .dropdown-menu[x-placement="right-start"],
	.comment-area-header .dropdown-menu[x-placement="right-start"] {
		transform: translate3d(37px, 0px, 0px) !important;
	} */

	/* .board-utils .dropdown-menu[data-popper-placement="left-start"],
	.comment-area-header .dropdown-menu[data-popper-placement="left-start"] {
		transform: translate(-50px, 33px) !important;
	} */
	/* .board-utils .dropdown-menu[x-placement="right-start"],
	.comment-area-header .dropdown-menu[x-placement="right-start"] {
		transform: translate3d(-162px, 0px, 0px) !important;
	} */
	
	.board-utils .dropdown-menu li,
	.comment-area-header .dropdown-menu li {
		margin: 0;
    	padding: 4px 0;
	}

	.board-utils .dropdown-header,
	.comment-area-header .dropdown-header {
		color: black;
		font-weight: 600;
		font-size: 0.9rem;
	}

	.board-utils .dropdown-item,
	.comment-area-header .dropdown-item {
		color: #17191c;
		font-size: 0.9rem;
	}

	.board-utils .dropdown-item:active,
	.comment-area-header .dropdown-item:active {
		background-color: #e9ecef;
	}
	
	button[name=commentLikeBtn] { /* btn border-0 p-0 shadow-none ml-auto */
		padding: 0;
		border: 0;
		background-color: transparent;
		margin-left: auto;
	}
	
	button[name=commentLikeBtn] .icon { 
		color: #868e96;
		margin-right: 4px;
	}
	
	/* https://stackoverflow.com/questions/54272325/how-to-style-ckeditor-content-for-read-only-display */
	/* These styles hide weird "things" in CKeditor Viewer (read only mode) */
	/* .ckeditor-viewer .ck.ck-editor__editable.ck-blurred .ck-widget.ck-widget_selected, 
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
	} */
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
		<div class="card rounded-xl">
			<div class="header-group">
				<div class="board-utils">
					<a class="board-category" href="${contextPath}/board/?categoryCode=${category.code}">
						<c:out value="${category.name}"/><i class="fa-angle-right fa-fw fa-solid"></i>
					</a>
					<div class="dropend ml-auto"> <!-- dropright -->
						<button class="board-utils-btn" data-bs-toggle="dropdown" data-bs-auto-close="outside" ${boardDetails.writer ? '' : 'disabled'}>
							<i class="fa-ellipsis-vertical fa-solid"></i>
						</button>
						<ul class="dropdown-menu">
							<h6 class="dropdown-header">게시글 관리</h6>
							<hr class="dropdown-divider">
							<li>
								<a class="dropdown-item" href="${contextPath}/board/modify?id=${boardDetails.id}">
									수정하기
								</a>
						      	<a class="dropdown-item" href="${contextPath}/board/delete?id=${boardDetails.id}" id="deleteBoardLink">
						      		삭제하기
						     	</a>
							</li>
						</ul>
					</div>
				</div>
				<h3 class="title mb-4"><c:out value="${boardDetails.title}"/></h3>
				<div class="board-info">
					<div class="board-info-left">
						<span><c:out value="${boardDetails.writerNickname}"/></span>
						<span><c:out value="${boardDetails.fullCreatedAt}"/></span>
						<span>조회 <c:out value="${boardDetails.viewCount}"/></span>
					</div>
					<div class="board-info-right">
						<div>
							<span><i class="fa-comment-dots fa-fw fa-regular"></i></span>
							<span>댓글</span>
							<span class="comment-cnt"><c:out value="${boardDetails.commentCount}"/></span>
						</div>
						<button class="btn border-0 p-0 shadow-none" type="button" id="boardLikeBtn">
							<span class="icon">
								<i class="fa-heart fa-fw ${boardDetails.liked ? 'fa-solid text-danger' : 'fa-regular'}"/></i>
							</span>
							<span>좋아요</span>
							<span class="board-like-cnt"><c:out value="${boardDetails.likeCount}"/></span>
						</button>
					</div>
				</div>
			</div>
			<div class="content-group py-4">
				<textarea class="d-none" id="content"></textarea>
				<%-- <c:out value="${boardDetails.content}" escapeXml="false"/> --%>
			</div>
			<div class="comment-group pt-4">
					<div class="comment-group-header">
						<div class="comment-cnt-wrap mb-3">
							<span>댓글</span>
							<span class="comment-cnt"><c:out value="${boardDetails.commentCount}"/></span>
						</div>
						<div class="comment-write-wrap">
							<form>
								<div class="input-group">
									<div class="comment-input form-control">
										<sec:authorize access="isAuthenticated()">
											<p class="font-weight-bold mb-2">
												<sec:authentication property="principal.nickname"/>
											</p>
										</sec:authorize>
										<textarea name="content" rows="1" placeholder="댓글을 남겨보세요"></textarea>
										<input type="hidden" name="boardId" value="${boardDetails.id}">
										<div class="mt-2">
											<button class="btn btn-sm btn-outline-secondary" type="submit" disabled>등록</button>
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
					<a class="btn btn-secondary rounded-md" href="${contextPath}/board/?categoryCode=${category.code}&${boardCri.queryParams}">
						목록
					</a>
					<div class="comment-pagination">
						<!------------------------>
						<!-- comment pagination -->
						<!------------------------>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	ClassicEditor
		.create(document.querySelector("#content"), {
			toolbar: []
		})
		.then(editor => {
			const toolbarContainer = editor.ui.view.stickyPanel;
			editor.ui.view.top.remove(toolbarContainer);
			editor.enableReadOnlyMode("editor");
			const content = '<c:out value="${boardDetails.content}" escapeXml="false"/>';
			editor.setData(content);
		})
		.catch(error => {
			console.error(error);
		});
	
	$(function() {
		const $commentListDiv = $("div.comment-list");
		const $commentPageDiv = $("div.comment-pagination");
		const boardId = "<c:out value='${boardDetails.id}'/>";
		let commentMap = new Map();
		
		function saveCommentsToMap(pagedComments, commentMap) {
			commentMap.clear();
			if (pagedComments.length == 0) {
				return;
			}
			
			$.each(pagedComments, function(index, commentDetails) {
				if (commentDetails) {
					commentMap.set(commentDetails.id, commentDetails);
				}
			});
		}
		
		function getPagedComments(boardId, page) {
			commentService.getPagedComments(boardId, page, function(result) {
				let pagedComments = result.data.pagedComments;
				saveCommentsToMap(pagedComments, commentMap);
				
				let pagedCommentsHtml = createPagedCommentsHtml(pagedComments);
				$commentListDiv.html(pagedCommentsHtml);
	
				let pagination = result.data.pagination;
				let paginationHtml = createPaginationHtml(pagination);
				$commentPageDiv.html(paginationHtml);
				
				$(".comment-cnt").text(pagination.validCnt);
			});
		}
		
		// ## initiate pagedComments
		getPagedComments(boardId, 1);
		
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
				$submitBtn.prop("disabled", false);
			} else {
				$submitBtn.prop("disabled", true);
			}
		});
		
		// show editComment form
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li = $(this).closest("li.list-group-item");
			let commentDetails = commentMap.get($li.data("id"));
			let editCommentHtml = createEditCommentHtml(commentDetails);
			
			if (!editCommentHtml) {
				alert("권한이 없습니다.");
				return;
			}
			
			$li.html(editCommentHtml);
			$li.find("textarea").trigger("input").focus();
		});

		// close editComment form
		$(document).on("click", ".comment-edit-wrap button[type='button']", function() {
			let $li = $(this).closest("li.list-group-item");
			let commentId = $li.data("id");
			
			let commentDetails = commentMap.get(commentId);
			let commentHtml = createCommentHtml(commentDetails);
			
			$li.html(createCommentHtml(commentDetails));
		});
		
		// write comment
		$(document).on("submit", ".comment-write-wrap form", function(e) {
			e.preventDefault();
			let $form = $(this);
			let comment = $form.serializeObject();
			let currentPage = 1;
			
			commentService.writeComment(comment, function(result) {
				alert(result.message);
				getPagedComments(boardId, currentPage);
				
				$form.trigger("reset");
				$form.find("textarea").trigger("input");
			});
		});

		// modify comment
		$(document).on("submit", ".comment-edit-wrap form", function(e) {
			e.preventDefault();
			let comment = $(this).serializeObject();
			let commentId = $(this).closest("li.list-group-item").data("id");
			let currentPage = $(".comment-pagination .active .page-link").data("page");
			
			commentService.modifyComment(commentId, comment, function(result) {
				alert(result.message);
				getPagedComments(boardId, currentPage);
			});
		});
		
		// delete comment
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) {
				return;
			}
			
			let commentId = $(this).closest("li.list-group-item").data("id");
			let currentPage = $(".page-item.active .page-link").data("page");
			
			commentService.deleteComment(commentId, function(result) {
				alert(result.message);
				getPagedComments(boardId, currentPage);
			});
		});
		
		// pagination
		$(document).on("click", ".comment-pagination .page-link", function() {
			getPagedComments(boardId, $(this).data("page"));
		});
		
		// toggle boardLike
		$("#boardLikeBtn").on("click", function() {
			likeService.toggleBoardLike(boardId, function(result) {
				alert(result.message);
				let iconClass = result.data.liked ? "fa-heart fa-fw fa-solid text-danger" : "fa-heart fa-fw fa-regular";
				$("#boardLikeBtn .icon").html(`<i class="\${iconClass}"></i>`);
				$(".board-like-cnt").text(result.data.likeCount);
			});
		});
		
		// toggle commentLike
		$(document).on("click", "button[name=commentLikeBtn]", function() {
			let $this = $(this);
			let commentId = $(this).closest("li").data("id");
			
			likeService.toggleCommentLike(commentId, function(result) {
				alert(result.message);
				let iconClass = result.data.liked ? "fa-thumbs-up fa-fw fa-regular text-primary" : "fa-thumbs-up fa-fw fa-regular";
				$this.find(".icon").html(`<i class="\${iconClass}"></i>`);
				$this.find(".comment-like-cnt").text(result.data.likeCount);
			});
		});
		
	});
</script>
</body>
</html>