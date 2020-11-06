module dts.chantilly.app;

import vibe.http.server;
import vibe.http.router;
import vibe.http.fileserver;
import vibe.core.core;
import vibe.core.log;

import std.functional;

import dts.chantilly.controllers.home;
import dts.chantilly.controllers.errors;

void main()
{
	// ======== WEBAPP ROUTER ========
	auto router = new URLRouter();
	router.get("/", &HomeController.index);
	router.get("*", serveStaticFiles("./public/"));

	// ======== SETTINGS ========
	auto settings = new HTTPServerSettings();
	settings.port = 8080;
	settings.bindAddresses = ["::"];
	settings.errorPageHandler = toDelegate(&ErrorsController.handler);

	bootstrap(settings, router);
}

void bootstrap(HTTPServerSettings settings, URLRouter router)
{
	listenHTTP(settings, router);

	logInfo("Please open http://127.0.0.1:8080/ in your browser.");
	runApplication();
}
