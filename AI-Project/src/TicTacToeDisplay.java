import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * 
 * This class is just used to display the tic tac toe game, the neural network
 * you play against, and a win counter.
 * 
 * The purpose of this part of the program is to give a visualization for what a
 * neural net is doing behind the scenes while also giving an example of what
 * kinds of tasks neural nets can perform.
 * 
 * @author Adam
 * 
 *
 */
public class TicTacToeDisplay extends JFrame
{
	private static final long serialVersionUID = 1L;
	private TicTacToe game = new TicTacToe();
	protected int oWins = 0;
	protected int xWins = 0;
	
	final File netInfo = new File("NetInfo.txt");
	static NeuralNet net = new NeuralNet(new int[] { 9, 27, 27, 9 });
	
	static InfoPanel info = new InfoPanel();
	static NetViewer netViewer = new NetViewer(net, true);
	static TicTacToeDisplay frame = new TicTacToeDisplay();
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					frame.setVisible(true);
					frame.setBounds(0, 0, 800, 800);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
	}
	
	/**
	 * Create the panel.
	 */
	public TicTacToeDisplay()
	{
		// addComponentListener(new ComponentAdapter()
		// {
		// @Override public void componentShown(ComponentEvent arg0)
		// {
		// info.setVisible(frame.isVisible());
		// info.setBounds(0, 800, 0, 0);
		// info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// frame.setBounds(0, 0, 800, 800);
		// frame.setVisible(frame.isVisible());
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// netViewer.setBounds(800, 0, 1000, 1000);
		// netViewer.setVisible(frame.isVisible());
		// netViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// }
		//
		// @Override public void componentHidden(ComponentEvent arg0)
		// {
		// info.setVisible(frame.isVisible());
		// info.setBounds(0, 800, 0, 0);
		// info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// frame.setBounds(0, 0, 800, 800);
		// frame.setVisible(frame.isVisible());
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// netViewer.setBounds(800, 0, 1000, 1000);
		// netViewer.setVisible(frame.isVisible());
		// netViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// }
		// });
		netViewer.setMaxMin(1);
		net.setLearningRate(.1);
		load();
		info.setString("O wins: " + oWins + " X wins: " + xWins);
		game = new TicTacToe(new TTTNeuralNetStrategy(net), new TTTPlayerStrategy());
		TicTacToeBoard gamePanel = new TicTacToeBoard(game);
		
		Timer update = new Timer(0, new ActionListener()
		{
			
			@Override public void actionPerformed(ActionEvent arg0)
			{
				if (!game.playerMove())
				{
					repaint();
					netViewer.repaint();
				}
				
				if (game.gameOver())
				{
					if (game.getWinner() == TicTacToe.O)
					{
						oWins++;
						info.setString("O wins: " + oWins + " X wins: " + xWins + " win ratio " + (double) oWins / xWins);
					}
					else if (game.getWinner() == TicTacToe.X)
					{
						xWins++;
						info.setString("O wins: " + oWins + " X wins: " + xWins + " win ratio " + (double) oWins / xWins);
					}
					game.reset();
					info.repaint();
				}
			}
			
		});
		update.start();
		gamePanel.addMouseListener(new MouseAdapter()
		{
			@Override public void mouseClicked(MouseEvent arg0)
			{
				if (game.getTurnStrategyType() == TTTStrategy.PLAYER)
				{
					int tileWidth = gamePanel.getWidth() / 3;
					int tileHeight = gamePanel.getHeight() / 3;
					if (arg0.getButton() == MouseEvent.BUTTON1)
					{
						game.nextTurn(arg0.getY() / tileHeight, arg0.getX() / tileWidth);
					}
				}
				netViewer.repaint();
				repaint();
			}
			
		});
		
		gamePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		gamePanel.setLayout(new BorderLayout(0, 0));
		setContentPane(gamePanel);
		
	}
	
	/**
	 * Saves information of the neural network in a text file
	 */
	@SuppressWarnings("resource") public void save()
	{
		try
		{
			PrintStream netInfoPrintStream = new PrintStream(netInfo);
			netInfoPrintStream.println(Arrays.deepToString(net.weights));
			netInfoPrintStream.println(Arrays.deepToString(net.biases));
			netInfoPrintStream.println(oWins);
			netInfoPrintStream.println(xWins);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads information from the text file
	 */
	public void load()
	{
		try
		{
			Scanner netInfoScan = new Scanner(netInfo);
			String weightString = netInfoScan.nextLine();
			net.weights = ArrayParser.parseDouble3DArray(weightString);
			String biasString = netInfoScan.nextLine();
			net.biases = ArrayParser.parseDouble2DArray(biasString);
			oWins = netInfoScan.nextInt();
			xWins = netInfoScan.nextInt();
			netInfoScan.close();
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override public void setVisible(boolean b)
	{
		if (b)
		{
			super.setVisible(true);
			frame.setBounds(0, 0, 800, 800);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			info.setVisible(true);
			info.setBounds(0, 800, 0, 0);
			
			netViewer.setBounds(800, 0, 1000, 1000);
			netViewer.setVisible(true);
		}
		else
		{
			super.setVisible(false);
		}
	}
	
}

/**
 * 
 * Class to show information about the games played
 */
class InfoPanel extends JFrame
{
	private static final long serialVersionUID = 1L;
	String in = "";
	
	/**
	 * Sets the message to be displayed in the info panel.
	 * 
	 * @param in The message to be displayed in the info panel.
	 */
	public void setString(String in)
	{
		this.in = in;
	}
	
	/**
	 * Paint method.
	 */
	@Override public void paint(Graphics g)
	{
		Image buff = createImage((int) g.getFontMetrics().getStringBounds(in, g).getWidth() + 200, (int) g.getFontMetrics().getStringBounds(in, g).getHeight() + 200);
		buff.getGraphics().drawString(in, 100, 100);
		g.drawImage(buff, 0, 0, null);
		this.setSize((int) g.getFontMetrics().getStringBounds(in, g).getWidth() + 200, (int) g.getFontMetrics().getStringBounds(in, g).getHeight() + 200);
	}
}