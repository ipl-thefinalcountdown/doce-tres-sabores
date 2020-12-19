<template>
  <page-component>
    <div class="container">
      <div class="justify-content-center">
        <searchable-table
          :items="items"
          :row-clicked="rowClicked"
          :filter-changed="(authGroups.includes('Designer')) ? filterChanged : undefined"
          :add-clicked="(authGroups.includes('Designer')) ? addClicked : undefined"
          :edit-clicked="editClicked"
          :delete-clicked="(authGroups.includes('Designer')) ? deleteClicked : undefined"
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
import ProjectModel from "../../models/project";
import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";
import { AxiosPromise } from "axios";

import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
	const Auth = namespace("auth");

@Component({
  components: {
    PageComponent,
    SearchableTable,
  },
  computed: {
    ...mapState({
      items(state: any) {
        if((<any>this).authGroups.includes('Client'))
        {
          if(state.api.user.projects == undefined) return [];
          return state.api.user.projects.map((p: ProjectModel) => {
            return {
              id: p.id,
              name: p.name
            };
          })
        } else {
          return state.api.projects.map((p: ProjectModel) => {
            return {
              id: p.id,
              name: p.name,
              client: p.clientName
            };
          });
        }

      },
      pending: (state: any) => state.api.pending,
      error: (state: any) => state.api.error,
    }),
  },
  methods: {
    ...mapActions(["getProjects", "deleteProject", "getUser"]),
  }
})
export default class ProjectListView extends Vue {
      @Auth.Getter
    private isAuthenticated!: boolean;

    @Auth.Getter
    public authTokenDecoded!: ExtendedJwtPayload;

    @Auth.Getter
    public authUser!: string;

    @Auth.Getter
    public authGroups!: string[];

  getProjects!: (obj: Params) => void;
  getUser!: (obj: Params) => void;
  deleteProject!: (obj: Params) => AxiosPromise;

  filterText?: string = "";

  data() {
    let obj = this;

    return {
      rowClicked(record: ProjectModel, index: number, event: Event) {
        router.push({name: 'view-project', params: {id: String(record?.id)}});
      },
      filterChanged(text: string) {
        obj.getProjects({ params: { filter: text } });
        obj.filterText = text;
      },
      addClicked(event: Event) {
        router.push({name: 'new-project'});
      },
      editClicked(record: ProjectModel, index: number, event: Event) {
        router.push({name: 'edit-project', params: {id: String(record?.id)}});
      },
      deleteClicked(record: ProjectModel, index: number, event: Event) {
        // perform delete on the API
        obj
          .deleteProject({ params: { id: record.id } })
          .then(() => {
            // success deletion
            createAlert(AlertType.Success, `Project ${record.id} deleted.`);
            // splice directly from the store state object
            (<Array<ProjectModel>>obj.$store.state.api.projects).splice(
              index,
              1
            );
          })
          .catch((err) => {
            // error on delete
            createAlert(
              AlertType.Danger,
              `Error deleting project ${record.id}: ${err}`
            );
          });
      },
    };
  }

  mounted() {
    if(this.authGroups.includes('Client')) {
      this.getUser({ params: { id: this.authUser } });
    } else {
      this.getProjects({ params: { filter: "" } });
    }
  }
}
</script>
