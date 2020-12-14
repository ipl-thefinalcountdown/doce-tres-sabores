<template>
	<page-component>
		<div class="container">
			<div class="justify-content-center">
				<searchable-table
					:items="items"
					:row-clicked="rowClicked"
					:filter-changed="filterChanged"
					:add-clicked="addClicked"
					:edit-clicked="editClicked"
					:delete-clicked="deleteClicked"
				/>
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
import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import { AxiosPromise } from "axios";

@Component({
	components: {
		PageComponent,
		SearchableTable
	},
	computed: mapState({
		items : (state : any) => {
			return state.api.variants.map((v: VariantModel) => {
				return {
					id: v.id,
					name: v.name,
					product: v.productName,
					weff_p: v.weff_p,
					weff_n: v.weff_n,
					ar: v.ar,
					sigmaC: v.sigmaC,
					pp: v.pp,
				};
			});
		}
	}),
	methods: {
		...mapActions([
			"getVariants",
			"deleteVariant"
		])
	},
	props: {
		productIdSelected: Number
	}
})
export default class VariantListView extends Vue {
	getVariants!: (obj : any) => void;
	deleteVariant!: (obj: Params) => AxiosPromise;

	productIdSelected?: number;

	data() {
		let obj = this;
		return {
			rowClicked(record : VariantModel, index : number, event : Event) {
				router.push({name: 'view-variant', params: {id: String(record?.id)}});
			},
			filterChanged(text : string) {
				obj.getVariants({ params: { filter: text } })
			},
			addClicked(event: Event) {
				router.push({name: 'new-variant'});
			},
			editClicked(record: VariantModel, index: number, event: Event) {
				router.push({name: 'edit-variant', params: {id: String(record?.id)}});
			},
			deleteClicked(record: VariantModel, index: number, event: Event) {
				// perform delete on the API
				obj
				.deleteVariant({ params: { id: record.id } })
				.then(() => {
					// success deletion
					createAlert(AlertType.Success, `Variant ${record.id} deleted.`);
					// splice directly from the store state object
					(<Array<VariantModel>>obj.$store.state.api.variants).splice(
					index,
					1
					);
				})
				.catch((err) => {
					// error on delete
					createAlert(
					AlertType.Danger,
					`Error deleting variant ${record.id}: ${err}`
					);
				});
			},
			fields: []
		}
	}

	mounted() {
		this.getVariants({ params: { filter: '' } })
	}
}
</script>
