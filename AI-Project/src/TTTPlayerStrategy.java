
public class TTTPlayerStrategy extends TTTStrategy
{
	public TTTPlayerStrategy() {
		
	}
	
	public TTTPlayerStrategy(TicTacToe game)
	{
		super(game);
	}

	@Override public void move(){}
	
	@Override public int getStrategyType() {return PLAYER;}
	
}
