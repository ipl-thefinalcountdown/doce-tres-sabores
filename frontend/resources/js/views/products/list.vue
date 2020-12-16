<template>
  <page-component>
    <div class="container">
      <b-row v-if="isFamily && family.name != undefined">
        <b-col
          ><h5>{{ family.name }}</h5></b-col
        >
      </b-row>
      <div class="justify-content-center">
        <searchable-table
          :items="isFamily ? familyItems : items"
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
import Vue from "vue";
import Component from "vue-class-component";
import { mapState, mapActions } from "vuex";

import PageComponent from "../../components/Page.vue";
import SearchableTable from "../../components/SearchableTable.vue";

import router from "../../router";
import ProductModel from "../../models/product";
import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import { AxiosPromise } from "axios";

var productMap = (p: ProductModel) => {
  return {
    id: p.id,
    name: p.name,
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
        return state.api.products.map(productMap);
      },
      familyItems: (state: any) => {
        if (state.api?.family?.products != undefined)
          return state.api.family.products.map(productMap);
        return [];
      },
      family: (state: any) => state.api.family,
      pending: (state: any) => state.api.pending,
      error: (state: any) => state.api.error,
    }),
  },
  methods: {
    ...mapActions(["getProducts", "getFamily", "deleteProduct"]),
  },
  watch: {
    $route(to, from) {
      (<any>this).loadData();
    },
  },
})
export default class ProjectListView extends Vue {
  getProducts!: (obj: Params) => void;
  getFamily!: (obj: Params) => void;
  deleteProduct!: (obj: Params) => AxiosPromise;

  loadData!: () => void;

  isFamily?: boolean;

  filterChanged?: Function;

  filterChangedCallback(text: string) {
    this.getProducts({ params: { filter: text } });
  }

  data() {
    let obj = this;

    return {
      isFamily: false,
      rowClicked(record: ProductModel, index: number, event: Event) {
        router.push({
          name: "view-product",
          params: { id: String(record?.id) },
        });
      },
      filterChanged: obj.filterChangedCallback,
      addClicked(event: Event) {
        router.push({ name: "new-product" });
      },
      editClicked(record: ProductModel, index: number, event: Event) {
        router.push({
          name: "edit-product",
          params: { id: String(record?.id) },
        });
      },
      deleteClicked(record: ProductModel, index: number, event: Event) {
        // perform delete on the API
        obj
          .deleteProduct({ params: { id: record.id } })
          .then(() => {
            // success deletion
            createAlert(AlertType.Success, `Product ${record.id} deleted.`);
            // splice directly from the store state object
            (obj.isFamily
              ? <Array<ProductModel>>obj.$store.state.api.family.products
              : <Array<ProductModel>>obj.$store.state.api.products
            ).splice(index, 1);
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting product ${record.id}: ${err}`
            );
          });
      },
      loadData() {
        let pathArr = obj.$route.path.split("/");
        if (pathArr[pathArr.length - 2] == "families") {
          obj.isFamily = true;
          obj.filterChanged = undefined;
          obj.getFamily({ params: { id: obj.$route.params.id } });
        } else {
          obj.isFamily = false;
          obj.filterChanged = obj.filterChangedCallback;
          obj.getProducts({ params: { filter: "" } });
        }
      },
    };
  }

  mounted() {
    this.loadData();
  }
}
</script>
