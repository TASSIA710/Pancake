package net.tassia.pancake.http.routes.admin;

import net.tassia.pancake.Pancake;
import net.tassia.pancake.http.GenericPancakeView;
import net.tassia.pancake.http.HttpRequest;
import net.tassia.pancake.http.HttpRoute;
import net.tassia.pancake.http.HttpView;

class GET_ConfigSMTP implements HttpRoute {
	private final AdminRoutes routes;
	private final HttpView content;

	public GET_ConfigSMTP(AdminRoutes routes) {
		this.routes = routes;
		this.content = new HttpView("/views/config/smtp.html");
	}

	@Override
	public byte[] route(Pancake pancake, HttpRequest request, String[] matches) {
		GenericPancakeView view = new GenericPancakeView(pancake, request);
		if (view.checkAccess()) return null;

		routes.addSideNav(view, AdminRoutes.SIDENAV_CONFIG);
		routes.addConfigMailNav(view, AdminRoutes.CONFIG_SMTP);

		view.setContent(content.view());

		return view.view("SMTP Config");
	}

}
