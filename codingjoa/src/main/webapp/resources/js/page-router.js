class PageRouter {
	constructor(container) {
		this.routers = new Map();
		this._initPopState();
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}
	
	navigate(path, params = {}) {
		console.log("## pageRouter.navigate");
		console.log("\t > path: ", path);
		console.log("\t > params: ", params);
		
		let url = new URL(path, window.location.origin);
		Object.entries(params).forEach(([key, value]) => {
			url.searchParams.set(key, value);
		});
		
		history.pushState(params, "", url.toString());
		
		const handler = this.routers.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log("## no handler found for path: ", path);
		}
	}
	
	_initPopState() {
		window.addEventListener("popstate", (e) => {
			if (this.routers.size == 0) {
				console.log("## no routers registerd yet, skipping popstate event handler");
				return;
			}
			
			console.log("## popstate triggered");
			const state = e.state || {};
			const path = window.location.pathname;
			
			this.navigate(path, state);
		});
	}
	
}