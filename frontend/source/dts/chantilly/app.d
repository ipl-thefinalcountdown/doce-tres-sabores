module dts.chantilly.app;

import vibe.http.router;
import vibe.http.fileserver;
import vibe.core.core;
import vibe.core.log;

import std.functional;
import std.format;

import dts.chantilly.controllers.home;
import dts.chantilly.controllers.errors;

import dts.chantilly.config;

import vibe.http.auth.basic_auth;

int main(string[] args)
{
	// ======== WEBAPP ROUTER ========
	auto router = new URLRouter();
	router.get("*", serveStaticFiles("./public/"));
	router.get("/", &HomeController.index);

	// initialize app config
	try {
		appConfig = args.length > 1
			? new Config(args[1])
			: Config.initialize();
	} catch(ConfigNotFoundException ex)
	{
		logFatal(ex.msg);
		return 1;
	}

	bootstrap(router);
	return 0;
}

void bootstrap(URLRouter router)
{
	// TODO: map this to config later
	appConfig.httpSettings.bindAddresses = ["::"];
	appConfig.httpSettings.errorPageHandler = toDelegate(&ErrorsController.handler);

	listenHTTP(appConfig.httpSettings, router);

	logInfo(format!"Please open http://%s:%d/ in your browser."(
		appConfig.httpSettings.bindAddresses[0],
		appConfig.httpSettings.port
	));
	runApplication();
}
