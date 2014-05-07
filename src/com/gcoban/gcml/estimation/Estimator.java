package com.gcoban.gcml.estimation;

import java.util.HashMap;
import java.util.Map;

import Jama.Matrix;

import com.gcoban.gcml.helper.MatrixHelper;

public class Estimator {

	/**
	 * Calculates average mean vector of data
	 * 
	 * @param data
	 * @return
	 */
	public static Matrix mean(final Matrix data) {

		Matrix meanVector = new Matrix(data.getColumnDimension(), 1);

		// Calculate sum of each instance of data into meanVector
		for (int i = 0; i < data.getRowDimension(); i++) {
			for (int j = 0; j < data.getColumnDimension(); j++) {
				meanVector.set(j, 0, meanVector.get(j, 0) + data.get(i, j));
			}
		}

		// Calculate average by dividing row count
		return meanVector.times(1.0 / data.getRowDimension());
	}

	/**
	 * Calculates average mean vectors for each label
	 * 
	 * @param data
	 * @param dataLabelVector
	 *            Labels for each instance in data
	 * @return
	 */
	public static Map<Double, Matrix> means(final Matrix data, final Matrix dataLabelVector) {

		Map<Double, Matrix> means = new HashMap<>();
		Map<Double, Integer> instanceCountForEachLabel = new HashMap<>();

		// Sum of instances
		for (int i = 0; i < data.getRowDimension(); i++) {
			double label = dataLabelVector.get(i, 0);

			// MeanVector for this label
			Matrix mean = means.get(label);

			// The instance which will be added to sum of mean
			Matrix rowVector = MatrixHelper.getRowVector(data, i);

			// If meanVector is null, sum will be equal to instance directly,
			// otherwise it will be total of them
			Matrix sum = mean == null ? rowVector : mean.plus(rowVector);
			means.put(label, sum);

			int instanceCount = mean == null ? 1 : instanceCountForEachLabel.get(label) + 1;
			instanceCountForEachLabel.put(label, instanceCount);
		}

		for (double label : means.keySet()) {
			Matrix mean = means.get(label).times(1.0 / instanceCountForEachLabel.get(label));
			means.put(label, mean);
		}

		return means;
	}

	/**
	 * Calculates covariance matrix of data
	 * 
	 * @param data
	 * @return
	 */
	public static Matrix covariance(final Matrix data) {

		return covariance(data, mean(data));
	}

	/**
	 * Calculates covariance matrix of data
	 * 
	 * @param data
	 * @param meanVector
	 *            Mean vector of data
	 * @return
	 */
	public static Matrix covariance(final Matrix data, final Matrix meanVector) {

		Matrix tempMatrix = new Matrix(data.getRowDimension(), data.getColumnDimension());

		// Matrix = Data Matrix - Mean Vector
		// Decrease each instance of data by meanVector into matrix
		for (int i = 0; i < tempMatrix.getRowDimension(); i++)
			for (int j = 0; j < tempMatrix.getColumnDimension(); j++)
				tempMatrix.set(i, j, data.get(i, j) - meanVector.get(j, 0));

		// Covariance Matrix = Tranpose(Matrix) * Matrix
		Matrix covarianceMatrix = tempMatrix.transpose().times(tempMatrix);
		// .times(1.0 / tempMatrix.getRowDimension());
		return covarianceMatrix;
	}

	/**
	 * Calculates variance vector of data
	 * 
	 * @param data
	 * @return
	 */
	public static Matrix variance(final Matrix data) {

		return variance(data, mean(data));
	}

	/**
	 * Calculates variance vector of data
	 * 
	 * @param data
	 * @param meanVector
	 *            Mean vector of data
	 * @return
	 */
	public static Matrix variance(final Matrix data, final Matrix meanVector) {

		Matrix covarianceMatrix = covariance(data, meanVector);
		Matrix varianceVector = new Matrix(covarianceMatrix.getColumnDimension(), 1);
		for (int i = 0; i < varianceVector.getRowDimension(); i++)
			varianceVector.set(i, 0, covarianceMatrix.get(i, i));
		return varianceVector;
	}
	
	/**
	 * Calculates standart deviation vector of variance vector
	 * 
	 * @param varianceVector
	 * @return
	 */
	public static Matrix standartDeviation(final Matrix varianceVector) {
		
		Matrix standartDeviationVector = new Matrix(varianceVector.getRowDimension(), 1);
		for (int i = 0; i < varianceVector.getRowDimension(); i++) {
			standartDeviationVector.set(i, 0, Math.sqrt(varianceVector.get(i, 0)));
		}
		return standartDeviationVector;
	}
}