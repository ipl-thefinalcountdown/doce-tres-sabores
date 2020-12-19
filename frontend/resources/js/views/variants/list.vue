<template>
  <page-component>
    <div class="container">
      <b-row v-if="isProduct && product.name != undefined" class="pb-3">
        <b-col
          ><h5>{{ product.name }}</h5></b-col
        >
        <b-col v-if="authGroups.includes('Manufacturer')">
          <div class="d-flex flex-row-reverse bd-highlight">
            <div class="pl-3">
              <b-button
                :to="{name: 'import-variants'}"
                size="sm"
                variant="outline-secondary"
              >
                Import
              </b-button>
            </div>
          </div>
        </b-col>
      </b-row>
      <div class="justify-content-center">
        <searchable-table
          :items="isProduct ? productItems : items"
          :row-clicked="rowClicked"
          :filter-changed="filterChanged"
          :add-clicked="(authGroups.includes('Manufacturer')) ? addClicked : undefined"
          :edit-clicked="(authGroups.includes('Manufacturer')) ? editClicked : undefined"
          :delete-clicked="(authGroups.includes('Manufacturer')) ? deleteClicked : undefined"
        />
      </div>
    </div>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import PageComponent from "../../components/Page.vue";
import SearchableTable from "../../components/SearchableTable.vue";

import { mapState, mapActions } from "vuex";
import router from "../../router";

import VariantModel from "../../models/variant";
import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import { AxiosPromise } from "axios";


  import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
	const Auth = namespace("auth");

var variantMap = (v: VariantModel) => {
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
};

@Component({
  components: {
    PageComponent,
    SearchableTable,
  },
  computed: {
    ...mapState({
      items: (state: any) => {
        return state.api.variants.map(variantMap);
      },
      productItems: (state: any) => {
        if (state.api?.product?.variants != undefined)
          return state.api.product.variants.map(variantMap);
        return [];
      },
      product: (state: any) => state.api.product,
      pending: (state: any) => state.api.pending,
      error: (state: any) => state.api.error,
    }),
  },
  methods: {
    ...mapActions(["getVariants", "getProduct", "deleteVariant"]),
  },
  watch: {
    $route(to, from) {
      (<any>this).loadData();
    },
  },
})
export default class VariantListView extends Vue {
      @Auth.Getter
    private isAuthenticated!: boolean;

    @Auth.Getter
    public authTokenDecoded!: ExtendedJwtPayload;

    @Auth.Getter
    public authUser!: string;

    @Auth.Getter
    public authGroups!: string[];

  getVariants!: (obj: any) => void;
  getProduct!: (obj: Params) => void;
  deleteVariant!: (obj: Params) => AxiosPromise;

  loadData!: () => void;

  isProduct?: boolean;

  filterChanged?: Function;
  filterChangedCallback(text: string) {
    this.getVariants({ params: { filter: text } });
  }

  data() {
    let obj = this;
    return {
      isProduct: false,
      rowClicked(record: VariantModel, index: number, event: Event) {
        router.push({
          name: "view-variant",
          params: { id: String(record?.id) },
        });
      },
      filterChanged: obj.filterChangedCallback,
      addClicked(event: Event) {
        router.push({ name: "new-variant" });
      },
      editClicked(record: VariantModel, index: number, event: Event) {
        router.push({
          name: "edit-variant",
          params: { id: String(record?.id) },
        });
      },
      deleteClicked(record: VariantModel, index: number, event: Event) {
        // perform delete on the API
        obj
          .deleteVariant({ params: { id: record.id } })
          .then(() => {
            // success deletion
            createAlert(AlertType.Success, `Variant ${record.id} deleted.`);
            // splice directly from the store state object
            (obj.isProduct
              ? <Array<VariantModel>>obj.$store.state.api.product.variants
              : <Array<VariantModel>>obj.$store.state.api.variants
            ).splice(index, 1);
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting variant ${record.id}: ${err}`
            );
          });
      },
      fields: [],
      loadData() {
        let pathArr = obj.$route.path.split("/");
        if (pathArr[pathArr.length - 2] == "products") {
          obj.isProduct = true;
          obj.filterChanged = undefined;
          obj.getProduct({ params: { id: obj.$route.params.id } });
        } else {
          obj.isProduct = false;
          obj.filterChanged = obj.filterChangedCallback;
          obj.getVariants({ params: { filter: "" } });
        }
      },
    };
  }

  mounted() {
    this.loadData();
  }
}
</script>
