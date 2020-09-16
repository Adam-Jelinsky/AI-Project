import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * TicTacToeBoard processes the tic tac toe game that is displayed in
 * TicTacToeDisplay.
 * 
 * @author Adam
 *
 */
public class TicTacToeBoard extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TicTacToe game;
	
	public TicTacToeBoard(TicTacToe game)
	{
		this.game = game;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(4));
		for (int i = 1; i < 3; i++)
		{
			g.drawLine(i * (getWidth() / 3), 0, i * (getWidth()) / 3, getHeight());
			g.drawLine(0, i * (getHeight() / 3), getWidth(), i * (getHeight() / 3));
		}
		// if (game.winner() == 0 && !game.isFilled())
		// {
		BufferedImage oBi = new BufferedImage(getWidth() / 3, getHeight() / 3, BufferedImage.TYPE_INT_ARGB);
		Graphics2D oG = oBi.createGraphics();
		oG.setStroke(new BasicStroke(3));
		oG.setColor(Color.BLUE);
		oG.drawArc(oBi.getWidth() / 10, oBi.getHeight() / 10, 4 * oBi.getWidth() / 5, 4 * oBi.getHeight() / 5, 0, 360);
		ImageIcon O = new ImageIcon(oBi);
		BufferedImage xBi = new BufferedImage(getWidth() / 3, getHeight() / 3, BufferedImage.TYPE_INT_ARGB);
		Graphics2D xG = xBi.createGraphics();
		xG.setStroke(new BasicStroke(3));
		xG.setColor(Color.RED);
		xG.drawLine(xBi.getWidth() / 5, xBi.getHeight() / 5, 4 * xBi.getWidth() / 5, 4 * xBi.getHeight() / 5);
		xG.drawLine(xBi.getWidth() / 5, 4 * xBi.getHeight() / 5, 4 * xBi.getWidth() / 5, xBi.getHeight() / 5);
		ImageIcon X = new ImageIcon(xBi);
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				switch (game.getSpot(row, col))
				{
					case TicTacToe.O:
						g.drawImage(O.getImage(), col * (getWidth() / 3), row * (getHeight() / 3), this);
						break;
					case TicTacToe.X:
						g.drawImage(X.getImage(), col * (getWidth() / 3), row * (getHeight() / 3), this);
						break;
				}
			}
		}
		// }
		// else
		// {
		// g2.setColor(Color.BLACK);
		// switch (game.winner())
		// {
		// case TicTacToe.X:
		// g2.drawString("X IS THE WINNER", getWidth() / 2 -
		// g2.getFontMetrics().stringWidth("X IS THE WINNER") / 2, getHeight() / 2);
		// break;
		// case TicTacToe.O:
		// g2.drawString("O IS THE WINNER", getWidth() / 2 -
		// g2.getFontMetrics().stringWidth("O IS THE WINNER") / 2, getHeight() / 2);
		// break;
		// default:
		// g2.drawString("TIE", getWidth() / 2 - g2.getFontMetrics().stringWidth("TIE")
		// / 2, getHeight() / 2);
		// break;
		//
		// }
		// g2.drawString("Press \'R\' to restart", getWidth() / 2 -
		// g2.getFontMetrics().stringWidth("Press \'R\' to restart") / 2, (int)
		// (getHeight() / 2 + g2.getFontMetrics().getStringBounds("Press \'R\' to
		// restart", g2).getHeight()));
		//
		//
		// }
	}
	
}
