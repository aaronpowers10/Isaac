package isaac.nonlinear;

import java.util.ArrayList;

public class NSGACreator implements IndividualsCreator {

	private int timesToRun;
	private Selector selector;
	private int generation;
	private double eta1;
	private double eta2;
	private double mutationProbability;

	public NSGACreator(int timesToRun, Selector selector, String fileName, double eta1, double eta2, double mutationProbability) {
		this.timesToRun = timesToRun;
		this.selector = selector;
		this.mutationProbability = mutationProbability;
		generation = 1;
		this.eta1 = eta1;
		this.eta2 = eta2;
	}

	public void setTimesToRun(int timesToRun) {
		this.timesToRun = timesToRun;
	}

	@Override
	public ArrayList<Individual> create(Population population) {
		ArrayList<Individual> individuals = new ArrayList<Individual>();
		selector.reset();
		SimulatedBinaryCrossover crossover = new SimulatedBinaryCrossover(selector, eta1);
		PolynomialMutation mutation = new PolynomialMutation(eta2,mutationProbability);
		for (int i = 0; i < timesToRun; i++) {
			ArrayList<Individual> children = crossover.createChild(population);
			Individual child1 = mutation.mutate(children.get(0));
			child1.setCreatedBy("SBX-and-PM-in-GEN-" + generation);

			Individual child2 = mutation.mutate(children.get(1));
			child2.setCreatedBy("SBX-and-PM-in-GEN-" + generation);
			individuals.add(child1);
			individuals.add(child2);
			
		}
		generation++;
		return individuals;

	}


}
