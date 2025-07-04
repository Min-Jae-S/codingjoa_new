const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

// top-menu.jsp
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

// main.jsp
function createConfigHtml(data) {
	let html = "";
	
	if (data instanceof Array) { // Array.isArray()
		$.each(data, function(key, item) {
			if (typeof item == "string") {
				html += `<p class='card-text'><i class='fa-solid fa-asterisk mr-2'></i>${item}</p>`;
			} else {
				$.each(item, function(key, value) {
					html += `<p class='card-text'><i class='fa-solid fa-asterisk mr-2'></i>${key}</p>`;
					$.each(value, function(key, value) {
						html += `<p class='card-text'><i class='fa-solid fa-caret-right ml-4 mr-2'></i>${value}</p>`;
					});
				});
			}
		});	
	} else {
		$.each(data, function(key, value) {
			html += `
				<p class='card-text'><i class='fa-solid fa-asterisk mr-2'></i>${key}</p>
				<p class='card-text'><i class='fa-solid fa-caret-right ml-4 mr-2'></i>${value}</p>`;
		});
	} 
	
	return html;
}

// account.jsp (password change form)
function createPasswordChangeForm() {
	console.log("## createPasswordChangeForm");
	
	return `
		<!-- password change form -->
		<h5 class="mb-4 font-weight-bold">계정 보안</h5>
		<div>
			<dl class="form-group">
				<dt><i class="fa-solid fa-check mr-2"></i>비밀번호</dt>
				<div class="show-wrap">
					<dd class="input-group" id="showPassword">
						<div>
							<span class="inner-text">********</span>
						</div>
						<button class="btn btn-outline-primary btn-sm">수정</button>
					</dd>
				</div> <!-- /.show-wrap -->
				<div class="form-wrap d-none">
					<form id="passwordChangeForm">
						<dd class="input-group">
							<input type="password" id="currentPassword" name="currentPassword" placeholder="현재 비밀번호를 입력해주세요." autocomplete="off"/>
							<div>
								<button class="btn btn-outline-primary btn-sm" type="submit">확인</button>
								<button class="btn btn-outline-secondary btn-sm" type="reset">취소</button>
							</div>
						</dd>
						<dd class="input-group">
							<input type="password" id="newPassword" name="newPassword" placeholder="새로운 비밀번호를 입력해주세요." autocomplete="off"/>
						</dd>
						<dd class="input-group">
							<input type="password" id="confirmPassword" name="confirmPassword" placeholder="확인 비밀번호를 입력해주세요." autocomplete="off"/>
						</dd>
					</form>
				</div> <!-- /.form-wrap -->
			</dl>
		</div>`;
}

// create paged comments
function createPagedCommentsHtml(pagedComments) {
	console.log("## createPagedCommentsHtml");
	
	if (!pagedComments || pagedComments.length == 0) {
		return "";
	}
	
	let commentItemsHtml = pagedComments.map(commentDetails => {
		if (!commentDetails) {
			return `
				<li class='list-group-item deleted-comment'>
					<div class='comment-area'>
						<div class='comment-area-header'>
							<div class='comment-info'>
								<span class='comment-writer'>삭제된 댓글</span>
							</div>
						</div>
						<div class='comment-area-body'>
							<div class='comment-content' style='line-height:180%;'>
								<p>삭제된 댓글입니다.</p>
							</div>
						</div>
					</div>
				</li>`;
		} else {
			return `
				<li class='list-group-item' data-id='${commentDetails.id}'>
					${createCommentHtml(commentDetails)}
				</li>`;
		}
	}).join("");
	
	return `<ul class='list-group list-group-flush'>${commentItemsHtml}</ul>`;
}

function createCommentHtml(commentDetails) {
	return `
		<div class='comment-thum'>
			<img src='${commentDetails.writerImagePath ? `${contextPath}${commentDetails.writerImagePath}` : `${contextPath}/resources/images/img_profile.png`}'>
		</div>
		<div class='comment-area'>
			<div class='comment-area-header'>
				<div class='comment-info'>
					<span class='comment-writer'>${commentDetails.writerNickname}</span>
					${commentDetails.isBoardWriter ? "<span class='badge badge-pill badge-primary'>글쓴이</span>" : ""}
					<span class='comment-createdat'>${commentDetails.createdAt}</span>
					<span class='comment-updatedat d-none'>${commentDetails.updatedAt}</span>
					<!-- test (commentId) -->
					<!-- <span class='text-danger font-weight-bold'>( id: ${commentDetails.id} )</span> -->
				</div>
				<div class='dropend ml-auto'>
					<button class='comment-utils-btn' data-bs-toggle='dropdown' data-bs-auto-close='outside' ${commentDetails.isWriter ? "" : "disabled"}>
						<i class='fa-ellipsis-vertical fa-solid'></i>
					</button>
					<ul class='dropdown-menu'>
						<h6 class='dropdown-header'>댓글 관리</h6>
						<hr class='dropdown-divider'>
						<li>
							<button class='dropdown-item' type='button' name='showEditCommentBtn'>수정하기</button>
							<button class='dropdown-item' type='button' name='deleteCommentBtn'>삭제하기</button>
						</li>
					</ul>
				</div>
			</div>
			<div class='comment-area-body'>
				<div class='comment-content'>
					<p>${commentDetails.content.replace(/(?:\r\n|\r|\n)/g, "<br>")}</p>
				</div>
			</div>
			<div class='comment-area-footer'>
				<button type='button' name='commentLikeBtn'>
					<span class='icon'>
						${commentDetails.isLiked ? "<i class='fa-thumbs-up fa-fw fa-regular text-primary'></i>" : "<i class='fa-thumbs-up fa-fw fa-regular'></i>"}
					</span>
					<span class='comment-like-cnt'>${commentDetails.likeCount}</span>
				</button>
			</div>
		</div>`;
}

// create edit comment
function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	console.log(commentDetails);

	if (!commentDetails.isWriter) {
		return "";
	}

	return `
		<div class='comment-thum'>
			<img src='${commentDetails.writerImagePath ? `${contextPath}${commentDetails.writerImagePath}` : `${contextPath}/resources/images/img_profile.png`}'>
		</div>
		<div class='comment-area'>
			<div class='comment-area-header'>
				<div class='comment-info'>
					<span class='comment-writer'>${commentDetails.writerNickname}</span>
					${commentDetails.isBoardWriter ? `<span class='badge badge-pill badge-primary'>글쓴이</span>` : ""}
					<span class='comment-createdat'>${commentDetails.createdAt}</span>
					<span class='comment-updatedat d-none'>${commentDetails.updatedAt}</span>
				</div>
			</div>
			<div class='comment-edit-wrap'>
				<form>
					<div class='input-group'>
						<div class='comment-edit form-control'>
							<textarea name='content' rows='1'>${commentDetails.content}</textarea>
							<div class='mt-2'>
								<button type='submit' class='btn btn-sm btn-outline-primary'>수정</button>
								<button type='button' class='btn btn-sm btn-outline-secondary ml-2'>취소</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>`;
}

// pagination
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

// ========================
//			ADMIN
// ========================

// create error page
function createErrorPageHtml() {
	console.log("## createErrorPageHtml");
	return `
		<div class="error-wrap">
			<p class="error-code">404</p>
			<p class="error-message">요청한 페이지를 찾을 수 없습니다.</p>
		</div>`;
}

// create welcome page
function createWelcomePageHtml() {
	console.log("## createWelcomePageHtml");
	return `<p class='welcome'>Welcome to Admin Dashboard</p>`;
}

// create user page (search, table, pagination)
function createUsersPageHtml(result) {
	console.log("## createUsersPageHtml");
	const { options, adminUserCri, pagedUsers, pagination } = result.data;
	
	let searchFormHtml = createUsersSearchFormHtml(options, adminUserCri);
	let tableHtml = createUsersTableHtml(pagedUsers);
	let paginationHtml = createPaginationHtml(pagination);
	
	return `
		<div class="card rounded-xl">
			<div class="card-body">
				<div class="form-wrap">
					${searchFormHtml}
				</div>
				<div class="table-wrap">
					${tableHtml}
				</div>
				<div class="mb-3">
					<button type="button" id="deleteUsersBtn" class="btn btn-primary rounded-md mr-auto" disabled>선택삭제</button>
				</div>
				<div class="user-pagination">
					${paginationHtml}
				</div>
			</div>
		</div>`;
}

// create user search form
function createUsersSearchFormHtml(options, adminBoardCri) {
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
	
	return `
		<form id="adminUsersSearchForm" class="form-inline">
			<select name="type" class="custom-select mr-3 rounded-md">
				${typeOptionHtml}
			</select>
			<div class="input-group">
				<input name="keyword" class="form-control rounded-md" value="${adminBoardCri.keyword}" placeholder="검색어를 입력해주세요."/>
				<div class="input-group-append">
					<button type="submit" class="btn btn-secondary rounded-md ml-3">검색</button>
				</div>
			</div>
			<select name="recordCnt" class="custom-select rounded-md ml-auto">
				${recordCntOptionHtml}
			</select>
		</form>`;
}

// create user table
function createUsersTableHtml(pagedUsers) {
	let rowsHtml = "";
	if (!pagedUsers || pagedUsers.length == 0) {
		rowsHtml = `
			<tr>
				<td colspan="9">
					<div class="no-results">일치하는 결과가 없습니다.</div>
				</td>	
			</tr>`;
	} else {
		rowsHtml = pagedUsers.map(user => {
			let providerRow = "";
			switch (user.provider) {
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
					//providerRow = `<img class="provider" src="${contextPath}/resources/images/provider/codingjoa.png">`;
					//providerRow = `<span class="provider">codingjoa</span>`;
					providerRow = `<span>-</span>`;
			}
		
			return `
				<tr>
					<td class="d-md-table-cell">
						<div class="form-check">
							<input class="form-check-input position-static" type="checkbox" name="userIds" value="${user.id}">
						</div>
					</td>		
					<td class="d-md-table-cell">
						<span>${user.id}</span>
					</td>
					<td class="d-md-table-cell">
						<span class="email">${user.email}</span>
					</td>
					<td class="d-md-table-cell">
						<span class="nickname">${user.nickname}</span>
					</td>
					<td class="d-md-table-cell">
						<span class="created-at">${user.createdAt}</span></br>
						${user.isUpdated ? `<span class="updated-at">${user.updatedAt}</span>` : ``}
					</td>
					<td class="d-md-table-cell text-start">
						<span class="text-primary mr-2 bi ${user.roles.includes("ROLE_USER") ? 'bi-check-square' : 'bi-square'}"></span>일반 사용자</br>
						<span class="text-primary mr-2 role bi ${user.roles.includes("ROLE_ADMIN") ? 'bi-check-square' : 'bi-square'}"></span>관리자
					</td>
					<td class="d-md-table-cell">
						${providerRow}
					</td>
					<td class="d-md-table-cell">
						<span class="linked-at">${user.linkedAt || '-'}</span>
					</td>
					<td>
						<button type="button" class="btn-unstyled" name="openUserEditModal" data-user-id="${user.id}">
							<i class="fas fa-gear fa-fw text-secondary"></i>
						</button>
					</td>
				</tr>`
			}).join("");
	}
	
	return `
		<table class="table users-table">
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
					<th class="d-md-table-cell">연동일</th>
					<th class="d-md-table-cell">관리</th>
				</tr>
			</thead>
			<tbody>
				${rowsHtml}
			</tbody>
		</table>`;
}

// create user edit modal
function createUserEditModalHtml(user) {
	let content = `
		<ul class="nav nav-tabs form-menu">
			<li class="nav-item">
				<button class="nav-link ml-4 active" data-target="userAccountFormWrap">회원정보 관리</button>
			</li>
			<li class="nav-item">
				<button class="nav-link" data-target="userPasswordFormWrap">비밀번호 관리</button>
			</li>
			<li class="nav-item">
				<button class="nav-link" data-target="userAuthFormWrap">권한 관리</button>
			</li>
			<button type="button" class="btn-close ml-auto mr-4" data-bs-dismiss="modal"></button>
		</ul>
		<div class="modify-forms-wrap">
			<div class="active" name="userAccountFormWrap">
				<form name="userEmailForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>이메일</label>
						<div class="input-group">
							<input class="form-control rounded-md" type="text" name="email" value="${user.email}" placeholder="이메일을 입력해주세요."/>
							<button type="submit" class="btn btn-primary rounded-md ml-3">수정</button>
						</div>
					</div>
				</form>
				<form name="userNicknameForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>닉네임</label>
						<div class="input-group">
							<input class="form-control rounded-md" type="text" name="nickname" value="${user.nickname}" placeholder="닉네임을 입력해주세요."/>
							<button type="submit" class="btn btn-primary rounded-md ml-3">수정</button>
						</div>
					</div>
				</form>
				<form name="userAddrForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>주소</label>
						<div class="input-group mb-3">
							<div>
								<input class="form-control rounded-md" type="text" name="zipcode" value="${user.zipcode}" placeholder="우편번호를 입력해주세요." readonly/>
							</div>
							<button type="button" class="btn btn-sm btn-secondary rounded-md ml-3" name="searchAddrBtn">주소 찾기</button>
							<button type="submit" class="btn btn-primary rounded-md ms-auto">수정</button>
						</div>
						<div class="input-group mb-3">
							<input class="form-control rounded-md" type="text" name="addr" value="${user.addr}" placeholder="주소를 입력해주세요." readonly/>
						</div>
						<div class="input-group">			
							<input class="form-control rounded-md" type="text" name="addrDetail" value="${user.addrDetail}" placeholder="상세주소를 입력해주세요."/>
						</div>
					</div>
				</form>
				<form name="userAgreeForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>광고성 정보 수신동의</label>
						<div class="form-check">
							<label class="form-check-label">
								<input class="form-check-input" type="checkbox" name="agree" ${user.agree ? "checked" : ""}/>
								<span>이메일</span>
							</label>
							<button type="submit" class="btn btn-primary rounded-md float-end">수정</button>
						</div>
					</div>
				</form>
			</div> <!-- /userAccountFormWrap -->
			<div name="userPasswordFormWrap">
				<form name="userPasswordForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>새로운 비밀번호</label>
						<div class="input-group">
							<input class="form-control rounded-md" type="password" name="newPassword" placeholder="비밀번호를 입력해주세요." autocomplete="off"/>
						</div>
					</div>
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>비밀번호 확인</label>
						<div class="input-group">
							<input class="form-control rounded-md" type="password" name="confirmPassword" placeholder="비밀번호 확인을 입력해주세요." autocomplete="off"/>
						</div>
					</div>
					<button type="submit" class="btn btn-primary rounded-md float-end">수정</button>
				</form>
			</div> <!-- /userPasswordFormWrap -->
			<div name="userAuthFormWrap">
				<form name="userAuthForm">
					<div class="form-group">
						<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>권한</label>
						<div class="form-check">
							<label class="form-check-label">
								<input class="form-check-input" type="checkbox" ${user.roles.includes("ROLE_USER") ? "checked" : ""} disabled>
								<span>일반 사용자</span>
							</label>
						</div>
						<div class="form-check">
							<label class="form-check-label">
								<input class="form-check-input" type="checkbox" name="roles" value="ROLE_ADMIN" ${user.roles.includes("ROLE_ADMIN") ? "checked" : ""}>
								<span>관리자</span>
							</label>
						</div>
					</div>
					<button type="submit" class="btn btn-primary rounded-md float-end">수정</button>
				</form>
			</div> <!-- /userAuthFormWrap -->
		</div> <!-- /modify-forms-wrap -->`;
		
	return `
		<div class="modal fade bd-example-modal-lg" id="userEditModal" data-user-id="${user.id}">
			<div class="modal-dialog modal-dialog-centered modal-lg">
				<div class="modal-content">
					<div class="modal-body">
						${content}
					</div>
				</div>
			</div>
		</div>`;
}

// create board page (search, table, pagination)
function createBoardsPageHtml(result) {
	console.log("## createBoardsPageHtml");
	const { options, adminBoardCri, pagedBoards, pagination } = result.data;
	
	let searchFormHtml = createBoardsSearchFormHtml(options, adminBoardCri);
	let tableHtml = createBoardsTableHtml(pagedBoards);
	let paginationHtml = createPaginationHtml(pagination);
	
	return `
		<div class="card rounded-xl">
			<div class="card-body">
				<div class="form-wrap">
					${searchFormHtml}
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

// create boards search form
function createBoardsSearchFormHtml(options, adminBoardCri) {
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
		<form id="adminBoardsSearchForm">
			<div class="d-flex mb-3">
				<select name="type" class="custom-select mr-3 rounded-md">
					${typeOptionHtml}
				</select>
				<div class="input-group">
					<input name="keyword" class="form-control rounded-md" value="${adminBoardCri.keyword}" placeholder="검색어를 입력해주세요."/>
					<div class="input-group-append">
						<button type="submit" class="btn btn-secondary rounded-md ml-3">검색</button>
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
					<select name="sort" class="custom-select rounded-md mr-3">
						${sortOptionHtml}
					</select>
					<select name="recordCnt" class="custom-select rounded-md">
						${recordCntOptionHtml}
					</select>
				</div>
			</div>
		</form>`;
}

// create board table
function createBoardsTableHtml(pagedBoards) {
	let rowsHtml = "";
	if (!pagedBoards || pagedBoards.length == 0) {
		rowsHtml = `
			<tr>
				<td colspan="9">
					<div class="no-results">일치하는 결과가 없습니다.</div>
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
		<table class="table boards-table">
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

function createUserRegistrationPageHtml() {
	console.log("## createUserRegistrationPageHtml");
	return `
			<div class="card rounded-xl">
				<div class="card-body">
					<div class="registration-form-wrap">
						<form id="userRegistrationForm">
							<div class="form-group">
								<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>이메일</label>
								<div class="input-group">
									<input class="form-control rounded-md" type="text" name="email" placeholder="이메일을 입력해주세요."/>
								</div>
							</div>
							<div class="form-group">
								<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>닉네임</label>
								<div class="input-group">
									<input class="form-control rounded-md" type="text" name="nickname" placeholder="닉네임을 입력해주세요."/>
								</div>
							</div>
							<div class="form-group">
								<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>비밀번호</label>
								<div class="input-group">
									<input class="form-control rounded-md" type="password" name="password" placeholder="비밀번호를 입력해주세요." autocomplete="off"/>
								</div>
							</div>
							<div class="form-group">
								<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>비밀번호 확인</label>
								<div class="input-group">
									<input class="form-control rounded-md" type="password" name="confirmPassword" placeholder="비밀번호 확인을 입력해주세요." autocomplete="off"/>
								</div>
							</div>
							<div class="form-group mb-0">
								<label class="font-weight-bold"><i class="fa-solid fa-check mr-2"></i>권한</label>
								<div class="form-check">
									<label class="form-check-label">
										<input class="form-check-input" type="checkbox" checked disabled/>
										<span>일반 사용자</span>
									</label>
								</div>
								<div class="form-check">
									<label class="form-check-label">
										<input class="form-check-input" type="checkbox" name="roles" value="ROLE_ADMIN"/>
										<span>관리자</span>
									</label>
								</div>
							</div>
							<div class="text-end">
								<button type="submit" class="btn btn-primary mr-2 rounded-md">등록</button>
								<button type="reset" class="btn btn-secondary rounded-md">취소</button>
							</div>	
						</form>
					</div>
				</div>
			</div>`;
}


