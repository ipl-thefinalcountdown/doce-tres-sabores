import { MaterialType } from './material'
import VariantModel from './variant'

export default interface StructureModel {
	id?: number;
	materialId?: MaterialType;
	name?: string;
	beamAmount?: number;
	beamLength?: number;
	beamImposedLoad?: number;
	beamSpacing?: number;
	maximumHeight?: number;
	variants?: VariantModel[];
}
