<template>
	<page-component>
		<b-container>
			<b-row>
				<b-col><h4 class="text-center">Discover our product families</h4></b-col>
			</b-row>
			<b-row cols="1" class="mt-5">
				<b-col><h5>Light Steel</h5><hr/></b-col>
				<b-col>
					<b-row cols-lg="3" cols-md="1">
						<b-col v-for="item in lightSteelItems" :key="item.id">
							<b-card
								:title="`${item.name}`"
								tag="article"
								style="max-width: 20rem;"
								class="mb-2"
							>
								<b-card-text></b-card-text>
								<b-button :to="{name: 'view-family', params: { id: item.id }}" variant="primary">Discover</b-button>
							</b-card>
						</b-col>
					</b-row>
				</b-col>
			</b-row>
			<b-row cols="1" class="mt-5">
				<b-col><h5>Profiled Sheeting</h5><hr/></b-col>
				<b-col>
					<b-row cols-lg="3" cols-md="1">
						<b-col v-for="item in profiledSheetingItems" :key="item.id">
							<b-card
								:title="`${item.name}`"
								tag="article"
								style="max-width: 20rem;"
								class="mb-2"
							>
								<b-card-text></b-card-text>
								<b-button :to="{name: 'view-family', params: { id: item.id }}" variant="primary">Discover</b-button>
							</b-card>
						</b-col>
					</b-row>
				</b-col>
			</b-row>
			<b-row cols="1" class="mt-5">
				<b-col><h5>Slab</h5><hr/></b-col>
				<b-col>
					<b-row cols-lg="3" cols-md="1">
						<b-col v-for="item in slabItems" :key="item.id">
							<b-card
								:title="`${item.name}`"
								tag="article"
								style="max-width: 20rem;"
								class="mb-2"
							>
								<b-card-text></b-card-text>
								<b-button :to="{name: 'view-family', params: { id: item.id }}" variant="primary">Discover</b-button>
							</b-card>
						</b-col>
					</b-row>
				</b-col>
			</b-row>
			<b-row cols="1" class="mt-5">
				<b-col><h5>Sandwich Panel</h5><hr/></b-col>
				<b-col>
					<b-row cols-lg="3" cols-md="1">
						<b-col v-for="item in sandwichPanelItems" :key="item.id">
							<b-card
								:title="`${item.name}`"
								tag="article"
								style="max-width: 20rem;"
								class="mb-2"
							>
								<b-card-text></b-card-text>
								<b-button :to="{name: 'view-family', params: { id: item.id }}" variant="primary">Discover</b-button>
							</b-card>
						</b-col>
					</b-row>
				</b-col>
			</b-row>
		</b-container>
	</page-component>
</template>

<script lang="ts">
	import Vue from "vue"
	import Component from "vue-class-component"
	import { mapState, mapActions } from "vuex";

	import PageComponent from '../components/Page.vue'
	import FamilyModel from "../models/family"

	import {AlertType, createAlert} from '../utils/alert'

	import { Params } from "../stores/api";
	import { AxiosPromise } from "axios";
import { MaterialType } from "../models/material";

	@Component({
		components: {
			PageComponent
		},
		computed: {
			...mapState({
			lightSteelItems: (state: any) =>
				state.api.families.filter((i: FamilyModel) => i.materialId == MaterialType.LIGHT_STEEL),
			profiledSheetingItems: (state: any) =>
				state.api.families.filter((i: FamilyModel) => i.materialId == MaterialType.PROFILED_SHEETING),
			slabItems: (state: any) =>
				state.api.families.filter((i: FamilyModel) => i.materialId == MaterialType.SLAB),
			sandwichPanelItems: (state: any) =>
				state.api.families.filter((i: FamilyModel) => i.materialId == MaterialType.SANDWICH_PANEL),
			pending: (state: any) => state.api.pending,
			error: (state: any) => state.api.error,
			}),
		},
		methods: {
			...mapActions(["getFamilies"]),
		},
	})
	export default class IndexView extends Vue {
		getFamilies!: () => void;

		mounted() {
			this.getFamilies();
		}
	}
</script>
