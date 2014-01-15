package com.gcoban.gcml.distance;

import Jama.Matrix;

public class EuclideanDistance {

	public static double calc(Matrix first, Matrix second) {

		if (first.getRowDimension() != second.getRowDimension())
			throw new IllegalArgumentException("Length of instance vectors should be equal.");

		int length = first.getRowDimension();

		double distance = 0;
		for (int i = 0; i < length; i++) {
			distance += Math.pow(first.get(i, 0) - second.get(i, 0), 2);
		}

		return Math.sqrt(distance);
	}
}