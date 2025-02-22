class PageRouter {
	constructor(containerSelector) {
		console.log("## PageRouter.constructor");
		this.routes = new Map();
	}
	
	addRoute(path, handler) {
		this.routes.set(path, handler);
	}
	
	navigate(path, params = {}) {
		console.log(`## navigating to route: ${path} with params:`, params);
		const handler = this.routes.get(path);
		if (handler) {
			handler(params);
		} else {
			console.log(`## no handler found for route: ${path}`);
		}
	}
}