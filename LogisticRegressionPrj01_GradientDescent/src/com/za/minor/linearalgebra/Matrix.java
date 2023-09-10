package com.za.minor.linearalgebra;
import java.util.stream.IntStream;
public class Matrix {
	private double data[][];
	public enum ScalarOperation {ADD, SUBTRACT, MULTIPLY, DIVIDE};
	public Matrix(int rows, int columns) { data = new double[rows][columns]; }
	public Matrix(double data[][]) {
		this.data = new double[data.length][data[0].length];
		IntStream.range(0, this.data.length).forEach(row ->
			IntStream.range(0, this.data[0].length).forEach(column -> this.data[row][column] = data[row][column]));
	}
	public Matrix add(Matrix matrix) throws Exception {
		if ((data.length != matrix.data.length) || 
				(data[0].length != matrix.data[0].length)) throw new Exception("matrices must have matching size");
		double returnData[][] = new double[data.length][data[0].length];
		IntStream.range(0, data.length).forEach(row ->
			IntStream.range(0, data[0].length).forEach(column ->
				returnData[row][column] = data[row][column] + matrix.data[row][column]));
		return new Matrix(returnData);
	}
	public Matrix subtract(Matrix matrix) throws Exception {
		return add(matrix.scalarOperation(-1, ScalarOperation.MULTIPLY));
	}
	public Matrix multiply(Matrix matrix) throws Exception {
		if (data[0].length != matrix.data.length) throw new Exception("matrices must have matching inner dimension"); 
		double returnData[][] = new double[data.length][matrix.data[0].length];
		IntStream.range(0, data.length).forEach(row ->
			IntStream.range(0, matrix.data[0].length).forEach(column -> {
				double result = 0;
				for (int i = 0; i < data[0].length; i++) result += data[row][i] * matrix.data[i][column];
				returnData[row][column] = result;
			}));
		return new Matrix(returnData);
	}
	public Matrix scalarOperation(double x, ScalarOperation scalarOperation) {
		double returnData[][] = new double[data.length][data[0].length];
		IntStream.range(0, data.length).forEach(row ->
			IntStream.range(0, data[0].length).forEach(column -> {
				switch (scalarOperation) {
					case ADD: 
						returnData[row][column] = data[row][column] + x;
						break;
					case SUBTRACT: 
						returnData[row][column] = data[row][column] - x;
						break;
					case MULTIPLY: 
						returnData[row][column] = data[row][column] * x;
						break;
					case DIVIDE:
						returnData[row][column] = data[row][column] / x;
						break;
				}
			}));		
		return new Matrix(returnData);
	}
	public static Matrix identity(int size) {
		Matrix matrix = new Matrix(size, size);
		IntStream.range(0, size).forEach(i -> matrix.data[i][i] = 1);
		return matrix;
	}
	public Matrix transpose() {
		double[][] returnData = new double[data[0].length][data.length];
		IntStream.range(0,  data.length).forEach(row -> 
			IntStream.range(0, data[0].length).forEach(column -> returnData[column][row] = data[row][column]));
		return new Matrix(returnData);
	}
	public double dotProduct(Matrix matrix) throws Exception {
		if (!this.isVector() || !matrix.isVector()) throw new Exception("can only dot product 2 vectors");
		else if ((this.flatten().length != matrix.flatten().length)) throw new Exception("both vectors must have same size");
		double returnValue = 0;
		for (int i = 0; i < this.flatten().length; i++) returnValue += this.flatten()[i] * matrix.flatten()[i];
		return returnValue;
	}
	public Matrix exp() {
		IntStream.range(0, data.length).forEach(r ->
			IntStream.range(0, data[0].length).forEach(c -> data[r][c] = Math.exp(data[r][c])));
		return this;
	}
	public Matrix oneOver() {
		IntStream.range(0, data.length).forEach(row ->
			IntStream.range(0, data[0].length).forEach(column -> data[row][column] = 1 / data[row][column]));
		return this;
	}
	public Matrix clear() { 
		IntStream.range(0, data.length).forEach(row ->
			IntStream.range(0, data[0].length).forEach(column -> data[row][column] = 0));
		return this;
	}
	public static Matrix toRowMatrix(double[] array) {
		double[][] data = new double[1][array.length];
		System.arraycopy(array, 0, data[0], 0, array.length);
		return new Matrix(data);
	}
	public Matrix getColumnMatrix(int column) {
		double[][] data = new double[this.data.length][1];
		IntStream.range(0,this.data.length).forEach(row -> data[row][0] = this.data[row][column]);
		return new Matrix(data);
	}
	public boolean isVector() {
		boolean flag = false;
		if (this.data.length == 1) flag = true;
		else if( this.data[0].length == 1) flag = true;
		return flag;
	}
	public double[] flatten() {
		double returnValue[] = new double[data.length * data[0].length];
		int i = 0;
		for (int row = 0; row < data.length; row++)
			for (int column = 0; column < data[0].length; column++) returnValue[i++] = data[row][column];
		return returnValue;
	}
	public double[][] getData() { return data; }
	public String toString() {
		StringBuffer bodySB = new StringBuffer();
		StringBuffer headingSB = new StringBuffer();
		headingSB.append("   |");
		IntStream.range(0, data[0].length).forEach(x -> headingSB.append("    c"+x +" "));
		headingSB.append("\n");
		IntStream.range(0, headingSB.length()).forEach(x -> bodySB.append("-"));
		bodySB.append("\n");
		IntStream.range(0, data.length).forEach(row -> {
			bodySB.append("r"+row+" |");
			IntStream.range(0, data[0].length).forEach(column -> {
				IntStream.range(0, 1 - (int)(data[row][column] / 10)).forEach(x -> bodySB.append(" "));
				if (data[row][column] >= 0) bodySB.append("  "+  String.format("%.2f",data[row][column]));
				else  bodySB.append(" "+  String.format("%.2f",data[row][column]));
			});
			bodySB.append("\n");
		});
		return headingSB.toString() + bodySB.toString();
	}
}
