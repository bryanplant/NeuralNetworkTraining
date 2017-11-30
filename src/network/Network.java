package network;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*	The Network class represents a single network in a population and its related functions and attributes. This class
 * 	is changed a bit from Project 2, as it had to incorporate new functionality for the 3 new training algorithms.
 */

public class Network implements Comparable<Network>{
	private Random random = new Random();
	private ArrayList<Layer> layers;
	private double learningRate;
	private ArrayList<ArrayList<Double>> genes = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> sigmas = new ArrayList<>();
	private double fitness;
	private double mutationRate;
	private double crossoverRate;
	private int numInputs;
	private int numHidLayers;
	private int numHidNodes;
	private int numOutputs;
	private int actFunHidden;
	private int actFunOutput;

	/*
	 * Create an MLP network
	 * @param numInputs: number of input nodes
	 * @param numHidLayers: number of hidden layers
	 * @param numHidNodes: number of nodes in hidden layers
	 * @param numOutputs: number of output nodes
	 * @param actFun: type of activation function for nodes
	 */
	public Network(int numInputs, int numHidLayers, int numHidNodes, int numOutputs, int actFunHidden, int actFunOutput) {
		layers = new ArrayList<Layer>();
		//create input layer with inputs number of nodes and a linear activation function
		layers.add(new Layer(numInputs, 1));
		
		//create hidden layers with hidNode number of nodes and given activation function
		for(int i = 0; i < numHidLayers; i++) {
			layers.add(new Layer(numHidNodes, actFunHidden));
		}
		
		//create output layer with outputs number of nodes and given activation function
		layers.add(new Layer(numOutputs, actFunOutput));
		
		//add connections between layers
		for(int i = 0; i < layers.size()-1; i++) {
			for(int j = 0; j < layers.get(i).size(); j++) {
				for(int k = 0; k < layers.get(i+1).size(); k++){
					double weight = (random.nextDouble()*2)-1;
					layers.get(i).getNeuron(j).addWeight(weight);
				}
			}
		}
		
		//adds weight vector of each neuron in the network to the genes list
		for(Layer layer : layers){
			for(int i = 0; i < layer.size(); i++){
				genes.add(layer.getNeuron(i).getWeights());
			}
		}
		
		//initialize sigmas to random nubmer between 0 and 1
		for(int i = 0; i < genes.size(); i++){
			sigmas.add(new ArrayList<Double>());
			for(int j = 0; j < genes.get(i).size(); j++){
				sigmas.get(i).add(random.nextDouble());
			}
		}
		
		//this block represents our manually tunable parameters
		this.learningRate = 0.01;
		this.mutationRate = 0.01;
		this.crossoverRate = 0.95;
		this.numInputs = numInputs;
		this.numHidLayers = numHidLayers;
		this.numHidNodes = numHidNodes;
		this.numOutputs = numOutputs;
		this.actFunHidden = actFunHidden;
		this.actFunOutput = actFunOutput;
	}
	
	//create a new network for genetic algorithm
	//@param genes - a list of the network's genes
	//@param numInputs - the number of inputs the network will have
	//@param numHidLayers - the number of hidden layers the network will have
	//@param numHidNodes - number of hidden nodes in each hidden layer
	//@param numOutputs - number of outputs the network has
	//@param actFunHidden - activation function for the hidden layers
	//@param actFunOutput - activation function for the output layer
	public Network(ArrayList<ArrayList<Double>> genes, int numInputs, int numHidLayers, int numHidNodes, int numOutputs, int actFunHidden, int actFunOutput) {
		layers = new ArrayList<Layer>();
		//create input layer with inputs number of nodes and a linear activation function
		layers.add(new Layer(numInputs, 1));
		
		//create hidden layers with hidNode number of nodes and given activation function
		for(int i = 0; i < numHidLayers; i++) {
			layers.add(new Layer(numHidNodes, actFunHidden));
		}
		
		//create output layer with outputs number of nodes and given activation function
		layers.add(new Layer(numOutputs, actFunOutput));
		
		int curGene = 0;
		//add weights to neurons based on genes
		for(Layer layer : layers){
			for(int i = 0; i < layer.size(); i++){
				layer.getNeuron(i).addWeights(genes.get(curGene));
				curGene++;
			}
		}
		
		this.genes = genes;
		//this block represents our manually tunable parameters
		this.learningRate = 0.01;
		this.mutationRate = 0.01;
		this.crossoverRate = 0.95;
		this.numInputs = numInputs;
		this.numHidLayers = numHidLayers;
		this.numHidNodes = numHidNodes;
		this.numOutputs = numOutputs;
		this.actFunHidden = actFunHidden;
		this.actFunOutput = actFunOutput;
	}
	
	//create a new network for evolution strategies
	//@param genes - a list of the network's genes
	//@param sigmas - a list of the network's sigma values
	//@param numInputs - the number of inputs the network will have
	//@param numHidLayers - the number of hidden layers the network will have
	//@param numHidNodes - number of hidden nodes in each hidden layer
	//@param numOutputs - number of outputs the network has
	//@param actFunHidden - activation function for the hidden layers
	//@param actFunOutput - activation function for the output layer
	public Network(ArrayList<ArrayList<Double>> genes, ArrayList<ArrayList<Double>> sigmas, int numInputs, int numHidLayers, int numHidNodes, int numOutputs, int actFunHidden, int actFunOutput) {
		layers = new ArrayList<Layer>();
		//create input layer with inputs number of nodes and a linear activation function
		layers.add(new Layer(numInputs, 1));
		
		//create hidden layers with hidNode number of nodes and given activation function
		for(int i = 0; i < numHidLayers; i++) {
			layers.add(new Layer(numHidNodes, actFunHidden));
		}
		
		//create output layer with outputs number of nodes and given activation function
		layers.add(new Layer(numOutputs, actFunOutput));
		
		int curGene = 0;
		//add weights to neurons based on genes
		for(Layer layer : layers){
			for(int i = 0; i < layer.size(); i++){
				layer.getNeuron(i).addWeights(genes.get(curGene));
				curGene++;
			}
		}
		
		this.genes = genes;
		this.sigmas = sigmas;
		//this block is tunable parameters
		this.learningRate = 0.01;
		this.mutationRate = 0.01;
		this.crossoverRate = 0.95;
		this.numInputs = numInputs;
		this.numHidLayers = numHidLayers;
		this.numHidNodes = numHidNodes;
		this.numOutputs = numOutputs;
		this.actFunHidden = actFunHidden;
		this.actFunOutput = actFunOutput;
	}

	//Randomly reset weights in network
	public void reset(){
		for(int i = 0; i < layers.size()-1; i++) {
			for(int j = 0; j < layers.get(i).size(); j++) {
				for(int k = 0; k < layers.get(i+1).size(); k++) {
					double weight = (random.nextDouble()*2)-1;
					layers.get(i).getNeuron(j).setWeightTo(k, weight);
				}
			}
		}
	}

	/*
	 * Backpropagates through the network, updating all weights based on the output of the network
	 * @param output: the expected output of network and nodes
	 */
	public void backprop(double output){
		//adjust all weights for MLP
		ArrayList<Double> oldDeltas = new ArrayList<Double>();
		for(int i = layers.size()-1; i > 0; i--) {
			ArrayList<Double> deltas = new ArrayList<Double>();
	
			for(int j = 0; j < layers.get(i).size(); j++) {	//iterate through output neurons
				Neuron outNeuron = layers.get(i).getNeuron(j);
				double delta;
				if(i == layers.size()-1) {	//updating output layer
					delta = output-outNeuron.getOutput()*outNeuron.derivActivate();
				}
				else {	//updating hidden layers
					delta = 0;
					for(int k = 0; k < oldDeltas.size(); k++) {
						delta += oldDeltas.get(k)*outNeuron.getWeightTo(k);
					}
					delta *= outNeuron.derivActivate();
				}
				deltas.add(delta);
				
				for(int k = 0; k < layers.get(i-1).size(); k++) {	//iterate through previous layer neurons
					Neuron inNeuron = layers.get(i-1).getNeuron(k);	
					double weight = inNeuron.getWeightTo(j);								
					weight += learningRate*delta*inNeuron.getOutput();
					layers.get(i-1).getNeuron(k).setWeightTo(j, weight);
				}
			}
			oldDeltas = deltas;
		}
	}
	
	//calulates the output of a network
	//@param inputs - the inputs for the network (from a sample)
	public void calcOutputs(double[] inputs) {
		//initialize input layer
		for(int i = 0; i < layers.get(0).size(); i++){
			layers.get(0).getNeuron(i).setOutput(inputs[i]);
		}
		
		//calculate output
		for(int i = 1; i < layers.size(); i++) {
			for(int j = 0; j < layers.get(i).size(); j++){
				ArrayList<Double> ins = new ArrayList<Double>();				//inputs to the neuron
				ArrayList<Double> weights = new ArrayList<Double>();			//corresponding weights to the neuron
				for(int k = 0; k < layers.get(i-1).size(); k++) {
					ins.add(layers.get(i-1).getNeuron(k).getOutput());
					weights.add(layers.get(i-1).getNeuron(k).getWeightTo(j));
				}
				layers.get(i).getNeuron(j).calculate(ins, weights);
			}
		}
	}

	/*
	 * Trains the neural network with backpropagation - calls function for backprop for details
	 * @param inputs: an array which stores the input values of a dataset
	 * @param output: stores the output value from the dataset
	 */
	public double train(double inputs[], double output){
		calcOutputs(inputs);

		//calculate error and back propagate
		double actualOutput = layers.get(layers.size()-1).getNeuron(0).getOutput();
		double error = Math.abs(actualOutput - output);
		backprop(output);
		return error;	//return absolute error
	}

	//evaluates the newtwork's fitness by calculating the error
	//@param samples - a list of samples, or the dataset
	public double evaluate(List<DataPoint> samples){	
		double error = 0;
		for(DataPoint sample : samples){
			calcOutputs(sample.getInputs());
			double actualOutput = layers.get(layers.size()-1).getNeuron(0).getOutput();
			error += Math.abs(actualOutput - sample.getClassVal());
		}	
		return error/samples.size();	//return average error
	}

	//prints out information about network
	public void printNetwork(){
		for(Layer l : layers) {
			l.printLayer(layers.indexOf(l)+1);
		}
	}
	
	//prints out information about the genes
	public void printGenes(){
		System.out.println("\nGenes:");
		for(int i = 0; i < genes.size(); i++){
			System.out.println(genes.get(i));
		}
		System.out.println();
	}
	
	//prints out sigma information
	public void printSigmas(){
		System.out.println("\nSigmas:");
		for(int i = 0; i < sigmas.size(); i++){
			System.out.println(sigmas.get(i));
		}
		System.out.println();
	}
	
	//returns the network's fitness rating
	public double getFitness() {
		return fitness;
	}
	
	//sets the network's fitness rating
	public void setFitness(double value) {
		fitness = value;
	}
	
	//returns the list of the network's genes
	public ArrayList<ArrayList<Double>> getGenes(){
		return genes;
	}
	
	//returns the list of the network's sigma values
	public ArrayList<ArrayList<Double>> getSigmas(){
		return sigmas;
	}
	
	//sets a network's individual gene
	public void setGene(int i, int j, double value){
		genes.get(i).set(j, value);
	}
	
	//sets a network's individual sigma value
	public void setSigma(int i, int j, double value){
		sigmas.get(i).set(j, value);
	}
	
	//returns the number of network inputs
	public int getNumInputs(){
		return numInputs;
	}
	
	//returns the number of hidden layers
	public int getNumHidLayers(){
		return numHidLayers;
	}
	
	//returns the number of hidden nodes
	public int getNumHidNodes(){
		return numHidNodes;
	}
	
	//returns the number of outputs of the network
	public int getNumOutputs(){
		return numOutputs;
	}
	
	//returns the network's activation function for the hidden layers
	public int getActFunHidden() {
		return actFunHidden;
	}
	
	//returns the activation function for the output layer
	public int getActFunOutput() {
		return actFunOutput;
	}
	
	//returns the mutation rate of the network
	public double getMutationRate(){
		return mutationRate;
	}
	
	//returns the learning rate for assessment of convergence rate
	public double getLearningRate(){
		return learningRate;
	}

	//compares the fitness of two networks
	@Override
	public int compareTo(Network o) {
		if(this.fitness < o.fitness)
			return 1;
		return -1;
	}
}

