package com.gcoban.gcml.hmm;

import java.util.ArrayList;

import Jama.Matrix;

public class BaumWelch {

	private static ArrayList<Matrix> ksi(ArrayList<Integer> observationIndexes, ArrayList<Double> initialProbabilities, Matrix observationProbabilities, Matrix transitionProbabilities, Matrix alpha, Matrix beta) {

		// N = #states
		int N = initialProbabilities.size();

		ArrayList<Matrix> ksi = new ArrayList<>();
		for (int t = 0; t < observationIndexes.size() - 1; t++) {
			ksi.add(new Matrix(N, N));
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					double num = alpha.get(t, i) * transitionProbabilities.get(i, j) * observationProbabilities.get(j, observationIndexes.get(t + 1)) * beta.get(t + 1, j);
					double denom = 0;
					for (int k = 0; k < N; k++) {
						for (int l = 0; l < N; l++) {
							denom += alpha.get(t, k) * transitionProbabilities.get(k, l) * observationProbabilities.get(l, observationIndexes.get(t + 1)) * beta.get(t + 1, l);
						}
					}
					ksi.get(t).set(i, j, num / denom);
				}
			}
		}
		return ksi;
	}

	private static Matrix gamma(ArrayList<Matrix> ksi) {

		// N = #states
		int N = ksi.get(0).getRowDimension();

		Matrix gamma = new Matrix(ksi.size(), N);
		for (int t = 0; t < ksi.size(); t++) {
			for (int i = 0; i < N; i++) {
				double total = 0;
				for (int j = 0; j < N; j++) {
					total += ksi.get(t).get(i, j);
				}
				gamma.set(t, i, total);
			}
		}
		return gamma;
	}

	public static Matrix run(ArrayList<Integer> observationIndexes, ArrayList<Double> initialProbabilities, Matrix observationProbabilities, Matrix transitionProbabilities, Matrix alpha, Matrix beta) {

		// N = #states
		int N = initialProbabilities.size();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				ArrayList<Matrix> ksi = ksi(observationIndexes, initialProbabilities, observationProbabilities, transitionProbabilities, alpha, beta);
				Matrix gamma = gamma(ksi);
				double total_ksi = 0;
				double total_gamma = 0;
				for (int t = 0; t < observationIndexes.size() - 1; t++) {
					total_ksi += ksi.get(t).get(i, j);
					total_gamma += gamma.get(t, i);
				}
				transitionProbabilities.set(i, j, total_ksi / total_gamma);
			}
		}

		return transitionProbabilities;
	}
}