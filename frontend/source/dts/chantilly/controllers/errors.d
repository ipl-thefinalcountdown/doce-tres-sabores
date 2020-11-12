module dts.chantilly.controllers.errors;

import vibe.http.server;

struct ErrorsController
{
	static void handler(HTTPServerRequest req,
		HTTPServerResponse res,
		HTTPServerErrorInfo error)
	{
		string errorDetails;
		auto pageTitle = error.message;
		auto errorMessage = error.message;

		if(error.code == 404)
			errorDetails = "Sorry, an error has occured, Requested page not found!";

		debug errorDetails ~= "\n" ~ error.debugMessage;

		res.render!("error.dt",
			pageTitle, errorMessage, errorDetails);
	}
}
