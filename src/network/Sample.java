package network;

/* Represents a single data sample - that is, a single output and the inputs
 * that produced it using the Rosenbrock function. The length of the array
 * corresponds to the dimension.
 */
public class Sample {
	//input array
	private double[] inputs;
	private double output;

	//create a sample by passing in an array created in main from function
	//@param inputs: the array of input data and correlated output
	public Sample(double[] inputs, double output) {
		this.inputs = inputs;
		this.output = output;
	}

	//returns input at a specific index
	//@param index: the index of the array needing to be returned
	public double getInput(int index) {
		return inputs[index];
	}

	//returns all the input values in the sample
	public double[] getInputs(){
		return inputs;
	}

	//returns output (located at array's end)
	public double getOutput() {
		return output;
	}
}
