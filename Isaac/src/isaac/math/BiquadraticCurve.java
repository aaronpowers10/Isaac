package isaac.math;

public class BiquadraticCurve {
	private double c1, c2, c3, c4, c5, c6;

	public BiquadraticCurve(double c1, double c2, double c3, double c4, double c5, double c6){
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.c5 = c5;
		this.c6 = c6;
	}

	public double evaluate(double x1, double x2){
		return c1 + c2*x1 + c3*x1*x1 + c4*x2 + c5*x2*x2 + c6*x1*x2;
	}

}
