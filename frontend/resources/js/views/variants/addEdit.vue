<template>
  <page-component>
    <div class="container">
      <item-edit v-if="itemLoaded" :on-submit="onSubmit" :on-reset="onReset">
        <form-field label="Name" placeholder="Enter name" v-model="form.name" />
        <form-searchable-select label="Product" placeholder="Product Name" v-model="form.productId" :options="products"/>
        <b-row>
          <b-col><form-field type="number" step=any label="Weff +" placeholder="Enter Weff +" v-model="form.weff_p" /></b-col>
          <b-col><form-field type="number" step=any label="Weff -" placeholder="Enter Weff -" v-model="form.weff_n" /></b-col>
        </b-row>
        <b-row>
          <b-col><form-field type="number" step=any label="Area" placeholder="Enter Area" v-model="form.ar" /></b-col>
          <b-col><form-field type="number" step=any label="Sigma C" placeholder="Enter Sigma C" v-model="form.sigmaC" /></b-col>
          <b-col><form-field type="number" step=any label="PP" placeholder="Enter PP" v-model="form.pp" /></b-col>
        </b-row>
        <b-row>
          <b-col><form-maptable disable-busy-state valueType="number" valueStep=any keyType="number" keyStep=any label="Mcr +" v-model="form.mcr_p" :fields="['length', {key: 'mcr_p',label: 'Mcr +'}]" /></b-col>
          <b-col><form-maptable disable-busy-state valueType="number" valueStep=any keyType="number" keyStep=any label="Mcr -" v-model="form.mcr_n" :fields="['length', {key: 'mcr_n',label: 'Mcr -'}]" /></b-col>
        </b-row>
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
import FormMaptable from "../../components/form/FormMaptable.vue";
import FormSearchableSelect from "../../components/form/FormSearchableSelect.vue"
import SmallScrollableTable from "../../components/SmallScrollableTable.vue";

import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";

import router from "../../router"

import VariantModel from "../../models/variant";
import ProductModel from "../../models/product";

@Component({
  components: {
    PageComponent,
    ItemEdit,
    FormField,
    FormMaptable,
    FormSearchableSelect,
    SmallScrollableTable
  },
  computed: mapState({
    variant: (state: any) => state.api.variant,
    products: (state: any) => {
      return state.api.products.map((p : ProductModel) => {
        return {
          value: p.id,
          text: `${p.id} - ${p.name}`
        }
      })
    },
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions([
      "getVariant",
      "getProducts",
      "addVariant",
      "updateVariant"
    ]),
  },
})
export default class VariantAddEditView extends Vue {
  getProducts!: (obj?: Params) => void;
  getVariant!: (obj?: Params) => AxiosPromise;
  addVariant!: (obj?: Params) => AxiosPromise;
  updateVariant!: (obj?: Params) => AxiosPromise;

  variant?: VariantModel;
  form?: VariantModel;
  itemLoaded?: boolean;

  isEdit: boolean = false;

  data() {
    let obj = this;
    return {
      form: {},
      itemLoaded: false,
      onSubmit() {
        if(obj.isEdit)
        {
          obj.updateVariant({
            params: {
              id: obj.variant?.id
            },
            data: obj.form
          }).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on updating variant ${obj.variant?.id}: ${err}`
            );
          })
        } else {
          obj.addVariant({data: obj.form}).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on adding variant: ${err}`
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
    let variantId = this.$route.params.id;
    if(this.isEdit)
    {
      this.getVariant({ params: { id: variantId } }).then(() => {
        this.form = {
          // TODO: remove ID when backend is changed
          id: this.variant?.id,
          name: this.variant?.name,
          productId: this.variant?.productId,
          ar: this.variant?.ar,
          sigmaC: this.variant?.sigmaC,
          pp: this.variant?.pp,
          weff_p: this.variant?.weff_p,
          weff_n: this.variant?.weff_n,
          mcr_p: this.variant?.mcr_p,
          mcr_n: this.variant?.mcr_n,
        };

        this.itemLoaded = true;
      }).catch((err) => {
        createAlert(
          AlertType.Danger,
          `Error on fetching variant ${variantId}: ${err}`
        );
      })
    } else {
      this.form = {
        mcr_p: new Map<number, number>(),
        mcr_n: new Map<number, number>(),
      };
    }
  }

  mounted() {
    let pathArr = this.$route.path.split("/");
    if(pathArr[pathArr.length - 1] == "edit")
      this.isEdit = true;
    else
      this.itemLoaded = true;

    this.setEditFields();
    this.getProducts({params: { filter: ''}});
  }
}
</script>
