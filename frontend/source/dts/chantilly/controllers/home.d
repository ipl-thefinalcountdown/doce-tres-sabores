module dts.chantilly.controllers.home;

import vibe.http.server;

struct HomeController
{
	static void index(HTTPServerRequest req, HTTPServerResponse res)
	{
		render!"index.dt"(res);
	}
}
