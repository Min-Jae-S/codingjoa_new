class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
		this._initRouters();
	}
	
	navigate(path, params = {}) {
		console.log("## pageRouter.navigate");
		console.log("\t > path: ", path);
		console.log("\t > params: ", params);
		
		let url = new URL(path, window.location.origin);
		$.each(params, function(key, value) {
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
			console.log("\t > state: ", state);
			console.log("\t > path: ", path);
			
			const handler = this.routers.get(path);
			if (handler) {
				handler(state);
			} else {
				console.log("## no handler found for path: ", path);
			}
		});
	}
	
	_initRouters() {
		
	}
	
	_addRouter(path, handler) {
		this.routers.set(path, handler);
	}
	
}