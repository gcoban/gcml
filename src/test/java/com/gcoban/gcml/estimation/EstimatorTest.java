package com.gcoban.gcml.estimation;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import Jama.Matrix;

public class EstimatorTest {
	
	private static Matrix data;
	private static double floatingPointTolerance = 0.01;

	@BeforeClass
	public static void beforeClass() {
		data = new Matrix(3, 3);
		
		data.set(0, 0, -1);
		data.set(0, 1, 1);
		data.set(0, 2, 2);
		
		data.set(1, 0, -2);
		data.set(1, 1, 3);
		data.set(1, 2, 1);
		
		data.set(2, 0, 4);
		data.set(2, 1, 0);
		data.set(2, 2, 3);
	}
	
	@Test
	public void variance() {
		assertEquals(10.3333, Estimator.variance(data).get(0, 0), floatingPointTolerance);
	}
	
	@Test
	public void mean() {
		Matrix meanVector = Estimator.mean(data);
		assertEquals(0.33, meanVector.get(0, 0), floatingPointTolerance);
		assertEquals(1.33, meanVector.get(1, 0), floatingPointTolerance);
		assertEquals(2, meanVector.get(2, 0), floatingPointTolerance);
	}
}
