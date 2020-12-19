<template>
	<page-component>
		<b-container>
			<div class="col-md-12">
				<div class="card card-container">
				<form class="p-3" name="form" @submit.prevent="handleLogin">
					<div class="form-group">
						<label for="username">Username</label>
						<input
							v-model="user.username"
							type="text"
							class="form-control"
							name="username"
						/>
					</div>
					<div class="form-group">
						<label for="password">Password</label>
						<input
							v-model="user.password"
							type="password"
							class="form-control"
							name="password"
						/>
					</div>
					<div class="form-group">
						<button class="btn btn-primary btn-block">Login</button>
					</div>
				</form>
				</div>
			</div>
		</b-container>
	</page-component>
</template>

<script lang="ts">
	import Vue from "vue"
	import Component from "vue-class-component"

	import { namespace } from "vuex-class";
	const Auth = namespace("auth");

	import PageComponent from '../components/Page.vue'
	import {UserAuthModel} from '../models/user'

	@Component({
		components: {
			PageComponent
		}
	})
	export default class LoginView extends Vue {
		private user: UserAuthModel = { username: "", password: "" };

		private submitted: boolean = false;
		private successful: boolean = false;
		private message: string = "";

		@Auth.Getter
		private isAuthenticated!: boolean;

		@Auth.Action
		private makeAuthRequest!: (data: any) => Promise<any>;

		mounted() {
			if (this.isAuthenticated) {
				this.$router.push("/profile");
			}
		}

		handleLogin() {
			this.message = "";
			this.submitted = true;

			this.makeAuthRequest(this.user).then(
				(data) => {
					this.message = data.message;
					this.successful = true;
					this.$router.push("/");
				}, (error) => {
					this.message = error;
					this.successful = false;
				}
			);
		}
	}
</script>
