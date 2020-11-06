module dts.chantilly.controllers.errors;

import vibe.http.server;

struct ErrorsController
{
	static void handler(HTTPServerRequest req,
		HTTPServerResponse res,
		HTTPServerErrorInfo error)
	{
		res.render!("errors/notfound.dt", req, error);
	}
}
