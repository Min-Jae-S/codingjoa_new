class PageRouter {
	constructor(containerSelector) {
		console.log("## PageRouter.constructor");
		this.$container = $(containerSelector);
		this.rotues = {};
	}
	
	register(path, callback) {
		this.routes[path] = callback;
	}
	
	navigate(path) {
		this.navigate(path, true);
	}
	
	navigate(path, pushState) {
		if (this.route[path]) {

			if (pushState) {
				history.pushState({ path }, "", path);
			}
		} else {
			console.log(`## route ${path} is not registerd`);
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