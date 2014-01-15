package com.gcoban.gcml.hmm;

import java.util.ArrayList;

import Jama.Matrix;

public class ForwardBackward {

	public static AlphaBeta run(ArrayList<Integer> observationIndexes, ArrayList<Double> initialProbabilities, Matrix observationProbabilities, Matrix transitionProbabilities) {

		// N = #states
		int N = initialProbabilities.size();

		// Initialization of alpha
		Matrix alpha = new Matrix(observationIndexes.size(), N);
		for (int i = 0; i < N; i++) {
			alpha.set(0, i, initialProbabilities.get(i) * observationProbabilities.get(i, observationIndexes.get(0)));
		}

		// Recursion of alpha
		for (int t = 1; t < observationIndexes.size(); t++) {

			for (int j = 0; j < N; j++) {
				double total = 0;
				for (int i = 0; i < N; i++) {
					total += alpha.get(t - 1, i) * transitionProbabilities.get(i, j);
				}
				alpha.set(t, j, total * observationProbabilities.get(j, observationIndexes.get(t)));
			}
		}

		// Initialization of beta
		Matrix beta = new Matrix(observationIndexes.size(), N);
		for (int i = 0; i < N; i++) {
			beta.set(beta.getRowDimension() - 1, i, 1);
		}

		// Recursion of beta
		for (int t = observationIndexes.size() - 2; t >= 0; t--) {

			for (int i = 0; i < N; i++) {
				double total = 0;
				for (int j = 0; j < N; j++) {
					total += transitionProbabilities.get(i, j) * observationProbabilities.get(j, observationIndexes.get(t + 1)) * beta.get(t + 1, j);
				}
				beta.set(t, i, total);
			}
		}

		return new AlphaBeta(alpha, beta);
	}
}