package net.tassia.pancake.http.routes.admin;

import net.tassia.pancake.Pancake;
import net.tassia.pancake.http.*;

class GET_ConfigGeneral implements HttpRoute {
	private final AdminRoutes routes;
	private final HttpView content;

	public GET_ConfigGeneral(AdminRoutes routes) {
		this.routes = routes;
		this.content = new HttpView("/views/config/general.html");
	}

	@Override
	public byte[] route(Pancake pancake, HttpRequest request, String[] matches) {
		GenericPancakeView view = new GenericPancakeView(pancake, request);
		if (view.checkAccess()) return null;
		if (view.checkAccess(request.getAuth().isRoot())) return null;

		routes.addSideNav(request.getAuth(), view, AdminRoutes.SIDENAV_CONFIG);
		routes.addConfigMailNav(view, AdminRoutes.CONFIG_GENERAL);

		view.setContent(content.view());

		return view.view("Configuration");
	}

}
