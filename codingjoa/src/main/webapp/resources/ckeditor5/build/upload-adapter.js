function getContextPath() {
    var hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

class UploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }

    upload() {
        return this.loader.file
        	.then(file => new Promise((resolve, reject) => {
            	this._initRequest();
            	this._initListeners(resolve, reject, file);
            	this._sendRequest(file);
        	}));
    }

    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        
        console.log("## _initRequest");
        console.log("1. xhr.readyState: " + xhr.readyState);
        
     	// Note that your request may look different. It is up to you and your editor
        // integration to choose the right communication channel. This example uses
        // a POST request with JSON as a data structure but your configuration
        // could be different.
        xhr.open('POST', getContextPath() + "/board/uploadTempImage", true);
        xhr.responseType = 'json';

        console.log("2. After open xhr and set responstType");
        console.log("3. xhr.readyState: " + xhr.readyState);
    }

    _initListeners(resolve, reject, file) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = "파일을 업로드 할 수 없습니다: " + file.name + ".";
        
        xhr.addEventListener('error', () => reject(genericErrorText));
        xhr.addEventListener('abort', () => reject());
        xhr.addEventListener('load', () => {
        	const response = xhr.response;
        	const readyStatus = xhr.readyState;
        	const status = xhr.status;
        	
        	console.log("## After load");
        	console.log("1. xhr.readyState: " + readyStatus);
        	console.log("2. xhr.status: " + status);
        	console.log("3. xhr.response: ");
        	console.log(response);
            
            if (status == "422") {
            	return reject(response.errorMap ? response.errorMap.file : genericErrorText);
            }
			
         	// This example assumes the XHR server's "response" object will come with
            // an "error" which has its own "message" that can be passed to reject() in the upload promise.
            // Your integration may handle upload errors in a different way so make sure
            // it is done properly. The reject() function must be called when the upload fails.
            if (!response || response.error) {
                return reject(response && response.error ? response.error.message : genericErrorText);
            }
         	
         	// If the upload is successful, resolve the upload promise with an object containing
            // at least the "default" URL, pointing to the image on the server.
            // This URL will be used to display the image in the content. Learn more in the
            // UploadAdapter#upload documentation.
            resolve({
            	// https://ckeditor.com/docs/ckeditor5/latest/framework/guides/deep-dive/upload-adapter.html
            	urls: {
            		default: getContextPath() + response.data.returnUrl
            	},
            	uploadIdx: response.data.uploadIdx
            });
        });
    }

    _sendRequest(file) {
        const data = new FormData();
        data.append("file", file);
        
     	// Important note: This is the right place to implement security mechanisms
        // like authentication and CSRF protection. For instance, you can use
        // XMLHttpRequest.setRequestHeader() to set the request headers containing
        // the CSRF token generated earlier by your application.

        // Send the request.
        this.xhr.send(data);
    }
}