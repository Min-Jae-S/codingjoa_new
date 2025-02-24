class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}
	
	route(path, params, pushState = true) {
		console.log("## routing to URL:", path, ", pushState: ", pushState);
		
		let url = new URL(path, window.location.origin);
		Object.entries(params).forEach(([key, value]) => {
			url.searchParams.set(key, value);
		});
		
		if (pushState && !this._isSameUrl(url)) {
			history.pushState(params, "", url.toString());
		}
		
		const handler = this.routers.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log("## no handler found for path:", path);
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
	
}