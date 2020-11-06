package io.github.ipl.tfc.docetressabores.ejbs;

import io.github.ipl.tfc.docetressabores.entities.Variant;

import javax.ejb.Stateless;

@Stateless
public class SimulationBean {

	private static double EE = 210000000.0;
	private static double alfaLT = 0.21;


	public boolean simulateVariant(int nb, double LVao, int q, Variant variant) {
		boolean secure = true;

		double[] msd = momentosFletoresAtuantes(nb, LVao, q, variant);

		double lambda1 = Math.PI * Math.sqrt(EE / variant.getSigmaC());


		double mrd_p = momentoResistenteProduto(lambda1, variant.getWeff_p(), variant.getMcr_p().get(LVao));
		double mrd_n = momentoResistenteProduto(lambda1, variant.getWeff_n(), variant.getMcr_n().get(LVao));

		for (int i = 0; i < msd.length; i++) {
			double rs = Math.abs(msd[i]) / Math.abs(msd[i] >= 0 ? mrd_p : mrd_n);
			if(rs >= 1){
				System.out.println("Não verifica segurança na secção " + i + " da variante " + variant.getName() + " do produto " + variant.getProduct().getName());
				secure = false;
			}
		}
		return secure;
	}


	private double[] momentosFletoresAtuantes(int nb, double L, int q, Variant variant){

		double qt = q + variant.getPp();

		int ns = 2 * nb - 1;

		double[] msd = new double[ns];

		double qt_times_L_times_L = qt * L * L;

		switch(nb){
			case 1:
				msd[0] = 1.0 / 8.0 * qt_times_L_times_L;
				break;
			case 2:
				msd[0] = msd[2] = 9.0 / 128.0 * qt_times_L_times_L;
				msd[1] = 1.0 / 8.0 * qt_times_L_times_L;
				break;
			case 3:
				msd[0] = msd[4] = 2.0 / 25.0 * qt_times_L_times_L;
				msd[1] = msd[3] = - 1.0 / 10.0 * qt_times_L_times_L;
				msd[2] = 1.0 / 40.0 * qt_times_L_times_L;
				break;
			case 4:
				msd[0] = msd[6] = 121.0 / 1568.0 * qt_times_L_times_L;
				msd[1] = msd[5] = - 3.0 / 28.0 * qt_times_L_times_L;
				msd[2] = msd[4] = 57.0 / 1568.0 * qt_times_L_times_L;
				msd[3] = - 1.0 / 14.0 * qt_times_L_times_L;
		}

		return msd;
	}

	private double momentoResistenteProduto(double lambda1, double weff, double mcr) {

		double lambdaLT = Math.PI * Math.sqrt(EE * weff / mcr);
		double lambdaLTb = lambdaLT / lambda1;
		double tetaLT = alfaLT * (lambdaLTb - 0.2);
		double phiLT = (lambdaLTb * lambdaLTb + tetaLT + 1) / 2.0;
		double chiLT = (phiLT - Math.sqrt(phiLT * phiLT - lambdaLTb * lambdaLTb)) / (lambdaLTb * lambdaLTb);

		// mrd
		return chiLT * EE * weff;
	}

}
