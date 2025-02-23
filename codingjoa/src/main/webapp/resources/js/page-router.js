class PageRouter {
	constructor() {
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
		window.addEventListener("popstate", function(e) {
			console.log("## popstate triggered");
			console.log(e);
			
			const state = e.state;
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
	
}