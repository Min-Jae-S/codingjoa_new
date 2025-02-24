class PageRouter {
	constructor() {
		this.routers = new Map();
		this._initPopState();
	}
	
	addRouter(path, handler) {
		this.routers.set(path, handler);
	}
	
	navigate(path, params = {}, replaceState = false) {
		console.log("## navigate");
		console.log("\t > path: ", path);
		console.log("\t > params: ", params);
		console.log("\t > replaceState: ", replaceState);
		
		let url = new URL(path, window.location.origin);
		Object.entries(params).forEach(([key, value]) => {
			url.searchParams.set(key, value);
		});
		
		if (replaceState) {
			history.replaceState(params, null, url.toString());
		} else {
			history.pushState(params, null, url.toString());
		}
		
		const handler = this.routers.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log("## no handler found for path: ", path);
		}
	}
	
	_initPopState() {
		window.addEventListener("popstate", (e) => {
			console.log("## popstate triggered");
			
			const state = e.state || {};
			const path = window.location.pathname;
			console.log("\t > state: ", state);
			console.log("\t > path: ", path);
			
			this.navigate(path, state, true);
		});
	}
	
}