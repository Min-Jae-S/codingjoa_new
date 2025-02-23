class PageRouter {
	constructor() {
		this.routes = new Map();
	}
	
	addRoute(path, handler) {
		this.routes.set(path, handler);
	}
	
	navigate(path, params = {}) {
		console.log("## pageRouter.navigate");
		console.log("\t > path: ", path);
		console.log("\t > params: ", params);
		
		const handler = this.routes.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log(`## no handler found for route: ${path}`);
		}
	}
	
}