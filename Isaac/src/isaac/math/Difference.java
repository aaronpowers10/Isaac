package isaac.math;

public interface Difference<T extends Evaluatable> {
	
	public double calculate(T x1, T x2);

}
