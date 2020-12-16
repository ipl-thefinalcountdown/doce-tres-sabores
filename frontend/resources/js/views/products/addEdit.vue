<template>
  <page-component>
    <div class="container">
      <item-edit v-if="itemLoaded" :on-submit="onSubmit" :on-reset="onReset">
        <form-field label="Name" placeholder="Enter name" v-model="form.name" />
        <form-searchable-select v-if="!isEdit" label="Family" placeholder="Select a family" v-model="form.familyId" :options="families"/>
      </item-edit>
      <div v-else class="text-center text-secondary my-2">
        <b-spinner class="align-middle"></b-spinner>
        <strong>Loading...</strong>
      </div>
    </div>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import { mapState, mapActions } from "vuex";
import { AxiosPromise } from "axios";

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
    ItemEdit,
    FormField,
    FormSearchableSelect
  },
  computed: mapState({
    product: (state: any) => state.api.product,
    families: (state: any) => {
      return state.api.families.map((c : FamilyModel) => {
        return {
          value: c.id,
          text: `${c.id} - ${c.name}`
        }
      })
    },
    pending: (state: any) => state.api.pending,
    error: (state: any) => state.api.error,
  }),
  methods: {
    ...mapActions([
      "getProduct",
      "getFamilies",
      "addProduct",
      "updateProduct"
    ]),
  },
})
export default class ProductAddEditView extends Vue {
  getFamilies!: (obj?: Params) => void;
  getProduct!: (obj?: Params) => AxiosPromise;
  addProduct!: (obj?: Params) => AxiosPromise;
  updateProduct!: (obj?: Params) => AxiosPromise;

  product?: ProductModel;
  form?: ProductModel;
  itemLoaded?: boolean;

  isEdit?: boolean;

  data() {
    let obj = this;
    return {
      form: {},
      isEdit: false,
      itemLoaded: false,
      onSubmit() {
        if(obj.isEdit)
        {
          obj.updateProduct({
            params: {
              id: obj.product?.id
            },
            data: obj.form
          }).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on updating product ${obj.product?.id}: ${err}`
            );
          })
        } else {
          obj.addProduct({data: obj.form}).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on adding product: ${err}`
            );
          })
        }
      },
      onReset(ev: Event) {
        ev.preventDefault();
        obj.setEditFields();
      },
    };
  }

  setEditFields() {
    let productId = this.$route.params.id;
    if(this.isEdit)
    {
      this.getProduct({ params: { id: productId } }).then(() => {
        this.form = {
          // TODO: remove ID when backend is changed
          id: this.product?.id,
          name: this.product?.name,
          familyId: this.product?.familyId
        };

        this.itemLoaded = true;
      }).catch((err) => {
        createAlert(
          AlertType.Danger,
          `Error on fetching project ${productId}: ${err}`
        );
      })
    }
  }

  mounted() {
    let pathArr = this.$route.path.split("/");
    if(pathArr[pathArr.length - 1] == "edit")
      this.isEdit = true;
    else
      this.itemLoaded = true;

    this.setEditFields();
    this.getFamilies();
  }
}
</script>
