package com.gcoban.gcml.helper;

import Jama.Matrix;

/**
 * Matrix helper for Jama Matrix class
 */
public class MatrixHelper {

	public static Matrix getRowVector(Matrix data, int i) {

		Matrix rowVector = new Matrix(data.getColumnDimension(), 1);
		for (int j = 0; j < data.getColumnDimension(); j++)
			rowVector.set(j, 0, data.get(i, j));
		return rowVector;
	}

	public static Matrix getColumnVector(Matrix data, int i) {

		Matrix columnVector = new Matrix(data.getRowDimension(), 1);
		for (int j = 0; j < data.getColumnDimension(); j++)
			columnVector.set(j, 0, data.get(j, i));
		return columnVector;
	}
	
	public static Matrix getDiagonalVector(Matrix data) {
		Matrix diagonalVector = new Matrix(data.getColumnDimension(), 1);
		for (int i = 0; i < diagonalVector.getRowDimension(); i++)
			diagonalVector.set(i, 0, data.get(i, i));
		return diagonalVector;
	}

	public static boolean compare(Matrix firstMatrix, Matrix secondMatrix) {

		int rowCount = firstMatrix.getRowDimension();
		int colCount = secondMatrix.getColumnDimension();

		if (rowCount != secondMatrix.getRowDimension() || colCount != secondMatrix.getColumnDimension())
			throw new IllegalArgumentException("Row and Column dimensions should be equal.");

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				if (firstMatrix.get(i, j) != secondMatrix.get(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * Deletes vector from every row of matrix
	 */
	public static Matrix minusVector(final Matrix matrix, final Matrix vector) {
		Matrix returnMatrix = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
		
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				returnMatrix.set(i, j, matrix.get(i, j) - vector.get(j, 0));
			}
		}
		
		return returnMatrix;
	}
}