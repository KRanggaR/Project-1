package com.za.minor.ml.logisticregression;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;
import com.za.minor.linearalgebra.Matrix;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Driver extends Application {
	static final double[][][] TRAINING_DATA = 
		{{{1.0, 1.00, 2.00}, {0.0}},
		 {{1.0, 2.45, 2.53}, {0.0}},
		 {{1.0, 1.42, 1.00}, {0.0}},
		 {{1.0, 3.00, 2.08}, {0.0}},
		 {{1.0, 3.71, 3.05}, {0.0}}, 
		 {{1.0, 3.71, 5.55}, {0.0}},
		 {{1.0, 2.00, 1.50}, {0.0}},
		 {{1.0, 4.71, 5.55}, {0.0}},
		 {{1.0, 1.00, 8.00}, {0.0}},
		 {{1.0, 2.00, 4.00}, {0.0}},
		 {{1.0, 1.50, 6.00}, {0.0}},
		 {{1.0, 3.00, 6.50}, {0.0}},
		 {{1.0, 6.00, 1.00}, {0.0}},
		 {{1.0, 5.00, 2.00}, {0.0}},
		 {{1.0, 4.03, 5.06}, {1.0}},
		 {{1.0, 6.02, 7.01}, {1.0}},
		 {{1.0, 7.04, 8.04}, {1.0}},
		 {{1.0, 9.05, 7.04}, {1.0}},
		 {{1.0, 8.00, 8.08}, {1.0}},
		 {{1.0, 9.08, 4.08}, {1.0}},
		 {{1.0, 7.00, 4.00}, {1.0}},
		 {{1.0, 6.00, 9.00}, {1.0}},
		 {{1.0, 9.00, 9.00}, {1.0}},
		 {{1.0, 8.00, 1.00}, {1.0}}};
	static LogisticRegression logisticRegression = new LogisticRegression();
	static Matrix w = null;
	static double m, b;
	static StringBuffer weightsSB = new StringBuffer(); 
	public static void main(String[] args) throws Exception {
		double[][] xArray = new double[TRAINING_DATA.length][TRAINING_DATA[0][0].length]; 
		double[][] yArray = new double[TRAINING_DATA.length][1]; 
		IntStream.range(0, TRAINING_DATA.length).forEach(i -> {
			IntStream.range(0, TRAINING_DATA[0][0].length).forEach(j -> xArray[i][j] = TRAINING_DATA[i][0][j]);
			yArray[i][0] = TRAINING_DATA[i][1][0];
		});
		Matrix x = new Matrix(xArray); 
		Matrix y = new Matrix(yArray); 
		w = logisticRegression.gradientDescent(x, y);
		m = -w.getData()[1][0] / w.getData()[2][0];
    	b = -w.getData()[0][0] / w.getData()[2][0];
    	weightsSB.append(" weights = (");
    	IntStream.range(0, w.getData().length - 1).forEach(i -> weightsSB.append(String.format("%.2f", w.getData()[i][0]) + ", "));
    	weightsSB.append(String.format("%.2f", w.getData()[w.getData().length-1][0])+ ")");
    	launch();
	}

	static void handleCommandLine() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("> to classify new candidate enter scores for interviews 1 & 2 (or exit):");
			String[] values = (bufferedReader.readLine()).split(" ");
			if (values[0].equals("exit")) System.exit(0);
			else {
				try { System.out.println(logisticRegression.classify(
						new Matrix(new double[][]{{1.0,Double.valueOf(values[0]),Double.valueOf(values[1])}}),w)); } 
				catch (Exception e) { System.out.println("invalid input"); }
			} 
		}
	}
	public void start(Stage stage) throws Exception {
		Platform.setImplicitExit(false);
		XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
	    series1.setName("Candidate was not hired");
	    XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
	    series2.setName("Candidate was hired");
	    IntStream.range(0, Driver.TRAINING_DATA.length).forEach(i -> {
	    	double x = Driver.TRAINING_DATA[i][0][1], y = Driver.TRAINING_DATA[i][0][2];
	    	if (Driver.TRAINING_DATA[i][1][0] == 0.0) series1.getData().add(new XYChart.Data<Number, Number>(x, y));
	        else series2.getData().add(new XYChart.Data<Number, Number>(x, y));
	    });
	    NumberAxis xAxis = new NumberAxis(0, 10, 1);
	    xAxis.setLabel("Score for candidate interview # 1"); 
	    NumberAxis yAxis = new NumberAxis(0, 10, 1); 
	    yAxis.setLabel("Score for candidate interview # 2"); 
	    ScatterChart<Number,Number> scatterChart = new ScatterChart<Number,Number>(xAxis,yAxis);
	    scatterChart.getData().add(series1);
	    scatterChart.getData().add(series2);
	    double score1X = 0.0, score1Y = Driver.m*score1X + Driver.b , score2X = 10.00, score2Y =  Driver.m*score2X + Driver.b;
	    XYChart.Series<Number, Number> series3 = new XYChart.Series<Number, Number>();
	    series3.getData().add(new XYChart.Data<Number, Number>(score1X, score1Y));
	    series3.getData().add(new XYChart.Data<Number, Number>(score2X, score2Y));
	    LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
	    lineChart.getData().add(series3);
	    lineChart.setOpacity(0.4);
	    Pane pane = new Pane();
	    pane.getChildren().addAll(scatterChart, lineChart);
	    stage.setScene(new Scene(pane, 580, 410));
	    stage.setOnHidden(e -> {try { handleCommandLine();} catch (Exception e1) { e1.printStackTrace();}});
	    System.out.println("Close display window to proceed");
	    stage.setTitle("Logistic Regression Tutorial 01  | y = " + 
	                   new String(String.format("%.2f", Driver.m)+"x + "+String.format("%.2f", Driver.b))+"  |  "+Driver.weightsSB);
	    stage.show(); 
	}
}
