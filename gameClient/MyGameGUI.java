package gameClient;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import dataStructure.*;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;
import gameClient.Fruit;
import gameClient.Player;
import Server.Game_Server;
import Server.game_service;
import org.json.JSONException;
import org.json.JSONObject;


public class MyGameGUI extends Thread
{
	private static Range xRange = new Range(0,0);
	private static Range yRange = new Range(0,0);
	private game_service gameServiceFirst;
	private ArrayList<Fruit> fruitArrayList;
	private ArrayList<Player> playerArrayList;
	private Fruit newFruit = new Fruit();
	private Player newPlayer = new Player();
	private DGraph Ggraph;
	private static KML_Logger kmlLogger = new KML_Logger();
	private static AlgoForGui myGameAlgo = new AlgoForGui();
	
	public static void main(String[] args)
	{
		MyGameGUI p = new MyGameGUI();
	}

	//constructor MyGameGUI() 
	public MyGameGUI() 
	{
		StdDraw.mgg = this;
		myGameAlgo.setMyGG(this);
		openWindow();
	}
	
	//getters and setters
	public void setPlayerArrayList(ArrayList<Player> playerArrayList)
	{
		playerArrayList = playerArrayList;
	}
	public void setFruitArrayList(ArrayList<Fruit> fruitArrayList)
	{
		fruitArrayList = fruitArrayList;
	}
	public Range getxRange()
	{
		return xRange;
	}
	public Range getyRange()
	{
		return yRange;
	}
	public game_service getGameServiceFirst()
	{
		return gameServiceFirst;
	}
	public void setGameServiceFirst(game_service gameServiceFirst)
	{
		gameServiceFirst = gameServiceFirst;
	}
	public void setGgraph(DGraph dGraph)
	{
		Ggraph = dGraph;
	}
	public ArrayList<Fruit> getFruitArrayList()
	{
		return fruitArrayList;
	}
	public ArrayList<Player> getPlayerArrayList()
	{
		return playerArrayList;
	}
	public Player getNewPlayer()
	{
		return newPlayer;
	}
	public Fruit getNewFruit()
	{
		return newFruit;
	}
	public DGraph getGgraph()
	{
		return Ggraph;
	}
	public AlgoForGui getAlgoGame()
	{
		return myGameAlgo;
	}

	/**
	 * creating the kml.
	 */
	public void toKml()
	{
		Thread thread = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					kmlLogger.initTheGame();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		thread.start();
		start();
	}




	/**
	 * get array list of int that says where the client want to put the players.
	 * @param arr the array that represent where to put the players.
	 */
	private void placePlayersManual(int[] arr)
	{
		for (int i = 0; i < arr.length ; i++)
		{
			gameServiceFirst.addRobot(arr[i]);
		}
	}


	/**
	 * set the xScale and yScale for the graph.
	 */
	public void setScales()
	{
		double minX = 0;
		double maxX = 0;
		double minY = 0;
		double maxY = 0;
		Range rangeX = null;
		Range rangeY = null;
		Range temprange = null;
		if(Ggraph.nodeSize()!=0) 
		{
			minX = Integer.MAX_VALUE;
			maxX = Integer.MIN_VALUE;
			minY = Integer.MAX_VALUE;
			maxY = Integer.MIN_VALUE;
			Collection<node_data> nodes = Ggraph.getV();
			for (node_data node : nodes)
			{
				if (node.getLocation().x() > maxX)
				{
					maxX = node.getLocation().x();
				}
				if (node.getLocation().x() < minX)
				{
					minX = node.getLocation().x();
				}
				if (node.getLocation().y() > maxY) 
				{
					maxY = node.getLocation().y();
				}
				if (node.getLocation().y() < minY)
				{
					minY = node.getLocation().y();
				}
			}
			rangeX = new Range(minX, maxX);
			rangeY = new Range(minY, maxY);
			xRange = rangeX;
			yRange = rangeY;
		}
		else
		{
			temprange = new Range(-100,100);
			xRange = temprange;
			yRange = temprange;
		}
		StdDraw.setXscale(xRange.get_min() , xRange.get_max());
		StdDraw.setYscale(yRange.get_min() , yRange.get_max());
	}

	/**
	 * opens a finish window.
	 */
	public void gameOver()
	{
		StdDraw.setCanvasSize(1920 ,1080);
		StdDraw.clear(Color.WHITE);
		StdDraw.setXscale(-100,100);
		StdDraw.setYscale(-100,100);
		StdDraw.picture(0 , 0 , "gameover.jpg");
		StdDraw.show();
	}

	/**
	 * open the play window and ask the client which scenario and which mode.
	 */
	public void openWindow()
	{
		int check = -1;
		StdDraw.setCanvasSize(1920,1080);
		StdDraw.clear(Color.CYAN);
		StdDraw.setYscale(-100 , 100);
		StdDraw.setXscale(-100 , 100);
		int mode = -1;
		while(mode == -1)
		{
			String userWishes = JOptionPane.showInputDialog(null, "which game mode do u want to play");
			try
			{
				mode = Integer.parseInt(userWishes);
				if(mode > 23 || mode < 0)
				{
					mode = -1;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		Object modeGame = null;
		String[] chooseGame = {"Manually Game","Auto Game"};
		while(check == -1)
		{
			try
			{
				modeGame = JOptionPane.showInputDialog(null, "Choose a Game mode");
				check =0;
			}
			catch
			(Exception e)
			{
				check = -1;
			}
		}
		if(modeGame == "Manually Game")
		{
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			startGameManual(mode);
		}
		else
		{
			StdDraw.clear();
			StdDraw.enableDoubleBuffering();
			myGameAlgo.startGame(mode);
		}
	}

	/**
	 * check is there is node or not.
	 */
	private node_data isThereNode(double x, double y)
	{
		Collection<node_data> temp = Ggraph.getV();
		for (node_data node: temp)
		{
			if(y >= node.getLocation().y() - 0.003 && y <= node.getLocation().y() + 0.003 &&
					x >= node.getLocation().x() - 0.003 && x <= node.getLocation().x() + 0.003) 
			{
				return node;
			}
		}
		return null;
	}

	/**
	 * move players mannualy.
	 */
	int playerId = 0;
	public void movePlayersManual(game_service game, DGraph dGraph)
	{
		List<String> log = game.move();
		double xLine , yLine = 0;
		int source = 0, destination = 0;
		if (!(log == null))
		{
    
			for (int i = 0; i < log.size(); i++)
			{
				String playerJson = log.get(i);
				JSONObject line;
				try 
				{
					line = new JSONObject(playerJson);
					JSONObject tomove = line.getJSONObject("Robot");
					int playerID = tomove.getInt("id");
					source = tomove.getInt("src");
					destination = tomove.getInt("dest");
				} catch (JSONException e) 
				{
					e.printStackTrace();
				}
				if (StdDraw.isMousePressed())
				{
					yLine = StdDraw.mouseY();
					xLine = StdDraw.mouseX();
					Node node = (Node) isThereNode(xLine, yLine);
					while(node == null)
					{
						xLine = StdDraw.mouseX();
						yLine = StdDraw.mouseY();
						node=(Node)isThereNode(xLine , yLine);
					}
					for (Player player : playerArrayList) 
					{
						if (player.getSource() == node.getKey())
						{
							playerId = player.getId();
						}
						System.out.println("the player id is: " + playerId);
					}
				}


				if (StdDraw.isMousePressed())
				{
					yLine = StdDraw.mouseY();
					xLine = StdDraw.mouseX();
					Node node = (Node) isThereNode(xLine , yLine);
					Edge edge = (Edge) Ggraph.getEdge(source, node.getKey()); 
					if (edge == null || node == null)
					{
						System.out.println("Error");
					}
					else
					{
						if (destination == -1)
						{
							game.chooseNextEdge(playerId , node.getKey());
						}
					}
				}
			}
		}
	}

	/**
	 * the thread that update the game.
	 */
	public void run()
	{
		while (gameServiceFirst.isRunning())
		{

			int points = 0;
			updateFruits();
			updatePlayers();
			movePlayersManual(gameServiceFirst, Ggraph);
			print();
			StdDraw.setPenColor(Color.ORANGE);
			StdDraw.text(xRange.get_max() , yRange.get_max() , "remaining time: " + gameServiceFirst.timeToEnd() / 1000);
			String info = gameServiceFirst.toString();
			JSONObject jsonGrade;
			try 
			{
				jsonGrade = new JSONObject(info);
				JSONObject grade = jsonGrade.getJSONObject("GameServer");
				points = grade.getInt("grade");
				StdDraw.text(xRange.get_min() , yRange.get_max() ,"points:" + points);
				printPlayers(playerArrayList);
				printFruit(fruitArrayList);
				StdDraw.show();
			} catch (JSONException e1) 
			{
				e1.printStackTrace();
			}
			
			try
			{
				sleep(15);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * update the players from the List.
	 */
	public void updatePlayers()
	{
		List<String> playersUpdate = gameServiceFirst.getRobots();
		if(!(playersUpdate == null))
		{
			System.out.println(playerArrayList.size());
			for (int i = 0; i <playerArrayList.size() ; i++)
			{
				playerArrayList.get(i).update(playersUpdate.get(i));
			}
		}
	}

	/**
	 * update the fruits from the list.
	 */
	public void updateFruits() 
	{
		List<String> fruitsUpdate = gameServiceFirst.getFruits();
		if (!(fruitsUpdate == null))
		{
			for (int i = 0; i < fruitArrayList.size() ; i++)
			{
				fruitArrayList.get(i).update(fruitsUpdate.get(i));
			}
		}
	}


	/**
	 * prints the graph.
	 */
	public void print()
	{
		double firstX;
		double firstY;
		double xLocation;
		double yLocation;
		double rightScaleX;
		double secondX;
		double secondY;
		Iterator firstIterator;
		Iterator secondIterator;
		Node sourceNode , destinationNode;
		Point3D sourcePoint , destinationPoint;

		StdDraw.clear();
		yLocation = ((yRange.get_max() + yRange.get_min())/2);
		xLocation = ((xRange.get_max() + xRange.get_min())/2);
		StdDraw.clear();
		StdDraw.picture(xLocation , yLocation,"backscreen.jpg");
		rightScaleX = ((xRange.get_max() - xRange.get_min()) * 0.02);
		StdDraw.setPenRadius(0.30);
		StdDraw.setPenColor(Color.GREEN);
		DGraph dGraph = Ggraph;
		if(!(dGraph == null))
		{
			fillCircled(dGraph , rightScaleX);
			StdDraw.setPenColor(Color.BLACK);
			firstIterator = dGraph.getV().iterator();
			while(firstIterator.hasNext())
			{
				Node temp1 = (Node)firstIterator.next();
				if(dGraph.getE(temp1.getKey())!=null)
				{
					secondIterator = dGraph.getE(temp1.getKey()).iterator();
					while(secondIterator.hasNext())
					{
						Edge temp2 = (Edge) secondIterator.next();
						if(!(temp2 == null))
						{
							StdDraw.setPenColor(Color.BLUE);
							double value = Math.round(temp2.getWeight() * 1000) / 1000;
							sourceNode = (Node) dGraph.getNode(temp2.getSrc());
							destinationNode = (Node) dGraph.getNode(temp2.getDest());
							destinationPoint = destinationNode.getLocation();
							sourcePoint = sourceNode.getLocation();
							StdDraw.line(sourcePoint.x() , sourcePoint.y() , destinationPoint.x() , destinationPoint.y());
							firstY = 0.8 * destinationPoint.y() + 0.2 * sourcePoint.y() ;
							firstX = 0.8 * destinationPoint.x() + 0.2 * sourcePoint.x();
							StdDraw.setPenColor(Color.BLACK);
							StdDraw.text(firstX , firstY , "" + value);
							StdDraw.setPenColor(Color.ORANGE);
							secondY = 0.9 * destinationPoint.y() + 0.1 * sourcePoint.y();
							secondX = 0.9 * destinationPoint.x() + 0.1 * sourcePoint.x();
							StdDraw.filledCircle(secondX , secondY ,rightScaleX * 0.3);
						}
					}
				}
			}
		}
	}

	private void fillCircled(DGraph dGraph , double rightScaleX)
	{
		Iterator it = dGraph.getV().iterator();
		while (it.hasNext()) 
		{
			Node node = (Node) it.next();
			Point3D p = node.getLocation();
			StdDraw.filledCircle(p.x(), p.y(),rightScaleX * 0.3);
			StdDraw.text(p.x(), p.y() + rightScaleX * 0.3, "" + node.getKey());
		}
	}

	/**
	 * print the fruits.
	 * @param fruitList the list of the fruits.
	 */
	public void printFruit(List<Fruit> fruitList)
	{
		for (Fruit fruit: fruitList)
		{
			StdDraw.picture(fruit.getLocation().x() , fruit.getLocation().y() , fruit.getPicture());
		}
	}
	/**
	 * print the players.
	 * @param playerList the list of the players.
	 */
	public void printPlayers(List<Player> playerList)
	{
		for (Player player : playerList)
		{
			StdDraw.picture(player.getLocation().x() , player.getLocation().y() , player.getPicture());
		}
	}

	
	/**
	 * start the manually game.
	 * @param scenario the number of scenario the we want to play.
	 */
	public void startGameManual(int scenario) 
	{
		game_service mainGame = Game_Server.getServer(scenario); 
		gameServiceFirst = mainGame;
		String string = mainGame.getGraph();
		DGraph dGraph = new DGraph();
		dGraph.init(string);
		Ggraph = dGraph; 
		setScales();
		String s = mainGame.toString();
		List<String> listFruit = gameServiceFirst.getFruits();
		fruitArrayList = newFruit.initToList(listFruit);  
		JSONObject game_info;
		int numPlayers = 0;
		try
		{
			game_info = new JSONObject(s);
			JSONObject players = game_info.getJSONObject("GameServer");
			numPlayers = players.getInt("robots");
		} catch (JSONException e)
		{
			
			e.printStackTrace();
		}
		
		print();
		printFruit(fruitArrayList);
		StdDraw.show();
		int [] playersArr = new int[numPlayers];
		int testing = -1;
		String[] arrOfString;
		int a = 0;
		while(testing == -1)
		{
			try
			{
				String stringSplit = JOptionPane.showInputDialog(null, "enter " + numPlayers + "node id for Robots.", "enter in the format: x,y,z,w", 1);
				if (numPlayers == 1)
				{
					a = Integer.parseInt(stringSplit);
					playersArr[0] = a;
				}
				else
				{
					arrOfString = stringSplit.split(",");
					for (int i = 0; i < numPlayers; i++)
					{
						playersArr[i] = Integer.parseInt(arrOfString[i]);
					}
				}
				testing = 0;
			}
			catch(Exception ee)
			{
				testing = -1;
			}
		}

		placePlayersManual(playersArr);
		List<String> playersList = mainGame.getRobots();
		playerArrayList = newPlayer.initToList(playersList);
		printPlayers(playerArrayList);
		StdDraw.show();
		gameServiceFirst.startGame();
		toKml();
	}
}