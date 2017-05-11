package isaac.nonlinear;

import java.util.ArrayList;

public class SimulatedBinaryCrossover {

	private Selector selector;
	private double eta;

	public SimulatedBinaryCrossover(Selector selector, double eta) {
		this.selector = selector;
		this.eta = eta;
	}

	public ArrayList<Individual> createChild(Population population) {

		ArrayList<Individual> children = new ArrayList<Individual>();

		Individual mother = selector.select(population);
		Individual father = selector.select(population);
		while (mother.equals(father)) {
			father = selector.select(population);
		}
		Individual child1 = mother.copy();
		Individual child2 = mother.copy();

		

		for (int i = 0; i < mother.size(); i++) {
			double random = Math.random();
			double beta;
			if (random <= 0.5) {
				beta = Math.pow(2 * random, 1 / (1 + eta));
			} else {
				beta = 1/Math.pow((2 * (1 - random)), 1 / (1 + eta));
			}			
			
			child1.set(i, 0.5 * ((1 + beta) * mother.get(i) + (1 - beta) * father.get(i)));
			child2.set(i, 0.5 * ((1 - beta) * mother.get(i) + (1 + beta) * father.get(i)));
		}

		children.add(child1);
		children.add(child2);
		
		if(child1.asVector().distanceTo(mother.asVector()) < child1.asVector().distanceTo(father.asVector())){
			child1.addParent(mother);
		} else {
			child1.addParent(father);
		}
		
		if(child2.asVector().distanceTo(mother.asVector()) < child2.asVector().distanceTo(father.asVector())){
			child2.addParent(mother);
		} else {
			child2.addParent(father);
		}

		return children;

	}
	
	public static double beta(double random){
		double eta = 1;
		double beta;
		if (random <= 0.5) {
			beta = Math.pow(2 * random, 1 / (1 + eta));
		} else {	
			beta = 1/Math.pow((2 * (1 - random)), 1 / (1 + eta));
			
			beta = Math.pow(1/(2 * (1 - random)), 1 / (1 + eta));
		}
		return beta;
	}

}
