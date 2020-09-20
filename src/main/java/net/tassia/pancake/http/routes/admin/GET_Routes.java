package net.tassia.pancake.http.routes.admin;

import net.tassia.pancake.Pancake;
import net.tassia.pancake.http.GenericPancakeView;
import net.tassia.pancake.http.HttpRequest;
import net.tassia.pancake.http.HttpRoute;
import net.tassia.pancake.http.HttpView;

class GET_Routes implements HttpRoute {
	private final AdminRoutes routes;
	private final String noneView;

	public GET_Routes(AdminRoutes routes) {
		this.routes = routes;
		this.noneView = new HttpView("/views/route/none.html").view();
	}

	@Override
	public byte[] route(Pancake pancake, HttpRequest request, String[] matches) {
		GenericPancakeView view = new GenericPancakeView(pancake, request);
		if (view.checkAccess()) return null;

		routes.addSideNav(view, AdminRoutes.SIDENAV_ROUTES);
		routes.addRoutesMailNav(pancake, view, null);

		view.setContent(noneView);

		return view.view("Routes");
	}

}