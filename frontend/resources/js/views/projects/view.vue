<template>
  <page-component>
    <div class="container">
      <item-details
        :items="items"
        :awaiting-items="pending.project"
        :edit-clicked="editClicked"
        :delete-clicked="deleteClicked"
      >
        <template #cell(client)="data">
          <b-link :to="{ name: 'view-user', params: { id: clientId }}">{{ data.value }}</b-link>
        </template>
      </item-details>
    </div>
  </page-component>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";

import { AxiosPromise } from "axios";

import PageComponent from "../../components/Page.vue";
import ItemDetails from "../../components/item/ItemDetails.vue";

import { mapState, mapActions } from "vuex";
import ProjectModel from "../../models/project";

import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import router from "../../router";

@Component({
  components: {
    PageComponent,
    ItemDetails,
  },
  computed: mapState({
    items: (state: any) => {
      return [state.api.project].map((p: ProjectModel) => {
        return {
          id: p.id,
          client: p.clientName,
          name: p.name,
        };
      });
    },
    clientId: (state: any) => state.api.project.clientId,
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
            router.push(`list-projects`);
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting project ${obj.projectId}: ${err}`
            );
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
