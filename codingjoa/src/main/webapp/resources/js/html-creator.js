const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

function createCategoryMenuHtml(categories, parentPath) {
	console.log("## createCategoryMenuHtml");
	if (!categories || categories.length == 0) {
		return "";
	}
	
	return categories.map(({ path, name }) => {
		//return `<button class='dropdown-item' type='button' data-path='${path}'>${name}</button>`;
		return `<a href='${contextPath}${parentPath}${path}' class='dropdown-item'>${name}</a>`;
	}).join("");
}

function createPasswordChangeForm() {
	console.log("## createPasswordChangeForm");
	let html = '<h5 class="mb-4 font-weight-bold">계정 보안</h5>';
	html += '<div>';
	html += '<dl class="form-group">';
	html += '<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>';
	html += '<div class="show-wrap">';
	html += '<dd class="input-group" id="showPassword">';
	html += '<div>';
	html += '<span class="inner-text">********</span>';
	html += '</div>';
	html += '<button class="btn btn-outline-primary btn-sm">수정</button>';
	html += '</dd>';
	html += '</div>';
	html += '<div class="form-wrap d-none">';
	html += '<form id="passwordChangeForm">';
	html += '<dd class="input-group">';
	html += '<input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호를 입력해주세요."/>';
	html += '<div>';
	html += '<button class="btn btn-outline-primary btn-sm" type="submit">확인</button>';
	html += '<button class="btn btn-outline-secondary btn-sm" type="reset">취소</button>';
	html += '</div>';
	html += '</dd>';
	html += '<dd class="input-group">';
	html += '<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요."/>';
	html += '</dd>';
	html += '<dd class="input-group">';
	html += '<input type="password" id="confirmPassword" name="confirmPassword" placeholder="확인 비밀번호를 입력해주세요."/>';
	html += '</dd>';
	html += '</form>';
	html += '</div>';
	html += '</dl>';
	html += '</div>';
	return html;
}

function createPagedCommentsHtml(pagedComments) {
	console.log("## createPagedCommentsHtml");
	let html = "";
	if (!pagedComments || pagedComments.length == 0) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(pagedComments, function(index, commentDetails) {
		if (!commentDetails) {
			html += "<li class='list-group-item deleted-comment'>";
			html += "<div class='comment-area'>";
			html += "<div class='comment-area-header'>";
			html += "<div class='comment-info'>";
			html += "<span class='comment-writer'>삭제된 댓글</span>";
			html += "</div>";
			html += "</div>";
			html += "<div class='comment-area-body'>";
			html += "<div class='comment-content' style='line-height:180%;'>";
			html += "<p>삭제된 댓글입니다.</p>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</li>";
			return true;
		}
		
		html += "<li class='list-group-item' data-id='" + commentDetails.id + "'>";
		html += createCommentHtml(commentDetails);
		html += "</li>";
	});
	html += "</ul>";
	return html;
}

function createCommentHtml(commentDetails) {
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.writerImagePath) {
		html += `<img src='${contextPath}${commentDetails.writerImagePath}'>`;
	} else {
		html += `<img src='${contextPath}/resources/images/img_profile.png'>`;
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += "<span class='comment-writer'>" + commentDetails.writerNickname + "</span>";
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += "<span class='comment-createdat'>" + commentDetails.createdAt + "</span>";
	html += "<span class='comment-updatedat d-none'>" + commentDetails.updatedAt + "</span>";
	html += "</div>";
	html += "<div class='dropend ml-auto'>"; // dropright --> dropend
	if (commentDetails.isWriter) {
		html += "<button class='comment-utils-btn' data-bs-toggle='dropdown' data-bs-auto-close='outside'>";
	} else {
		html += "<button class='comment-utils-btn' data-bs-toggle='dropdown' data-bs-auto-close='outside' disabled>";
	}
	
	html += "<i class='fa-ellipsis-vertical fa-solid'></i>";
	html += "</button>";
	html += "<ul class='dropdown-menu'>";
	html += "<h6 class='dropdown-header'>댓글 관리</h6>";
	html += "<hr class='dropdown-divider'>";
	html += "<li>";
	html += "<button class='dropdown-item' type='button' name='showEditCommentBtn'>수정하기</button>";
	html += "<button class='dropdown-item' type='button' name='deleteCommentBtn'>삭제하기</button>";
	html += "</li>";
	html += "</ul>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-body'>";
	html += "<div class='comment-content'>";
	html += "<p>" + commentDetails.content.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-footer'>";
	html += "<button type='button' name='commentLikeBtn'>";
	html += "<span class='icon'>";
	if (commentDetails.isLiked) {
		html += "<i class='fa-thumbs-up fa-fw fa-regular text-primary'></i>";
	} else {
		html += "<i class='fa-thumbs-up fa-fw fa-regular'></i>";
	}

	html += "</span>";
	html += "<span class='comment-like-cnt'>" + commentDetails.likeCount + "</span>";	
	html += "</button>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	console.log(commentDetails);
	
	let html = "";
	if (!commentDetails.isWriter) {
		return html;
	}
	
	html += "<div class='comment-thum'>";
	if (commentDetails.writerImagePath) {
		html += `<img src='${contextPath}${commentDetails.writerImagePath}'>`;
	} else {
		html += `<img src='${contextPath}/resources/images/img_profile.png'>`;
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += "<span class='comment-writer'>" + commentDetails.writerNickname + "</span>";
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += "<span class='comment-createdat'>" + commentDetails.createdAt + "</span>";
	html += "<span class='comment-updatedat d-none'>" + commentDetails.updatedAt + "</span>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-edit-wrap'>";
	html += "<form>"
	html += "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += "<textarea name='content' rows='1'>" + commentDetails.content + "</textarea>";
	html += "<div class='mt-2'>";
	html += "<button type='submit' class='btn btn-sm btn-outline-primary'>수정</button>";
	html += "<button type='button' class='btn btn-sm btn-outline-secondary ml-2'>취소</button>";
	html += "</div>";		
	html += "</div>";			
	html += "</div>";	
	html += "</form>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createPaginationHtml(pagination) {
	if (!pagination) {
		return "";
	}
	
	const { startPage, endPage, prevPage, nextPage, page, pageCnt, prev, next, first, last } = pagination;
	
	let firstPageBtn = "";
	if (first) {
		firstPageBtn = `
			<li class="page-item">
				<button type='button' class='page-link' data-page='1'>
					<i class='fa-solid fa-fw fa-angles-left'></i>
				</button>
			</li>`;
	}
	
	let prevPageBtn = "";
	if (prev) {
		prevPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${prevPage}'>
					<i class='fa-solid fa-fw fa-angle-left'></i>
				</button>
			</li>`;
	}
	
	let pageBtns = "";
	for (let i = startPage; i <= endPage; i++) {
		pageBtns += `
			<li class='page-item ${i == page ? "active" : ""}'>
				<button type='button' class='page-link' data-page='${i}'>${i}</button>
			</li>`;
	}
	
	let nextPageBtn = "";
	if (next) {
		nextPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${nextPage}'>
					<i class='fa-solid fa-fw fa-angle-right'></i>
				</button>
			</li>`;
	}

	let lastPageBtn = "";
	if (last) {
		lastPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${pageCnt}'>
					<i class='fa-solid fa-fw fa-angles-right'></i>
				</button>
			</li>`;
	}

	return `
		<ul class='pagination'>
			${firstPageBtn}
			${prevPageBtn}
			${pageBtns}
			${nextPageBtn}
			${lastPageBtn}
		</ul>`;
}

// =============================================
//				ADMIN HTML CREATOR
// =============================================

function createErrorPageHtml() {
	return `
		<div class="error-wrap">
			<p class="error-code">404</p>
			<p class="error-message">요청한 페이지를 찾을 수 없습니다.</p>
		</div>`;
}

function createWelcomePageHtml() {
	return `<p class='welcome'>Welcome to Admin Dashboard</p>`;
}

function createUsersPageHtml(result) {
	console.log("## createUsersPageHtml");
	const { options, adminUserCri, pagedUsers, pagination } = result.data;
	
	let formHtml = createUsersFormHtml(options, adminUserCri);
	let tableHtml = createUsersTableHtml(pagedUsers);
	let paginationHtml = createPaginationHtml(pagination);
	
	return `
		<div class="card rounded-xl">
			<div class="card-body">
				<div class="form-wrap">
					${formHtml}
				</div>
				<div class="table-wrap">
					${tableHtml}
				</div>
				<div class="mb-3">
					<button type="button" id="deleteUsersBtn" class="btn btn-primary rounded-md mr-auto" disabled>선택삭제</button>
				</div>
				<div class="board-pagination">
					${paginationHtml}
				</div>
			</div>
		</div>`;
}

function createUsersFormHtml(options, adminUserCri) {
	return "";
}

function createUsersTableHtml(pagedUsers) {
	let rowsHtml = pagedUsers.map((adminUser, index) => {
		let providerRow = ``;
		
		switch (adminUser.provider) {
			case "kakao":
				providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/kakao.png">`;
				break;
				
			case "naver":
				providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/naver.png">`;
				break;
			
			case "google":
				providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/google.png">`;
				break;

			case "github":
				providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/github.png">`;
				break;
				
			default:
				providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/codingjoa.png">`;
		}
		
		return `
			<tr>
				<td class="d-md-table-cell">
					<div class="form-check">
						<input class="form-check-input position-static" type="checkbox" name="userIds" value="${adminUser.id}">
					</div>
				</td>		
				<td class="d-md-table-cell">
					<span>${adminUser.id}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminUser.email}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminUser.nickname}</span>
				</td>
				<td class="d-md-table-cell">
					<span class="created-at">${adminUser.createdAt}</span></br>
					${adminUser.isUpdated ? `<span class="updated-at">${adminUser.updatedAt}</span>` : ``}
				</td>
				<td class="d-md-table-cell">
					<span>${adminUser.roles}</span>
				</td>
				<td class="d-md-table-cell">
					${providerRow}
				</td>
				<td>
					<button class="btn btn-sm btn-info" type="button" data-bs-toggle="collapse" data-bs-target="#collapseUser${index}">정보</button>
				</td>
			</tr>
			<tr class="collapse" id="collapseUser${index}">
				<td colspan="8">
					<div class="modify-form-wrap">
						<form>
							<div class="form-group mb-4">
								<label class="font-weight-bold">이메일</label>
								<div class="input-group mb-3">
									<input class="form-control rounded-md" name="email" value="${adminUser.email}" placeholder="이메일을 입력해주세요."/>
								</div>
							</div>
							<div class="form-group mb-4">
								<label class="font-weight-bold">닉네임</label>
								<div class="input-group">
									<input class="form-control rounded-md" name="nickname" value="${adminUser.nickname}" placeholder="닉네임을 입력해주세요."/>
								</div>
							</div>
							<div class="form-group mb-4">
								<label class="font-weight-bold">주소</label>
								<div class="input-group mb-3 w-40">
									<input class="form-control rounded-md" name="zipcode" value="${adminUser.zipcode}" placeholder="우편번호를 입력해주세요." readonly/>
									<button type="button" class="btn btn-sm btn-outline-secondary rounded-md" id="searchAddrBtn">주소 찾기</button>
								</div>
								<div class="input-group mb-3">
									<input class="form-control rounded-md" name="addr" value="${adminUser.addr}" placeholder="주소를 입력해주세요." readonly/>
								</div>
								<div class="input-group">			
									<input class="form-control rounded-md" name="addrDetail" value="${adminUser.addrDetail}" placeholder="상세주소를 입력해주세요."/>
								</div>
							</div>
							<div class="form-check mb-4">		
								<input class="form-check-input" type="checkbox" value="" checked>
								<label class="form-check-label">마케팅 활용 및 광고 수신 동의</label>
							</div>
							<div class="text-end">
								<button type="submit" class="btn btn-primary mr-2 rounded-md">수정</button>
								<button type="reset" class="btn btn-secondary rounded-md">취소</button>
							</div>
						</form>
					</div>
				</td>
			</tr>
		`
		}).join("");
	
//	<div class="form-group mb-4">
//		<div class="form-check small mb-1">
//			<label class="form-check-label">
//				<checkbox class="form-check-input" name="agree"/>
//				<span>마케팅 활용 및 광고 수신 동의</span>
//			</label>
//		</div>
//	</div>
	
	return `
		<table class="table">
			<thead>
				<tr>
					<th class="d-md-table-cell">
						<div class="form-check">
					  		<input class="form-check-input position-static" type="checkbox" id="toggleAllUsers">
						</div>
					</th>
					<th class="d-md-table-cell">번호</th>
					<th class="d-md-table-cell">이메일</th>
					<th class="d-md-table-cell">닉네임</th>
					<th class="d-md-table-cell">가입일 (수정일)</th>
					<th class="d-md-table-cell">권한</th>
					<th class="d-md-table-cell">계정 연동</th>
					<th class="d-md-table-cell"></th>
				</tr>
			</thead>
			<tbody>
				${rowsHtml}
			</tbody>
		</table>`;
}

function createBoardsPageHtml(result) {
	console.log("## createBoardsPageHtml");
	const { options, adminBoardCri, pagedBoards, pagination } = result.data;
	
	let formHtml = createBoardsFormHtml(options, adminBoardCri);
	let tableHtml = createBoardsTableHtml(pagedBoards);
	let paginationHtml = createPaginationHtml(pagination);
	
	return `
		<div class="card rounded-xl">
			<div class="card-body">
				<div class="form-wrap">
					${formHtml}
				</div>
				<div class="table-wrap">
					${tableHtml}
				</div>
				<div class="mb-3">
					<button type="button" id="deleteBoardsBtn" class="btn btn-primary rounded-md mr-auto" disabled>선택삭제</button>
				</div>
				<div class="board-pagination">
					${paginationHtml}
				</div>
			</div>
		</div>`;
}

function createBoardsFormHtml(options, adminBoardCri) {
	let typeOptionHtml = Object.entries(options.typeOption)
		.map(([key, value]) => {
			let selected = (adminBoardCri.type == key) ? "selected" : "";
			return `<option value="${key}" ${selected}>${value}</option>`;
		}).join("");
	
	let recordCntOptionHtml = Object.entries(options.recordCntOption)
		.map(([key, value]) => {
			let selected = (adminBoardCri.recordCnt == key) ? "selected" : "";
			return `<option value="${key}" ${selected}>${value}</option>`;
		}).join("");
	
	let sortOptionHtml = Object.entries(options.sortOption)
		.map(([key, value]) => {
			let selected = (adminBoardCri.sort == key) ? "selected" : "";
			return `<option value="${key}" ${selected}>${value}</option>`;
		}).join("");
	
	let categoryOptionHtml = Object.entries(options.categoryOption)
		.map(([key, value]) => {
			let checked = adminBoardCri.categories.some(categoryId => categoryId == key) ? "checked" : "";
			return `
				<li class="form-check">
					<label class="form-check-label">
						<input class="form-check-input position-static" type="checkbox" name="categories" value="${key}" ${checked}>${value}
					</label>
				</li>`;
		}).join("");
	
	let categoryBadgesHtml = adminBoardCri.categories
		.map(categoryId => {
			let categoryName = options.categoryOption[categoryId];
			return `
				<span class="badge category-badge">
					${categoryName}
					<button class="category-badge-btn" type="button" name="categoryBadgeBtn" data-category-id="${categoryId}"></button>
				</span>`;
		}).join("");
	
	return `
		<form id="adminBoardsForm">
			<div class="d-flex mb-3">
				<select id="type" name="type" class="custom-select mr-3 rounded-md">
					${typeOptionHtml}
				</select>
				<div class="input-group">
					<input id="keyword" name="keyword" class="form-control rounded-md" value="${adminBoardCri.keyword}" placeholder="검색어를 입력해주세요."/>
					<div class="input-group-append">
						<button type="submit" class="btn btn-outline-secondary rounded-md">검색</button>
					</div>
				</div>
			</div>
			<div class="d-flex justify-content-between">
				<div class="d-flex">
					<div class="dropdown categories">
						<button class="custom-select rounded-md dropdown-toggle" type="button" data-bs-toggle="dropdown" data-bs-auto-close="outside">게시판</button>
						<ul class="dropdown-menu rounded-md">
							${categoryOptionHtml}
						</ul>
					</div>
					<div class="selected-categories">
						${categoryBadgesHtml}
					</div>
				</div>
				<div class="d-flex">
					<select id="sort" name="sort" class="custom-select rounded-md mr-3">
						${sortOptionHtml}
					</select>
					<select id="recordCnt" name="recordCnt" class="custom-select rounded-md">
						${recordCntOptionHtml}
					</select>
				</div>
			</div>
		</form>`;
}

function createBoardsTableHtml(pagedBoards) {
	let rowsHtml = "";
	if (!pagedBoards || pagedBoards.length == 0) {
		rowsHtml = `
			<tr>
				<td colspan="9">
					<div class="no-board">등록된 게시글이 없습니다.</div>
				</td>	
			</tr>`;
	} else {
		rowsHtml = pagedBoards.map(adminBoard => `
			<tr>
				<td class="d-md-table-cell">
					<div class="form-check">
						<input class="form-check-input position-static" type="checkbox" name="boardIds" value="${adminBoard.id}">
					</div>
				</td>		
				<td class="d-md-table-cell">
					<span>${adminBoard.id}</span>
				</td>
				<td class="d-md-table-cell text-left">
					<a href="${contextPath}/board/read?id=${adminBoard.id}">${adminBoard.title}</a>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.writerNickname}</span></br>
					<span class="email">${adminBoard.writerEmail}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.categoryName}</span>
				</td>
				<td class="d-md-table-cell">
					<span class="created-at">${adminBoard.createdAt}</span></br>
					${adminBoard.isUpdated ? `<span class="updated-at">${adminBoard.updatedAt}</span>` : ``}
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.viewCount}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.commentCount}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.likeCount}</span>
				</td>
			</tr>`).join("");
	}
	
	return `
		<table class="table">
			<thead>
				<tr>
					<th class="d-md-table-cell">
						<div class="form-check">
					  		<input class="form-check-input position-static" type="checkbox" id="toggleAllBoards">
						</div>
					</th>
					<th class="d-md-table-cell">번호</th>
					<th class="d-md-table-cell">제목</th>
					<th class="d-md-table-cell">작성자</th>
					<th class="d-md-table-cell">게시판</th>
					<th class="d-md-table-cell">작성일 (수정일)</th>
					<th class="d-md-table-cell">조회</th>
					<th class="d-md-table-cell">댓글</th>
					<th class="d-md-table-cell">좋아요</th>
				</tr>
			</thead>
			<tbody>
				${rowsHtml}
			</tbody>
		</table>`;
}

function createCategoryBadgeHtml(categoryId) {
	let savedOptions = JSON.parse(localStorage.getItem("adminBoardOptions"));
	let categoryName = savedOptions.categoryOption[categoryId];
	
	return `
		<span class="badge category-badge">
			${categoryName}
			<button class="category-badge-btn" type="button" name="categoryBadgeBtn" data-category-id="${categoryId}"></button>
		</span>`;
}


