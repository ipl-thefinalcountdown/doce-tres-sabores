import ProductModel from './product';

export default interface FamilyModel {
	id?: number;
	name?: string;
	products?: ProductModel[];
}
