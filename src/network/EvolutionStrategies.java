package network;

import java.util.ArrayList;
import java.util.Random;

public class EvolutionStrategies {
	private static Random rand = new Random();
	
	public static ArrayList<Network> createNextGeneration(ArrayList<Network> population){
		ArrayList<Network> newGeneration = new ArrayList<Network>(population);
		newGeneration = crossover(newGeneration);
		updateSigmasAndMutate(newGeneration);
		return newGeneration;
	}
	
	public static ArrayList<Network> crossover(ArrayList<Network> parents){
		ArrayList<Network> offspring = new ArrayList<Network>();
		while(!parents.isEmpty()){	//each iteration two parents will be removed from parents list
			Network parent1 = parents.get(0);	//get the first individual in list
			Network parent2 = parents.get(rand.nextInt(parents.size()-1)+1);	//get a random individual from list that is not parent 1
			ArrayList<ArrayList<Double>> parentGenes1 = parent1.getGenes();		//get parent 1 genes
		    ArrayList<ArrayList<Double>> parentGenes2 = parent2.getGenes();		//get parent 2 genes
		    ArrayList<ArrayList<Double>> parentSigmas1 = parent1.getSigmas();	//get parent 1 sigmas
		    ArrayList<ArrayList<Double>> parentSigmas2 = parent1.getSigmas();	//get parent 2 sigmas
		    ArrayList<ArrayList<Double>> offspringGenes1 = new ArrayList<>();	//initialize offspring 1 genes
		    ArrayList<ArrayList<Double>> offspringGenes2 = new ArrayList<>();	//initialize offspring 2 genes
		    ArrayList<ArrayList<Double>> offspringSigmas1 = new ArrayList<>();	//initialize offspring 1 sigmas
		    ArrayList<ArrayList<Double>> offspringSigmas2 = new ArrayList<>();	//initialize offspring 2 sigmas
		    for(int i = 0; i < parentGenes1.size(); i++){	//initialize the size of the outer offspring genes list and sigmas list
		    	offspringGenes1.add(new ArrayList<Double>());
		    	offspringGenes2.add(new ArrayList<Double>());
		    	offspringSigmas1.add(new ArrayList<Double>());
		    	offspringSigmas2.add(new ArrayList<Double>());
		    }
		    for (int i = 0; i < parentGenes1.size(); i++) {	//iterate through outer list
		    	for(int j = 0; j < parentGenes1.get(i).size(); j++) {	//iterate through inner list
		    		if(rand.nextInt(2) == 0){	//select either 0 or 1 randomly
		    			offspringGenes1.get(i).add(parentGenes1.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 1
		    			offspringGenes2.get(i).add(parentGenes2.get(i).get(j)); //set gene i of offspring 2 to gene i of parent 2
		    			offspringSigmas1.get(i).add(parentSigmas1.get(i).get(j)); //set sigma i of offspring 1 to gene i of parent 1
		    			offspringSigmas2.get(i).add(parentSigmas2.get(i).get(j)); //set sigma i of offspring 2 to gene i of parent 2
		    		}
		    		else{
		    			offspringGenes1.get(i).add(parentGenes2.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 2
		    			offspringGenes2.get(i).add(parentGenes1.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 1
		    			offspringSigmas1.get(i).add(parentSigmas2.get(i).get(j)); //set sigma i of offspring 1 to gene i of parent 2
		    			offspringSigmas2.get(i).add(parentSigmas1.get(i).get(j)); //set sigma i of offspring 1 to gene i of parent 1
		    		}
		    	}
		    }
		    //set the structure of offspring network the same as parent network
		    int numInputs = parent1.getNumInputs();
		    int numHidLayers = parent1.getNumHidLayers();
		    int numHidNodes = parent1.getNumHidNodes();
		    int numOutputs = parent1.getNumOutputs();
		    int actFunHidden = parent1.getActFunHidden();
		    int actFunOutput = parent1.getActFunOutput();
		    
		    //add offspring to offspring list
		    offspring.add(new Network(offspringGenes1, offspringSigmas1, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    offspring.add(new Network(offspringGenes2, offspringSigmas2, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    
		    //remove the parents that were crossed over from the list of parents
		    parents.remove(parent1);
		    parents.remove(parent2);
		}
		return offspring;
	}
	
	public static void updateSigmasAndMutate(ArrayList<Network> networks){
		for(Network network : networks){	//iterate through all networks
	    	for(int i = 0; i < network.getSigmas().size(); i++) {	//iterate through rows of genes
	    		for(int j = 0; j < network.getSigmas().get(i).size(); j++){	//iterate through columns of genes
    				double newSigma = network.getSigmas().get(i).get(j);	//get sigma that is to be changed
    				double newGene = network.getSigmas().get(i).get(j);	//get gene that is to be changed
    				newSigma += network.getLearningRate()*newSigma*rand.nextGaussian(); //use additive model to modify sigma
    				network.setSigma(i, j, newSigma); //set sigma value
    				
    				newGene += newSigma*rand.nextGaussian();	//mutate gene based on sigma
    				network.setGene(i, j, newGene);				//set gene value								
	    		}
	    	}
	    }
	}
}
