<template>
	<page-component>
		<div class="container-fluid">
			<div class="row justify-content-center">
				<searchable-table/>
			</div>
		</div>
	</page-component>
</template>

<script lang="ts">
import Vue from "vue"
import Component from "vue-class-component"

import PageComponent from '../components/Page.vue'
import SearchableTable from '../components/SearchableTable.vue'

@Component({
	components: {
		PageComponent,
		SearchableTable
	}
})
export default class VariantsView extends Vue {}
</script>

<style>

</style>
