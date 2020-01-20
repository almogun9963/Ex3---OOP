package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import utils.StdDraw;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.*;
import dataStructure.edge_data;
import dataStructure.graph;


public class AlgoForGui extends  Thread
{
	private MyGameGUI myGameGui;
	private KML_Logger kml = new KML_Logger();


	public AlgoForGui() 
	{
		return;
	}

	public AlgoForGui(game_service theGame, List<Fruit> fruitList, List<Player> robotList) 
	{
		this.myGameGui = (MyGameGUI) theGame;
	}

	/**
	 * start the automatic game.
	 * @param scenario the scenario the client picked.
	 */
	public void startGame(int scenario)  
	{
		game_service game = Game_Server.getServer(scenario); 
		this.myGameGui.setGameServiceFirst(game);
		String string = game.getGraph();
		DGraph dGraph = new DGraph();
		dGraph.init(string);
		this.myGameGui.setGgraph(dGraph);  
		this.myGameGui.setScales();  
		String info = game.toString();
		System.out.println(info);
		List<String> list = this.myGameGui.getGameServiceFirst().getFruits();
		this.myGameGui.setFruitArrayList(this.myGameGui.getNewFruit().initToList(list));  
		int numPlayers = 0;


		ArrayList<Fruit> copiedlist = this.myGameGui.getNewFruit().copy(this.myGameGui.getFruitArrayList());
		JSONObject jsocObj;
		try {
			jsocObj = new JSONObject(info);
			JSONObject players = jsocObj.getJSONObject("GameServer");
			numPlayers = players.getInt("robots");
		} catch (JSONException e1) 
		{

			e1.printStackTrace();
		}

		placePlayers(numPlayers , copiedlist);  

		List<String> playersList = game.getRobots();
		this.myGameGui.setPlayerArrayList(this.myGameGui.getNewPlayer().initToList(playersList)); 
		this.myGameGui.getGameServiceFirst().startGame();
		Thread thread = new Thread(new Runnable() 
		{
			@Override
			public void run()
			{
				try
				{
					kml.initTheGame();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		thread.start();
		this.start();


	}

	/**
	 * update fruit and robots from the server.
	 */
	public void run()
	{
		while(this.myGameGui.getGameServiceFirst().isRunning())
		{
			this.myGameGui.updateFruits();
			this.myGameGui.updatePlayers();
			movePlayers(this.myGameGui.getGameServiceFirst(),this.myGameGui.getGgraph());
			this.myGameGui.print();
			StdDraw.setPenColor(Color.YELLOW);
			StdDraw.text(myGameGui.getxRange().get_max(), myGameGui.getyRange().get_max() + 0.0015, "time remaining: " + myGameGui.getGameServiceFirst().timeToEnd() / 1000);
			int score = 0;
			try
			{
				String info = myGameGui.getGameServiceFirst().toString();
				JSONObject jobj1 = new JSONObject(info);
				JSONObject jobj2 = jobj1.getJSONObject("GameServer");
				score = jobj2.getInt("grade");
			}catch (Exception ee)
			{
				ee.printStackTrace();
			}
			StdDraw.text(myGameGui.getxRange().get_min(),myGameGui.getyRange().get_max() + 0.0015,"Score:" + score);
			this.myGameGui.printFruit(this.myGameGui.getFruitArrayList());
			this.myGameGui.printPlayers(this.myGameGui.getPlayerArrayList());
			StdDraw.show();
			try
			{
				sleep(15);
			}catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * place the players.
	 * @param numPlayers the amount of players.
	 * @param copied the list of the fruits.
	 */
	public void placePlayers(int numPlayers, ArrayList<Fruit> copied) 
	{
		edge_data edge = null;
		for (int i = 0; i < numPlayers ; i++)
		{
			edge = this.myGameGui.getNewFruit().getFruitEdge(this.myGameGui.getGgraph(),copied.get(0));
			if(copied.get(0).getType() == 1)
			{
				this.myGameGui.getGameServiceFirst().addRobot(Math.min(edge.getDest(),edge.getSrc()));
			}
			else if(copied.get(0).getType() == -1)
			{
				this.myGameGui.getGameServiceFirst().addRobot(Math.max(edge.getDest(),edge.getSrc()));
			}
			copied.remove(0);
		}
	}

	/**
	 * move the robots automaticly.
	 * @param game the game that the client chose.
	 * @param g the graph for the given game.
	 */
	public void movePlayers(game_service game,DGraph g)
	{
		int playerID , source , destination;
		List<String> log = game.move();
		if(log != null)
		{
			for (int i = 0; i <log.size() ; i++) 
			{
				String robot_json = log.get(i);
				try
				{
					JSONObject line = new JSONObject(robot_json);
					JSONObject tomove = line.getJSONObject("Robot");
					playerID = tomove.getInt("id");
					source = tomove.getInt("src");
					destination = tomove.getInt("dest");


					if(destination == -1)
					{
						destination = getNextNode(this.myGameGui.getPlayerArrayList().get(i),g,this.myGameGui.getFruitArrayList());
						game.chooseNextEdge(playerID,destination);
					}

				}catch(Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}


	public void setMyGG(MyGameGUI p)
	{
		this.myGameGui = p;
	}

	/**
	 * find a random path to the first fruit in our arraylist.
	 * @return the node key the we should move to.
	 */
	public int getNextNode(Player player , graph graph, ArrayList<Fruit> arr ) 
	{
		int ans = -1;
		ans = -1;
		Collection<edge_data> ee = graph.getE(arr.get(0).getTag());
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r)
		{
			itr.next();
			i++;
		}
		ans = itr.next().getDest();
		return ans;
	}


	public void update(game_service theGame, List<Fruit> fruitList, List<Player> robotList) 
	{
		
	}
}