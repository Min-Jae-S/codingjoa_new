class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
		this.errorHandler = null;
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}
	
	route(path, params = {}, pushState = true) {
		console.log(`## route, URL: ${path}, pushState: ${pushState}`);
		
		let url = new URL(path, window.location.origin);
		Object.entries(params).forEach(([key, value]) => {
			url.searchParams.set(key, value);
		});
		
		if (pushState && !this._isSameUrl(url)) {
			history.pushState(params, "", url.toString());
		}
		
		const handler = this.routers.get(path);
		if (handler) {
			console.log(`\t > handler found for path: ${path}`);
			handler(params);
		} else if(this.errorHandler) {
			console.log(`\t > no handler found for path: ${path}, using errorHandler`);
			this.errorHandler();
		} else {
			console.log(`\t > no handler found for path: ${path}, and no errorHandler set`);
			alert("오류가 발생했습니다.");
		}
	}
	
	_initPopState() {
		window.addEventListener("popstate", (e) => {
			console.log("## popstate triggered");
			
			const state = e.state || {};
			const path = window.location.pathname;
			
			this.route(path, state, false);
		});
	}
	
	_isSameUrl(url) {
		return window.location.href == url.toString();
	}
	
	setErrorHandler(handler) {
		this.errorHandler = handler;
	}
	
}