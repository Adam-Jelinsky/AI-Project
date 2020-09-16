import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class will take handwritten digits by the user and run it through a
 * neural network in order to recognize what the user wrote. This part is
 * supposed to demonstrate the more "intelligent" part of neural networks.
 * Meanwhile, the settings also allow to demonstrate the process of
 * training/testing a neural network.
 * 
 * @author Adam
 *
 */
public class DrawingPanel extends JFrame
{
	private static final long serialVersionUID = 1L;
	double[] drawBoard = new double[784];
	private int tileWidth = this.getWidth() / 28;
	private int tileHeight = this.getHeight() / 28;
	
	final static File digitNetInfo = new File("DigitNetInfo.txt");
	
	static NeuralNet net;
	
	BufferedImage numDrawing;
	
	static DrawingPanel drawPanel = new DrawingPanel();
	static DrawingControlPanel controlPanel = new DrawingControlPanel(drawPanel);
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					drawPanel.setVisible(true);
					drawPanel.setBounds(100, 100, 840, 840);
					drawPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					controlPanel.setVisible(true);
					controlPanel.setBounds(1000, 100, 500, 500);
					controlPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
	}
	
	/**
	 * Default constructor.
	 */
	public DrawingPanel()
	{
		load();
		repaint();
		this.addKeyListener(new KeyAdapter()
		{
			@Override public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyChar() == 'c')
				{
					centerDrawBoard();
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
		
		// code for drawing with mouse/touchscreen (both work)
		getContentPane().addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override public void mouseDragged(MouseEvent arg0)
			{
				tileWidth = getWidth() / 28;
				tileHeight = getHeight() / 28;
				if (drawBoard[28 * (arg0.getY() / tileHeight) + (arg0.getX() / tileWidth)] != 1)
				{
					int[] pos = new int[] { 1, 0, -1 };
					double colorScale;
					for (int i = 0; i < 3; i++)
					{
						for (int j = 0; j < 3; j++)
						{
							if (!((arg0.getY() / tileHeight) + pos[i] > 27 || (arg0.getY() / tileHeight) + pos[i] < 0 || (arg0.getX() / tileWidth) + pos[j] > 27 || (arg0.getX() / tileWidth) + pos[j] < 0))
							{
								if (pos[i] == pos[j] && pos[i] == 0) colorScale = 1;
								else if (Math.abs(pos[i]) == Math.abs(pos[j])) colorScale = .25;
								else colorScale = .5;
								drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] += colorScale;
								if (drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] > 1) drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] = 1;
							}
						}
					}
					
				}
				repaint();
			}
		});
		getContentPane().addMouseListener(new MouseAdapter()
		{
			@Override public void mousePressed(MouseEvent arg0)
			{
				tileWidth = getWidth() / 28;
				tileHeight = getHeight() / 28;
				if (drawBoard[28 * (arg0.getY() / tileHeight) + (arg0.getX() / tileWidth)] != 1)
				{
					int[] pos = new int[] { 1, 0, -1 };
					double colorScale;
					for (int i = 0; i < 3; i++)
					{
						for (int j = 0; j < 3; j++)
						{
							if (!((arg0.getY() / tileHeight) + pos[i] > 27 || (arg0.getY() / tileHeight) + pos[i] < 0 || (arg0.getX() / tileWidth) + pos[j] > 27 || (arg0.getX() / tileWidth) + pos[j] < 0))
							{
								if (pos[i] == pos[j] && pos[i] == 0) colorScale = 1;
								else if (Math.abs(pos[i]) == Math.abs(pos[j])) colorScale = .25;
								else colorScale = .5;
								drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] += colorScale;
								if (drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] > 1) drawBoard[28 * ((arg0.getY() / tileHeight) + pos[i]) + ((arg0.getX() / tileWidth) + pos[j])] = 1;
							}
						}
					}
					
				}
				repaint();
			}
		});
		
	}
	
	/**
	 * Paint method.
	 */
	public void paint(Graphics g)
	{
		tileWidth = getWidth() / 28;
		tileHeight = getHeight() / 28;
		for (int x = 0; x < 28; x++)
		{
			for (int y = 0; y < 28; y++)
			{
				g.setColor(new Color((int) ((1 - drawBoard[28 * y + x]) * 0xff), (int) ((1 - drawBoard[28 * y + x]) * 0xff), (int) ((1 - drawBoard[28 * y + x]) * 0xff)));
				g.fillRect(tileWidth * x, tileHeight * y, tileWidth, tileHeight);
			}
		}
	}
	
	/**
	 * Loads the weights & biases for the neural net from a saved .txt file.
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
		System.out.println("Successfully Loaded");
	}
	
	/**
	 * Feeds the image drawn by the user into the neural net and displays the output
	 * on the control panel.
	 */
	public void feedImage()
	{
		
		net.feedForward(drawBoard);
		double max = -1;
		for (int count = 0; count < 10; count++)
		{
			if (net.getOutputLayer()[count] > max)
			{
				max = net.getOutputLayer()[count];
			}
		}
		drawBoard = new double[784];
	}
	
	/**
	 * Centers the image drawn by the user.
	 */
	public void centerDrawBoard()
	{
		int numPts = 0;
		double sumX = 0;
		double sumY = 0;
		for (int y = 0; y < 28; y++)
		{
			for (int x = 0; x < 28; x++)
			{
				if (drawBoard[28 * y + x] != 0)
				{
					numPts++;
					sumX += x;
					sumY += y;
				}
			}
		}
		int shiftX = (int) (sumX / numPts - 14);
		int shiftY = (int) (sumY / numPts - 14);
		
		double[] drawBoardCopy = Arrays.copyOf(drawBoard, drawBoard.length);
		for (int y = 0; y < 28; y++)
		{
			for (int x = 0; x < 28; x++)
			{
				try
				{
					drawBoard[28 * y + x] = drawBoardCopy[28 * (y + shiftY) + (x + shiftX)];
				}
				catch (IndexOutOfBoundsException e)
				{
					drawBoard[28 * y + x] = 0;
				}
			}
		}
	}
	
	/**
	 * Sets visibility of the control panel.
	 * 
	 * @param b Boolean representation of whether the control panel is visible or
	 *        not.
	 */
	public void setControlPanelVisible(boolean b)
	{
		controlPanel.setVisible(b);
	}
}

/**
 * Control panel class for the drawing panel in order for it to exist as a
 * separate screen because I'm too lazy to design a proper UI.
 * 
 * @author Adam
 *
 */
class DrawingControlPanel extends JFrame
{
	private static final long serialVersionUID = 1L;
	DrawingPanel dPanel;
	String info = "";
	
	Rectangle guessButtonRectangle = new Rectangle((int) (.1 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
	Rectangle clearButtonRectangle = new Rectangle((int) (.6 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
	
	public DrawingControlPanel(DrawingPanel dPanel)
	{
		getContentPane().addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent arg0)
			{
				Rectangle guessButton = new Rectangle((int) (.1 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
				Rectangle clearButton = new Rectangle((int) (.6 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
				if (guessButton.contains(arg0.getPoint()))
				{
					dPanel.centerDrawBoard();
					dPanel.feedImage();
					int maxIn = 0;
					for (int i = 0; i < 10; i++)
					{
						if (DrawingPanel.net.getOutputLayer()[i] > DrawingPanel.net.getOutputLayer()[maxIn]) maxIn = i;
					}
					info = "I think that is a " + maxIn;
					
					// orders confidence values by size and creates int[] of the indecies in order
					// of the confidence intervals
					double[] layer = Arrays.copyOf(DrawingPanel.net.getOutputLayer(), DrawingPanel.net.getOutputLayer().length);
					int[] ranking = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
					for (int i = 0; i < 10; i++)
					{
						int maxIndex = i;
						for (int j = i + 1; j < 10; j++)
						{
							if (layer[maxIndex] < layer[j]) maxIndex = j;
						}
						double temp = layer[i];
						layer[i] = layer[maxIndex];
						layer[maxIndex] = temp;
						int tempInt = ranking[i];
						ranking[i] = ranking[maxIndex];
						ranking[maxIndex] = tempInt;
					}
					
					info += "\noutput layer order: " + Arrays.toString(ranking);
					dPanel.repaint();
					repaint();
				}
				if (clearButton.contains(arg0.getPoint()))
				{
					dPanel.drawBoard = new double[784];
					dPanel.repaint();
					repaint();
				}
			}
		});
	}
	
	/**
	 * Paint method.
	 */
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.GREEN);
		g.fillRect((int) (.1 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
		g.setColor(Color.RED);
		g.fillRect((int) (.6 * getWidth()), (int) (.1 * getHeight()), (int) (.3 * getWidth()), (int) (.2 * getHeight()));
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SERIF, Font.BOLD, 18));
		g.drawString("Guess The Number", (int) (.25 * getWidth() - (g.getFontMetrics().stringWidth("Guess The Number") / 2)), (int) (.2 * getWidth()));
		g.drawString("Clear Drawing", (int) (.75 * getWidth() - (g.getFontMetrics().stringWidth("Clear Drawing") / 2)), (int) (.2 * getWidth()));
		String[] split = info.split("\n");
		for (int i = 0; i < split.length; i++)
		{
			g.drawString(split[i], (int) (.5 * getWidth() - g.getFontMetrics().stringWidth(split[i]) / 2), (int) (.4 * getHeight() + i * g.getFontMetrics().getAscent()));
		}
	}
	
}
