"use strict"

import Vue from "vue"
import VueRouter from "vue-router"

import IndexView from "./views/index.vue"
import VariantsView from './views/variants.vue'

Vue.use(VueRouter)

export default new VueRouter({
    mode: 'hash',

    routes: [
		{ path: '/', name: 'index', component: IndexView },
		{ path: '/variants', name: 'index', component: VariantsView }
    ]
})
