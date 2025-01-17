"use strict"

import Vapi from "vuex-rest-api"
import { Store } from "vuex-rest-api/dist/Store"
import VariantModel from "../models/variant"
import ProjectModel from "../models/project"
import StructureModel from "../models/structure"
import ProductModel from "../models/product"
import FamilyModel from "../models/family"
import UserModel from "../models/user"

export interface Params {
	params?: ParamsOptions
	data?: object
}

export interface ParamsOptions {
	id?: number | string,
	param1?: number | string,
	param2?: number | string,
	type?: number,
	filter?: string
}

const api : Store = new Vapi({
	baseURL: `/api/`,
	state: {
		variants: <Array<VariantModel>>[],
		variant: <VariantModel>{},

		simulation: <Array<VariantModel>>[],

		projects: <Array<ProjectModel>>[],
		project: <ProjectModel>{},

		products: <Array<ProductModel>>[],
		product: <ProductModel>{},

		clients: <Array<UserModel>>[],
		client: <UserModel>{},

		users: <Array<UserModel>>[],
		user: <UserModel>{},

		structures: <Array<StructureModel>>[],
		structure: <StructureModel>{},

		families: <Array<FamilyModel>>[],
		family: <FamilyModel>{},
	}
})
	.get({
		action: "getVariants",
		property: "variants",
		path: (opt : ParamsOptions) => `/variants/?filter=${opt.filter}`
	})
	.get({
		action: "getVariantsByType",
		property: "variants",
		path: (opt : ParamsOptions) => `/variants/?type=${opt.type}&filter=${opt.filter}`
	})
	.get({
		action: "getVariant",
		property: "variant",
		path: (opt : ParamsOptions) => `/variants/${opt.id}`
	})
	.post({
		action: "addVariant",
		property: "variant",
		path: (opt : ParamsOptions) => `/variants/`
	})
	.put({
		action: "updateVariant",
		property: "variant",
		path: (opt : ParamsOptions) => `/variants/${opt.id}`
	})
	.delete({
		action: "deleteVariant",
		path: (opt : ParamsOptions) => `/variants/${opt.id}`
	})
	.get({
		action: "getProducts",
		property: "products",
		path: (opt : ParamsOptions) => `/products/?filter=${opt.filter}`
	})
	.get({
		action: "getProduct",
		property: "product",
		path: (opt : ParamsOptions) => `/products/${opt.id}`
	})
	.post({
		action: "addProduct",
		property: "product",
		path: (opt : ParamsOptions) => `/products/`
	})
	.put({
		action: "updateProduct",
		property: "product",
		path: (opt : ParamsOptions) => `/products/${opt.id}`
	})
	.delete({
		action: "deleteProduct",
		path: (opt : ParamsOptions) => `/products/${opt.id}`
	})
	.get({
		action: "getFamilies",
		property: "families",
		path: (opt : ParamsOptions) => `/families/`
	})
	.get({
		action: "getFamily",
		property: "family",
		path: (opt : ParamsOptions) => `/families/${opt.id}`
	})
	.get({
		action: "getProjects",
		property: "projects",
		path: (opt : ParamsOptions) => `/projects/?filter=${opt.filter}`
	})
	.get({
		action: "getProject",
		property: "project",
		path: (opt : ParamsOptions) => `/projects/${opt.id}`
	})
	.get({
		action: "getProjectDocument",
		path: (opt : ParamsOptions) => `/projects/${opt.id}/documents/${opt.param1}`
	})
	.put({
		action: "addProjectStructure",
		path: (opt : ParamsOptions) => `/projects/${opt.id}/structures/${opt.param1}`
	})
	.delete({
		action: "deleteProjectStructure",
		path: (opt : ParamsOptions) => `/projects/${opt.id}/structures/${opt.param1}`
	})
	.delete({
		action: "deleteDocument",
		path: (opt : ParamsOptions) => `/documents/${opt.id}`
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
	.get({
		action: "getClients",
		property: "clients",
		path: (opt : ParamsOptions) => `/clients/?filter=${opt.filter}`
	})
	.get({
		action: "getClient",
		property: "client",
		path: (opt : ParamsOptions) => `/clients/${opt.id}`
	})
	.get({
		action: "getUser",
		property: "user",
		path: (opt : ParamsOptions) => `/users/${opt.id}`
	})
	.get({
		action: "getStructure",
		property: "structure",
		path: (opt : ParamsOptions) => `/structures/${opt.id}`
	})
	.get({
		action: "getStructures",
		property: "structures",
		path: (opt : ParamsOptions) => `/structures/?filter=${opt.filter}`
	})
	.get({
		action: "getStructuresByType",
		property: "structures",
		path: (opt : ParamsOptions) => `/structures/?type=${opt.type}&filter=${opt.filter}`
	})
	.post({
		action: "requestSimulation",
		property: "simulation",
		path: (opt : ParamsOptions) => `/structures/simulation/`
	})
	.post({
		action: "addStructure",
		property: "structure",
		path: (opt : ParamsOptions) => `/structures/`
	})
	.put({
		action: "updateStructure",
		property: "structure",
		path: (opt : ParamsOptions) => `/structures/${opt.id}`
	})
	.delete({
		action: "deleteStructure",
		path: (opt : ParamsOptions) => `/structures/${opt.id}`
	})
	.post({
		action: "addUser",
		path: (opt : ParamsOptions) => `/users/`
	})
	.getStore();

export default api;
