package com.gcoban.gcml.distance;

import Jama.Matrix;

public class HammingDistance {

	public static double hamming(Matrix first, Matrix second) {

		if (first.getRowDimension() != second.getRowDimension())
			throw new IllegalArgumentException("Length of instance vectors should be equal.");

		int sum = 0;
		for (int i = 0; i < first.getRowDimension(); i++) {
			if (first.get(i, 0) != second.get(i, 0)) {
				sum++;
			}
		}
		return sum;
	}
}