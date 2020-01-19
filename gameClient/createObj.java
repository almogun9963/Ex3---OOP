package gameClient;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import Server.game_service;


public class createObj 
{
	private game_service game;

	public createObj(game_service game) 
	{
		this.game = game;
	}
	/**
	 * update the game.
	 * @param game
	 */
	public void update(game_service game) 
	{
		this.game = game;
	}


	/**
	 * create a list of fruits.
	 * @return the update list.
	 */
	public List<Fruit> creatFruits()
	{
		List<Fruit> ftemp = new ArrayList<Fruit>();
		Iterator<String> fIterator = game.getFruits().iterator();
		while(fIterator.hasNext())
		{
			String fstring = fIterator.next();
			fstring = fstring.substring(9 ,fstring.length() - 1);
			Fruit fruit = creatFruit(fstring);
			ftemp.add(fruit);
		}
		
		for(int i = 0; i < ftemp.size() ; i++)
		{
			for(int j = ftemp.size() - 1;j > i; j--)
			{
				if(ftemp.get(j).getValue() > ftemp.get( j - 1).getValue())
				{
					Fruit ftemp2 = ftemp.get(j);
					ftemp.set(j, ftemp.get(j - 1));
					ftemp.set(j-1, ftemp2);
				}
			}
		}
		return ftemp;
	}
	/**
	 * create object of fruit.
	 * @return Fruit 
	 * @param str is string json.
	 */
	private Fruit creatFruit(String str) 
	{	
		Gson gson = new Gson();
		try
		{
			Fruit fruit = gson.fromJson(str, Fruit.class);
			return fruit;
		} 
		catch ( JsonSyntaxException  e) 
		{
			throw new RuntimeException("not afruit");
		}
	}



	/**
	 * The function create object of GameServer From at string of json
	 * @return GameServer 
	 * @param str - string type if json
	 */
	public static  GameServer creatGameServer(String str) 
	{	
		str=str.substring(14,str.length()-1);
		Gson gson = new Gson();
		try
		{
			GameServer game = gson.fromJson(str, GameServer.class);
			return game;
		} 
		catch ( JsonSyntaxException  e) 
		{
			throw new RuntimeException("not a GameServe");
		}
	}


	
	/**
	 * The function create object of Player.
	 * @return Player 
	 * @param str is string json.
	 */
	private  Player creatRobot(String str) 
	{	
		str = str.substring(9 ,str.length() - 1);
		Gson gson = new Gson();
		try
		{
			Player p1 = gson.fromJson(str, Player.class);
			return p1;
		} 
		catch ( JsonSyntaxException  e)
		{
			throw new RuntimeException("not a Robot");
		}
	}
	/**
	 * Create a list of Player.
	 * @return the update List.
	 */
	public List<Player> creatRobotsList()
	{
		List<String> players = game.getRobots();
		List<Player> ans = new ArrayList<Player>();
		for(int i = 0 ;i < players.size(); i++)
		{
			Player p1 = creatRobot(players.get(i));
			ans.add(p1);
		}
		return ans;
	}
}