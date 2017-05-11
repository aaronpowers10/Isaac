package isaac.nonlinear;

import java.io.FileWriter;
import java.util.ArrayList;

import isaac.math.BoundedNumeric;
import isaac.math.Difference;
import isaac.math.BoundedDifference;
import isaac.math.Vector;

public abstract class Individual implements Comparable<Individual> {

	private Vector<BoundedNumeric> variables;
	private ArrayList<Individual> parents;
	private String createdBy;

	public Individual(){
		variables = new Vector<BoundedNumeric>();
		parents = new ArrayList<Individual>();
		createdBy = "Initial Guess";
	}

	public Individual(int size){
		variables = new Vector<BoundedNumeric>(size);
		parents = new ArrayList<Individual>();
		createdBy = "Initial Guess";
	}
	
	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}
	
	public String createdBy(){
		return createdBy;
	}

	public void setVariables(Vector<BoundedNumeric> variables){
		this.variables = variables;
	}

	public int size(){
		return variables.size();
	}

	public void addVariable(BoundedNumeric variable){
		variables.add(variable);
	}

	public double get(int index){
		return variables.get(index).get();
	}

	public BoundedNumeric getVariable(int index){
		return variables.get(index);
	}

	public void set(int index, double value){
		variables.get(index).set(value);
	}

	public abstract double fitness();

	public Vector<BoundedNumeric> asVector(){
		return variables;
	}

	public void addParent(Individual parent){
		parents.add(parent);
	}

	public Individual mostFitParent(){
		return parents.get(0);
	}
	
	public Individual getAncestor(int index){
		if(index == 0){
			return mostFitParent();
		} else {
			return mostFitParent().getAncestor(index - 1);
		}
	}

	public boolean hasParent(){
		if(parents.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
	public double distanceToParent(){
		if(hasParent()){
			return variables.distanceTo(mostFitParent().asVector());
		} else {
			return -1;
		}
	}
	
	public double distanceToParent(Difference<BoundedNumeric> difference){
		if(hasParent()){
			return variables.distanceTo(mostFitParent().asVector(),difference);
		} else {
			return -1;
		}
	}
	
	public double distanceTo(Individual individual){
		return variables.distanceTo(individual.asVector());
	}
	
	public double distanceTo(Individual individual, Difference<BoundedNumeric> difference){
		return variables.distanceTo(individual.asVector(),difference);
	}

	public int ancestorGenerations(){
		if(hasParent()){
			return 1 + mostFitParent().ancestorGenerations();
		} else {
			return 0;
		}
	}
	
	public double maxAncestorDifference(){
		double maxDifference = 0;
		Individual individual = this;
		while(individual.hasParent()){
			if(individual.distanceToParent(new BoundedDifference()) > maxDifference){
				maxDifference = individual.distanceToParent(new BoundedDifference());
			}
			individual = individual.mostFitParent();
		}
		
		return maxDifference;
	}
	
	public double maxAncestorDifference(int index){
		double maxDifference = 0;
		Individual individual = this;
		for(int i=0;i<index;i++){
			if(individual.distanceToParent(new BoundedDifference()) > maxDifference){
				maxDifference = individual.distanceToParent(new BoundedDifference());
			}
			individual = individual.mostFitParent();
		}
		
		return maxDifference;
	}
	
	public int generationsWithin(double limit){
		int result = 0;
		Individual individual = this;
		while(individual.hasParent()){
			if(individual.distanceToParent(new BoundedDifference())/individual.size() < limit){
				result++;
			} else {
				return result;
			}
			individual = individual.mostFitParent();
		}
		return result;
		
	}
	
	public double dydx(){
		if(hasParent()){
			return (fitness() - mostFitParent().fitness())/this.distanceToParent(new BoundedDifference());
		} else {
			return 0;
		}
	}
	
	public int generationsWithImprovement(){
		int result = 0;
		Individual individual = this;
		while(individual.hasParent()){
			if(individual.fitness() < individual.mostFitParent().fitness()){
				result++;
			} else {
				return result;
			}
			individual = individual.mostFitParent();
		}
		return result;
	}
	
	private boolean sameSign(double x1, double x2){
		return x1*x2 >= 0.0;
	}
	
	public int generationsInSameDirections(){
		int result = 1;
		Individual individual = this;
		while(individual.mostFitParent().hasParent()){
			for(int i=0; i<individual.size();i++){
				double delta1 = individual.get(i) - individual.mostFitParent().get(i);
				double delta2 = individual.mostFitParent().get(i) - individual.mostFitParent().mostFitParent().get(i);
				if(!sameSign(delta1,delta2)){
					return result;
				}
			}
			result ++;
			individual = individual.mostFitParent();
		}
		return result;
	}

	@Override
	public int compareTo(Individual otherIndividual) {
		return (int)(10000000*(fitness() - otherIndividual.fitness()));
	}

	public abstract Individual copy();


}
