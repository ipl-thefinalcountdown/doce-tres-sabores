<template>
  <div>
    <b-navbar toggleable="lg" variant="light">
      <b-navbar-brand to="/">Chantilly</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item :to="{name: 'list-products'}">Products</b-nav-item>
          <b-nav-item :to="{name: 'list-variants'}">Variants</b-nav-item>
          <b-nav-item :to="{name: 'list-projects'}">Projects</b-nav-item>
          <b-nav-item :to="{name: 'list-structures'}">Structures</b-nav-item>
          <b-nav-item :to="{name: 'simulate-structure'}">Simulation</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-form v-if="!isAuthenticated">
            <b-button size="sm" class="my-2 my-sm-0" :to="{name: 'login'}">Login</b-button>
          </b-nav-form>
          <b-nav-item-dropdown v-else right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <em>{{ authUser }}</em>
            </template>
            <b-dropdown-item href="#">Profile</b-dropdown-item>
            <b-dropdown-item @click.prevent="handleLogout()">Sign Out</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script lang="ts">
import Vue from "vue"
	import Component from "vue-class-component"

  import { namespace } from "vuex-class";
  import { ExtendedJwtPayload } from '../../stores/auth';
	const Auth = namespace("auth");

	import {UserAuthModel} from '../../models/user'
import router from "../../router";

	@Component({
		components: {}
  })
  export default class LoginView extends Vue {
		private user: UserAuthModel = { username: "", password: "" };

		private submitted: boolean = false;
		private successful: boolean = false;
		private message: string = "";

		@Auth.Getter
    private isAuthenticated!: boolean;

    @Auth.Getter
    public authTokenDecoded!: ExtendedJwtPayload;

    @Auth.Getter
    public authUser!: string;

    @Auth.Getter
    public authGroups!: string[];

		@Auth.Action
    private makeAuthLogout!: () => Promise<void>;

    mounted()
    {
      console.log(this.authTokenDecoded);
    }

		handleLogout() {
      this.makeAuthLogout().then(() => router.push('/login'))
		}
	}
</script>
