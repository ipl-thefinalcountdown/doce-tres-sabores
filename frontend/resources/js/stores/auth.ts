
"use strict"

import axios, { AxiosResponse } from 'axios';
import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators';
import { UserAuthModel } from '../models/user';

@Module({ namespaced: true })
class Auth extends VuexModule {
	public token: string = localStorage.getItem('user-token') || '';
	public status: string = '';

  @Mutation
  public setAuthRequest(): void {
	this.status = 'loading'
  }

	@Mutation
  public setAuthSuccess(token: string): void {
	this.status = 'success'
	this.token = token
	}

	@Mutation
	public setAuthError(): void {
		this.status = 'error'
	}

  @Action
  public makeAuthRequest(user: UserAuthModel): Promise<AxiosResponse> {
		return new Promise((resolve, reject) => {
			this.context.commit('setAuthRequest')
			axios({
				url: 'auth/login',
				data: `username=${user.username}&password=${user.password}`,
				headers: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				method: 'POST'
			})
			.then(resp => {
				const token = 'Bearer ' + resp.data.token
				localStorage.setItem('user-token', token)
				axios.defaults.headers.common['Authorization'] = token
				this.context.commit('setAuthSuccess', resp)
				//this.context.dispatch(USER_REQUEST)
				resolve(resp)
			})
			.catch(err => {
				this.context.commit('setAuthError', err)
				localStorage.removeItem('user-token')
				reject(err)
			})
		})
	}

	@Action
	public makeAuthLogout(): Promise<void> {
		return new Promise((resolve, reject) => {
			this.context.commit('makeAuthLogout')
			localStorage.removeItem('user-token')
			// remove the axios default header
			delete axios.defaults.headers.common['Authorization']
			resolve()
		})
	}

	get isAuthenticated(): boolean {
		return !!this.token
	}
	get authStatus(): string {
		return this.status
	}
}
export default Auth;
