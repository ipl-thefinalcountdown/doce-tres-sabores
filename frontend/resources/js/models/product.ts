import VariantModel from "./variant";

export default interface ProductModel {
	id?: number;
	name?: string;
	variants?: VariantModel[];
	familyId?: number;
}
