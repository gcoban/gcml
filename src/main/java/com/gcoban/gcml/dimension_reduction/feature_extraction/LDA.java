package com.gcoban.gcml.dimension_reduction.feature_extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import Jama.Matrix;

import com.gcoban.gcml.estimation.Estimator;

public class LDA {

	public static Matrix run(Matrix data, Matrix dataLabelVector) {

		// Label indicates class in the comments
		// Find labels
		Set<Double> labelSet = new HashSet<>();
		for (int i = 0; i < dataLabelVector.getRowDimension(); i++)
			labelSet.add(dataLabelVector.get(i, 0));

		Map<Double, ArrayList<Integer>> rowIndicesForEachLabel = new HashMap<>();
		for (double label : labelSet)
			rowIndicesForEachLabel.put(label, new ArrayList<Integer>());
		for (int i = 0; i < data.getRowDimension(); i++) {
			double label = dataLabelVector.get(i, 0);
			rowIndicesForEachLabel.get(label).add(i);
		}

		// Create matrix for each label
		Map<Double, Matrix> matricesForEachLabel = new HashMap<>();
		for (double label : labelSet) {
			Matrix matrix = data.getMatrix(ArrayUtils.toPrimitive(rowIndicesForEachLabel.get(label).toArray(new Integer[0])), 0, data.getColumnDimension() - 1);
			matricesForEachLabel.put(label, matrix);
		}

		// Calculate mi = mean vectors for each label
		Map<Double, Matrix> meanVectorsForEachLabel = new HashMap<>();
		for (double label : labelSet) {
			Matrix meanVector = Estimator.mean(matricesForEachLabel.get(label));
			meanVectorsForEachLabel.put(label, meanVector);
		}

		// Calculate m = common mean vector
		Matrix meanVector = new Matrix(data.getColumnDimension(), 1);
		for (double label : labelSet)
			meanVector = meanVector.plus(meanVectorsForEachLabel.get(label));
		meanVector = meanVector.times(1.0 / labelSet.size());

		// Calculate within-class scatter matrices for each label
		Map<Double, Matrix> SiForEachLabel = new HashMap<>();
		for (double label : labelSet) {
			Matrix tempMatrix = new Matrix(matricesForEachLabel.get(label).getRowDimension(), matricesForEachLabel.get(label).getColumnDimension());

			// Matrix = Data Matrix - Mean Vector
			// Decrease each instance of data by meanVector into matrix
			for (int i = 0; i < tempMatrix.getRowDimension(); i++)
				for (int j = 0; j < tempMatrix.getColumnDimension(); j++)
					tempMatrix.set(i, j, matricesForEachLabel.get(label).get(i, j) - meanVector.get(j, 0));

			Matrix Si = tempMatrix.transpose().times(tempMatrix);
			SiForEachLabel.put(label, Si);
		}

		// Calculate SW = total within-class scatter
		Matrix SW = new Matrix(data.getColumnDimension(), data.getColumnDimension());
		for (double label : labelSet)
			SW = SW.plus(SiForEachLabel.get(label));

		// Calculate SB = between-class scatter
		Matrix SB = new Matrix(data.getColumnDimension(), data.getColumnDimension());
		for (double label : labelSet) {
			Matrix meanDifference = meanVectorsForEachLabel.get(label).minus(meanVector);
			double Ni = matricesForEachLabel.get(label).getRowDimension();
			SB = SB.plus(meanDifference.times(meanDifference.transpose()).times(Ni));
		}

		Matrix SW_inverse = null;
		try {
			SW_inverse = SW.inverse();
		} catch (Exception e) {
			// If SW is singular matrix, it is not invertible
			// Hence, use pseudoInverse instead of inverse
			// SW_inverse = MatrixHelper.pseudoInverse(SW);
		}

		Matrix solution = SW_inverse.times(SB);

		// Find the reduced dimension k, whose eigenvalue is non-zero
		// for each dimension
		int k = 2;
		// for (k = s.getRowDimension() - 1; k >= 0; k--)
		// {
		// if (s.get(k, k) != 0)
		// break;
		// }

		// Calculate, the full data matrix with new features, and return
		// its k dimension as reduced data matrix
		return data.times(solution).getMatrix(0, data.getRowDimension() - 1, 0, k);
	}
}