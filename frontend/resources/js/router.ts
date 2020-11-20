"use strict"

import Vue from "vue"
import VueRouter from "vue-router"

import IndexView from "./views/index.vue"
import LoginView from "./views/login.vue"
import VariantListView from './views/variants/list.vue'
import VariantView from './views/variants/view.vue'
import ProjectListView from './views/projects/list.vue'
import ProjectView from './views/projects/view.vue'
import ProjectAddEditView from './views/projects/addEdit.vue'

Vue.use(VueRouter)

export default new VueRouter({
	mode: 'hash',

	routes: [
		{ path: '/', name: 'index', component: IndexView },
		{ path: '/login', name: 'login', component: LoginView },
		{ path: '/variants', name: 'list-variants', component: VariantListView },
		{ path: '/variants/:id', name: 'view-variant', component: VariantView },
		{ path: '/projects', name: 'list-projects', component: ProjectListView },
		{ path: '/projects/new', name: 'new-project', component: ProjectAddEditView },
		{ path: '/projects/:id', name: 'view-project', component: ProjectView },
		{ path: '/projects/:id/edit', name: 'edit-project', component: ProjectAddEditView }
	]
})
