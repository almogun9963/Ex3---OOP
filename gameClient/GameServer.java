package gameClient;

public class GameServer 
{
	private int fruits;
	private int moves;
	private int grade;
	private int players;
	private String graph;

	public GameServer(int fruits, int moves, int grade, int players, String graph)
	{
		this.fruits = fruits;
		this.moves = moves;
		this.grade = grade;	
		this.players = players;
		this.graph = graph;
	}
	
	//getters and setters
	public int getFruits() 
	{
		return fruits;
	}
	
	public int getMoves() 
	{
		return moves;
	}
	
	public int getPlayers() 
	{
		return players;
	}
	
	public int getGrade() 
	{
		return grade;
	}
	
	public String getGraph() 
	{
		return graph;
	}

}