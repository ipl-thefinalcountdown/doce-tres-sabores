<template>
  <page-component>
    <div class="container">
      <b-row cols="1">
        <b-col>
          <item-details
            :items="items"
            :awaiting-items="pending.structure"
            :edit-clicked="editClicked"
            :delete-clicked="deleteClicked"
          >
          </item-details>
        </b-col>
        <b-col>
          <searchable-table
            :items="variants"
            :row-clicked="rowClicked"
          />
        </b-col>
      </b-row>
    </div>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import { AxiosPromise } from "axios";

import PageComponent from "../../components/Page.vue";
import ItemDetails from "../../components/item/ItemDetails.vue";
import SearchableTable from "../../components/SearchableTable.vue";

import { mapState, mapActions } from "vuex";

import { AlertType, createAlert } from "../../utils/alert";
import { deSnakeCase } from "../../utils/string";

import { Params } from "../../stores/api";
import router from "../../router";
import { MaterialType } from "../../models/material";
import StructureModel from "../../models/structure";
import VariantModel from "../../models/variant";

@Component({
  components: {
    PageComponent,
    ItemDetails,
    SearchableTable,
  },
  computed: mapState({
    variants: (state: any) => {
      if(state.api.structure.variants == undefined)
        return [];
      return state.api.structure.variants.map((v: VariantModel) => {
        return {
          id: v.id,
		name: v.name,
		product: v.productName,
		weff_p: v.weff_p,
		weff_n: v.weff_n,
		ar: v.ar,
		sigmaC: v.sigmaC,
		pp: v.pp,
        }
      });
    },
    items: (state: any) => {
      return [state.api.structure].map((s: StructureModel) => {
		  let mappedStructure = { ...s };

		delete mappedStructure["variants"];
		delete mappedStructure["materialId"];
        return {
          ...mappedStructure,
          materialName: deSnakeCase(MaterialType[<MaterialType>s.materialId] ?? ""),
        };
      });
    },
    clientUsername: (state: any) => state.api.project.clientUsername,
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getStructure", "deleteStructure"]),
  },
})
export default class StructureView extends Vue {
  getStructure!: (obj: Params) => void;
  deleteStructure!: (obj: Params) => AxiosPromise;

  structureId?: number | string;

  data() {
    let obj = this;
    return {
      editClicked() {
        router.push({name: 'edit-structure', params: {id: String(obj.structureId)}});
      },
      deleteClicked() {
        obj
          .deleteStructure({ params: { id: obj.structureId } })
          .then(() => {
            // success deletion
            router.push({name: `list-structures`});
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting structure ${obj.structureId}: ${err}`
            );
          });
      },
      rowClicked(record: VariantModel, index: number, event: Event) {
        router.push({
          name: "view-variant",
          params: { id: String(record?.id) },
        });
      },
    };
  }

  mounted() {
    this.structureId = this.$route.params.id;
    this.getStructure({ params: { id: this.structureId } });
  }
}
</script>
