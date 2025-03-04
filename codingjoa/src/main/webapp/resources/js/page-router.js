class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
		this.errorHandler = null;
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}

	route(routingPath, pushStatePath, params = {}, pushState = true) {
		console.log("## route");
		
		let url = pushStatePath ? new URL(pushStatePath, window.location.origin) : new URL(routingPath, window.location.origin);
		Object.entries(params).forEach(([key, value]) => {
			url.searchParams.set(key, value);
		});
		
		if (pushState && !this._isSameUrl(url)) {
			let decodedUrl = decodeURIComponent(url.toString());
			console.log("\t > push state:", decodedUrl);
			history.pushState(params, "", decodedUrl);
		} else {
			console.log("\t > no push state");
		}
		
		const handler = this.routers.get(routingPath); 
		if (handler) {
			console.log("\t > handler found");
			handler(params);
		} else if(this.errorHandler) {
			console.log("\t no handler found, using errorHandler");
			this.errorHandler();
		} else {
			console.log("\t no handler found, and no errorHandler set");
			alert("오류가 발생했습니다.");
		}
	}
	
	_initPopState() {
		window.addEventListener("popstate", (e) => {
			console.log("## popstate triggered");
			
			const params = e.state || {};
			const path = window.location.pathname;
			
			this.route(path, null, params, false);
		});
	}
	
	_isSameUrl(targetUrl) {
		const currentUrl = new URL(window.location.href);
		
		// sort query params for comparison
		currentUrl.searchParams.sort();
		targetUrl.searchParams.sort();
		
		return (currentUrl.pathname == targetUrl.pathname) && (currentUrl.search == targetUrl.search);
	}
	
	setErrorHandler(handler) {
		this.errorHandler = handler;
	}
	
}