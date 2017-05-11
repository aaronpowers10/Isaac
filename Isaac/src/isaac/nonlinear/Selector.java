package isaac.nonlinear;

public interface Selector {

	public Individual select(Population population);

	public void reset();

}
