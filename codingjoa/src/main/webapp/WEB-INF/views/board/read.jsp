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
<title><c:out value="${boardDetails.boardTitle}"/></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/css/common.css">
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://kit.fontawesome.com/c503d71f81.js"></script>
<script src="${contextPath}/resources/js/comment.js"></script>
<script src="${contextPath}/resources/js/likes.js"></script>
<script src="${contextPath}/resources/js/render.js"></script>
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
	
	.card {
		padding: 2.25rem 2.25rem 2.25rem 2.25rem;
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
		font-size: 15px;
		color: #868e96;
		/* color: #979797;
		color: #757575; */
	}
	
	.header-group .header-meta a {
		color: black;
		text-decoration: none;
	}
	
	a.comment-likes {
		font-size: 15px;
		text-decoration: none;
	}

	a.comment-likes:hover {
		color: #007bff;
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
		margin-bottom: 0;
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
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
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
						<%-- <sec:authorize access="isAuthenticated()">
							<sec:authentication property="principal.member" var="member"/>
							<c:if test="${member.memberIdx eq boardDetails.boardWriterIdx}">
								<button class="board-utils btn btn-lg" data-toggle="dropdown" data-offset="0,10">
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
							</c:if>
						</sec:authorize> --%>
					</div>
					<%-- <h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/></h3> --%>
					<h3 class="title mb-4"><c:out value="${boardDetails.boardTitle}"/> (boardIdx = ${boardDetails.boardIdx})</h3>
					<div class="header-meta d-flex mb-2">
						<span class="mr-3"><c:out value="${boardDetails.memberId}"/></span>
						<span class="mr-3">
							<fmt:formatDate value="${boardDetails.regdate}" pattern="yyyy.MM.dd. HH:mm"/>
						</span>
						<span class="mr-1">조회</span>
						<span><c:out value="${boardDetails.boardViews}"/></span>
						<div class="ml-auto">
							<a class="mr-3" href="#">
								<span><i class="fa-regular fa-comment-dots"></i></span>
								<span>댓글</span>
								<span class="comment-cnt"><c:out value="${boardDetails.commentCnt}"/></span>
							</a>
							<a class="board-likes" href="#">
								<span><i class="fa-regular fa-heart"></i></span>
								<span>좋아요</span>
								<span class="board-likes-cnt"><c:out value="${boardDetails.boardLikesCnt}"/></span>
							</a>
						</div>
					</div>
				</div>
				<div class="content-group py-4">
					<div id="boardContent">
						<c:out value="${boardDetails.boardContent}" escapeXml="false"/>
					</div>
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
								<button class="btn btn-sm" id="writeCommentBtn" disabled>등록</button>
							</div>
						</div>
					</div>
					<div class="comment-list mt-4">
						<!-- comment -->
					</div>
					<div class="mt-4">
						<a class="btn btn-secondary float-left" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
							${boardCri.getQueryString()}">목록</a>
						<div class="comment-pagination">
							<!-- pagination -->
						</div>
					</div>
				</div>
			</div>
			<%-- <div class="card-bottom">
				<a class="btn btn-secondary" href="${contextPath}/board/?boardCategoryCode=${category.categoryCode}&
					${boardCri.getQueryString()}">목록</a>
			</div> --%>
		</div>
		<div class="col-sm-1"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		const boardIdx = "<c:out value='${boardDetails.boardIdx}'/>";
		const boardWriterIdx = "<c:out value='${boardDetails.boardWriterIdx}'/>";
		let curPage = 1;
		
		// get comment list
		commentService.getCommentList(boardIdx, curPage, function(result) {
			let commentList = result.data.commentList;
			let commentHtml = makeCommentHtml(commentList, boardWriterIdx);
			$("div.comment-list").html(commentHtml);

			let pagination = result.data.pagination;
			let paginationHtml = makePaginationHtml(pagination);
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
			
			if ($(this).val() != "") {
				$(this).closest("div").find("button[name='modifyCommentBtn']")
					.attr("disabled", false).addClass("btn-outline-primary");
			} else {
				 $(this).closest("div").find("button[name='modifyCommentBtn']")
				 	.attr("disabled", true).removeClass("btn-outline-primary");
			}
		});
		
		// write comment
		$("#writeCommentBtn").on("click", function() {
			let comment = {
				commentBoardIdx : boardIdx,
				commentContent : $("#commentContent").val(),
			};
			
			commentService.writeComment(comment, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, 1, function(result) {
					let commentList = result.data.commentList;
					let commentHtml = makeCommentHtml(commentList, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = makePaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
					$("#commentContent").val("");
				});
			});
		});
		
		// get comment
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.data("comment-idx");
			
			commentService.getComment(commentIdx, function(result) {
				let commentDetails = result.data;
				let editCommentHtml = makeEditCommentHtml(commentDetails);
				$li.find("div.comment-area").addClass("d-none").after(editCommentHtml);
				
				let $textarea = $li.find("div.comment-edit textarea");
				$textarea.height("auto");
				$textarea.height($textarea.prop("scrollHeight") + "px");
				$textarea.focus();
			});
		});

		$(document).on("click", "button[name=closeEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			$li.find("div.comment-area").removeClass("d-none")
				.next("div.input-group").remove();
		});
		
		// update comment
		$(document).on("click", "button[name=modifyCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.data("comment-idx");
			let comment = {
				commentContent : $li.find("div.comment-edit textarea").val(),
			};
			
			commentService.modifyComment(commentIdx, comment, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, curPage, function(result) {
					let commentList = result.data.commentList;
					let commentHtml = makeCommentHtml(commentList, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = makePaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// delete comment
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) {
				return;
			}
			
			let commentIdx = $(this).closest("li").data("comment-idx");
			commentService.deleteComment(commentIdx, function(result) {
				alert(result.message);
				commentService.getCommentList(boardIdx, curPage, function(result) {
					let commentList = result.data.commentList;
					let commentHtml = makeCommentHtml(commentList, boardWriterIdx);
					$("div.comment-list").html(commentHtml);

					let pagination = result.data.pagination;
					let paginationHtml = makePaginationHtml(pagination);
					$("div.comment-pagination").html(paginationHtml);
					$("span.comment-cnt").text(pagination.totalCnt);	
				});
			});
		});
		
		// pagination
		$(document).on("click", "a.page-link", function(e) {
			e.preventDefault();
			let clickedPage = $(this).data("page");
			commentService.getCommentList(boardIdx, clickedPage, function(result) {
				console.log("## page has changed from %s to %s", curPage, clickedPage);
				curPage = clickedPage;
				$("li.page-item").removeClass("active");
				$(this).closest("li.page-item").addClass("active");
				
				let commentList = result.data.commentList;
				let commentHtml = makeCommentHtml(commentList, boardWriterIdx);
				$("div.comment-list").html(commentHtml);

				let pagination = result.data.pagination;
				let paginationHtml = makePaginationHtml(pagination);
				$("div.comment-pagination").html(paginationHtml);
				$("span.comment-cnt").text(pagination.totalCnt);	
			});
		});
		
		// board-likes
		$("a.board-likes").on("click", function(e) {
			e.preventDefault();
			likesService.toggleBoardLikes(boardIdx, function(result) {
				alert(result.message);
			});
		});
		
		// comment-likes
		$(document).on("click", "a.comment-likes ", function(e) {
			e.preventDefault();
			let commentIdx = $(this).closest("li").data("comment-idx");
			likesService.toggleCommentLikes(commentIdx, function(result) {
				alert(result.message);
			});
		});
		
	});
</script>
</body>
</html>