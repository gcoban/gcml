package com.gcoban.gcml.estimation.kernel;

import Jama.Matrix;

public class KernelFunction {

	public static double Gaussian(Matrix instanceVector) {

		int d = instanceVector.getRowDimension();
		double distance = 0;
		for (int i = 0; i < d; i++) {
			distance += Math.pow(instanceVector.get(i, 0), 2);
		}

		return Math.pow(2 * Math.PI, -0.5 * d) * Math.pow(Math.E, -0.5 * distance);
	}
}