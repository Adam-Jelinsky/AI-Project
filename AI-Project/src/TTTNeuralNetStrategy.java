import java.util.Arrays;

/**
 * Tic-tac-toe strategy class that makes moves based on instructions from a
 * neural network.
 * 
 * @author Adam
 *
 */
public class TTTNeuralNetStrategy extends TTTStrategy
{
	public static void main(String[] args)
	{
		// TicTacToe game = new TicTacToe();
		// NeuralNetStrategy strat = new NeuralNetStrategy();
		// RandomStrategy random = new RandomStrategy();
		// game.setStratO(strat);
		// game.setStratX(random);
		// for (int i = 0; i < 1000; i++)
		// {
		// game.aiMove();
		// }
		NeuralNet net = new NeuralNet(new int[] { 3, 4, 3 });
		net.feedForward(new double[] { 1, 0, 1 });
		net.backprop(new double[] { 1, 1, 1 });
		System.out.println(Arrays.toString(net.feedForward(new double[] { 1, 0, 1 })));
	}
	
	NeuralNet net = new NeuralNet(new int[] { 9,  9 });
	
	/**
	 * Default constructor, randomizes the weights/biases of the network on the
	 * interval [-1,1] and sets the learning rate to 1.
	 */
	public TTTNeuralNetStrategy()
	{
		net.randomize(-1, 1);
		net.setLearningRate(1);
	}
	
	/**
	 * Constructor that takes in a TicTacTOe object.
	 * 
	 * @param game Game the strategy will play on.
	 */
	public TTTNeuralNetStrategy(TicTacToe game)
	{
		super(game);
		net.randomize(-1, 1);
		net.setLearningRate(1);
	}
	
	/**
	 * Constructor that takes in a neural net.
	 * 
	 * @param net Neural network for the object.
	 */
	public TTTNeuralNetStrategy(NeuralNet net)
	{
		try
		{
			if (net.getInputLayer().length != 9) throw new InvalidNetworkException();
			if (net.getOutputLayer().length != 9) throw new InvalidNetworkException();
		}
		catch (InvalidNetworkException e)
		{
			System.out.println("Input Length: " + net.getInputLayer().length);
			System.out.println("Output Length: " + net.getOutputLayer().length);
			e.printStackTrace();
		}
		this.net = net;
	}
	
	/**
	 * @see TTTStrategy#move()
	 */
	@Override public void move()
	{
		double[] in = new double[9];
		for (int i = 0; i < 9; i++)
		{
			in[i] = game.getSpot(i);
		}
		double[] out = net.feedForward(in);
		double[] outCopy = Arrays.copyOf(out, out.length); // not copying the array messes with NetViewer
		int maxPos = 0;
		do
		{
			for (int i = 0; i < 9; i++)
			{
				if (outCopy[i] > outCopy[maxPos]) maxPos = i;
			}
			outCopy[maxPos] = -1;
		}
		while (!game.nextTurn(maxPos));
	}
	
	@Override public int getStrategyType()
	{
		return NEURAL_NET;
	}
	
}
