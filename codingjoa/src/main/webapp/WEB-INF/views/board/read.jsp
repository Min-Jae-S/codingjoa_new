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
		min-height: 250px;
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
	
	.comment-cnt {
		font-size: 1.3rem;
		font-weight: bold;
	}
	
	.comment-list .list-group-item {
		padding: 1.25rem 0;
	}
	
	.comment-list .list-group-item:last-child {
		padding-bottom: 0;
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
	
	.comment-likes {
		margin-top: auto;
	}
	
	.comment-likes .btn {
		color: #007acf !important;
		box-shadow: none !important;
		padding-top: 0;
		padding-bottom: 0;
	}
	
	.textarea-border {
		border: 1px solid #868e96;
	}
	
	.test-div {
		border-top: 1px solid black;
		padding: 2rem 0;
	}
	
	.test-div div.d-flex {
		justify-content: space-between;
	}
	
	.test-div .test-item {
		width: 30%;
		text-align: left !important;
	}
</style>
</head>
<body>

<c:import url="/WEB-INF/views/include/top-menu.jsp"/>

<div class="container board-container">
	<div class="row">
		<div class="col-sm-2"></div>
		<div class="col-sm-8">
			<div class="card mb-3">
				<div class="header-group">
					<div class="category dropright mb-2">
						<a class="board-category" 
							href="${contextPath}/board/main?boardCategoryCode=${category.categoryCode}">
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
				<div class="content-group py-4">
					<div id="boardContent">
						<c:out value="${boardDetails.boardContent}" escapeXml="false"/>
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
	<div class="test-div mt-5">
		<div class="mb-4 d-flex">
			<button class="btn btn-primary btn-sm test-item" name="commentBtn" data-idx="a">GET: api/comments/a</button>				
			<button class="btn btn-primary btn-sm test-item" name="commentBtn" data-idx="9999">GET: api/comments/9999</button>
			<button class="btn btn-primary btn-sm test-item" name="commentBtn" data-idx="">GET: api/comments/</button>
		</div>
		<div class="mb-4 d-flex">				
			<button class="btn btn-info btn-sm test-item" name="commentListBtn" data-idx="a">GET: api/boards/a/comments</button>				
			<button class="btn btn-info btn-sm test-item" name="commentListBtn" data-idx="9999">GET: api/boards/9999/comments</button>					
			<button class="btn btn-info btn-sm test-item" name="commentListBtn" data-idx="">GET: api/boards//comments</button>					
		</div>
		<div class="mb-4 d-flex">			
			<button class="btn btn-warning btn-sm test-item" name="patchBtn" data-idx="a">PATCH: api/comments/a</button>				
			<button class="btn btn-warning btn-sm test-item" name="patchBtn" data-idx="9999">PATCH: api/comments/9999</button>					
			<button class="btn btn-warning btn-sm test-item" name="patchBtn" data-idx="">PATCH: api/comments/</button>					
		</div>
		<div class="mb-4 d-flex">				
			<button class="btn btn-danger btn-sm test-item" name="deleteBtn" data-idx="a">DELETE: api/comments/a</button>				
			<button class="btn btn-danger btn-sm test-item" name="deleteBtn" data-idx="9999">DELETE: api/comments/9999</button>					
			<button class="btn btn-danger btn-sm test-item" name="deleteBtn" data-idx="">DELETE: api/comments/</button>					
		</div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom-menu.jsp"/>

<script>
	$(function() {
		let boardIdx = "<c:out value='${boardDetails.boardIdx}'/>";
		let boardCategoryCode = "<c:out value='${boardDetails.boardCategoryCode}'/>";
		let boardWriterIdx = "<c:out value='${boardDetails.boardWriterIdx}'/>";
		let commentListURL = "${contextPath}/api/boards/" + boardIdx + "/comments";
		
		commentService.getCommentList(commentListURL , function(result) {
			let commentList = result.data;
			if (commentList.length != 0) {
				let html = makeCommentHtml(commentList, boardWriterIdx);
				$(".comment-list").html(html);
			}
		});
		
		$("button[name='commentBtn']").on("click", function() {
			let url = "${contextPath}/api/comments/" + $(this).data("idx");
			console.log("## URL = " + url);
			commentService.getComment(url , function(result) {
				// ... 
			});
		});

		$("button[name='commentListBtn']").on("click", function() {
			let url = "${contextPath}/api/boards/" + $(this).data("idx") + "/comments";
			console.log("## URL = " + url);
			commentService.getCommentList(url, function(result) { 
				// ... 
			});
		});

		$("button[name='patchBtn']").on("click", function() {
			let comment = null;
			let url = "${contextPath}/api/comments/" + $(this).data("idx");
			console.log("## URL = " + url);
			commentService.modifyComment(url, comment, function(result) { 
				// ...
			});
		});

		$("button[name='deleteBtn']").on("click", function() {
			let url = "${contextPath}/api/comments/" + $(this).data("idx");
			console.log("## URL = " + url);
			commentService.deleteComment(url, function(result) { 
				// ... 
			});
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
					$(this).closest("div").find("button").addClass("btn-outline-primary");
				} else {
					$(this).closest("div").find("button").removeClass("btn-outline-primary");
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
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length != 0) {
						let html = makeCommentHtml(commentList, boardWriterIdx);
						$(".comment-list").html(html);
					}
					$("#commentContent").val("");
				});
			});
		});
		
		$(document).on("click", "button[name=showEditCommentBtn]", function() {
			let $li =  $(this).closest("li");
			let commentIdx = $li.attr("comment-idx");
			
			commentService.getComment("${contextPath}/comments/" + commentIdx, function(result) {
				let commentDetails = result.data;
				let html = makeEditCommentHtml(commentDetails);
				$li.find("div.comment-area").addClass("d-none").after(html);
				
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

		$(document).on("click", "button[name=modifyCommentBtn]", function() {
			let comment = {
				commentIdx : $(this).closest("li").attr("comment-idx"),
				commentBoardIdx : boardIdx,
				boardCategoryCode : boardCategoryCode,
				commentContent : $("#commentContent").val(),
			};
			
			commentService.modifyComment("${contextPath}/comments/" + comment.commentIdx, comment, function(result) {
				alert(result.message);
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length != 0) {
						let html = makeCommentHtml(commentList, boardWriterIdx);
						$(".comment-list").html(html);
					}
				});
			});
		});
		
		$(document).on("click", "button[name=deleteCommentBtn]", function() {
			if (!confirm("댓글을 삭제하시겠습니까?")) {
				return;
			}
			
			let commentIdx = $(this).closest("li").attr("comment-idx");
			commentService.deleteComment("${contextPath}/comments/" + commentIdx, function(result) {
				alert(result.message);
				commentService.getCommentList(commentListURL, function(result) {
					let commentList = result.data;
					if (commentList.length == 0) {
						let html = makeCommentHtml(commentList, boardWriterIdx);
						$(".comment-list").html(html);
					}
				});
			});
		});
		
	});
</script>
</body>
</html>