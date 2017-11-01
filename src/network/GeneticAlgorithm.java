package network;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
	private static Random rand = new Random();

	public static ArrayList<Network> createNextGeneration(ArrayList<Network> networks) {
		ArrayList<Network> newGeneration = select(networks);
		newGeneration = crossover(newGeneration);
		newGeneration = mutate(newGeneration);
		return newGeneration;
	}

	private static ArrayList<Network> select(ArrayList<Network> networks) {
		ArrayList<Network> parents = new ArrayList<Network>();
		ArrayList<Network> pool = new ArrayList<Network>();

		// add each network to the gene pool a certain number of times corresponding to
		// the fitness of the network
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
		ArrayList<Network> offspring = new ArrayList<Network>();
		while(!parents.isEmpty()){
			Network parent1 = parents.get(0);
			Network parent2 = parents.get(rand.nextInt(parents.size()-1)+1);
			ArrayList<ArrayList<Double>> parentGenes1 = parent1.getGenes();
		    ArrayList<ArrayList<Double>> parentGenes2 = parent2.getGenes();
		    ArrayList<ArrayList<Double>> offspringGenes1 = new ArrayList<>();
		    ArrayList<ArrayList<Double>> offspringGenes2 = new ArrayList<>();
		    for(int i = 0; i < parentGenes1.size(); i++){
		    	offspringGenes1.add(new ArrayList<Double>());
		    	offspringGenes2.add(new ArrayList<Double>());
		    }
		    for (int i = 0; i < parentGenes1.size(); i++) {
		    	for(int j = 0; j < parentGenes1.get(i).size(); j++) {
		    		if(rand.nextInt(2) == 0){
		    			offspringGenes1.get(i).add(parentGenes1.get(i).get(j));
		    			offspringGenes2.get(i).add(parentGenes2.get(i).get(j));
		    		}
		    		else{
		    			offspringGenes1.get(i).add(parentGenes2.get(i).get(j));
		    			offspringGenes2.get(i).add(parentGenes1.get(i).get(j));
		    		}
		    	}
		    }
		    int numInputs = parent1.getNumInputs();
		    int numHidLayers = parent1.getNumHidLayers();
		    int numHidNodes = parent1.getNumHidNodes();
		    int numOutputs = parent1.getNumOutputs();
		    int actFunHidden = parent1.getActFunHidden();
		    int actFunOutput = parent1.getActFunOutput();
		    offspring.add(new Network(offspringGenes1, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    offspring.add(new Network(offspringGenes2, numInputs, numHidLayers, numHidNodes, numOutputs, actFunHidden, actFunOutput));
		    parents.remove(parent1);
		    parents.remove(parent2);
		}
		
		return offspring;
	}

	private static ArrayList<Network> mutate(ArrayList<Network> networks) {
		ArrayList<Network> mutated = new ArrayList<Network>();
	    for(Network network : networks){
	    	for(int i = 0; i < network.getGenes().size(); i++) {
	    		for(int j = 0; j < network.getGenes().get(i).size(); j++){
	    			if (rand.nextInt(100) == 0) {
	    				double gene = network.getGenes().get(i).get(j);
	    				network.setGene(i, j, gene + (rand.nextDouble()-.5)/2);
	    			}
	    		}
	    	}
	      mutated.add(network);
	    }
	    return mutated;
	}
}
