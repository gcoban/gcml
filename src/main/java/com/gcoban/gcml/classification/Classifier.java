package com.gcoban.gcml.classification;

import Jama.Matrix;

public abstract class Classifier {

	protected Matrix trainData;
	protected Matrix trainDataLabelVector;

	protected Matrix testData;
	private Matrix testDataLabelVector;
	public Matrix testDataPredictionVector;

	public Classifier(Matrix trainData, Matrix trainDataLabelVector, Matrix testData) {

		this.trainData = trainData;
		this.trainDataLabelVector = trainDataLabelVector;
		this.testData = testData;
		this.testDataPredictionVector = new Matrix(testData.getRowDimension(), testData.getColumnDimension());
	}

	public double GetAccuracy() {

		int sum = 0;
		for (int i = 0; i < testDataPredictionVector.getRowDimension(); i++) {
			if (testDataPredictionVector.get(i, 0) == testDataLabelVector.get(i, 0)) {
				sum++;
			}
		}
		return (double) sum / testDataPredictionVector.getRowDimension();
	}

	public void setTestDataLabelVector(Matrix testDataLabelVector) {

		this.testDataLabelVector = testDataLabelVector;
	}
}