package net.tassia.pancake.http.routes.admin;

import net.tassia.pancake.Pancake;
import net.tassia.pancake.http.GenericPancakeView;
import net.tassia.pancake.http.HttpRequest;
import net.tassia.pancake.http.HttpRoute;

class GET_RootInfo implements HttpRoute {
	private final AdminRoutes routes;

	public GET_RootInfo(AdminRoutes routes) {
		this.routes = routes;
	}

	@Override
	public byte[] route(Pancake pancake, HttpRequest request, String[] matches) {
		GenericPancakeView view = new GenericPancakeView(pancake, request);
		if (view.checkAccess()) return null;

		routes.addSideNav(view, AdminRoutes.SIDENAV_ROOT);
		routes.addRootMailNav(view, AdminRoutes.ROOT_GENERAL);

		view.setContent(""); // TODO

		return view.view("Root");
	}

}