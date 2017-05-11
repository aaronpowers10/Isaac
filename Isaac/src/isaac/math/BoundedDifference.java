package isaac.math;

public class BoundedDifference implements Difference<BoundedNumeric> {

	@Override
	public double calculate(BoundedNumeric x1, BoundedNumeric x2) {
		return (x1.get() - x2.get())/x1.range();
	}

}
