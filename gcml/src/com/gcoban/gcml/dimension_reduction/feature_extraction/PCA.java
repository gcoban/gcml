package com.gcoban.gcml.dimension_reduction.feature_extraction;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

import com.gcoban.gcml.estimation.Estimator;

public class PCA {

	public static Matrix run(Matrix data) {

		Matrix meanVector = Estimator.mean(data);
		Matrix covarianceMatrix = Estimator.covariance(data, meanVector);
		SingularValueDecomposition svd = covarianceMatrix.svd();
		Matrix u = svd.getU(); // Eigenvectors
		Matrix s = svd.getS(); // Eigenvalues

		// Find the reduced dimension k, whose eigenvalue is non-zero
		// for each dimension
		int k;
		for (k = s.getRowDimension() - 1; k >= 0; k--) {
			if (s.get(k, k) != 0)
				break;
		}

		// Delete mean vector from each row
		// so that tempMatrix = x - m
		Matrix tempMatrix = data.copy();
		for (int i = 0; i < tempMatrix.getRowDimension(); i++)
			for (int j = 0; j < tempMatrix.getColumnDimension(); j++)
				tempMatrix.set(i, j, tempMatrix.get(i, j) - meanVector.get(j, 0));

		// Calculate, the full data matrix with new features, and return
		// its k dimension as reduced data matrix
		// so that return z = (x - m) * W
		return tempMatrix.times(u).getMatrix(0, tempMatrix.getRowDimension() - 1, 0, k);
	}
}