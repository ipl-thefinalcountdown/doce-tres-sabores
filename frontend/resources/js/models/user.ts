import ProjectModel from './project'

export interface UserAuthModel {
	username?: string;
	password?: string;
};

export default interface UserModel {
	username?: string;
	name?: string;
	address?: string;
	email?: string;
	phoneNumber?: string;
	projects?: Array<ProjectModel>;
};
