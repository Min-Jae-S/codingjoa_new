import UploadAdapter from '/ckeditor5/adapter/upload-adapter.js'

export default function makeEditor(target) {
	return ClassicEditor
				.create(document.querySelector(target), {
					//removePlugins: ["MediaEmbedToolbar"],
					extraPlugins: [UploadAdapterPlugin],
					fontFamily: {
						options: ["defalut", "Arial", "궁서체", "바탕", "돋움"],
						supportAllValues: true
					},
					fontSize: {
						options: [ 10, 12, "default", 16, 18, 20, 22 ],
						supportAllValues: true
					},
					placeholder: "내용을 입력하세요."
					//language: "ko"
				})
				/*
				.then(editor => {
					console.log(editor);
					console.log(Array.from(editor.ui.componentFactory.names()));
					CKEDITOR = editor;
				})
				.catch(error => {
					console.error(error); 
				});
				*/
}

function UploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
        return new UploadAdapter(loader)
    }
}