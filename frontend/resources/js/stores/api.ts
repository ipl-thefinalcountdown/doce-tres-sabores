"use strict"

import Vapi from "vuex-rest-api"
import { Store } from "vuex-rest-api/dist/Store"
import VariantModel from "../models/variant"
import ProjectModel from "../models/project"

export interface Params {
	params?: ParamsOptions
	data?: object
}

export interface ParamsOptions {
	id?: number | string,
	filter?: string
}

const api : Store = new Vapi({
	baseURL: `/api/`,
	state: {
		variants: <Array<VariantModel>>[],
		variant: <VariantModel>{},

		projects: <Array<ProjectModel>>[],
		project: <ProjectModel>{},

		clients: <Array<ProjectModel>>[],
		client: <ProjectModel>{}
	}
})
	.get({
		action: "getVariants",
		property: "variants",
		path: (opt : ParamsOptions) => `/variants/?filter=${opt.filter}`
	})
	.get({
		action: "getProjects",
		property: "projects",
		path: (opt : ParamsOptions) => `/projects/?filter=${opt.filter}`
	})
	.get({
		action: "getClients",
		property: "clients",
		path: (opt : ParamsOptions) => `/clients/?filter=${opt.filter}`
	})
	.get({
		action: "getProject",
		property: "project",
		path: (opt : ParamsOptions) => `/projects/${opt.id}`
	})
	.post({
		action: "updateProject",
		property: "project",
		path: (opt : ParamsOptions) => `/projects/${opt.id}`
	})
	.post({
		action: "addProject",
		property: "project",
		path: (opt : ParamsOptions) => `/projects/`
	})
	.put({
		action: "updateProject",
		property: "project",
		path: (opt : ParamsOptions) => `/projects/${opt.id}`
	})
	.delete({
		action: "deleteProject",
		path: (opt : ParamsOptions) => `/projects/${opt.id}`
	})
	.getStore();

export default api;
