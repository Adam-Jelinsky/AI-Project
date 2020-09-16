import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Creates a visual representation of a neural network.
 * 
 * @author Adam
 *
 */
class NetViewer extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	NeuralNet net;
	public boolean showBiases;
	double maxMin = 1;
	double maxWeight = maxMin;
	double maxBias = maxMin;
	double minWeight = -1 * maxMin;
	double minBias = -1 * maxMin;
	
	/**
	 * Constructor that takes in a NeuralNet and boolean. Defaults showBiases to
	 * false.
	 * 
	 * @param net NeuralNet to be displayed.
	 * @param showBiases Boolean Representation of whether or not to display the
	 *        biases.
	 */
	NetViewer(NeuralNet net)
	{
		this.net = net;
		this.showBiases = false;
		this.addKeyListener(new KeyListener()
		{
			
			@Override public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyChar() == 'b')
				{
					alternateShowBiases();
					repaint();
				}
			}
			
			@Override public void keyReleased(KeyEvent arg0)
			{
				
			}
			
			@Override public void keyTyped(KeyEvent arg0)
			{
				
			}
			
		});
	}
	
	/**
	 * Constructor that takes in a NeuralNet and boolean.
	 * 
	 * @param net NeuralNet to be displayed.
	 * @param showBiases Boolean Representation of whether or not to display the
	 *        biases.
	 */
	NetViewer(NeuralNet net, boolean showBiases)
	{
		this.net = net;
		this.showBiases = showBiases;
		this.addKeyListener(new KeyListener()
		{
			
			@Override public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyChar() == 'b')
				{
					alternateShowBiases();
					repaint();
				}
			}
			
			@Override public void keyReleased(KeyEvent arg0)
			{
				
			}
			
			@Override public void keyTyped(KeyEvent arg0)
			{
				
			}
			
		});
	}
	
	/**
	 * Toggles wether or not the biases are to be displayed.
	 */
	public void alternateShowBiases()
	{
		this.showBiases = !this.showBiases;
	}
	
	/**
	 * Paint method.
	 */
	@Override public void paint(Graphics g)
	{
		// painting all activation values
		// OR biases for hidden layers and activations for input/output layers
		double newMaxBias = Double.MIN_NORMAL;
		double newMinBias = -1 * Double.MIN_NORMAL;
		for (int col = 0; col < net.activations.length; col++)
		{
			int squareWidth = Math.min(((getWidth() - 200) / net.activations.length), ((getHeight() - 200) / net.activations[col].length)) - 10;
			for (int row = 0; row < net.activations[col].length; row++)
			{
				if (col == 0) // painting input layer activations
				{
					try
					{
						if (net.activations[col][row] > 0) g.setColor(new Color(0, (int) (255 * net.activations[col][row]), 0));
						else g.setColor(new Color((int) (-255 * net.activations[col][row]), 0, 0));
					}
					catch (IllegalArgumentException e)
					{
						
					}
					g.fillRect(100 + col * ((getWidth() - 200) / net.activations.length), 100 + row * (((getHeight() - 200) / net.activations[col].length)), squareWidth, squareWidth);
				}
				else
				{
					if (col != net.activations.length - 1)
					{
						if (showBiases) // painting hidden layer biases
						{
							try
							{
								if (net.biases[col][row] > 0) g.setColor(new Color(0, (int) (255 * net.biases[col][row] / maxBias), 0));
								else g.setColor(new Color((int) (255 * net.biases[col][row] / minBias), 0, 0));
							}
							catch (IllegalArgumentException e)
							{
								if (net.biases[col][row] > 0) g.setColor(Color.GREEN);
								else g.setColor(Color.RED);
							}
							if (net.biases[col][row] > newMaxBias) newMaxBias = net.biases[col][row];
							if (net.biases[col][row] < newMinBias) newMinBias = net.biases[col][row];
							g.fillRect(100 + col * ((getWidth() - 200) / net.activations.length), 100 + row * (((getHeight() - 200) / net.activations[col].length)), squareWidth, squareWidth);
						}
						else // painting hidden layer activations
						{
							try
							{
								if (net.activations[col][row] > 0) g.setColor(new Color(0, (int) (255 * net.activations[col][row]), 0));
								else g.setColor(new Color((int) (-255 * net.activations[col][row]), 0, 0));
							}
							catch (IllegalArgumentException e)
							{
								if (net.activations[col][row] > 0) g.setColor(Color.GREEN);
								else g.setColor(Color.RED);
							}
							g.fillRect(100 + col * ((getWidth() - 200) / net.activations.length), 100 + row * (((getHeight() - 200) / net.activations[col].length)), squareWidth, squareWidth);
						}
					}
					else // painting output layer activations
					{
						try
						{
							if (net.activations[col][row] > 0) g.setColor(new Color(0, (int) (255 * net.activations[col][row]), 0));
							else g.setColor(new Color((int) (-255 * net.activations[col][row]), 0, 0));
						}
						catch (IllegalArgumentException e)
						{
							if (net.activations[col][row] > 0) g.setColor(Color.GREEN);
							else g.setColor(Color.RED);
						}
						g.fillRect(100 + col * ((getWidth() - 200) / net.activations.length), 100 + row * (((getHeight() - 200) / net.activations[col].length)), squareWidth, squareWidth);
					}
					
				}
			}
		}
		
		// draws connections between neurons
		double newMaxWeight = Double.MIN_NORMAL;
		double newMinWeight = -1 * Double.MIN_NORMAL;
		for (int col = 1; col < net.activations.length; col++)
		{
			int squareWidth = Math.min(((getWidth() - 200) / net.activations.length), ((getHeight() - 200) / net.activations[col].length)) - 10;
			int prevSquareWidth = Math.min(((getWidth() - 200) / net.activations.length), ((getHeight() - 200) / net.activations[col - 1].length)) - 10;
			for (int row = 0; row < net.activations[col].length; row++)
			{
				for (int prevRow = 0; prevRow < net.activations[col - 1].length; prevRow++)
				{
					try
					{
						if (net.weights[col - 1][row][prevRow] > 0) g.setColor(new Color(0, (int) (255 * net.weights[col - 1][row][prevRow] / maxWeight), 0));
						else g.setColor(new Color((int) (255 * net.weights[col - 1][row][prevRow] / minWeight), 0, 0));
					}
					catch (IllegalArgumentException e)
					{
						if (net.weights[col - 1][row][prevRow] > 0) g.setColor(Color.GREEN);
						else g.setColor(Color.RED);
					}
					if (net.weights[col - 1][row][prevRow] > newMaxWeight) newMaxWeight = net.weights[col - 1][row][prevRow];
					if (net.weights[col - 1][row][prevRow] < newMinWeight) newMinWeight = net.weights[col - 1][row][prevRow];
					g.drawLine(100 + col * ((getWidth() - 200) / net.activations.length) + squareWidth / 2, 100 + row * (((getHeight() - 200) / net.activations[col].length)) + squareWidth / 2, 100 + (col - 1) * ((getWidth() - 200) / net.activations.length) + prevSquareWidth / 2, 100 + prevRow * (((getHeight() - 200) / net.activations[col - 1].length)) + prevSquareWidth / 2);
				}
			}
		}
		
		// creates new max/min values and repaints if necessary
		if (newMaxWeight != maxWeight || newMaxBias != maxBias || newMinWeight != minWeight || newMinBias != minBias)
		{
			maxWeight = newMaxWeight;
			minWeight = newMinWeight;
			maxBias = newMaxBias;
			minBias = newMinBias;
			this.paint(g);
		}
		
		// draws message at the top of the window
		g.setColor(Color.BLACK);
		String message = "";
		if (showBiases) message = "Max Weight: " + maxWeight + " Min Weight: " + minWeight + " MaxBias: " + maxBias + " Min Bias:" + minBias + " Show Biases: " + showBiases;
		else message = "Max Weight: " + maxWeight + " Min Weight: " + minWeight + " Show Biases: " + showBiases;
		g.clearRect(100, 90 - g.getFontMetrics().getAscent(), getWidth() - 100, g.getFontMetrics().getAscent());
		g.drawString(message, 100, 90);
	}
	
	/**
	 * Sets the max/min values of the displayed weights and biases.
	 * 
	 * @param maxMin Max value of the displayed weights and biases and -1*min value
	 *        of the displayed weights and biases.
	 */
	public void setMaxMin(double maxMin)
	{
		this.maxMin = Math.abs(maxMin);
		maxWeight = this.maxMin;
		maxBias = this.maxMin;
	}
}
