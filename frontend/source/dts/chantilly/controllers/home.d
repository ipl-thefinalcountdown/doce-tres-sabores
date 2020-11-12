module dts.chantilly.controllers.home;

import dts.chantilly.config;

import vibe.http.server;

struct HomeController
{
	static void index(HTTPServerRequest req, HTTPServerResponse res)
	{
		auto pageTitle = "Chantilly";
		render!("index.dt", pageTitle, appConfig)(res);
	}
}
