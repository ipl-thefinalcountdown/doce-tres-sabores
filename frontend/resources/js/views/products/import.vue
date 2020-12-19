<template>
  <page-component>
    <div class="container">
      <item-edit :on-submit="onSubmit">
		<b-form-group
			id="fieldset-1"
			label="Enter variants file"
		>
			<b-form-file
				v-model="file1"
				required
				placeholder="Choose a file or drop it here..."
				drop-placeholder="Drop file here..."
			></b-form-file>
		</b-form-group>
		<b-form-group
			id="fieldset-1"
			label="Enter the mcr map file"
		>
			<b-form-file
				v-model="file2"
				required
				placeholder="Choose a file or drop it here..."
				drop-placeholder="Drop file here..."
			></b-form-file>
		</b-form-group>
      </item-edit>
    </div>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import { mapState, mapActions } from "vuex";
import axios from "axios";

import PageComponent from "../../components/Page.vue";
import ItemEdit from "../../components/item/ItemAddEdit.vue";
import FormField from "../../components/form/FormField.vue";
import FormSearchableSelect from "../../components/form/FormSearchableSelect.vue"

import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";

import router from "../../router"

import ProductModel from "../../models/product";
import FamilyModel from "../../models/family";

@Component({
  components: {
    PageComponent,
    ItemEdit
  },
})
export default class ProductImportVariantsView extends Vue {
	file1?: any;
	file2?: any;

	data() {
		let obj = this;
		return {
			file1: null,
        	file2: null,
			onSubmit() {
				let formData = new FormData();
				formData.append('fileVariant', obj.file1);
				formData.append('fileResistance', obj.file2);
				axios({
					url: `products/${obj.$route.params.id}/import`,
					data: formData,
					headers: {
						'content-type': 'multipart/form-data'
					},
					method: 'POST'
				}).then(res => {
					// go back
					router.go(-1);
				}).catch(err => {
					createAlert(
						AlertType.Danger,
						`Error importing files ${obj.file1.name} and ${obj.file2.name}: ${err}`
					);
				});
			}
		}
	}
}
</script>
