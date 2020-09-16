/**
 * This class runs the tic tac toe game.
 * 
 * @author Adam
 *
 */
public class TicTacToe
{
	public final static int EMPTY = 0;
	public final static int O = 1;
	public final static int X = -1;
	private int numMoves = 0;
	
	private int[] board = new int[9];
	private TTTStrategy stratO = null;
	private TTTStrategy stratX = null;
	
	private int turn = O;
	private int winner = EMPTY;
	
	public TicTacToe()
	{
		
	}
	
	/**
	 * Constructor that takes in strategy objects to play the game.
	 */
	public TicTacToe(TTTStrategy stratO, TTTStrategy stratX)
	{
		stratO.setGame(this);
		this.stratO = stratO;
		stratO.setPlayAs(O);
		
		stratX.setGame(this);
		this.stratX = stratX;
		stratX.setPlayAs(X);
		
	}
	
	/**
	 * Makes a move if stratO and/or stratX are not player strategies.
	 * 
	 * @return boolean representation of whether or not it is the users move.
	 */
	public boolean playerMove()
	{
		switch (turn)
		{
			case O:
				if (stratO.getStrategyType() != TTTStrategy.PLAYER)
				{
					stratO.move();
					return false;
				}
				return true;
			case X:
				if (stratX.getStrategyType() != TTTStrategy.PLAYER)
				{
					stratX.move();
					return false;
				}
				return true;
		}
		return false;
	}
	
	/**
	 * Sets the strategy for the O player.
	 * 
	 * @param stratO Strategy object to be used for O.
	 */
	public void setStratO(TTTStrategy stratO)
	{
		stratO.game = this;
		this.stratO = stratO;
		stratO.setPlayAs(O);
	}
	
	/**
	 * Sets the strategy for the X player.
	 * 
	 * @param stratX Strategy object to be used for X.
	 */
	public void setStratX(TTTStrategy stratX)
	{
		stratX.game = this;
		this.stratX = stratX;
		stratX.setPlayAs(X);
	}
	
	/**
	 * Method that finds the strategy type of the current turns player.
	 * 
	 * @return The strategy type of the current turns player.
	 */
	public int getTurnStrategyType()
	{
		switch (turn)
		{
			case O:
				return stratO.getStrategyType();
			case X:
				return stratX.getStrategyType();
		}
		return EMPTY;
	}
	
	/**
	 * Sets the turn to be the next turn.
	 * 
	 * @return The current int value of the turn (TicTacToe.X or TicTacToe.Y)
	 */
	public int nextTurn()
	{
		return (turn *= -1) * -1;
	}
	
	/**
	 * Makes a move for the current turns player at the specified row & column.
	 * 
	 * @param row Row on the board to be filled.
	 * @param col Column on the board to be filled.
	 * 
	 * @return Boolean representing whether or not a move could have and had been
	 *         made.
	 */
	public boolean nextTurn(int row, int col)
	{
		if (row / 3 != 0 && col / 3 != 0 && row < 0 && col < 0) throw new IndexOutOfBoundsException();
		if (board[3 * row + col] == EMPTY)
		{
			numMoves++;
			board[3 * row + col] = nextTurn();
			return true;
		}
		return false;
	}
	
	/**
	 * Since the board is stored as an int array, this method allows for placing a
	 * piece based on the position on said array. For reference, the corresponding
	 * index to a given row & column is 3*row + col.
	 * 
	 * @param rawPos Index of the board array to make a move on.
	 * 
	 * @return Boolean representing whether or not a move could have and had been
	 *         made.
	 */
	public boolean nextTurn(int rawPos)
	{
		if (rawPos > 8 || rawPos < 0) return false;
		if (board[rawPos] == EMPTY)
		{
			numMoves++;
			board[rawPos] = nextTurn();
			return true;
		}
		return false;
	}
	
	/**
	 * Finds the winner of the board and stores it as an int in the object. Only use
	 * once, then use getWinner() every time after that when you require the winner.
	 * 
	 * @return Integer representation of the winner (TicTacToe.EMPTY, TicTacToe.X,
	 *         and TicTacToe.O)
	 */
	public int winner()
	{
		if (board[0] == board[1] && board[1] == board[2] && board[0] != EMPTY) return winner = board[0];
		if (board[3] == board[4] && board[4] == board[5] && board[3] != EMPTY) return winner = board[3];
		if (board[6] == board[7] && board[7] == board[8] && board[6] != EMPTY) return winner = board[6];
		if (board[0] == board[3] && board[3] == board[6] && board[0] != EMPTY) return winner = board[0];
		if (board[1] == board[4] && board[4] == board[7] && board[1] != EMPTY) return winner = board[1];
		if (board[2] == board[5] && board[5] == board[8] && board[2] != EMPTY) return winner = board[2];
		if (board[0] == board[4] && board[4] == board[8] && board[0] != EMPTY) return winner = board[0];
		if (board[2] == board[4] && board[4] == board[6] && board[6] != EMPTY) return winner = board[2];
		return EMPTY;
	}
	
	/**
	 * Returns the winner of the tic tac toe game given the board represented by an
	 * int array. The int must have a length of 9 and only contain TicTacToe.O,
	 * TicTacToe.X, and TicTacToe.EMPTY
	 * 
	 * @param board The board of a TicTacToe object
	 * @return The winner of the game, returns TicTacToe.EMPTY if neither X nor O
	 *         has won.
	 */
	public static int winner(int[] board)
	{
		if (board[0] == board[1] && board[1] == board[2] && board[0] != TicTacToe.EMPTY) return board[0];
		if (board[3] == board[4] && board[4] == board[5] && board[3] != TicTacToe.EMPTY) return board[3];
		if (board[6] == board[7] && board[7] == board[8] && board[6] != TicTacToe.EMPTY) return board[6];
		if (board[0] == board[3] && board[3] == board[6] && board[0] != TicTacToe.EMPTY) return board[0];
		if (board[1] == board[4] && board[4] == board[7] && board[1] != TicTacToe.EMPTY) return board[1];
		if (board[2] == board[5] && board[5] == board[8] && board[2] != TicTacToe.EMPTY) return board[2];
		if (board[0] == board[4] && board[4] == board[8] && board[0] != TicTacToe.EMPTY) return board[0];
		if (board[2] == board[4] && board[4] == board[6] && board[6] != TicTacToe.EMPTY) return board[2];
		return TicTacToe.EMPTY;
	}
	
	/**
	 * @return The winner value calculated by the winner() method.
	 */
	public int getWinner()
	{
		return winner;
	}
	
	/**
	 * @return Boolean representing whether or not
	 */
	public boolean isFilled()
	{
		return numMoves > 8;
	}
	
	/**
	 * resets the game
	 */
	public void reset()
	{
		numMoves = 0;
		board = new int[9];
		turn = O;
		winner = EMPTY;
	}
	
	/**
	 * gets the int value of the spot on the board
	 * 
	 * @param row number of the row
	 * @param col number of the column
	 * @return 0 for EMPTY 1 for O, -1 for X
	 */
	public int getSpot(int row, int col)
	{
		return board[3 * row + col];
	}
	
	/**
	 * gets the int value of the spot on the board
	 * 
	 * @param rawPos raw pos number of the board (3*row + col)
	 * @return 0 for EMPTY 1 for O, -1 for X
	 */
	public int getSpot(int rawPos)
	{
		return board[rawPos];
	}
	
	/**
	 * @return 1 if its O's turn, -1 if X's
	 */
	public int getTurn()
	{
		return turn;
	}
	
	/**
	 * @return number of moves made
	 */
	public int getNumMoves()
	{
		return numMoves;
	}
	
	/**
	 * @return boolean representation of wether or not the game is over
	 */
	public boolean gameOver()
	{
		return getNumMoves() > 8 || winner() != EMPTY;
	}
	
}
