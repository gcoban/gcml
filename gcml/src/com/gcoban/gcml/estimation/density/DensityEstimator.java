package com.gcoban.gcml.estimation.density;

import Jama.Matrix;

import com.gcoban.gcml.estimation.kernel.KernelFunction;
import com.gcoban.gcml.helper.MatrixHelper;

public class DensityEstimator {

	/**
	 * Kernel estimator for class-conditional density The function will return p
	 * (meanVector | Ci = label) If the estiomator is needed without class
	 * condition or if labels are not available, just think like all labels are
	 * the same => set param:dataLabelVector and param:label according to this
	 * 
	 * @param data
	 * @param dataLabelVector
	 * @param x
	 * @param label
	 *            Class condition
	 * @param h
	 *            Window width
	 */
	public static double Kernel(Matrix data, Matrix dataLabelVector, Matrix x, double label, double h) {

		int d = data.getColumnDimension();
		int Nlabel = 0;

		int sum = 0;
		for (int i = 0; i < data.getRowDimension(); i++) {
			if (label == dataLabelVector.get(i, 0)) {
				Matrix instanceVector = MatrixHelper.getRowVector(data, i);
				sum += KernelFunction.Gaussian(x.minus(instanceVector).times(1.0 / h));
				Nlabel++;
			}
		}

		return (1.0 / (Nlabel * Math.pow(h, d))) * sum;
	}
}