<template>
  <page-component>
    <div class="container">
      <div v-if="mode == 'edit' || mode == 'new'" class="d-flex flex-row-reverse bd-highlight">
        <b-button class="right" variant="warning" @click="onSimulate">Simulate</b-button>
      </div>
      <item-edit v-if="itemLoaded" :on-submit="onSubmit" :on-reset="onReset">
        <b-row v-if="mode == 'edit' || mode == 'new'">
          <b-col>
            <form-field label="Name" placeholder="Enter name" v-model="form.name" />
          </b-col>
        </b-row>

        <b-row cols="1" cols-sm="1" cols-lg="3">
          <b-col
            ><form-field
              type="number" step=1
              label="Beam Amount"
              placeholder="Enter beam amount"
              v-model="form.beamAmount"
          /></b-col>
          <b-col
            ><form-field
              type="number" step=any
              label="Beam Length"
              placeholder="Enter beam length"
              v-model="form.beamLength"
          /></b-col>
          <b-col
            ><form-field
              type="number" step=1
              label="Beam Imposed Load"
              placeholder="Enter beam imposed load"
              v-model="form.beamImposedLoad"
          /></b-col>
        </b-row>

        <b-row cols="1">
          <b-col
            ><form-searchable-select
              label="Material type"
              placeholder="Material type"
              v-model="form.materialId"
              :options="materials"
          /></b-col>
        </b-row>
        <b-row v-if="mode == 'edit' || mode == 'new'">
          <b-col v-if="subTypes.includes('maximumHeight')">
            <form-field
                type="number" step=1
                label="Maximum Height"
                placeholder="Enter maximum height"
                v-model="form.maximumHeight"
            />
          </b-col>
          <b-col v-if="subTypes.includes('beamSpacing')">
            <form-field
                type="number" step=1
                label="Beam Spacing"
                placeholder="Enter beam spacing"
                v-model="form.beamSpacing"
            />
          </b-col>
        </b-row>

        <b-row>
          <b-col
            ><form-searchable-multiselect
              label="Variants"
              placeholder="Variant"
              v-model="selectedVariants"
              :options="variants"
          /></b-col>
        </b-row>


      </item-edit>
      <div v-else class="text-center text-secondary my-2">
        <b-spinner class="align-middle"></b-spinner>
        <strong>Loading...</strong>
      </div>
      <b-row v-if="results">
        <b-col>
          <testable-table :items="resultData" />
        </b-col>
      </b-row>
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
import FormSearchableSelect from "../../components/form/FormSearchableSelect.vue";
import FormSearchableMultiselect from "../../components/form/FormSearchableMultiselect.vue";
import TestableTable from "../../components/TestableTable.vue";

import { AlertType, createAlert } from "../../utils/alert";
import { deSnakeCase } from "../../utils/string";

import { Params } from "../../stores/api";

import router from "../../router";

import ProductModel from "../../models/product";
import FamilyModel from "../../models/family";
import { MaterialType } from "../../models/material";
import VariantModel from "../../models/variant";
import StructureModel from "../../models/structure";

var variantMap = (v: VariantModel) => {
    return {
      value: v.id,
      text: `${v.name}`,
    };
  };

@Component({
  components: {
    PageComponent,
    ItemEdit,
    FormField,
    FormSearchableSelect,
    FormSearchableMultiselect,
    TestableTable
  },
  computed: {
    ...mapState({
      originalVariants: (state: any) => state.api.variants,
      structure: (state: any) => state.api.structure,
      variants: (state: any) => {
        return state.api.variants.map(variantMap);
      },
      simulation: (state: any ) => state.api.simulation,
      pending: (state: any) => state.api.pending,
      error: (state: any) => state.api.error,
    }),
    resultData: function() {
      let simulationIds = (<any>this).simulation.map((v: VariantModel) => v.id);
      let variantIds = (<any>this).selectedVariants.map((v: any) => v.value);
      return (<any>this).originalVariants
        .filter((v: VariantModel) => variantIds.includes(v.id))
        .map((v: VariantModel) => {
        return {
          id: v.id,
          name: v.name,
          product: v.productName,
          weff_p: v.weff_p,
          weff_n: v.weff_n,
          ar: v.ar,
          sigmaC: v.sigmaC,
          pp: v.pp,
          valid: simulationIds.includes(v.id)
        }
      })
    },
    materials: () =>
      Object.keys(MaterialType)
        .filter((key: any) => isNaN(key))
        .map((key: any) => {
          return {
            value: MaterialType[key],
            text: `${deSnakeCase(key)}`,
          };
        }),
  },
  methods: {
    ...mapActions([
      "getVariantsByType",
      "requestSimulation",
      "updateStructure",
      "addStructure",
      "getStructure"]),
  },
  watch: {
    'form.materialId': function() {
      let obj = (<any>this);
      if(obj.editIgnoreFlag && obj.mode == 'edit')
        obj.editIgnoreFlag = false;
      else
        obj.selectedVariants = [];
      if(obj.form.materialId != undefined)
        obj.getVariantsByType({params: { type: obj.form.materialId, filter: '' }});
      switch(obj.form.materialId)
      {
        case MaterialType.SLAB:
          obj.subTypes = ['maximumHeight'];
          break;
        case MaterialType.LIGHT_STEEL:
          obj.subTypes = ['beamSpacing'];
          break;
        default:
          obj.subTypes = [];
          break;
      }
    },
    selectedVariants: function() {
      let obj = (<any>this);
      obj.results = false;
    },
    $route(to, from) {
      let obj = (<any>this);

      // clean simulation residue
      obj.results = false;
      obj.form = {};
      obj.selectedVariants = [];

      obj.loadData();
    }
  }
})
export default class AddEditSimulateStructureView extends Vue {
  getVariantsByType!: (obj?: Params) => AxiosPromise;
  requestSimulation!: (obj?: Params) => AxiosPromise;
  getStructure!: (obj?: Params) => AxiosPromise;
  updateStructure!: (obj?: Params) => AxiosPromise;
  addStructure!: (obj?: Params) => AxiosPromise;

  loadData!: () => void;
  onSimulate!: () => void;

  structure?: StructureModel;
  originalVariants?: Array<VariantModel>;
  selectedVariants?: any;
  form?: StructureModel;
  itemLoaded?: boolean;
  subTypes?: Array<string>;
  editIgnoreFlag?: boolean;

  mode?: string;
  results?: boolean;

  resolvedForm() {
    let obj = this;
    return {
      ...(() => {
        if(obj.form?.name != undefined) return {
            name: obj.form?.name
          };
        else return {};
      })(),
      beamAmount: new Number(obj.form?.beamAmount),
      beamLength: new Number(obj.form?.beamLength),
      beamImposedLoad: new Number(obj.form?.beamImposedLoad),
      ...(() => {
        if(obj.subTypes?.includes('beamSpacing')) return {
            beamSpacing: new Number(obj.form?.beamSpacing)
          };
        else return {};
      })(),
      ...(() => {
        if(obj.subTypes?.includes('maximumHeight')) return {
            maximumHeight: new Number(obj.form?.maximumHeight)
          };
        else return {};
      })(),
      materialId: obj.form?.materialId,
      variants: this.originalVariants?.filter((v: VariantModel) => {
        let sVariantsId = this.selectedVariants.map((s : any) => s.value);
        return sVariantsId.includes(v.id)
      })
    };
  }

  setEditFields() {
    let structureId = this.$route.params.id;
    if(this.mode == 'edit')
    {
      this.getStructure({ params: { id: structureId } }).then(() => {
        this.form = {
          id: this.structure?.id,
          name: this.structure?.name,
          beamAmount: this.structure?.beamAmount,
          beamLength: this.structure?.beamLength,
          beamImposedLoad: this.structure?.beamImposedLoad,
          ...(() => {
            if(this.structure?.materialId == MaterialType.LIGHT_STEEL) return {
                beamSpacing: this.structure?.beamSpacing
              };
            else return {};
          })(),
          ...(() => {
            if(this.structure?.materialId == MaterialType.SLAB) return {
                maximumHeight: this.structure?.maximumHeight
              };
            else return {};
          })(),
          materialId: this.structure?.materialId,
        };

        this.selectedVariants = this.structure?.variants?.map(variantMap);
        this.editIgnoreFlag = true;
        this.itemLoaded = true;
      }).catch((err) => {
        createAlert(
          AlertType.Danger,
          `Error on fetching structure ${structureId}: ${err}`
        );
      })
    }
  }

  data() {
    let obj = this;
    return {
      form: {},
      editIgnoreFlag: false,
      selectedVariants: [],
      mode: '',
      itemLoaded: false,
      results: false,
      subTypes: [],
      onSimulate() {
        obj.requestSimulation({data: obj.resolvedForm()});
        obj.results = true
      },
      onSubmit() {
        switch(obj.mode)
        {
          case 'simulation':
            obj.onSimulate();
            break;
          case 'edit':
            obj.updateStructure({
              params: {
                id: obj.structure?.id
              },
              data: obj.resolvedForm()
            }).then(() => {
              // go back
              router.go(-1);
            }).catch((err)=> {
              createAlert(
                AlertType.Danger,
                `Error on updating structure ${obj.structure?.id}: ${err}`
              );
            })
            break;
          case 'new':
            obj.addStructure({data: obj.resolvedForm()}).then(() => {
              // go back
              router.go(-1);
            }).catch((err)=> {
              createAlert(
                AlertType.Danger,
                `Error on adding structure: ${err}`
              );
            })
        }
      },
      onReset(ev: Event) {
        ev.preventDefault();
      },
      loadData() {
        let pathArr = obj.$route.path.split("/");
        obj.mode = pathArr[pathArr.length - 1];
        if(obj.mode == 'edit')
          obj.setEditFields();
        else
          this.itemLoaded = true;
      }
    };
  }

  mounted() {
    this.loadData();
  }
}
</script>
