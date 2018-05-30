package vn.com.epi.gate.refdiff;

public class AbstractMeasuresRow {

	private int correct;
	private int partially;
	private int missing;
	private int spurious;
	private double precision;
	private double recall;
	private double F1;
	private String name;
	private double alpha;
	private static  double beta = 1;
	
	public AbstractMeasuresRow(String name, double alpha) {
		
		this.name = name;
		this.alpha = alpha;
	}


	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getF1() {
		return F1;
	}

	public String getName() {
		return name;
	}

	public double getAlpha() {
		return alpha;
	}
	
	public void calculate(int correct, int partially, int missing, int spurious) {
		
		this.correct = correct;
		this.partially = partially;
		this.missing = missing;
		this.spurious = spurious;
		precision = (correct + alpha * partially) / (correct + partially + spurious);
		recall = (correct + alpha * partially) / (correct + partially + missing);
		if(precision == 0) {
			F1 = 0;
		}
		else {			
			F1 = (beta * beta + 1) * precision * recall / (beta * beta * precision + recall);
		}
	}


	public int getCorrect() {
		return correct;
	}


	public int getPartially() {
		return partially;
	}


	public int getMissing() {
		return missing;
	}


	public int getSpurious() {
		return spurious;
	}


	public void setSpurious(int spurious) {
		this.spurious = spurious;
	}

}