import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class DigitTrain
{
	static int[] numTrainingImages = new int[] { 5923, 6742, 5958, 6131, 5842, 5421, 5918, 6265, 5851, 5949 }; // numTrainingImages[i] corresponds to the number of training images for the
																												// number i
	static int[] numTestingImages = new int[] { 980, 1135, 1032, 1010, 982, 892, 958, 1028, 974, 1009 };// numTestingImages[i] corresponds to the number of testing images for the
																										// number i
	static NeuralNet net;
	private static Random rand = new Random();
	
	private static int rightAnswers = 0;
	
	final static File digitNetInfo = new File("DigitNetInfo.txt");
	
	static double sumCost;
	
	private static int[] numSuccess = new int[10];
	
	public static void main(String[] args)
	{
		net = new NeuralNet(new int[] { 784, 24, 10 });
		load();
		// net.setLearningRate(.2);
		// // net.randomizeWeights(-.2,.2);
		// // net.randomizeBiases(-4,4);
		// net.randomize(1);
		// runTrainingSet(12000, false);
		runTestingSet(1000);
		
	}
	
	/**
	 * Sets the static net of the class
	 * 
	 * @param net to be trained
	 */
	public void setNet(NeuralNet net)
	{
		if (net.getInputLayer().length != 784 || net.getOutputLayer().length != 10) try
		{
			throw new InvalidNetworkException();
		}
		catch (InvalidNetworkException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DigitTrain.net = net;
	}
	
	/**
	 * 
	 * method that trains the network via a certain number of randomly ordered int
	 * arrays of the 10 numerical digits
	 * 
	 * @param numTrains number of 10 digit blocks of randomly arranged digits the
	 *        network is trained by
	 * @param loadNet boolean dictating whether or not the network is loaded from
	 *        the text file
	 */
	public static void runTrainingSet(int numTrains, boolean loadNet)
	{
		rightAnswers = 0;
		int prevRightAnswers = 0;
		if (loadNet) load();
		for (int i = 1; i < numTrains + 1; i++)
		{
			train();
			if (i % 100 == 0)
			{
				System.out.println("Trained " + i + " times with " + (double) (rightAnswers - prevRightAnswers) / 10 + "% success");
				prevRightAnswers = rightAnswers;
			}
		}
		System.out.println("Overall training accuracy of: " + (double) 10 * rightAnswers / numTrains + "%");
		System.out.println(Arrays.toString(numSuccess));
		save();
	}
	
	/**
	 * Tests the accuracy of the network
	 * 
	 * @param numTests number of tests using randomly ordered int arrays of the 10
	 *        numerical digits
	 * 
	 * @return overall accuracy of the testing
	 */
	public static double runTestingSet(int numTests)
	{
		numSuccess = new int[10];
		rightAnswers = 0;
		int prevRightAnswers = 0;
		for (int i = 1; i < numTests + 1; i++)
		{
			test();
			if (i % 100 == 0)
			{
				System.out.println("Tested " + i + " times with " + (double) (rightAnswers - prevRightAnswers) / 10 + "% success");
				prevRightAnswers = rightAnswers;
			}
		}
		System.out.println("Overall testing accuracy of: " + (double) 10 * rightAnswers / numTests + "%");
		System.out.println(Arrays.toString(numSuccess));
		return (double) 10 * rightAnswers / numTests;
	}
	
	/**
	 * Trains the network by randomly selecting data and backpropagating it
	 */
	public static void train()
	{
		int[] dig = permdig();
		int num;
		for (int i = 0; i < 10; i++)
		{
			num = dig[i];
			int trainingNum = rand.nextInt(numTrainingImages[num]) + 1;
			BufferedImage img = null;
			try
			{
				img = ImageIO.read(new File("Training Data//" + num + "_0" + trainingNum + ".png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			double[] in = new double[784];
			for (int x = 0; x < 28; x++)
			{
				for (int y = 0; y < 28; y++)
				{
					in[28 * y + x] = 1 - (double) ((img.getRGB(x, y) << 8) >>> 8) / 0xffffff;
				}
			}
			double[] desiredOut = new double[10];
			desiredOut[num] = 1;
			// centerDrawBoard(in);
			net.feedForward(in);
			// net.setLearningRate(net.cost(desiredOut));
			sumCost += net.cost(desiredOut);
			double max = -1;
			int maxIn = -1;
			for (int count = 0; count < 10; count++)
			{
				if (net.getOutputLayer()[count] > max)
				{
					max = net.getOutputLayer()[count];
					maxIn = count;
				}
			}
			if (maxIn == num)
			{
				rightAnswers++;
				numSuccess[num]++;
			}
			net.backprop(desiredOut);
		}
	}
	
	/**
	 * tests the network, no backpropagation
	 */
	public static void test()
	{
		int[] dig = permdig();
		int num;
		for (int i = 0; i < 10; i++)
		{
			num = dig[i];
			int testingNum = rand.nextInt(numTestingImages[num]) + 1;
			BufferedImage img = null;
			try
			{
				img = ImageIO.read(new File("Testing Data//" + num + "_0" + testingNum + ".png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			double[] in = new double[784];
			for (int x = 0; x < 28; x++)
			{
				for (int y = 0; y < 28; y++)
				{
					in[28 * y + x] = 1 - (double) ((img.getRGB(x, y) << 8) >>> 8) / 0xffffff;
				}
			}
			// centerDrawBoard(in);
			net.feedForward(in);
			double max = -2;
			int maxIn = -1;
			for (int count = 0; count < 10; count++)
			{
				if (net.getOutputLayer()[count] > max)
				{
					max = net.getOutputLayer()[count];
					maxIn = count;
				}
			}
			if (maxIn == num)
			{
				rightAnswers++;
				numSuccess[num]++;
			}
		}
	}
	
	/**
	 * @return random permutation of the numbers 0-9
	 */
	public static int[] permdig()
	{
		int[] out = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int i;
		int j;
		for (int count = 0; count < 20; count++)
		{
			i = rand.nextInt(10);
			j = rand.nextInt(10);
			int temp = out[i];
			out[i] = out[j];
			out[j] = temp;
		}
		return out;
	}
	
	/**
	 * saves the weights and biases of the network
	 */
	@SuppressWarnings("resource") public static void save()
	{
		int[] netDim = new int[net.activations.length];
		for (int i = 0; i < net.activations.length; i++)
		{
			netDim[i] = net.activations[i].length;
		}
		try
		{
			PrintStream netInfoPrintStream = new PrintStream(digitNetInfo);
			netInfoPrintStream.println(Arrays.toString(netDim));
			netInfoPrintStream.println(Arrays.deepToString(net.weights));
			netInfoPrintStream.println(Arrays.deepToString(net.biases));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Saved Successfully");
	}
	
	/**
	 * loads the weights and biases of the network
	 */
	public static void load()
	{
		
		try
		{
			Scanner netInfoScan = new Scanner(digitNetInfo);
			String dimensionString = netInfoScan.nextLine();
			net = new NeuralNet(ArrayParser.parseIntArray(dimensionString));
			String weightString = netInfoScan.nextLine();
			net.weights = ArrayParser.parseDouble3DArray(weightString);
			String biasString = netInfoScan.nextLine();
			net.biases = ArrayParser.parseDouble2DArray(biasString);
			netInfoScan.close();
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Loaded Successfully");
	}
	
	public static void getReport()
	{
		load();
		int count = 0;
		double sumAbs = 0;
		double sum = 0;
		for (int i = 0; i < net.weights.length; i++)
		{
			for (int j = 0; j < net.weights[i].length; j++)
			{
				for (int k = 0; k < net.weights[i][j].length; k++)
				{
					count++;
					sumAbs += Math.abs(net.weights[i][j][k]);
					sum += net.weights[i][j][k];
				}
			}
		}
		System.out.println("Average of absolute values of weights: " + sumAbs / count);
		System.out.println("Average weight: " + sum / count);
		System.out.println();
		
		count = 0;
		sum = 0;
		for (int i = 0; i < net.biases.length; i++)
		{
			for (int j = 0; j < net.biases[i].length; j++)
			{
				count++;
				sumAbs += Math.abs(net.biases[i][j]);
				sum += net.biases[i][j];
			}
		}
		System.out.println("Average of absolute value of biases: " + sumAbs / count);
		System.out.println("Average bias: " + sum / count);
	}
	
	/**
	 * 
	 * Centers images defined by in.
	 * @param in Double array representation of the greyscale values of the pixels in an image.
	 */
	public static void centerDrawBoard(double[] in)
	{
		int numPts = 0;
		double sumX = 0;
		double sumY = 0;
		for (int y = 0; y < 28; y++)
		{
			for (int x = 0; x < 28; x++)
			{
				if (in[28 * y + x] != 0)
				{
					numPts++;
					sumX += x;
					sumY += y;
				}
			}
		}
		int shiftX = (int) (sumX / numPts - 14);
		int shiftY = (int) (sumY / numPts - 14);
		
		double[] drawBoardCopy = Arrays.copyOf(in, in.length);
		for (int y = 0; y < 28; y++)
		{
			for (int x = 0; x < 28; x++)
			{
				try
				{
					in[28 * y + x] = drawBoardCopy[28 * (y + shiftY) + (x + shiftX)];
				}
				catch (IndexOutOfBoundsException e)
				{
					in[28 * y + x] = 0;
				}
			}
		}
	}
}