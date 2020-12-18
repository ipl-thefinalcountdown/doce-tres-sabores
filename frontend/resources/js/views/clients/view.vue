<template>
  <page-component>
    <div class="container">
      <b-row cols="1">
        <b-col>
          <item-details
            :items="items"
            :awaiting-items="pending.project"
          >
          </item-details>
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
import UserModel from "../../models/user";

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
  },
  computed: mapState({
    items: (state: any) => {
      return [state.api.client].map((c: UserModel) => {
        return {
          username: c.username,
          name: c.name,
          address: c.address,
          email: c.email,
          phoneNumber: c.phoneNumber,
        };
      });
    },
    clientUsername: (state: any) => state.api.project.clientUsername,
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getClient"]),
  },
})
export default class ClientView extends Vue {
  getClient!: (obj: Params) => void;

  clientId?: number | string;

  data() {
    return {}
  }

  mounted() {
    this.clientId = this.$route.params.id;
    this.getClient({ params: { id: this.clientId } });
  }
}
</script>
