import ProjectModel from './project'

export default interface UserModel {
	username?: string;
	name?: string;
	address?: string;
	email?: string;
	phoneNumber?: string;
	projects?: Array<ProjectModel>;
}
