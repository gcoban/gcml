package com.gcoban.gcml.hmm;

import java.util.ArrayList;

import Jama.Matrix;

public class Viterbi {

	public static ArrayList<Integer> run(ArrayList<Integer> observationIndexes, ArrayList<Double> initialProbabilities, Matrix observationProbabilities, Matrix transitionProbabilities) {

		// N = #states
		int N = initialProbabilities.size();

		// Initialization
		Matrix delta = new Matrix(observationIndexes.size(), N);
		Matrix psi = new Matrix(observationIndexes.size(), N);
		for (int i = 0; i < N; i++) {
			delta.set(0, i, initialProbabilities.get(i) * observationProbabilities.get(i, observationIndexes.get(0)));
			psi.set(0, i, 0);
		}

		// Recursion
		for (int t = 1; t < observationIndexes.size(); t++) {
			for (int j = 0; j < N; j++) {
				double max = Double.NEGATIVE_INFINITY;
				int argMax = 0;
				for (int i = 0; i < N; i++) {
					double value = delta.get(t - 1, i) * transitionProbabilities.get(i, j);
					if (value > max) {
						max = value;
						argMax = i;
					}
				}
				delta.set(t, j, max * observationProbabilities.get(j, observationIndexes.get(t)));
				psi.set(t, j, argMax);
			}
		}

		// Termination
		ArrayList<Integer> path = new ArrayList<Integer>();
		for (int i = 0; i < observationIndexes.size(); i++) {
			path.add(0);
		}

		double max = Double.NEGATIVE_INFINITY;
		int argMax = 0;
		for (int i = 0; i < N; i++) {
			double value = delta.get(delta.getRowDimension() - 1, i);
			if (value > max) {
				max = value;
				argMax = i;
			}
		}
		path.set(path.size() - 1, argMax);

		// Finding Path (State Squence)
		for (int t = path.size() - 2; t >= 0; t--) {
			path.set(t, (int) psi.get(t + 1, path.get(t + 1)));
		}

		return path;
	}
}