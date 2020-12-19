"use strict"

import Vue from "vue"
import VueRouter from "vue-router"

import IndexView from "./views/index.vue"
import LoginView from "./views/auth/login.vue"
import RegisterView from "./views/auth/register.vue"
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
import ProfileView from './views/auth/profile.vue'

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

const authenticatedRole = (role: 'Manufacturer' | 'Designer' | 'Client') => {
	return (to: any, from: any, next : Function) => {
		if (store.getters['auth/isAuthenticated'] && store.getters['auth/authGroups'].includes(role)) {
			next()
			return
		}
		next('/login')
	}
}

const authDesignerOrClient = (to: any, from: any, next: Function) => {
	let groups = store.getters['auth/authGroups'];
	if (store.getters['auth/isAuthenticated'] && (groups.includes('Designer') || groups.includes('Client'))) {
		next()
		return
	}
	next('/login')
}

export default new VueRouter({
	mode: 'hash',

	routes: [
		{ path: '/', name: 'index', component: IndexView },

		{ path: '/variants', name: 'list-variants', component: VariantListView, beforeEnter: ifAuthenticated },
		{ path: '/variants/new', name: 'new-variant', component: VariantAddEditView, beforeEnter: authenticatedRole('Manufacturer') },
		{ path: '/variants/:id', name: 'view-variant', component: VariantView, beforeEnter: ifAuthenticated },
		{ path: '/variants/:id/edit', name: 'edit-variant', component: VariantAddEditView, beforeEnter: authenticatedRole('Manufacturer') },

		{ path: '/families/:id', name: 'view-family', component: ProductListView, beforeEnter: ifAuthenticated },

		{ path: '/products', name: 'list-products', component: ProductListView, beforeEnter: ifAuthenticated },
		{ path: '/products/new', name: 'new-product', component: ProductAddEditView, beforeEnter: authenticatedRole('Manufacturer') },
		{ path: '/products/:id', name: 'view-product', component: VariantListView, beforeEnter: ifAuthenticated },
		{ path: '/products/:id/import', name: 'import-variants', component: ProductImportVariantsView, beforeEnter: authenticatedRole('Manufacturer') },
		{ path: '/products/:id/edit', name: 'edit-product', component: ProductAddEditView, beforeEnter: authenticatedRole('Manufacturer') },

		{ path: '/projects', name: 'list-projects', component: ProjectListView, beforeEnter: authDesignerOrClient },
		{ path: '/projects/new', name: 'new-project', component: ProjectAddEditView, beforeEnter: authenticatedRole('Designer') },
		{ path: '/projects/:id', name: 'view-project', component: ProjectView, beforeEnter: authDesignerOrClient },
		{ path: '/projects/:id/structures/new', name: 'project-add-structure', component: ProjectAddStructureView, beforeEnter: authenticatedRole('Designer') },
		{ path: '/projects/:id/upload', name: 'project-upload-files', component: ProjectUploadFilesView, beforeEnter: authDesignerOrClient },
		{ path: '/projects/:id/edit', name: 'edit-project', component: ProjectAddEditView, beforeEnter: authDesignerOrClient },

		{ path: '/login', name: 'login', component: LoginView, beforeEnter: ifNotAuthenticated },
		{ path: '/register', name: 'register', component: RegisterView, beforeEnter: ifNotAuthenticated },
		{ path: '/profile/', name: 'view-profile', component: ProfileView, beforeEnter: ifAuthenticated },

		{ path: '/clients/:id', name: 'view-client', component: ClientView, beforeEnter: authDesignerOrClient },

		{ path: '/structures/simulation', name: 'simulate-structure', component: AddEditSimulateStructureView, beforeEnter: authenticatedRole('Designer')},
		{ path: '/structures', name: 'list-structures', component: StructureListView, beforeEnter: authDesignerOrClient },
		{ path: '/structures/new', name: 'new-structure', component: AddEditSimulateStructureView, beforeEnter: authenticatedRole('Designer') },
		{ path: '/structures/:id', name: 'view-structure', component: StructureView, beforeEnter: authDesignerOrClient },
		{ path: '/structures/:id/edit', name: 'edit-structure', component: AddEditSimulateStructureView, beforeEnter: authenticatedRole('Designer') },

		// Routes redirects (breadcrumb)
		{ path: '/families/', redirect: { name: 'index' } },
		{ path: '/projects/:id/structures', redirect: '/projects/:id' },
		{ path: '/clients/', redirect: { name: 'index' } },

		// FIXME: Breadcrumb bug
		{ path: '/:id/structures', redirect: '/projects/:id' },
	]
})
