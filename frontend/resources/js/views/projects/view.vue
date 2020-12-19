<template>
  <page-component>
    <div class="container">
      <b-row cols="1">
        <b-col>
          <b-row>
            <b-col>
              <b-row>
                <b-col><p>Documents</p></b-col>
                <b-col>
                  <div class="d-flex flex-row-reverse bd-highlight">
                    <div class="pl-3">
                      <b-button
                        :to="{name: 'project-upload-files'}"
                        size="sm"
                        variant="outline-secondary"
                      >
                        Upload
                      </b-button>
                    </div>
                  </div>
                </b-col>
              </b-row>
              <small-file-table
                :items="documents"
                :download-clicked="downloadClicked"
                :delete-clicked="deleteDocumentClicked"
              />
            </b-col>
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
          </b-row>
        </b-col>
        <b-col>
          <searchable-table
            :items="structures"
            :row-clicked="rowClicked"
            :add-clicked="addStructureClicked"
            :delete-clicked="deleteStructureClicked"
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
import SmallFileTable from "../../components/SmallFileTable.vue";

import { mapState, mapActions } from "vuex";
import ProjectModel from "../../models/project";
import DocumentModel from "../../models/document";

import { AlertType, createAlert } from "../../utils/alert";
import { deSnakeCase } from "../../utils/string";

import { Params } from "../../stores/api";
import router from "../../router";
import { MaterialType } from "../../models/material";
import StructureModel from "../../models/structure";
import FileDownload from 'js-file-download';

@Component({
  components: {
    PageComponent,
    ItemDetails,
    SearchableTable,
    SmallFileTable
  },
  computed: mapState({
    documents: (state: any) => {
      if(state.api.project.documents == undefined)
        return [];
      return state.api.project.documents;
    },
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
    ...mapActions([
      "getProject",
      "deleteProject",
      "getProjectDocument",
      "deleteDocument",
      "deleteProjectStructure"]),
  },
})
export default class ProjectView extends Vue {
  getProject!: (obj: Params) => void;
  deleteProject!: (obj: Params) => AxiosPromise;
  getProjectDocument!: (obj: Params) => AxiosPromise;
  deleteDocument!:(obj: Params) => AxiosPromise;
  deleteProjectStructure!:(obj: Params) => AxiosPromise;

  projectId?: number | string;

  data() {
    let obj = this;
    return {
      editClicked() {
        router.push({name: 'edit-project', params: {id: String(obj.projectId)}});
      },
      deleteDocumentClicked(record: DocumentModel, index: number, event: Event) {
        obj.deleteDocument({params: {id: record.id}})
        .then(() => {
          createAlert(AlertType.Success, `Document ${record.id} deleted.`);

          (<Array<DocumentModel>>obj.$store.state.api.project.documents).splice(
            index,
            1
          );
        }).catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting document ${record.id}: ${err}`
            );
          });
      },
      downloadClicked(record: DocumentModel, index: number, event: Event) {
        obj.getProjectDocument({params: {id: obj.projectId, param1: record.id}})
        .then((response) => {
            FileDownload(response.data, record.filePath || 'file');
        });
      },
      addStructureClicked() {
        router.push({
          name: "project-add-structure",
          params: { id: String(obj.projectId) },
        });
      },
      deleteStructureClicked(record: StructureModel, index: number, event: Event) {
        obj.deleteProjectStructure({ params: {
          id: obj.projectId,
          param1: record.id
        } }).then(() => {
            // success deletion
            createAlert(AlertType.Success, `Structure ${record.id} deleted.`);
            // splice directly from the store state object
            (<Array<StructureModel>>obj.$store.state.api.project.structures).splice(index, 1);
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting structure ${record.id}: ${err}`
            );
          });
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
