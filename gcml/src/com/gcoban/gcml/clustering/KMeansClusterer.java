package com.gcoban.gcml.clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Jama.Matrix;

import com.gcoban.gcml.distance.EuclideanDistance;
import com.gcoban.gcml.estimation.Estimator;
import com.gcoban.gcml.helper.MatrixHelper;

public class KMeansClusterer {

	public static Matrix run(Matrix data, double k) {

		return run(data, k, 100);
	}

	public static Matrix run(Matrix data, double k, int maxIteration) {

		// Estimated group for each instance
		Matrix dataGroupVector = new Matrix(data.getRowDimension(), 1, -1);

		/*
		 * Create m = meanVectorForEachGroup and fill it random k different
		 * instances. There is another ways -which can work better- for chosing
		 * initial mean. Two of them: 1) The mean of all data can be calculated
		 * and small random vectors may be added to the mean to get the k
		 * initial mi. 2) One can calculate the principal component, divide its
		 * range into k equal intervals, partitioning the data into k groups,
		 * and then take the means of these groups as the initial centers.
		 */
		Map<Double, Matrix> m = new HashMap<>();
		Set<Integer> addedInstanceIndices = new HashSet<>();
		Random random = new Random();
		for (double i = 0; i < k; i++) {
			int j = 0;
			while (true) {
				if (j == maxIteration)
					break;

				int randomInstanceIndex = random.nextInt(data.getRowDimension());
				if (addedInstanceIndices.add(randomInstanceIndex)) {
					m.put(i, MatrixHelper.getRowVector(data, randomInstanceIndex));
					dataGroupVector.set(randomInstanceIndex, 0, i);
					break;
				}

				j++;
			}
		}

		// Run the algorithm
		boolean converged = false;
		Matrix oldDataGroupVector = dataGroupVector.copy();
		while (!converged) {
			// For each instance
			for (int i = 0; i < data.getRowDimension(); i++) {
				// Find the nearest group center of instance i
				double nearest = nearestGroupByEuclideanDistance(MatrixHelper.getRowVector(data, i), m);

				// Set the nearest group center as group of instance i
				dataGroupVector.set(i, 0, nearest);
			}

			// Update means
			m = Estimator.means(data, dataGroupVector);

			// Check if convergence is reached
			converged = MatrixHelper.compare(oldDataGroupVector, dataGroupVector);

			oldDataGroupVector = dataGroupVector.copy();
		}

		return dataGroupVector;
	}

	public static double reconstructionError(Matrix data, Matrix dataLabels) {

		Map<Double, Matrix> meanVectors = Estimator.means(data, dataLabels);
		double error = 0;
		for (int i = 0; i < data.getRowDimension(); i++) {
			double label = dataLabels.get(i, 0);
			Matrix meanVector = meanVectors.get(label);
			Matrix instance = MatrixHelper.getRowVector(data, i);
			double distance = EuclideanDistance.calc(instance, meanVector);
			error += Math.pow(distance, 2);
		}

		return error;
	}

	public static double nearestGroupByEuclideanDistance(Matrix instanceVector, Map<Double, Matrix> groups) {

		double nearest = 0; // Nearest group center
		double nearestDistance = Double.MAX_VALUE; // Distance to nearest group
													// center

		for (double label : groups.keySet()) {
			double distance = EuclideanDistance.calc(instanceVector, groups.get(label));
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearest = label;
			}
		}

		return nearest;
	}
}