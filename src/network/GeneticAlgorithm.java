package network;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
	private static Random rand = new Random();

	public static ArrayList<Network> createNextGeneration(ArrayList<Network> networks) {
		ArrayList<Network> newGeneration = select(networks);	//select parents
		newGeneration = crossover(newGeneration);				//crossover parents
		newGeneration = mutate(newGeneration);					//mutate offspring
		return newGeneration;									//return offspring
	}

	private static ArrayList<Network> select(ArrayList<Network> networks) {
		ArrayList<Network> parents = new ArrayList<Network>();	//list containing selected parents
		ArrayList<Network> pool = new ArrayList<Network>();		//list representing the pool of individuals to pick from

		// add each network to the gene pool a certain number of times corresponding to
		// the fitness of the network
		// networks with a higher fitness will be added to the pool more times
		for (int i = 0; i < networks.size(); i++) {
			for (int j = 0; j < (int) networks.get(i).getFitness(); j++) {
				pool.add(networks.get(i));
			}
		}

		// randomly select parents from the gene pool
		for (int i = 0; i < networks.size(); i++) {
			int index = rand.nextInt(pool.size());
			parents.add(pool.get(index));
		}

		return parents;
	}

	private static ArrayList<Network> crossover(ArrayList<Network> parents){
		ArrayList<Network> offspring = new ArrayList<Network>();	//result of crossover of parents
		while(!parents.isEmpty()){	//each iteration two parents will be removed from parents list
			Network parent1 = parents.get(0);	//get the first individual in list
			Network parent2 = parents.get(rand.nextInt(parents.size()-1)+1);	//get a random individual from list that is not parent 1
			ArrayList<ArrayList<Double>> parentGenes1 = parent1.getGenes();		//get parent 1 genes
		    ArrayList<ArrayList<Double>> parentGenes2 = parent2.getGenes();		//get parent 2 genes
		    ArrayList<ArrayList<Double>> offspringGenes1 = new ArrayList<>();	//initialize offspring 1 genes
		    ArrayList<ArrayList<Double>> offspringGenes2 = new ArrayList<>();	//initialize offspring 2 genes
		    for(int i = 0; i < parentGenes1.size(); i++){	//initialize the size of the outer offspring genes list
		    	offspringGenes1.add(new ArrayList<Double>());
		    	offspringGenes2.add(new ArrayList<Double>());
		    }
		    for (int i = 0; i < parentGenes1.size(); i++) {	//iterate through outer list
		    	for(int j = 0; j < parentGenes1.get(i).size(); j++) {	//iterate through inner list
		    		if(rand.nextInt(2) == 0){	//select either 0 or 1 randomly
		    			offspringGenes1.get(i).add(parentGenes1.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 1
		    			offspringGenes2.get(i).add(parentGenes2.get(i).get(j)); //set gene i of offspring 2 to gene i of parent 2
		    		}
		    		else{
		    			offspringGenes1.get(i).add(parentGenes2.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 2
		    			offspringGenes2.get(i).add(parentGenes1.get(i).get(j)); //set gene i of offspring 1 to gene i of parent 1
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
		    offspring.add(new Network(offspringGenes1, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    offspring.add(new Network(offspringGenes2, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    
		    //remove the parents that were crossed over from the list of parents
		    parents.remove(parent1);
		    parents.remove(parent2);
		}
		
		return offspring;
	}

	private static ArrayList<Network> mutate(ArrayList<Network> networks) {
		ArrayList<Network> mutated = new ArrayList<Network>(); //list of mutated networks
	    for(Network network : networks){	//iterate through all networks
	    	for(int i = 0; i < network.getGenes().size(); i++) {	//iterate through rows of genes
	    		for(int j = 0; j < network.getGenes().get(i).size(); j++){	//iterate through columns of genes
	    			if (rand.nextDouble() <= network.getMutationRate()) {	//if a random number is less than or equal to the mutation rate then mutate
	    				double gene = network.getGenes().get(i).get(j);	//get gene that is to be mutated
	    				network.setGene(i, j, gene + (rand.nextDouble()-.5)/2); //add a random number between -.25 and .25 to the gene
	    			}
	    		}
	    	}
	      mutated.add(network);	//add mutated individual to list
	    }
	    return mutated;
	}
}
