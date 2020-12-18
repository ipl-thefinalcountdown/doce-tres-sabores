<template>
  <page-component>
    <div class="container">
      <b-row cols="1">
        <b-col>
          <item-details
            :items="items"
            :awaiting-items="pending.project"
            :edit-clicked="editClicked"
            :delete-clicked="deleteClicked"
          >
            <template #cell(client)="data">
              <b-link :to="{ name: 'view-client', params: { id: clientUsername }}">{{ data.value }}</b-link>
            </template>
          </item-details>
        </b-col>
        <b-col>
          <searchable-table
            :items="structures"
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
import ProjectModel from "../../models/project";

import { AlertType, createAlert } from "../../utils/alert";
import { deSnakeCase } from "../../utils/string";

import { Params } from "../../stores/api";
import router from "../../router";
import { MaterialType } from "../../models/material";
import StructureModel from "../../models/structure";

@Component({
  components: {
    PageComponent,
    ItemDetails,
    SearchableTable,
  },
  computed: mapState({
    structures: (state: any) => {
      if(state.api.project.structures == undefined)
        return [];
      return state.api.project.structures.map((s: StructureModel) => {
        return {
          id: s.id,
          name: s.name,
          materialName: deSnakeCase(MaterialType[<MaterialType>s.materialId])
        }
      });
    },
    items: (state: any) => {
      return [state.api.project].map((p: ProjectModel) => {
        return {
          id: p.id,
          client: p.clientName,
          name: p.name,
        };
      });
    },
    clientUsername: (state: any) => state.api.project.clientUsername,
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getProject", "deleteProject"]),
  },
})
export default class ProjectView extends Vue {
  getProject!: (obj: Params) => void;
  deleteProject!: (obj: Params) => AxiosPromise;

  projectId?: number | string;

  data() {
    let obj = this;
    return {
      editClicked() {
        router.push({name: 'edit-project', params: {id: String(obj.projectId)}});
      },
      deleteClicked() {
        obj
          .deleteProject({ params: { id: obj.projectId } })
          .then(() => {
            // success deletion
            router.push({name: `list-projects`});
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting project ${obj.projectId}: ${err}`
            );
          });
      },
      rowClicked(record: StructureModel, index: number, event: Event) {
        router.push({
          name: "view-structure",
          params: { id: String(record?.id) },
        });
      },
    };
  }

  mounted() {
    this.projectId = this.$route.params.id;
    this.getProject({ params: { id: this.projectId } });
  }
}
</script>
