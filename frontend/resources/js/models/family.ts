import { MaterialType } from './material';
import ProductModel from './product';

export default interface FamilyModel {
	id?: number;
	name?: string;
	materialId?: MaterialType;
	products?: ProductModel[];
}
