import ProjectModel from './project'

export default interface ClientModel {
	id?: number;
	name?: string;
	address?: string;
	email?: string;
	phoneNumber?: string;
	projects?: Array<ProjectModel>;
}
