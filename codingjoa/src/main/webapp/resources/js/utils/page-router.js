class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
		this.errorHandler = null;
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}

	route(routingPath, pushStatePath = null, params = {}, pushState = true) {
		console.log("## route");
		console.log("\t > routingPath: %s, pushStatePath: %s", routingPath, pushStatePath);
		console.log("\t > params:", params);
		
		let url = pushStatePath ? new URL(pushStatePath, window.location.origin) : new URL(routingPath, window.location.origin);
		url.search = new URLSearchParams(params).toString();
		console.log("\t > url.search:", decodeURIComponent(url.search));
		
		if (pushState && !this._isSameUrl(url)) {
			let decodedUrl = decodeURIComponent(url.toString());
			console.log("\t > push state:", decodedUrl);
			history.pushState(params, "", decodedUrl);
		}
		
		const handler = this.routers.get(routingPath); 
		if (handler) {
			handler(params);
		} else if (this.errorHandler) {
			console.log("\t > no handler found, using errorHandler");
			this.errorHandler();
		} else {
			console.log("\t > no handler found, no errorHandler set");
			alert("오류가 발생했습니다.");
		}
	}
	
	_initPopState() {
		window.addEventListener("popstate", (e) => {
			console.log("## popstate triggered");
			
			const params = e.state || {};
			const path = window.location.pathname;
			console.log("\t > params:", params);
			console.log("\t > path:", path);
			
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
	
	getRouters() {
		return Array.from(this.routers.keys());
	}
	
}