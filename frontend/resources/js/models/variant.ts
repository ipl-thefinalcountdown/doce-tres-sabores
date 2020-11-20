export default interface VariantModel {
	code?: number;
	name?: string;
	productName?: string;
	weff_p?: number;
	weff_n?: number;
	ar?: number;
	sigmaC?: number;
	pp?: number;
	mcr_p?: Map<number, number>;
	mcr_n?: Map<number, number>;
}
