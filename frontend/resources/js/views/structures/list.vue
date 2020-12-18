<template>
  <page-component>
    <div class="container">
      <div class="justify-content-center">
        <searchable-table
          :items="items"
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
import StructureModel from "../../models/project";
import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import { AxiosPromise } from "axios";

@Component({
  components: {
    PageComponent,
    SearchableTable,
  },
  computed: {
    ...mapState({
      items: (state: any) => {
        return state.api.structures.map((s: StructureModel) => {
          return {
            id: s.id,
            name: s.name
          };
        });
      },
      pending: (state: any) => state.api.pending,
      error: (state: any) => state.api.error,
    }),
  },
  methods: {
    ...mapActions(["getStructures", "deleteStructure"]),
  },
})
export default class StructureListView extends Vue {
  getStructures!: (obj: Params) => void;
  deleteStructure!: (obj: Params) => AxiosPromise;

  filterText?: string = "";

  data() {
    let obj = this;

    return {
      rowClicked(record: StructureModel, index: number, event: Event) {
        router.push({
          name: "view-structure",
          params: { id: String(record?.id) },
        });
      },
      filterChanged(text: string) {
        obj.getStructures({ params: { filter: text } });
        obj.filterText = text;
      },
      addClicked(event: Event) {
        router.push({ name: "new-structure" });
      },
      editClicked(record: StructureModel, index: number, event: Event) {
        router.push({
          name: "edit-structure",
          params: { id: String(record?.id) },
        });
      },
      deleteClicked(record: StructureModel, index: number, event: Event) {
        // perform delete on the API
        obj
          .deleteStructure({ params: { id: record.id } })
          .then(() => {
            // success deletion
            createAlert(AlertType.Success, `Structure ${record.id} deleted.`);
            // splice directly from the store state object
            (<Array<StructureModel>>obj.$store.state.api.structures).splice(
              index,
              1
            );
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting structure ${record.id}: ${err}`
            );
          });
      },
    };
  }

  mounted() {
    this.getStructures({ params: { filter: "" } });
  }
}
</script>
