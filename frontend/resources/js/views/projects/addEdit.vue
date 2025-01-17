<template>
  <page-component>
    <div class="container">
      <item-edit v-if="itemLoaded" :on-submit="onSubmit" :on-reset="onReset">
        <form-field v-if="authGroups.includes('Designer')"
          label="Name" placeholder="Enter name" v-model="form.name" />
        <b-form-group v-if="authGroups.includes('Designer') && isEdit">
            <b-form-checkbox v-model="form.completed"
            :value="true"
            :unchecked-value="false"
          >Completed</b-form-checkbox>
        </b-form-group>
        <form-searchable-select v-if="!isEdit" label="Client" placeholder="Client Name" v-model="form.clientUsername" :options="clients"/>
        <b-form-group v-if="isEdit && authGroups.includes('Client')" label="Observations">
            <b-form-textarea
              id="observations"
              v-model="form.observations"
              placeholder="Enter something..."
              rows="3"
              max-rows="6"
            ></b-form-textarea>
        </b-form-group>
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

import ProjectModel from "../../models/project";
import UserModel from "../../models/user";

import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
	const Auth = namespace("auth");

@Component({
  components: {
    PageComponent,
    ItemEdit,
    FormField,
    FormSearchableSelect,
  },
  computed: mapState({
    project: (state: any) => state.api.project,
    clients: (state: any) => {
      return state.api.clients.map((c : UserModel) => {
        return {
          value: c.username,
          text: `${c.username} - ${c.name}`
        }
      })
    },
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions([
      "getProject",
      "getClients",
      "addProject",
      "updateProject"
    ]),
  },
})
export default class ProjectAddEditView extends Vue {
    @Auth.Getter
    private isAuthenticated!: boolean;

    @Auth.Getter
    public authTokenDecoded!: ExtendedJwtPayload;

    @Auth.Getter
    public authUser!: string;

    @Auth.Getter
    public authGroups!: string[];

  getClients!: (obj?: Params) => void;
  getProject!: (obj?: Params) => AxiosPromise;
  addProject!: (obj?: Params) => AxiosPromise;
  updateProject!: (obj?: Params) => AxiosPromise;

  project?: ProjectModel;
  form?: ProjectModel;
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
          obj.updateProject({
            params: {
              id: obj.project?.id
            },
            data: obj.form
          }).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on updating project ${obj.project?.id}: ${err}`
            );
          })
        } else {
          obj.addProject({data: {
            ...obj.form,
            designerUsername: obj.authUser
            }
          }).then(() => {
            // go back
            router.go(-1);
          }).catch((err)=> {
            createAlert(
              AlertType.Danger,
              `Error on adding project: ${err}`
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
    let projectId = this.$route.params.id;
    if(this.isEdit)
    {
      this.getProject({ params: { id: projectId } }).then(() => {
        this.form = {
          name: this.project?.name,
          completed: this.project?.completed,
          clientUsername: this.project?.clientUsername,
          observations: this.project?.observations,
          designerUsername: this.project?.designerUsername
        };

        this.itemLoaded = true;
      }).catch((err) => {
        createAlert(
          AlertType.Danger,
          `Error on fetching project ${projectId}: ${err}`
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
    this.getClients();
  }
}
</script>
