class PageRouter {
	constructor() {
		console.log("## PageRouter.constructor");
		this.rotues = {};
	}
	
	register(path, callback) {
		this.routes[path] = callback;
	}
	
	navigate(path) {
		if (this.routes[path]) {
            this.routes[path]();
            history.pushState({ path: path }, '', path);
        } else {
            //console.log("404 페이지 없음");
            //this.routes['/404']();
        }
	}
	
	onPopstate() {
		const path = window.location.pathname;
		if (this.routes[path]) {
			this.routes[path]();
		}
	}
	
	init() {
		window.addEvnetListener("popstate", this.onPopstate.bind(this));
		this.navigate(whindow.location.pathname);
	}
	
}