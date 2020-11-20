<template>
	<page-component>
		<div class="container">
			<item-details :items="items"/>
		</div>
	</page-component>
</template>

<script lang="ts">
	import Vue from "vue"
	import Component from "vue-class-component"

	import PageComponent from '../../components/Page.vue'
	import ItemDetails from '../../components/item/ItemDetails.vue'

	@Component({
		components: {
			PageComponent,
			ItemDetails
		}
	})
	export default class VariantView extends Vue {
		data() {
			return {
				items: {}
			}
		}
	}
</script>
