
"use strict"

import axios, { AxiosResponse } from 'axios';
import { VuexModule, Module, Mutation, Action } from 'vuex-module-decorators';
import { UserAuthModel } from '../models/auth';
import jwtDecode from 'jwt-decode';
import { JwtPayload } from 'jwt-decode';

export interface ExtendedJwtPayload extends JwtPayload {
	groups?: string[]
}

@Module({ namespaced: true })
export class Auth extends VuexModule {
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

	@Mutation
	public setAuthLogout(): void {
		this.status = '';
		this.token = '';
		}

  @Action
  public makeAuthRequest(user: UserAuthModel): Promise<AxiosResponse> {
		return new Promise((resolve, reject) => {
			this.context.commit('setAuthRequest')
			axios({
				url: 'auth/login',
				data: `username=${encodeURI(user.username || '')}&password=${encodeURI(user.password || '')}`,
				headers: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				method: 'POST'
			})
			.then(resp => {
				const token = resp.data.token
				localStorage.setItem('user-token', token)
				axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
				this.context.commit('setAuthSuccess', token)
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
			this.context.commit('setAuthLogout')
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

	get authTokenDecoded(): ExtendedJwtPayload {
		if (this.token == undefined || this.token == '') return {};
		return jwtDecode(this.token);
	}

	get authUser(): string {
		return this.authTokenDecoded?.sub || '';
	}

	get authGroups(): string[] {
		return this.authTokenDecoded?.groups || [];
	}
}

export default Auth;
