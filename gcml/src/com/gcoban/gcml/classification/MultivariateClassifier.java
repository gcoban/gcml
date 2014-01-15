package com.gcoban.gcml.classification;

import Jama.Matrix;

public class MultivariateClassifier {

	public static class LinearDiscriminantParameters {

		public Matrix wi;
		public double wi0;
	}

	public static class QuadraticDiscriminantParameters {

		public Matrix Wi;
		public Matrix wi;
		public double wi0;
	}

	/**
	 * Calculates necessary parameters for LinearDiscriminant function. Should
	 * be called for every class(label) on training data i indicates class in
	 * parameters
	 * 
	 * @param PCi
	 *            Probability of class i
	 * @param mi
	 *            Mean vector of class i
	 * @param S
	 *            Shared covariance matrix
	 * @return
	 */
	public static LinearDiscriminantParameters getLinearDiscriminantParameters(final double PCi, final Matrix mi, final Matrix S) {

		LinearDiscriminantParameters parameters = new LinearDiscriminantParameters();

		// double det = S.det();
		Matrix S_inverse = S.inverse();
		parameters.wi = S_inverse.times(mi);
		parameters.wi0 = -0.5 * mi.transpose().times(S_inverse).times(mi).get(0, 0) + Math.log(PCi);

		return parameters;
	}

	/**
	 * Calculates necessary parameters for QuadraticDiscriminant function.
	 * Should be called for every class(label) on training data i indicates
	 * class in parameters
	 * 
	 * @param PCi
	 *            Probability of class i
	 * @param mi
	 *            Mean vector of class i
	 * @param Si
	 *            Covariance matrix of class is
	 * @return
	 */
	public static QuadraticDiscriminantParameters getQuadraticDiscriminantParameters(final double PCi, final Matrix mi, final Matrix Si) {

		QuadraticDiscriminantParameters parameters = new QuadraticDiscriminantParameters();

		double det = Si.det();
		Matrix Si_inverse = Si.inverse();
		parameters.Wi = Si_inverse.times(-0.5);
		parameters.wi = Si_inverse.times(mi);
		parameters.wi0 = -0.5 * mi.transpose().times(Si_inverse).times(mi).get(0, 0) + Math.log(PCi);
		parameters.wi0 += det == 0 ? 0 : -0.5 * Math.log(det);

		return parameters;
	}

	/**
	 * Calculates linear discriminant for classification. Should be called for
	 * every instance on verification or test data
	 * 
	 * @param parameters
	 * @param x
	 *            Data instance vector
	 * @return
	 */
	public static double getLinearDiscriminant(LinearDiscriminantParameters parameters, Matrix x) {

		return parameters.wi.transpose().times(x).get(0, 0) + parameters.wi0;
	}

	/**
	 * Calculates quadratic discriminant for classification. Should be called
	 * for every instance on verification or test data
	 * 
	 * @param parameters
	 * @param x
	 *            Data instance vector
	 * @return
	 */
	public static double getQuadraticDiscriminant(QuadraticDiscriminantParameters parameters, Matrix x) {

		return x.transpose().times(parameters.Wi).times(x).get(0, 0) + parameters.wi.transpose().times(x).get(0, 0) + parameters.wi0;
	}
}