"use strict"

import Vue from "vue"
import VueRouter from "vue-router"

import IndexView from "./views/index.vue"
import LoginView from "./views/login.vue"
import VariantListView from './views/variants/list.vue'
import VariantView from './views/variants/view.vue'
import VariantAddEditView from './views/variants/addEdit.vue'
import ProjectListView from './views/projects/list.vue'
import ProductListView from './views/products/list.vue'
import ProductAddEditView from './views/products/addEdit.vue'
import ProjectView from './views/projects/view.vue'
import ProjectAddEditView from './views/projects/addEdit.vue'
import StructureListView from './views/structures/list.vue'
import AddEditSimulateStructureView from './views/structures/addEditSimulate.vue'
import StructureView from './views/structures/view.vue'
import ClientView from './views/clients/view.vue'
import ProductImportVariantsView from './views/products/import.vue'
import ProjectUploadFilesView from './views/projects/upload.vue'
import ProjectAddStructureView from './views/projects/newStructure.vue'

Vue.use(VueRouter)

import store from './store'
import { Params } from "./stores/api"

const ifNotAuthenticated = (to: any, from: any, next: Function) => {
	if (!store.getters['auth/isAuthenticated']) {
		next()
		return
	}
	next('/')
}

const ifAuthenticated = (to: any, from: any, next : Function) => {
	if (store.getters['auth/isAuthenticated']) {
		next()
		return
	}
	next('/login')
}

//   export default new Router({
// 	mode: 'history',
// 	routes: [
// 	  {
// 		path: '/',
// 		name: 'Home',
// 		component: Home,
// 	  },
// 	  {
// 		path: '/account',
// 		name: 'Account',
// 		component: Account,
// 		beforeEnter: ifAuthenticated,
// 	  },
// 	  {
// 		path: '/login',
// 		name: 'Login',
// 		component: Login,
// 		beforeEnter: ifNotAuthenticated,
// 	  },
// 	],
//   })

export default new VueRouter({
	mode: 'hash',

	routes: [
		{ path: '/', name: 'index', component: IndexView },

		{ path: '/variants', name: 'list-variants', component: VariantListView },
		{ path: '/variants/new', name: 'new-variant', component: VariantAddEditView },
		{ path: '/variants/:id', name: 'view-variant', component: VariantView },
		{ path: '/variants/:id/edit', name: 'edit-variant', component: VariantAddEditView },

		{ path: '/families/:id', name: 'view-family', component: ProductListView },
		{ path: '/families/', redirect: {name: 'index'}},

		{ path: '/products', name: 'list-products', component: ProductListView },
		{ path: '/products/new', name: 'new-product', component: ProductAddEditView },
		{ path: '/products/:id', name: 'view-product', component: VariantListView },
		{ path: '/products/:id/import', name: 'import-variants', component: ProductImportVariantsView },
		{ path: '/products/:id/edit', name: 'edit-product', component: ProductAddEditView },

		{ path: '/projects', name: 'list-projects', component: ProjectListView },
		{ path: '/projects/new', name: 'new-project', component: ProjectAddEditView },
		{ path: '/projects/:id', name: 'view-project', component: ProjectView },
		{ path: '/projects/:id/structures/new', name: 'project-add-structure', component: ProjectAddStructureView },
		{ path: '/projects/:id/structures', redirect: '/projects/:id' },
		{ path: '/projects/:id/upload', name: 'project-upload-files', component: ProjectUploadFilesView },
		{ path: '/projects/:id/edit', name: 'edit-project', component: ProjectAddEditView },

		// FIXME: Breadcrumb bug
		{ path: '/:id/structures', redirect: '/projects/:id' },

		{ path: '/login', name: 'login', component: LoginView, beforeEnter: ifNotAuthenticated },
		// { path: '/register', name: 'login', component: RegisterView },

		{ path: '/clients/', redirect: {name: 'index'}},
		{ path: '/clients/:id', name: 'view-client', component: ClientView },
		// { path: '/users/:id/edit', name: 'edit-user', component: UserEditView },

		{ path: '/structures/simulation', name: 'simulate-structure', component: AddEditSimulateStructureView },
		{ path: '/structures', name: 'list-structures', component: StructureListView },
		{ path: '/structures/new', name: 'new-structure', component: AddEditSimulateStructureView },
		{ path: '/structures/:id', name: 'view-structure', component: StructureView },
		{ path: '/structures/:id/edit', name: 'edit-structure', component: AddEditSimulateStructureView },
	]
})
