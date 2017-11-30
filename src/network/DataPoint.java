package network;

/* Represents a single data point in the cluster, and contains a list of inputs and then 
 * a class value.
 */
public class DataPoint {
	private double[] inputs;
	private int classVal;

	//create a sample by passing in an array created in main from function
	//@param inputs: the array of input data and correlated output
	public DataPoint(double[] inputs, int classVal) {
		this.inputs = inputs;
		this.classVal = classVal;
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
	public int getClassVal() {
		return classVal;
	}
}
