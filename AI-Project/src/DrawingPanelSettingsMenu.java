import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

public class DrawingPanelSettingsMenu extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static JLabel[] labels = null;
	static JSpinner[] spinners = null;
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
					DrawingPanelSettingsMenu frame = new DrawingPanelSettingsMenu();
					frame.setVisible(true);
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
	@SuppressWarnings("unchecked") public DrawingPanelSettingsMenu()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, screenWidth/4, screenHeight/6);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		@SuppressWarnings("rawtypes") JComboBox settingsChoice = new JComboBox();
		settingsChoice.setBounds(5, 5, getWidth()-30, screenHeight/54);
		settingsChoice.setModel(new DefaultComboBoxModel<Object>(new String[] { "Resizing Neural Net", "Training Neural Net" }));
		settingsChoice.setSelectedIndex(0);
		contentPane.add(settingsChoice);
		
		settingsChoice.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (settingsChoice.getSelectedIndex() == 0)
				{
					setBounds(100, 100, screenWidth/4, screenHeight/6);
					contentPane.removeAll();
					contentPane.add(settingsChoice);
					repaint();
					SpinnerNumberModel mod = new SpinnerNumberModel();
					mod.setStepSize(1);
					mod.setValue(0);
					mod.setMinimum(0);
					JSpinner layerSizeSpinner = new JSpinner(mod);
					
					spinners = new JSpinner[0];
					labels = new JLabel[0];
					
					JButton btnSetSize = new JButton("Set Size");
					btnSetSize.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							int[] dimensionArray = new int[(int) layerSizeSpinner.getValue() + 2];
							dimensionArray[0] = 784;
							dimensionArray[dimensionArray.length - 1] = 10;
							for (int i = 1; i < dimensionArray.length - 1; i++)
							{
								dimensionArray[i] = (int) spinners[i - 1].getValue();
							}
							NeuralNet net = new NeuralNet(dimensionArray);
							PrintStream netInfoPrintStream;
							try
							{
								netInfoPrintStream = new PrintStream(new File("DigitNetInfo.txt"));
								netInfoPrintStream.println(Arrays.toString(dimensionArray));
								netInfoPrintStream.println(Arrays.deepToString(net.weights));
								netInfoPrintStream.println(Arrays.deepToString(net.biases));
							}
							catch (FileNotFoundException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
					btnSetSize.setToolTipText("WARNING: Changing the size of the neural network will result in all saved data being lost");
					btnSetSize.setBounds(getWidth()/2 - btnSetSize.getWidth()/2, getHeight() - screenHeight/15, getWidth()/5, 2*getHeight()/15);
					contentPane.add(btnSetSize);
					
					layerSizeSpinner.addChangeListener(new ChangeListener()
					{
						public void stateChanged(ChangeEvent arg0)
						{
							for (JLabel label : labels)
							{
								contentPane.remove(label);
							}
							for (JSpinner spinner : spinners)
							{
								contentPane.remove(spinner);
							}
							labels = new JLabel[(int) layerSizeSpinner.getValue()];
							spinners = new JSpinner[(int) layerSizeSpinner.getValue()];
							
							SpinnerNumberModel[] mods = new SpinnerNumberModel[(int) layerSizeSpinner.getValue()];
							
							for (int i = 0; i < (int) layerSizeSpinner.getValue(); i++)
							{
								labels[i] = new JLabel("Layer " + (i + 1) + " Size: ");
								mods[i] = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
								spinners[i] = new JSpinner(mods[i]);
							}
							for (int i = 0; i < (int) layerSizeSpinner.getValue(); i++)
							{
								
								labels[i].setBounds(getWidth()/3, getHeight()/3 + (2 * i + 1) * getHeight()/9, contentPane.getGraphics().getFontMetrics().stringWidth(labels[i].getText()) + 10, 20);
								spinners[i].setBounds(getWidth()/3, getHeight()/3 + (2 * i + 2) * getHeight()/9, contentPane.getGraphics().getFontMetrics().stringWidth(labels[i].getText()) + 10, 20);
								contentPane.add(labels[i]);
								contentPane.add(spinners[i]);
								repaint();
							}
							setBounds(100, 100, screenWidth/4, screenHeight/7 + (2 * (int) layerSizeSpinner.getValue() + 2) * getHeight()/9);
							btnSetSize.setBounds(2*getWidth()/5, getHeight() - 100, btnSetSize.getWidth(), btnSetSize.getHeight());
						}
						
					});
					layerSizeSpinner.setBounds(2*getWidth()/5, getHeight()/3, 2*getWidth()/15, screenHeight/54);
					contentPane.add(layerSizeSpinner);
					
					Label layerLabel = new Label("Number of Hidden Layers");
					layerLabel.setAlignment(Label.CENTER);
					layerLabel.setBounds(getWidth()/3, getHeight()/6, contentPane.getGraphics().getFontMetrics().stringWidth(layerLabel.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(layerLabel);
				}
				else
				{
					contentPane.removeAll();
					contentPane.add(settingsChoice);
					setBounds(100, 100, screenWidth/4, screenHeight/3);
					repaint();
					
					JLabel lblLearningRate = new JLabel("Learning Rate");
					lblLearningRate.setBounds(getWidth()/2 -contentPane.getGraphics().getFontMetrics().stringWidth(lblLearningRate.getText())/2, getHeight()/6 - contentPane.getGraphics().getFontMetrics().getAscent(), contentPane.getGraphics().getFontMetrics().stringWidth(lblLearningRate.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(lblLearningRate);
					
					JSpinner learningRate = new JSpinner();
					learningRate.setModel(new SpinnerNumberModel(new Double(1), new Double(0), null, new Double(.1)));
					learningRate.setBounds(getWidth()/2 - (getWidth()/9)/2, getHeight()/6, getWidth()/9, getHeight()/18);
					contentPane.add(learningRate);
					
					JLabel lblNumberOfTraining = new JLabel("Number of Training Examples");
					lblNumberOfTraining.setHorizontalAlignment(SwingConstants.CENTER);
					lblNumberOfTraining.setBounds(getWidth()/2-contentPane.getGraphics().getFontMetrics().stringWidth(lblNumberOfTraining.getText())/2, getHeight()/6 + 2*getHeight()/11 - contentPane.getGraphics().getFontMetrics().getAscent(), contentPane.getGraphics().getFontMetrics().stringWidth(lblNumberOfTraining.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(lblNumberOfTraining);
					
					JSpinner trainingExamples = new JSpinner();
					trainingExamples.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(100)));
					trainingExamples.setBounds(getWidth()/2 - getWidth()/6/2, getHeight()/6 + 2*getHeight()/11, getWidth()/6, getHeight()/18);
					contentPane.add(trainingExamples);
					
					
					JLabel lblNumberOfTesting = new JLabel("Number of Testing Examples");
					lblNumberOfTesting.setHorizontalAlignment(SwingConstants.CENTER);
					lblNumberOfTesting.setBounds(getWidth()/2-contentPane.getGraphics().getFontMetrics().stringWidth(lblNumberOfTesting.getText())/2, getHeight()/6 + 2*(2*getHeight()/11) - contentPane.getGraphics().getFontMetrics().getAscent(), contentPane.getGraphics().getFontMetrics().stringWidth(lblNumberOfTesting.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(lblNumberOfTesting);
					
					JSpinner testingExamples = new JSpinner();
					testingExamples.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(100)));
					testingExamples.setBounds(getWidth()/2-getWidth()/6/2, getHeight()/6 + 2*(2*getHeight()/11), getWidth()/6, getHeight()/18);
					contentPane.add(testingExamples);
					
					JLabel lblRandomizeWeights = new JLabel("Randomize Weights");
					lblRandomizeWeights.setBounds(getWidth()/12, testingExamples.getY() + testingExamples.getHeight(), contentPane.getGraphics().getFontMetrics().stringWidth(lblRandomizeWeights.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(lblRandomizeWeights);
					
					JSpinner randomizeWeights = new JSpinner();
					randomizeWeights.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(.1)));
					randomizeWeights.setBounds(getWidth()/12, testingExamples.getY() + testingExamples.getHeight() + contentPane.getGraphics().getFontMetrics().getAscent(), getWidth()/6, getHeight()/18);
					contentPane.add(randomizeWeights);
					
					JLabel lblRandomizeBiases = new JLabel("Randomize Biases");
					lblRandomizeBiases.setBounds(getWidth()-getWidth()/12-(contentPane.getGraphics().getFontMetrics().stringWidth(lblRandomizeBiases.getText()) + 10), testingExamples.getY() + testingExamples.getHeight(), contentPane.getGraphics().getFontMetrics().stringWidth(lblRandomizeBiases.getText()) + 10, contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(lblRandomizeBiases);
					
					JSpinner randomizeBiases = new JSpinner();
					randomizeBiases.setModel(new SpinnerNumberModel(new Double(0), new Double(0), null, new Double(.1)));
					randomizeBiases.setBounds(getWidth()- getWidth()/12 - getWidth()/6, testingExamples.getY() + testingExamples.getHeight() + contentPane.getGraphics().getFontMetrics().getAscent(), getWidth()/6, getHeight()/18);
					contentPane.add(randomizeBiases);
					
					JCheckBox chckbxOverwrite = new JCheckBox("Overwrite Previous Neural Net");
					chckbxOverwrite.setHorizontalAlignment(SwingConstants.CENTER);
					chckbxOverwrite.setBounds(0,randomizeBiases.getY() + randomizeBiases.getHeight(), getWidth(), 2*contentPane.getGraphics().getFontMetrics().getAscent());
					contentPane.add(chckbxOverwrite);
					
					JButton btnTrain = new JButton("Test/Train");
					btnTrain.setBounds(getWidth()/2 - (getWidth()/5)/2, chckbxOverwrite.getY()+chckbxOverwrite.getHeight(), getWidth()/5, getHeight()/7);
					contentPane.add(btnTrain);
					btnTrain.addActionListener(new ActionListener()
					{
						
						@Override public void actionPerformed(ActionEvent e)
						{
							boolean loadNet = chckbxOverwrite.isSelected();
							DigitTrain.load();
							if (loadNet)
							{
								double rWeight = (double) randomizeWeights.getValue();
								DigitTrain.net.randomizeWeights(-rWeight, rWeight);
								double rBias = (double) randomizeBiases.getValue();
								DigitTrain.net.randomizeBiases(-rBias, rBias);
							}
							int numTrains = (int) trainingExamples.getValue();
							btnTrain.setText("Training...");
							btnTrain.repaint();
							if(numTrains != 0)DigitTrain.runTrainingSet(numTrains, false);
							btnTrain.setText("Testing...");
							int numTests = (int) testingExamples.getValue();
							DigitTrain.runTestingSet(numTests);
							btnTrain.setText("Test/Train");
						}
						
					});
				}
				repaint();
			}
		});
		if (settingsChoice.getSelectedIndex() == 0)
		{
			SpinnerNumberModel mod = new SpinnerNumberModel();
			mod.setStepSize(1);
			mod.setValue(0);
			mod.setMinimum(0);
			JSpinner layerSizeSpinner = new JSpinner(mod);
			
			spinners = new JSpinner[0];
			labels = new JLabel[0];
			
			JButton btnSetSize = new JButton("Set Size");
			btnSetSize.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					int[] dimensionArray = new int[(int) layerSizeSpinner.getValue() + 2];
					dimensionArray[0] = 784;
					dimensionArray[dimensionArray.length - 1] = 10;
					for (int i = 1; i < dimensionArray.length - 1; i++)
					{
						dimensionArray[i] = (int) spinners[i - 1].getValue();
					}
					NeuralNet net = new NeuralNet(dimensionArray);
					PrintStream netInfoPrintStream;
					try
					{
						netInfoPrintStream = new PrintStream(new File("DigitNetInfo.txt"));
						netInfoPrintStream.println(Arrays.toString(dimensionArray));
						netInfoPrintStream.println(Arrays.deepToString(net.weights));
						netInfoPrintStream.println(Arrays.deepToString(net.biases));
					}
					catch (FileNotFoundException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			btnSetSize.setToolTipText("WARNING: Changing the size of the neural network will result in all progress being lost");
			btnSetSize.setBounds(172, getHeight() - 100, 89, 23);
			contentPane.add(btnSetSize);
			
			layerSizeSpinner.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent arg0)
				{
					for (JLabel label : labels)
					{
						contentPane.remove(label);
					}
					for (JSpinner spinner : spinners)
					{
						contentPane.remove(spinner);
					}
					labels = new JLabel[(int) layerSizeSpinner.getValue()];
					spinners = new JSpinner[(int) layerSizeSpinner.getValue()];
					
					SpinnerNumberModel[] mods = new SpinnerNumberModel[(int) layerSizeSpinner.getValue()];
					
					for (int i = 0; i < (int) layerSizeSpinner.getValue(); i++)
					{
						labels[i] = new JLabel("Layer " + (i + 1) + " Size: ");
						mods[i] = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
						spinners[i] = new JSpinner(mods[i]);
					}
					for (int i = 0; i < (int) layerSizeSpinner.getValue(); i++)
					{
						
						labels[i].setBounds(166, 59 + (2 * i + 1) * 20, contentPane.getGraphics().getFontMetrics().stringWidth(labels[i].getText()) + 10, 20);
						spinners[i].setBounds(166, 59 + (2 * i + 2) * 20, contentPane.getGraphics().getFontMetrics().stringWidth(labels[i].getText()) + 10, 20);
						contentPane.add(labels[i]);
						contentPane.add(spinners[i]);
						repaint();
					}
					setBounds(100, 100, 450, 149 + (2 * (int) layerSizeSpinner.getValue() + 2) * 20);
					btnSetSize.setBounds(172, getHeight() - 100, 89, 23);
				}
				
			});
			layerSizeSpinner.setBounds(186, 59, 62, 20);
			contentPane.add(layerSizeSpinner);
			
			Label layerLabel = new Label("Number of Layers");
			layerLabel.setAlignment(Label.CENTER);
			layerLabel.setBounds(166, 31, 101, 22);
			contentPane.add(layerLabel);
		}
		
	}
}
