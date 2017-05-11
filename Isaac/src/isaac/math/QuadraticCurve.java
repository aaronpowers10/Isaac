package isaac.math;
import static java.lang.Math.pow;

public class QuadraticCurve {
	private double c1, c2, c3;

	public QuadraticCurve(double c1, double c2, double c3){
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
	}

	public double evaluate(double x){
		return c1 + c2*x + c3*x*x;
	}

	public double invert(double y){
		return (-c2 - pow((pow(c2,2) - 4*c3*(c1-y)),0.5))/(2*c3);
	}

}
