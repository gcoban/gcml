package com.gcoban.gcml.hmm;

import Jama.Matrix;

public class AlphaBeta {

	public Matrix alpha;
	public Matrix beta;

	public AlphaBeta(Matrix alpha, Matrix beta) {

		this.alpha = alpha;
		this.beta = beta;
	}
}