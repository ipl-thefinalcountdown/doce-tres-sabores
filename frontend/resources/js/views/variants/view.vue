<template>
  <page-component>
    <b-container>
		<b-row>
			<b-col>
				<small-scrollable-table
					:fields="['length', {key: 'mcr_p',label: 'Mcr +'}]"
					:items="mcr_p"
				/>
				<small-scrollable-table
					:fields="['length', {key: 'mcr_n',label: 'Mcr -'}]"
					:items="mcr_n"
				/>
			</b-col>
			<b-col cols="8">
				<item-details
					:items="items"
					:awaiting-items="pending.variant"
					:edit-clicked="(authGroups.includes('Manufacturer')) ? editClicked : undefined"
          			:delete-clicked="(authGroups.includes('Manufacturer')) ? deleteClicked : undefined"
				>
					<template #cell(product)="data">
						<b-link :to="{ name: 'view-product', params: { id: productId }}">{{ data.value }}</b-link>
					</template>
				</item-details>
			</b-col>
		</b-row>
    </b-container>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import { AxiosPromise } from "axios";

import PageComponent from "../../components/Page.vue";
import ItemDetails from "../../components/item/ItemDetails.vue";
import SmallScrollableTable from "../../components/SmallScrollableTable.vue";

import { mapState, mapActions } from "vuex";
import VariantModel from "../../models/variant";

import { AlertType, createAlert } from "../../utils/alert";
import { objToArray } from "../../utils/hashmap";
import { fieldKeys } from "../../utils/fields";

import { Params } from "../../stores/api";
import router from "../../router";

  import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
const Auth = namespace("auth");

@Component({
  components: {
    PageComponent,
	ItemDetails,
	SmallScrollableTable
  },
  computed: mapState({
    items: (state: any) => {
			return [state.api.variant].map((v: VariantModel) => {
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
		},
	productId: (state: any) => state.api.variant.productId,
	mcr_p: (state: any) => {
		let val = state.api.variant.mcr_p;
		if(val != undefined)
			return objToArray(val, ["length", "mcr_p"]);
		return [];
	},
	mcr_n: (state: any) => {
		let val = state.api.variant.mcr_n;
		if(val != undefined)
			return objToArray(val, ["length", "mcr_n"]);
		return [];
	},
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getVariant", "deleteVariant"]),
  },
})
export default class VariantView extends Vue {
	    @Auth.Getter
    private isAuthenticated!: boolean;

    @Auth.Getter
    public authTokenDecoded!: ExtendedJwtPayload;

    @Auth.Getter
    public authUser!: string;

    @Auth.Getter
	public authGroups!: string[];

  getVariant!: (obj: Params) => void;
  deleteVariant!: (obj: Params) => AxiosPromise;

  variantId?: number | string;

  data() {
    let obj = this;
    return {
      editClicked() {
        router.push({name: 'edit-variant', params: {id: String(obj.variantId)}});
      },
      deleteClicked() {
        obj
          .deleteVariant({ params: { id: obj.variantId } })
          .then(() => {
            // success deletion
            router.push({name:`list-variants`});
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting project ${obj.variantId}: ${err}`
            );
          });
      },
    };
  }

  mounted() {
    this.variantId = this.$route.params.id;
    this.getVariant({ params: { id: this.variantId } });
  }
}
</script>
