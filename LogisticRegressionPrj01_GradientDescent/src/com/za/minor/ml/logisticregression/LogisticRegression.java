package com.za.minor.ml.logisticregression;
import java.util.stream.IntStream;
import com.za.minor.linearalgebra.Matrix;
public class LogisticRegression {
	static final double ALPHA = 0.05;
	static final int MAX_NUMB_OF_ITERATIONS = 750; 
	Matrix gradientDescent(Matrix x, Matrix y) throws Exception {  
		double[][] wArray = new double[x.getData()[0].length][1];
		IntStream.range(0, x.getData()[0].length).forEach(i -> wArray[i][0] = 1); 
		Matrix w = new Matrix(wArray);
		for (int i = 0; i < MAX_NUMB_OF_ITERATIONS; i++) 
			w = w.subtract(x.transpose().scalarOperation(ALPHA, Matrix.ScalarOperation.MULTIPLY)
					                    .multiply((logistic(x.multiply(w))).subtract(y)));
		return w;
	}
	String classify(Matrix x, Matrix w) throws Exception {  
		String returnValue = null;
	    double prob = logistic(x.multiply(w)).getData()[0][0];
	    if (prob <= 0.5) returnValue = new String("prob = " + prob + " | Candidate classified as 0 (will not be hired)");
	    else returnValue = new String("prob = " + prob + " | Candidate classified as 1 (will be hired)");
	    return returnValue;
	}
	private Matrix logistic(Matrix ws) { 
		return  ws.exp().oneOver().scalarOperation(1, Matrix.ScalarOperation.ADD).oneOver(); 
	}
}
