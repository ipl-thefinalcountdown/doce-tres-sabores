<template>
	<div>
		<page-navbar/>
			<main id="app-content" class="py-4">
				<slot></slot>
			</main>
		<page-footer/>
	</div>
</template>

<script lang="ts">
	import Vue from "vue"
	import Component from "vue-class-component"

	import PageNavbar from '../components/PageNavbar.vue'
	import PageFooter from '../components/PageFooter.vue'

	@Component({
		props: ["content"],
		components: {
			PageNavbar,
			PageFooter
		}
	})
	export default class PageComonent extends Vue {

	}
</script>
