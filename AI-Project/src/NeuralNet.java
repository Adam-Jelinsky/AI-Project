import java.util.InputMismatchException;
import java.util.Random;

/**
 * This class is for a neural net. A video overview of which can be found here:
 * https://www.youtube.com/watch?v=aircAruvnKk
 * 
 * The math for the backpropagation algorithm I used can be found here:
 * http://neuralnetworksanddeeplearning.com/chap2.html
 * 
 * @author Adam
 *
 */
public class NeuralNet
{
	
	protected double activations[][];
	protected double weights[][][];
	protected double biases[][];
	protected double learningRate = 1;
	
	/**
	 * Constructor for the neural network, there must be at least 2 values in the
	 * input.
	 * 
	 * @param layerSizes The size of each layer in the net. There must be at least 2
	 *        values in the array, the first for the input layer, and the last for
	 *        the output layer.
	 */
	public NeuralNet(int[] layerSizes)
	{
		activations = new double[layerSizes.length][];
		for (int i = 0; i < layerSizes.length; i++)
		{
			activations[i] = new double[layerSizes[i]];
		}
		biases = new double[activations.length][];
		weights = new double[activations.length - 1][][];
		biases[0] = new double[activations[0].length];
		for (int layer = 1; layer < activations.length; layer++)
		{
			weights[layer - 1] = new double[activations[layer].length][activations[layer - 1].length];
			biases[layer] = new double[activations[layer].length];
		}
	}
	
	/**
	 * Feeds forward a double array through the network.
	 * 
	 * @param inputs The values in the input layer. Must be the same size as the
	 *        input layer.
	 * 
	 * @return The values in the output layer
	 */
	public double[] feedForward(double[] inputs)
	{
		if (inputs.length != activations[0].length) throw new InputMismatchException();
		activations[0] = inputs;
		return feedForward(1);
	}
	
	/**
	 * Helper method for the feedForward() method.
	 * 
	 * @param layer The layer at which backpropagation is occurring.
	 * 
	 * @return The output layer.
	 */
	private double[] feedForward(int layer)
	{
		if (layer >= activations.length) return activations[layer - 1];
		
		for (int i = 0; i < activations[layer].length; i++)
		{
			activations[layer][i] = sigmoid(VectorOperation.dotProd(activations[layer - 1], weights[layer - 1][i]) + biases[layer][i], layer);
		}
		return feedForward(layer + 1);
	}
	
	/**
	 * Backpropagates the network.
	 * 
	 * @param desiredOut The intended output for the network, which is used to "fix"
	 *        the network.
	 */
	public void backprop(double[] desiredOut)
	{
		
		double[] error = VectorOperation.subtract(desiredOut, activations[activations.length - 1]);
		for (int i = 0; i < error.length; i++)
			error[i] *= sigmoidDerivative(activations[activations.length - 1][i], activations.length - 1);
		backpropLayerWeights(activations.length - 1, error);
		backpropLayerBiases(activations.length - 1, error);
		for (int i = activations.length - 2; i > 0; i--)
		{
			error = VectorOperation.transposeMatrixMult(weights[i], error);
			for (int j = 0; j < error.length; j++)
				error[j] *= sigmoidDerivative(activations[i][j], i);
			backpropLayerWeights(i, error);
			backpropLayerBiases(i, error);
		}
		// error = VectorOperation.transposeMatrixMult(weights[0], error);
		// for (int j = 0; j < error.length; j++)
		// error[j] *= sigmoidDerivative(activations[0][j], 0);
		// backpropLayerBiases(0, error);
		// if (++numBatches == batchSize)
		// {
		// numBatches = 0;
		// deltaBiases = VectorOperation.scalarMult(deltaBiases, (double) 1 /
		// batchSize);
		// deltaWeights = VectorOperation.scalarMult(deltaWeights, (double) 1 /
		// batchSize);
		// biases = VectorOperation.add(biases, deltaBiases);
		// weights = VectorOperation.add(weights, deltaWeights);
		// deltaBiases = VectorOperation.scalarMult(deltaBiases, 0);
		// deltaWeights = VectorOperation.scalarMult(deltaWeights, 0);
		// }
	}
	
	/**
	 * Does the weights portion of the backpropagation algorithm. Helper method for
	 * the backprop() method.
	 * 
	 * @param layer Layer number where the backpropagation occurs.
	 * @param error The error vector.
	 */
	private void backpropLayerWeights(int layer, double[] error)
	{
		for (int i = 0; i < weights[layer - 1].length; i++)
		{
			for (int j = 0; j < weights[layer - 1][i].length; j++)
			{
				weights[layer - 1][i][j] += learningRate * activations[layer - 1][j] * error[i];
			}
		}
	}
	
	/**
	 * Does the biases portion of the backpropagation algorithm. Helper method for
	 * the backprop() method.
	 * 
	 * @param layer Layer number where the backpropagation occurs.
	 * @param error The error vector.
	 */
	private void backpropLayerBiases(int layer, double[] error)
	{
		for (int i = 0; i < biases[layer].length; i++)
		{
			biases[layer][i] += learningRate * error[i];
		}
	}
	
	/**
	 * Cost function of the network. Must be used AFTER info is fed forward.
	 * 
	 * @param expectedOut The expected output of the network.
	 * @return The square of the distance between the actual and expected output
	 *         vectors.
	 */
	public double cost(double[] expectedOut)
	{
		double out = 0;
		double dOut;
		for (int i = 0; i < expectedOut.length; i++)
		{
			dOut = activations[activations.length - 1][i] - expectedOut[i];
			out += dOut * dOut;
		}
		return out;
	}
	
	/**
	 * Collapsing function of the network.
	 * 
	 * @param in Input value of the function.
	 * @param layer Layer of the neuron the function is being used for.
	 * @return The value of the collapsing function given the input in and layer
	 *         layer.
	 */
	protected double sigmoid(double in, int layer)
	{
		if (layer == activations.length - 1) return 1 / (1 + Math.exp(-in));
		return 2 / (1 + Math.exp(-in)) - 1;
	}
	
	/**
	 * 
	 * @param sigValue Value of the sigmoid function at a certain in value.
	 * @param layer Layer of the neuron where this function is used for.
	 * @return Derivative of the collapsing function in terms of the collapsing
	 *         function. If the collapsing function is overridden, then this function must be
	 *         overridden as well to match.
	 */
	protected double sigmoidDerivative(double sigValue, int layer)
	{
		if (layer == activations.length - 1) return sigValue * (1 - sigValue);
		return (1 - sigValue * sigValue) / 2;
	}
	
	/**
	 * Randomizes the weights on the interval [min,max].
	 * 
	 * @param min Minimum value of the weights.
	 * @param max Maximum value of the weights.
	 */
	public void randomizeWeights(double min, double max)
	{
		Random rand = new Random();
		for (int i = 0; i < weights.length; i++)
		{
			for (int j = 0; j < weights[i].length; j++)
			{
				for (int k = 0; k < weights[i][j].length; k++)
				{
					weights[i][j][k] = min + (max - min) * rand.nextDouble();
				}
			}
		}
	}
	
	/**
	 * Randomizes the biases on the interval [min,max].
	 * 
	 * @param min Minimum value of the biases.
	 * @param max Maximum value of the biases.
	 */
	public void randomizeBiases(double min, double max)
	{
		Random rand = new Random();
		for (int i = 0; i < biases.length; i++)
		{
			for (int j = 0; j < biases[i].length; j++)
			{
				biases[i][j] = min + (max - min) * rand.nextDouble();
			}
		}
	}
	
	/**
	 * Randomizes the weights and biases on the interval [min,max].
	 * 
	 * @param min Minimum value of the weights/biases.
	 * @param max Maximum value of the weights/biases.
	 */
	public void randomize(double min, double max)
	{
		randomizeWeights(min, max);
		randomizeBiases(min, max);
	}
	
	/**
	 * Randomizes the weights on the interval [-r,r].
	 * 
	 * @param r Value of the maximum and -1 * the minimum. If negative, it will be
	 *        made positive.
	 */
	public void randomize(double r)
	{
		r = Math.abs(r);
		randomize(-1 * r, r);
	}
	
	/**
	 * @return Input layer of the neural network.
	 */
	public double[] getInputLayer()
	{
		return activations[0];
	}
	
	/**
	 * @return Output layer of the neural network.
	 */
	public double[] getOutputLayer()
	{
		return activations[activations.length - 1];
	}
	
	/**
	 * @return Learning rate of the neural network.
	 */
	public double getLearningRate()
	{
		return learningRate;
	}
	
	/**
	 * Sets learning rate of the neural network.
	 * 
	 * @param learningRate The new learning rate of the network.
	 */
	public void setLearningRate(double learningRate)
	{
		this.learningRate = learningRate;
	}
}
