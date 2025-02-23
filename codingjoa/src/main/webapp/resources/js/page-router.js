class PageRouter {
	constructor() {
		this.routers = new Map();
	}
	
	addRoute(path, handler) {
		this.routers.set(path, handler);
	}
	
	navigate(path, params = {}) {
		console.log("## pageRouter.navigate");
		console.log("\t > path: ", path);
		console.log("\t > params: ", params);
		
		const handler = this.routers.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log("## no handler found for path: ", path);
		}
	}
	
}