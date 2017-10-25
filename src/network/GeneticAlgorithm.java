package network;

import java.util.ArrayList;

public class GeneticAlgorithm {
	public static ArrayList<Network> createNextGeneration(ArrayList<Network> networks){
		ArrayList<Network> newGeneration = select(networks);
		newGeneration = crossover(newGeneration);
		newGeneration = mutate(newGeneration);	
		return newGeneration;
	}
	
	private static ArrayList<Network> select(ArrayList<Network> networks){
		ArrayList<Network> parents = new ArrayList<Network>();
		
		
		return parents;
	}
	
	private static ArrayList<Network> crossover(ArrayList<Network> networks){
		ArrayList<Network> offspring = new ArrayList<Network>();
		
		
		return offspring;
	}
	
	private static ArrayList<Network> mutate(ArrayList<Network> networks){
		ArrayList<Network> mutated = new ArrayList<Network>();
		
		
		return mutated;
	}
}
