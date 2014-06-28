package com.gcoban.gcml.classification;

import java.util.HashMap;

import Jama.Matrix;

import com.gcoban.gcml.estimation.kernel.KernelFunction;
import com.gcoban.gcml.helper.MatrixHelper;

public class ParzenWindowsClassifier extends Classifier {

	private double h;

	public ParzenWindowsClassifier(Matrix trainData, Matrix trainDataLabelVector, Matrix testData) {

		super(trainData, trainDataLabelVector, testData);
	}

	public void run(double h) {

		this.setH(h);

		for (int i = 0; i < testData.getRowDimension(); i++) {
			Matrix x = MatrixHelper.getRowVector(testData, i);

			// For every class, calculate discriminant function
			// and chose the label with max value
			HashMap<Double, Double> discriminant = GetDiscriminant(x);
			double max = Double.NEGATIVE_INFINITY;
			for (Double label : discriminant.keySet()) {
				if (discriminant.get(label) > max) {
					testDataPredictionVector.set(i, 0, label);
					max = discriminant.get(label);
				}
			}
		}
	}

	private HashMap<Double, Double> GetDiscriminant(Matrix x) {

		HashMap<Double, Double> sum = new HashMap<>();

		for (int i = 0; i < trainData.getRowDimension(); i++) {
			Matrix instanceVector = MatrixHelper.getRowVector(trainData, i);
			double label = trainDataLabelVector.get(i, 0);
			double previousSum = sum.containsKey(label) ? sum.get(label) : 0;
			double kernel = KernelFunction.Gaussian(x.minus(instanceVector).times(1.0 / getH()));
			double newSum = previousSum + kernel;
			sum.put(label, newSum);
		}

		return sum;
	}

	public double getH() {

		return h;
	}

	public void setH(double h) {

		this.h = h;
	}
}
