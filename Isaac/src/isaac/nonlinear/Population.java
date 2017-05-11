package isaac.nonlinear;

import java.util.ArrayList;
import java.util.Collections;

public class Population {

	private ArrayList<Individual> individuals;
	private ArrayList<IndividualsCreator> primaryCreators;
	private ArrayList<IndividualsCreator> secondaryCreators;
	private ArrayList<IndividualsDestructor> destructors;

	public Population(){
		individuals = new ArrayList<Individual>();
		primaryCreators = new ArrayList<IndividualsCreator>();
		secondaryCreators = new ArrayList<IndividualsCreator>();
		destructors = new ArrayList<IndividualsDestructor>();
	}
	
	public void append(ArrayList<Individual> moreIndividuals){
		for(int i=0;i<moreIndividuals.size();i++){
			individuals.add(moreIndividuals.get(i));
		}
	}
	
	public void replaceWith(ArrayList<Individual> newIndividuals){
		this.individuals = newIndividuals;
	}
	
	public void keepBest(int numberToKeep){
		individuals = new ArrayList<Individual>(individuals.subList(0, numberToKeep ));
	}
	
	public ArrayList<Individual> filter(IndividualFilter filter){
		ArrayList<Individual> result = new ArrayList<Individual>();
		for(int i=0;i<individuals.size();i++){
			if(filter.filter(individuals.get(i))){
				result.add(individuals.get(i));
			}
		}
		return result;
	}

	public int size(){
		return individuals.size();
	}

	public Individual get(int index){
		return individuals.get(index);
	}

	public void remove(int index){
		individuals.remove(index);
	}

	public void addIndividual(Individual individual){
		individuals.add(individual);
	}

	public void addIndividuals(ArrayList<Individual> moreIndividuals){
		individuals.addAll(moreIndividuals);
	}

	public void addPrimaryCreator(IndividualsCreator creator){
		primaryCreators.add(creator);
	}
	
	public void addSecondaryCreator(IndividualsCreator creator){
		secondaryCreators.add(creator);
	}

	public void addDestructor(IndividualsDestructor destructor){
		destructors.add(destructor);
	}
	
	private ArrayList<Individual> appendTo(ArrayList<Individual> list1, ArrayList<Individual> list2){
		for(int i=0;i<list2.size();i++){
			list1.add(list2.get(i));
		}
		return list1;
		
	}

	public void advance(){
		Collections.sort(individuals);
		ArrayList<Individual> offspring = new ArrayList<Individual>();
		for(int i=0;i<primaryCreators.size();i++){
			appendTo(offspring,primaryCreators.get(i).create(this));
		}
		
		for(int i=0;i<secondaryCreators.size();i++){
			appendTo(offspring,secondaryCreators.get(i).create(this));
		}
		
		this.keepBest(1);
		this.append(offspring);
		
		for(int i=0; i<destructors.size(); i++){
			destructors.get(i).removeFrom(this);
		}
		
	}

	public Individual getAlpha(){
		return individuals.get(0);
	}


}
