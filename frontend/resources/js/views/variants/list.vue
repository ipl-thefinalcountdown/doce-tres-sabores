<template>
	<page-component>
		<div class="container">
			<div class="justify-content-center">
				<searchable-table :items="items" :row-clicked="rowClicked" :filter-changed="filterChanged"/>
			</div>
		</div>
	</page-component>
</template>

<script lang="ts">
import Vue from "vue"
import Component from "vue-class-component"

import PageComponent from '../../components/Page.vue'
import SearchableTable from '../../components/SearchableTable.vue'

import { mapState, mapActions } from "vuex";
import router from "../../router";

import VariantModel from '../../models/variant'

@Component({
	components: {
		PageComponent,
		SearchableTable
	},
	computed: mapState({
		items : (state : any) => state.api.variants
	}),
	methods: {
		...mapActions([
			"getVariants",
		])
	}
})
export default class VariantListView extends Vue {
	getVariants!: (obj : any) => void;

	data() {
		let obj = this;
		return {
			rowClicked(record : VariantModel, index : number, event : Event) {
				router.push({name: 'view-variant', params: {id: String(record?.code)}});
			},
			filterChanged(text : string) {
				obj.getVariants({ params: { filter: text } })
			},
			fields: []
		}
	}

	mounted() {
		this.getVariants({ params: { filter: '' } })
	}
}
</script>
