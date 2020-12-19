<template>
  <page-component>
    <div class="container">
      <b-row cols="1">
        <b-col>
          <item-details
            :items="items"
            :awaiting-items="pending.user"
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

  import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
	const Auth = namespace("auth");


@Component({
  components: {
    PageComponent,
    ItemDetails,
  },
  computed: mapState({
    items: (state: any) => {
      return [state.api.user].map((c: UserModel) => {
        return {
          username: c.username,
          name: c.name,
          address: c.address,
          email: c.email,
          phoneNumber: c.phoneNumber,
        };
      });
    },
    pending: (state: any) => state.api.pending,
  }),
  methods: {
    ...mapActions(["getUser"]),
  },
})
export default class ProfileView extends Vue {
  getUser!: (obj: Params) => void;

  @Auth.Getter
private isAuthenticated!: boolean;

@Auth.Getter
public authTokenDecoded!: ExtendedJwtPayload;

@Auth.Getter
public authUser!: string;

@Auth.Getter
public authGroups!: string[];


  data() {
    return {}
  }

  mounted() {
    this.getUser({ params: { id: this.authUser } });
  }
}
</script>
