function createEditor(targetId) {
	return ClassicEditor
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
								{ key: "data-id", value: true }
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
			});
}