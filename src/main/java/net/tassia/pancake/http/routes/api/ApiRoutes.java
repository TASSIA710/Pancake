package net.tassia.pancake.http.routes.api;

import net.tassia.pancake.http.PancakeHttpServer;

public class ApiRoutes {

    public void registerRoutes(PancakeHttpServer server) {

    	// Auth
		server.POST("\\/api\\/v0\\/auth\\/login", new V0_POST_Login());
		server.POST("\\/api\\/v0\\/auth\\/register", new V0_POST_Register());

        // Accounts
        server.GET("\\/api\\/v0\\/accounts\\/(.*)", new V0_GET_Account());

        // Emails
        server.GET("\\/api\\/v0\\/emails\\/(.*)", new V0_GET_Email());

        // Groups
        server.GET("\\/api\\/v0\\/groups\\/(.*)", new V0_GET_Group());

    }

}
