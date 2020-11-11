module dts.chantilly.config;

import dyaml.node;
import dyaml.loader;

import std.exception : basicExceptionCtors;

import vibe.http.server;

class ConfigNotFoundException : Exception
{
	///
	mixin basicExceptionCtors;
}

class Config {
	public static Config instance;

	HTTPServerSettings httpSettings;

	this(string filename)
	{
		httpSettings = new HTTPServerSettings();

		Node root = Loader.fromFile(filename).load();

		// port
		httpSettings.port = root["port"].as!ushort;
	}

	static Config initialize()
	{
		foreach(ext; [".yml", ".yaml"])
		{
			import std.file : exists, isFile;

			string configName = "config" ~ ext;
			if(exists(configName) && isFile(configName))
				return new Config(configName);
		}

		throw new ConfigNotFoundException("config.yml not found. Please create one!");
	}
}

// single config instance
Config appConfig;
