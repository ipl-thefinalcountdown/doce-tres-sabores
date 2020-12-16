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
// import StructureListView from './views/structures/list.vue'

Vue.use(VueRouter)

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
		{ path: '/products/:id/edit', name: 'edit-product', component: ProductAddEditView },

		{ path: '/projects', name: 'list-projects', component: ProjectListView },
		{ path: '/projects/new', name: 'new-project', component: ProjectAddEditView },
		{ path: '/projects/:id', name: 'view-project', component: ProjectView },
		{ path: '/projects/:id/edit', name: 'edit-project', component: ProjectAddEditView },

		{ path: '/login', name: 'login', component: LoginView },
		// { path: '/register', name: 'login', component: RegisterView },

		// { path: '/users/:id', name: 'view-user', component: UserView },
		// { path: '/users/:id/edit', name: 'edit-user', component: UserEditView },

		// { path: '/structures', name: 'list-structures', component: StructureListView },
		// { path: '/structures/new', name: 'new-structure', component: StructureAddEditView },
		// { path: '/structures/:id', name: 'view-project', component: StructureView },
		// { path: '/structures/:id/edit', name: 'edit-project', component: StructureAddEditView },

		// { path: '/simulation', name: 'view-project', component: SimulationIndexView },

		// { path: '/structures/simulation', name: 'view-project', component: SimulationView },
		// { path: '/structures/:id/simulation', name: 'view-project', component: StructureSimulationView },
	]
})
