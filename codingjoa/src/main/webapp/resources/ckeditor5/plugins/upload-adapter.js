class UploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }
    
    upload() {
    	console.log("## UploadAdapter.upload");
        return this.loader.file
        	.then(file => new Promise((resolve, reject) => {
            	this._initRequest();
            	this._initListeners(resolve, reject, file);
            	this._sendRequest(file);
        	}));
    }
    
    _getContextPath() {
    	const hostIndex = location.href.indexOf(location.host) + location.host.length;
    	return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
    }

    _initRequest() {
    	console.log("\t > _initRequest");
     	// Note that your request may look different. It is up to you and your editor integration to choose the right communication channel. 
    	// This example uses a POST request with JSON as a data structure but your configuration could be different.
    	const contextPath = this._getContextPath();
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open('POST', `${contextPath}/api/board/image`, true);
        xhr.responseType = 'json';
    }

    _initListeners(resolve, reject, file) {
    	console.log("\t > _initListeners");
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = "파일을 업로드 할 수 없습니다: " + file.name + ".";
        
        xhr.addEventListener('error', () => reject(genericErrorText));
        xhr.addEventListener('abort', () => reject());
        xhr.addEventListener('load', () => {
        	const response = xhr.response;
        	const readyStatus = xhr.readyState;
        	const status = xhr.status;
        	
        	if (status >= 400) {
        		console.log("%c> ERROR", "color:red");
            	console.log(response);
                return reject(response ? response.message : genericErrorText);
        	} 
        	
            console.log("%c> SUCCESS", "color:green");
    		console.log(JSON.stringify(response, null, 2)); 
         	
         	// If the upload is successful, resolve the upload promise with an object 
         	// containing at least the "default" URL, pointing to the image on the server.
            // This URL will be used to display the image in the content. 
            // Learn more in the UploadAdapter#upload documentation.
            resolve({
            	// https://ckeditor.com/docs/ckeditor5/latest/framework/guides/deep-dive/upload-adapter.html
            	// response.data: BoardImageDto(long id, String path)
            	id: response.data.id,
            	src: this._getContextPath() + response.data.path //url: response.data.path
            	//alt: response.data.name
            });
        });
    }

    _sendRequest(file) {
    	console.log("\t > _sendRequest");
        const data = new FormData();
        data.append("file", file);
        
     	// Important note: 
     	// This is the right place to implement security mechanisms like authentication and CSRF protection. 
     	// For instance, you can use XMLHttpRequest.setRequestHeader() to set the request headers 
     	// containing the CSRF token generated earlier by your application.
        const token = localStorage.getItem("ACCESS_TOKEN");
		if (token) {
			this.xhr.setRequestHeader("Authorization", `Bearer ${token}`);
		}

        // Send the request.
        this.xhr.send(data);
    }
}