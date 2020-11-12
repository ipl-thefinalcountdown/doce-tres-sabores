const mix = require('laravel-mix');

mix.setPublicPath('public/');

mix
	.ts("resources/js/app.ts", "dist/js")
	.sass("resources/sass/app.scss", "dist/css")
	.sourceMaps(false);
