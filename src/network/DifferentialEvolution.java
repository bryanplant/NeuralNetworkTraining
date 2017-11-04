package network;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DifferentialEvolution {
	private static Random rand = new Random();
	private static double beta = 0.5; //scaling factor -- tunable
	
	public static ArrayList<Network> createNextGeneration(ArrayList<Network> population, List<Sample> samples){
		ArrayList<Network> newGeneration = new ArrayList<Network>(population);
		newGeneration = crossover(newGeneration, samples);
		
		return newGeneration;
	}
	
	public static ArrayList<Network> crossover(ArrayList<Network> parents, List<Sample> samples){
		ArrayList<Network> offspring = new ArrayList<Network>();
		
		for(int num = 0; num < parents.size(); num++){	//each iteration a new parent will be selected
			Network parent = parents.get(num);	//select parent to change
			Network x1, x2, x3;
			do {
				x1 = parents.get(rand.nextInt(parents.size()));	//select x1
			} while(x1 == parent);
			do {
				x2 = parents.get(rand.nextInt(parents.size()));	//select x2
			} while(x2 == parent || x2 == x1);
			do {
				x3 = parents.get(rand.nextInt(parents.size()));	//select x3
			} while(x3 == parent || x3 == x1 || x3 == x2);
			ArrayList<ArrayList<Double>> u = createTrialVector(x1, x2, x3);	//create trial vector with x1, x2, x3
			
			ArrayList<ArrayList<Double>> parentGenes = parent.getGenes();		//get parent genes
		    ArrayList<ArrayList<Double>> offspringGenes = new ArrayList<>();	//initialize offspring genes

		    for(int i = 0; i < parentGenes.size(); i++){	//initialize the size of the outer offspring genes list and sigmas list
		    	offspringGenes.add(new ArrayList<Double>());
		    }
		    
		    for (int i = 0; i < parentGenes.size(); i++) {	//iterate through outer list
		    	for(int j = 0; j < parentGenes.get(i).size(); j++) {	//iterate through inner list
		    		if(rand.nextInt(2) == 0){	//select either 0 or 1 randomly
		    			offspringGenes.get(i).add(parentGenes.get(i).get(j)); //set gene i of offspring 1 to gene i of parent
		    		}
		    		else{
		    			offspringGenes.get(i).add(u.get(i).get(j)); //set gene i of offspring 1 to gene i of trial vector
		    		}
		    	}
		    }
		    //set the structure of offspring network the same as parent network
		    int numInputs = parent.getNumInputs();
		    int numHidLayers = parent.getNumHidLayers();
		    int numHidNodes = parent.getNumHidNodes();
		    int numOutputs = parent.getNumOutputs();
		    int actFunHidden = parent.getActFunHidden();
		    int actFunOutput = parent.getActFunOutput();
		    
		    //create network from offspring genes
		    Network newNetwork = new Network(offspringGenes, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput);
		    
		    if(parent.getFitness() < newNetwork.evaluate(samples)) { //compare fitness of parent and offspring -- lower is better
		    	offspring.add(parent);	//add parent if it has lower fitness than parent
		    }
		    else {
		    	offspring.add(newNetwork);	//add child if it has a lower fitness than parent
		    }
		}
		return offspring;	//return population of offspring
	}
	
	public static ArrayList<ArrayList<Double>> createTrialVector(Network x1, Network x2, Network x3) {	
		ArrayList<ArrayList<Double>> diff1 = x2.getGenes(); //get genes of difference vector 1
		ArrayList<ArrayList<Double>> diff2 = x3.getGenes(); //get genes of difference vector 2
		ArrayList<ArrayList<Double>> diff = new ArrayList<>(); //genes of result of difference vectors
		
		for(int i = 0; i < diff1.size(); i++) {
			diff.add(new ArrayList<Double>());
			for(int j = 0; j < diff1.get(i).size(); j++) {
				diff.get(i).add(beta*(diff1.get(i).get(j) - diff2.get(i).get(j))); //calculate difference of genes and multiply by beta
			}
		}
		
		ArrayList<ArrayList<Double>> target = x1.getGenes(); //set target vector to the genes of x1
		ArrayList<ArrayList<Double>> u = new ArrayList<>(); //genes of resulting trial vector
		for(int i = 0; i < target.size(); i++) {
			u.add(new ArrayList<Double>());
			for(int j = 0; j < target.get(i).size(); j++) {
				u.get(i).add(target.get(i).get(j) + diff.get(i).get(j));
			}
		}
		
		return u;	//return trial vector
	}
}
