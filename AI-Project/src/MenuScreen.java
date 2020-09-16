import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Part of the application to be launched. Is a "main menu" of sorts for
 * accessing the tic-tac-toe game, the digit recognizer, and the settings for
 * the digit recognizer.
 * 
 * @author Adam
 *
 */
public class MenuScreen extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static TicTacToeDisplay tttDisplay;
	static DrawingPanel dPanel;
	static DrawingPanelSettingsMenu dPanelSettings;
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MenuScreen frame = new MenuScreen();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
					frame.setResizable(false);
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public MenuScreen()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(screenWidth / 2 - (screenWidth / 5) / 2, screenHeight / 10, screenWidth / 5, screenHeight / 5);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton ticTacToeButton = new JButton("TicTacToe");
		ticTacToeButton.setBounds(getWidth() / 10, getHeight() / 6, getWidth() / 3, getHeight() / 3);
		ticTacToeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				tttDisplay = new TicTacToeDisplay();
				tttDisplay.setVisible(true);
				tttDisplay.setBounds(0, 0, 800, 800);
				tttDisplay.addComponentListener(new ComponentAdapter()
				{
					@Override public void componentHidden(ComponentEvent e)
					{
						TicTacToeDisplay.info.setVisible(false);
						TicTacToeDisplay.netViewer.setVisible(false);
					}
				});
				
			}
		});
		
		JButton drawingPanelButton = new JButton("Digit Recognizer");
		drawingPanelButton.setBounds(getWidth() / 2 + getWidth() / 10, getHeight() / 6, getWidth() / 3, getHeight() / 3);
		drawingPanelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dPanel = new DrawingPanel();
				DrawingPanel.controlPanel = new DrawingControlPanel(dPanel);
				dPanel.setVisible(true);
				dPanel.setControlPanelVisible(true);
				dPanel.setBounds(100, 100, 840, 840);
				DrawingPanel.controlPanel.setBounds(1000, 100, 500, 500);
				dPanel.setDefaultCloseOperation(HIDE_ON_CLOSE);
				DrawingPanel.controlPanel.setDefaultCloseOperation(HIDE_ON_CLOSE);
				dPanel.addComponentListener(new ComponentAdapter()
				{
					@Override public void componentHidden(ComponentEvent e)
					{
						dPanel.setControlPanelVisible(false);
					}
				});
				DrawingPanel.controlPanel.addComponentListener(new ComponentAdapter()
				{
					@Override public void componentHidden(ComponentEvent e)
					{
						dPanel.setVisible(false);
					}
				});
				
			}
		});
		
		JButton drawingPanelSettingsButton = new JButton("Settings");
		drawingPanelSettingsButton.setBounds(getWidth() / 2 + getWidth() / 10, 2 * getHeight() / 4, getWidth() / 3, getHeight() / 4);
		drawingPanelSettingsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				dPanelSettings = new DrawingPanelSettingsMenu();
				dPanelSettings.setVisible(true);
				dPanelSettings.setDefaultCloseOperation(HIDE_ON_CLOSE);
			}
		});
		
		contentPane.setLayout(null);
		contentPane.add(ticTacToeButton);
		contentPane.add(drawingPanelButton);
		contentPane.add(drawingPanelSettingsButton);
	}
}
