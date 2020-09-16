/**
 * Strategy class for tic-tac-toe that dictates how a certain player moves in
 * the game.
 * 
 * @author Adam
 *
 */
public abstract class TTTStrategy
{
	protected TicTacToe game;
	
	public static final int PLAYER = 0;
	public static final int NEURAL_NET = 1;
	// public static final int GENETIC_ALGORITHM = 2; (ended up scrapping this idea,
	// tic-tac-toe is too simple of a game for a genetic learning algorithm to be justified)
	public static final int OPTIMAL = 3; // used for testing/training, not in final product
	public static final int RANDOM = 4; // used for testing/training, not in final product
	
	protected int playAs;
	
	/**
	 * Default constructor.
	 */
	public TTTStrategy() {}
	
	/**
	 * Constructor for TTTStrategy.
	 * 
	 * @param game The game the strategy will be used in.
	 */
	public TTTStrategy(TicTacToe game)
	{
		this.game = game;
	}
	
	// getter & setter for game
	public TicTacToe getGame() {return game;}
	public void setGame(TicTacToe game) {this.game = game;}
	
	
	// setter for playAs (int value representation of wether or not it plays as X or O
	public void setPlayAs(int playAs) {this.playAs = playAs;}
	
	/**
	 * Abstract method that is overriden to dictate how a specific strategy will
	 * make its move.
	 */
	public abstract void move();
	
	/**
	 * Returns the strategy type in the form of an integer, all of which are stored
	 * in this class as final ints, the kinds are: PLAYER, NEURAL_NET, OPTIMAL, and
	 * RANDOM.
	 * 
	 * @return The strategy type of the class.
	 */
	public abstract int getStrategyType();
}
