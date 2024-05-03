console.log("## editor plugins ready - ckeditor-plugins.js");

function uploadAdapter(editor) {
	console.log("\t > register upload adapter");
    editor.plugins.get("FileRepository").createUploadAdapter = (loader) => {
        return new UploadAdapter(loader);
    };
}

// https://ckeditor.com/docs/ckeditor5/latest/api/module_image_imageupload_imageuploadediting-ImageUploadEditing.html#event-uploadComplete
function uploadCompleteListener(editor) {
	console.log("\t > register upload complete listener");
	editor.plugins.get("ImageUploadEditing").on("uploadComplete", (evt, {data, imageElement}) => {
		console.log("## upload complete and set attributes src, data-idx");
		editor.model.change(writer => {
			evt.stop();
			writer.setAttribute("src", data.url, imageElement);
			writer.setAttribute("dataIdx", data.idx, imageElement);
			//writer.setAttribute("alt", data.alt, imageElement);
		});
	});
}

// https://github.com/ckeditor/ckeditor5/issues/5204
function attributeExtender(editor) {
	console.log("\t > extend custom attribute(dataIdx) to blockObject and inlineOjbect");
	editor.model.schema.extend("$blockObject", { allowAttributes: "dataIdx" });
	editor.model.schema.extend("$inlineObject", { allowAttributes: "dataIdx" });
}

// view-to-model converter(upcast)
function viewToModelConverter(editor) {
	console.log("\t > register VIEW-TO-MODEL converter (upcast)");
	editor.conversion.for("upcast").attributeToAttribute({
        view: "data-idx",
        model: "dataIdx"
    });
}

// model-to-view converter(editing downcast)
// https://stackoverflow.com/questions/56402202/ckeditor5-create-element-image-with-attributes
// https://gitlab-new.bap.jp/chinhnc2/ckeditor5/-/blob/690049ec7b8e95ba840ab1c882b5680f3a3d1dc4/packages/ckeditor5-engine/docs/framework/guides/deep-dive/conversion-preserving-custom-content.md
function modelToViewEditingConverter(editor) {
	console.log("\t > register MODEL-TO-VIEW converter (editingDowncast)");
	editor.conversion.for("editingDowncast").add(dispatcher => { // downcastDispatcher
        dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => {
        	//console.log("## MODEL-TO-VIEW conversion - editing downcast");
        	console.log("## editing downcast");
        	const modelElement = data.item;
        	if (!conversionApi.consumable.consume(modelElement, evt.name)) {
            	return;
        	}
        	
            const viewWriter = conversionApi.writer;
            const imageContainer = conversionApi.mapper.toViewElement(modelElement);
            const imageElement = imageContainer.getChild(0);
//            console.log("\t > modelElement		: " + modelElement.name);
//            console.log("\t > imageContainer		: " + imageContainer.name);
//            console.log("\t > imageElement		: " + imageElement.name);
            
            if (data.attributeNewValue !== null) {
            	viewWriter.setAttribute("data-idx", data.attributeNewValue, imageElement);
            } else {
            	viewWriter.removeAttribute("data-idx", imageElement);
            }
        });
    });
}

// model-to-view converter(data downcast)
function modelToViewDataConverter(editor) {
	console.log("\t > register MODEL-TO-VIEW converter (dataDowncast)");
	editor.conversion.for("dataDowncast").add(dispatcher => {
		dispatcher.on("attribute:dataIdx", (evt, data, conversionApi) => { 
			//console.log("## MODEL-TO-VIEW conversion - data downcast");
			console.log("## data downcast");
			const modelElement = data.item;
        	if (!conversionApi.consumable.consume(modelElement, evt.name)) {
            	return;
        	}
        	
        	const viewWriter = conversionApi.writer;
            const imageContainer = conversionApi.mapper.toViewElement(modelElement);
            const imageElement = (modelElement.name === "imageBlock") ? imageContainer.getChild(0) : imageContainer;
//            console.log("\t > modelElement		: " + modelElement.name);
//            console.log("\t > imageContainer		: " + imageContainer.name);
//            console.log("\t > imageElement		: " + imageElement.name);
            
            if (data.attributeNewValue !== null) {
                viewWriter.setAttribute("data-idx", data.attributeNewValue, imageElement);
            } else {
            	viewWriter.removeAttribute("data-idx", imageElement);
            }
		});
	});	
}