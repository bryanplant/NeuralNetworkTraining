package network;

import java.util.ArrayList;

public class Neuron {
	private int actFun;
	private ArrayList<Double> weights;
	private double output;

	/*
	 * @param actFun: activation function to be used
	 */
	public Neuron(int actFun) {
		this.actFun = actFun;
		weights = new ArrayList<Double>();
	}

	//the function to calculate the output of the activation function of the neuron
	public void activate() {
		switch (actFun) {
		case 1:
			break;
		case 2:						//sigmoidal - logistic
			output = 1/(1+Math.exp(-output));
			break;
		}
	}
	
	//returns derivative of activation function
	public double derivActivate() {
		switch (actFun) {
		case 1:
			return 1;
		case 2:						//sigmoidal - logistic
			return output * (1-output);
		default:
			return 0;
		}
	}

	//adds a connection to the previous layer neuron and the weight of the connection
	public void addWeight(double weight){
		weights.add(weight);
	}

	/*
	 * calculates the output of the neuron
	 * @param ins: inputs to the neuron
	 * @param weights: corresponding weights of inputs
	 */
	public void calculate(ArrayList<Double> ins, ArrayList<Double> weights){
		output = 0;
		for(int i = 0; i < ins.size(); i++){
			output+=ins.get(i)*weights.get(i);
		}
		activate(); //call activation function to adjust output
	}

	//returns the weight of the connection this neuron has
	public double getWeightTo(int index){
		return weights.get(index);
	}

	//sets the weight of the connection this neuron is the end of
	public void setWeightTo(int index, double weight) {
		weights.set(index, weight);
	}
	
	public void addWeights(ArrayList<Double> weights){
		this.weights.addAll(weights);
	}

	//prints out info about neuron
	public void printNeuron(int num){
		System.out.println("\tNode " + num + ":");

		for(int i = 0; i < weights.size(); i++){
			System.out.println("\t  Connection " + (i+1) + " Weight: " + weights.get(i));
		}

		System.out.println("\t  Output: " + output);
	}

	//returns the neuron's output
	public double getOutput(){
		return output;
	}

	//set's the neuron's output (if special layer)
	public void setOutput(double value){
		output = value;
	}

	//returns the neuron's activation function
	public int getActFun(){
		return actFun;
	}

	//returns this neuron's connection list
	public ArrayList<Double> getWeights() {
		return weights;
	}
}


