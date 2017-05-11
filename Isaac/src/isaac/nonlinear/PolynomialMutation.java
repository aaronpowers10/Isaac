package isaac.nonlinear;

public class PolynomialMutation {

	private double eta;
	private double probability;

	public PolynomialMutation(double eta, double probability) {
		this.eta = eta;
		this.probability = probability;
	}

	public Individual mutate(Individual parent) {
		Individual child = parent.copy();
		for (int i = 0; i < parent.size(); i++) {
			double random = Math.random();
			
			if (Math.random() > probability) {
				child.set(i, parent.get(i));
			} else {
				double slope = parent.getVariable(i).max() - parent.getVariable(i).min();
				child.set(i, parent.getVariable(i).min() + random*slope );
			}
		}
		
		child.addParent(parent.mostFitParent());

		return child;

	}

}
