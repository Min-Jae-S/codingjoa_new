console.log("## Editor service ready - editor.js");

function createWriteEditor(targetId) {
	console.log("## WriteEditor initialize");
	ClassicEditor
		.create(document.querySelector(targetId), {
			extraPlugins: [
				uploadAdapter, 
				uploadCompleteListener, 
				attributeExtender,
				viewToModelConverter, 
				modelToViewEditingConverter, 
				modelToViewDataConverter
			],
			ui: {
				viewportOffset: {
					top: document.querySelector(".navbar-custom").clientHeight
				}
			},
			// https://ckeditor.com/docs/ckeditor5/latest/features/general-html-support.html
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
			},
			placeholder: "내용을 입력하세요."
		})
		.then(editor => {
			return editor;
		})
		.catch(error => {
			console.error(error);
		});
}

function createReadEditor(targetId) {
	console.log("## Editor initialize (read-only mode)");
	ClassicEditor
		.create(document.querySelector(targetId), {
			toolbar: []
		})
		.then(editor => {
			const toolbarContainer = editor.ui.view.stickyPanel;
			editor.ui.view.top.remove(toolbarContainer);
			editor.enableReadOnlyMode("editor");
			return editor;
		})
		.catch(error => {
			console.error(error);
		});
}

function createModifyEditor(targetId) {
	console.log("## ModifyEditor initialize");
	ClassicEditor
		.create(document.querySelector(targetId), {
			extraPlugins: [
				uploadAdapter, 
				uploadCompleteListener, 
				attributeExtender,
				viewToModelConverter, 
				modelToViewEditingConverter, 
				modelToViewDataConverter
			],
			ui: {
				viewportOffset: {
					top: document.querySelector(".navbar-custom").clientHeight;
				}
			},
			htmlSupport: { 
				allow: [{
					attributes: [{
						key: "data-idx", value: true 
					}]
				}]
			},
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
			editor.model.document.on('change:data', () => {
				let boardContent = editor.getData();
				$("#boardContent").val(boardContent);
			});
			return editor;
		})
		.catch(error => {
			console.error(error);
		});
}