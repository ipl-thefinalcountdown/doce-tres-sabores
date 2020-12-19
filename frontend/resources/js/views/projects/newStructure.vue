<template>
  <page-component>
    <div class="container">
      <item-edit v-if="itemLoaded" :on-submit="onSubmit">
        <form-searchable-select
          label="Structure to add"
          placeholder="Structure"
          v-model="form"
          :options="structures"
        />
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
import FormSearchableSelect from "../../components/form/FormSearchableSelect.vue";

import { AlertType, createAlert } from "../../utils/alert";

import { Params } from "../../stores/api";

import router from "../../router";

import ProjectModel from "../../models/project";
import UserModel from "../../models/user";
import StructureModel from "../../models/structure";

@Component({
  components: {
    PageComponent,
    ItemEdit,
    FormSearchableSelect,
  },
  computed: mapState({
    structures: (state: any) => {
	  if (state.api.project?.structures == undefined) return [];
	  let structureIds = state.api.project?.structures.map((s: StructureModel) => s.id);
      return state.api.structures.map((s: StructureModel) => {
        return {
          value: s.id,
          text: `${s.id} - ${s.name}`,
        };
      }).filter((i:any) => !structureIds.includes(i.value));
    },
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getStructures", "getProject", "addProjectStructure"]),
  },
})
export default class ProjectAddEditView extends Vue {
  getProject!: (obj?: Params) => AxiosPromise;
  getStructures!: (obj?: Params) => AxiosPromise;
  addProjectStructure!: (obj?: Params) => AxiosPromise;

  form?: number | string;
  itemLoaded?: boolean;

  isEdit?: boolean;

  data() {
    let obj = this;
    return {
      form: undefined,
      isEdit: false,
      itemLoaded: false,
      onSubmit() {
        obj
          .addProjectStructure({
            params: {
              id: obj.$route.params.id,
              param1: obj.form,
            },
          })
          .then((res) => {
            router.push({
              name: `view-project`,
              params: {
                id: obj.$route.params.id,
              },
            });
          })
          .catch((err) => {
            createAlert(
              AlertType.Danger,
              `Error adding structure ${obj.form} to the project: ${err}`
            );
          });
      },
    };
  }

  mounted() {
    this.getProject({ params: { id: this.$route.params.id } });
    this.getStructures({ params: { filter: "" } });
    this.itemLoaded = true;
  }
}
</script>
