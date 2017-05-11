package isaac.nonlinear;

import isaac.math.BoundedNumeric;
import isaac.math.Evaluatable;
import isaac.math.Vector;

public interface Optimizer {

	public void run(Vector<BoundedNumeric> x, Vector<Evaluatable> residuals, Vector<BoundedNumeric> previousVariables);

}
