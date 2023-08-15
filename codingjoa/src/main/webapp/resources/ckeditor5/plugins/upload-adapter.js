const hostIndex = location.href.indexOf(location.host) + location.host.length;
const contextPath = location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));

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
     	// Note that your request may look different. It is up to you and your editor
        // integration to choose the right communication channel. This example uses
        // a POST request with JSON as a data structure but your configuration could be different.
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open('POST', contextPath + "/api/upload/board-image", true);
        xhr.responseType = 'json';
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
        	
        	if (status == 200) {
        		console.log("%c> SUCCESS", "color:green");
        		console.log(JSON.stringify(response, null, 2)); 
        	}
        	
         	// This example assumes the XHR server's "response" object will come with an "error" 
         	// which has its own "message" that can be passed to reject() in the upload promise.
            // Your integration may handle upload errors in a different way so make sure it is done properly.
            // The reject() function must be called when the upload fails.
            if (!response || response.error) {
            	console.log("%c> ERROR", "color:red");
            	console.log(response);
                return reject(response && response.error ? response.error.message : genericErrorText);
            }
         	
         	// If the upload is successful, resolve the upload promise with an object 
         	// containing at least the "default" URL, pointing to the image on the server.
            // This URL will be used to display the image in the content. 
            // Learn more in the UploadAdapter#upload documentation.
            resolve({
            	// https://ckeditor.com/docs/ckeditor5/latest/framework/guides/deep-dive/upload-adapter.html
//            	urls: {
//            		default: getContextPath() + response.data.returnUrl
//            	},
            	
            	// response.data = BoardImageDto(int boardImageIdx, String boardImageUrl)
            	idx: response.data.boardImageIdx,
            	url: contextPath + response.data.boardImageUrl
            	//alt: response.data.boardImageName
            });
        });
    }

    _sendRequest(file) {
        const data = new FormData();
        data.append("file", file);
        
     	// Important note: 
     	// This is the right place to implement security mechanisms like authentication and CSRF protection. 
     	// For instance, you can use XMLHttpRequest.setRequestHeader() to set the request headers 
     	// containing the CSRF token generated earlier by your application.

        // Send the request.
        console.log("## Send upload request");
        this.xhr.send(data);
    }
}