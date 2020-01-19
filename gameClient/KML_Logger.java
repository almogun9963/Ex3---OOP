package gameClient;

import java.io.PrintWriter;
import java.io.FileWriter;
import Server.Game_Server;
import java.util.Iterator;
import java.util.List;
import Server.game_service;
import dataStructure.node_data;
import dataStructure.DGraph;
import dataStructure.edge_data;
import java.io.IOException;

public class KML_Logger implements Runnable
{
	private int scenario;
	private String startString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" ;
	private String endString = "</Document>\r\n" + 
			"</kml>";
	private game_service theGame;
	private DGraph graph;
	private String graphString = "";
	private String fruitString = "";
	private String robotString = "";
	private createObj jsonObj;
	private long time;
	private boolean isfirstRun = true;
	private List<Player> robotList;
	private List<Fruit> fruitList;




	/**
	 * while the game is running we update the place of the robot and fruit and we update the kml.
	 */
	@Override
	public void run() 
	{
		this.theGame.startGame();
		AlgoForGui algorithm = new AlgoForGui(this.theGame, this.fruitList, this.robotList);

		while(this.theGame.isRunning())
		{
			algorithm.update(this.theGame, this.fruitList, this.robotList);
			synchronized(this) 
			{
				if(this.isfirstRun) 
				{
					if(this.theGame.timeToEnd() > 30000)
					{
						this.time = 60000;
					}else
					{
						this.time = 30000;
					}	
					this.isfirstRun = false;
				}
				algorithm.moveRobots();
				this.jsonObj.update(this.theGame);
				this.fruitList = this.jsonObj.creatFruits();
				this.robotList = this.jsonObj.creatRobotsList();

				for(int i = 0 ;i < this.robotList.size() ;i++) 
				{
					this.robotString = this.robotString + robotKml( this.robotList.get(i), this.theGame.timeToEnd() );
				}

				for(int i = 0 ;i < this.fruitList.size() ;i++) 
				{
					this.fruitString = this.fruitString + fruitKml(this.fruitList.get(i), this.theGame.timeToEnd());
				}	
			}
		}
		synchronized(this) 
		{
			String kml = this.startString + this.graphString + this.fruitString + this.robotString + this.endString;
			try 
			{
				save(kml);
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public void setsenario(int n)
	{
		this.scenario = n;
	}

	/**
	 * make kml for the graph
	 */
	private void graphkml() 
	{
		for(Iterator<node_data> variablesIterator = this.graph.getV().iterator() ; variablesIterator.hasNext() ;)
		{
			node_data node = variablesIterator.next();
			this.graphString = this.graphString + "<Placemark>" +
					"<Style id=\"grn-blank\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" + 
					"      </IconStyle>\r\n" + 
					"    </Style>" +
					"<Point>\r\n" + 
					"      <coordinates>" + node.getLocation().x() + "," + node.getLocation().y() + ",0</coordinates>\r\n" + 
					"    </Point>" +
					"</Placemark>";
			try 
			{
				for(Iterator<edge_data> edgeIterator = this.graph.getE( node.getKey() ).iterator() ; edgeIterator.hasNext() ;) 
				{
					edge_data edgeData = edgeIterator.next();
					this.graphString = this.graphString + "<name>polygon.kml</name>\r\n" + 
							"\r\n" + 
							"	<Style id=\"orange-5px\">\r\n" + 
							"		<LineStyle>\r\n" + 
							"			<color>ff00aaff</color>\r\n" + 
							"			<width>5</width>\r\n" + 
							"		</LineStyle>\r\n" + 
							"	</Style>\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"	<Placemark>\r\n" + 
							"\r\n" + 
							"		<name>A polygon</name>\r\n" + 
							"		<styleUrl>#orange-5px</styleUrl>\r\n" + 
							"\r\n" + 
							"		<LineString>\r\n" + 
							"\r\n" + 
							"			<tessellate>1</tessellate>\r\n" + 
							"			<coordinates>\r\n" + 
							node.getLocation().x() + ","+node.getLocation().y() + ",0\r\n"+
							graph.getNode(edgeData.getDest()).getLocation().x()+"," + graph.getNode(edgeData.getDest()).getLocation().y() + ",0\r\n" +
							"			</coordinates>\r\n" + 
							"\r\n" + 
							"		</LineString>\r\n" + 
							"\r\n" + 
							"	</Placemark>";
				}
			}
			catch(Exception e) 
			{

			}
		}

	}

	/** 
	 * addes the robots to the automatic game based on the most rewarding fruit. 
	 */
	public void addRobotsToAutomatic()
	{
		int variables = 0;
		int i = 0;
		GameServer theServer = createObj.creatGameServer( this.theGame.toString() );
		int countFruit = theServer.getFruits();
		int countRobot = theServer.getPlayers();


		while(countRobot > 0 && countFruit > 0 )
		{
			int fruitType = this.fruitList.get(i).getType();
			if(fruitType == 1)
			{
				variables = this.fruitList.get(i).getFruitEdge(this.graph,this.fruitList.get(i)).getSrc();
			}else 
			{
				variables = this.fruitList.get(i).getFruitEdge(this.graph,this.fruitList.get(i)).getDest();
			}

			this.theGame.addRobot(variables);
			countFruit = countFruit - 1;
			countRobot = countRobot - 1;
			i = i + 1;
		}
		int key[] = new int[this.graph.nodeSize()];
		int j = 0;
		for(Iterator<node_data> variablesIterator = this.graph.getV().iterator() ; variablesIterator.hasNext() ;) 
		{
			int point = variablesIterator.next().getKey();
			key[j] = point;
			j = j + 1;
		}
		while(countRobot > 0)	
		{
			int r = (int)(Math.random() * key.length);
			this.theGame.addRobot( key[r]);
			countRobot--;
		}
	}

	/**
	 * init with the game scenario.
	 */
	public void initTheGame() 
	{
		this.theGame = Game_Server.getServer(this.scenario);
		this.graph = new DGraph();
		this.graph.init(this.theGame.getGraph());
		graphkml();
		this.jsonObj = new createObj(this.theGame);
		this.fruitList = this.jsonObj.creatFruits();
		addRobotsToAutomatic();
		this.robotList = this.jsonObj.creatRobotsList();
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * make kml for fruit .
	 * @param fruit is the current fruit.
	 * @param remainingTime is the remaining time.
	 * @return the fruit's kml.
	 */
	public String fruitKml(Fruit fruit,long remainingTime) 
	{
		String ans = "";
		long t = (this.time - remainingTime)/1000;

		if(fruit.getType() == 1) 
		{
			ans = "<Placemark>\r\n" + 
					"      <TimeSpan>\r\n" +
					"     <begin>" + t + "</begin>\r\n" + 
					"        <end>" + (t + 1)+"</end>" +
					" </TimeSpan>\r\n" +  
					"<Style id=\"electronics\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/shapes/electronics.png\r\n" + 
					"\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" + 
					"      </IconStyle>\r\n" + 
					"    </Style>" +
					"      <Point>\r\n" + 
					"        <coordinates>" + fruit.getLocation().x() + "," + fruit.getLocation().y() + ",0 </coordinates>\r\n" + 
					"      </Point>\r\n" + 
					"    </Placemark>";
		}
		else 
		{
			ans = "<Placemark>\r\n" + 
					"      <TimeSpan>\r\n" +
					"     <begin>" + t + "</begin>\r\n" + 
					"        <end>" + (t + 1) + "</end>" +
					" </TimeSpan>\r\n" + 
					"<Style id=\"movies\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/shapes/movies.png\r\n" + 
					"\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" +  
					"      </IconStyle>\r\n" + 
					"    </Style>" +
					"      <Point>\r\n" + 
					"        <coordinates>" + fruit.getLocation().x() + "," + fruit.getLocation().y() + ",0 </coordinates>\r\n" + 
					"      </Point>\r\n" + 
					"    </Placemark>";
		}
		return ans;
	}

	/**
	 * make kml for the robot by the time.
	 * @param robot is the current robot.
	 * @param remainingTime is the remaining time.
	 * @return the robot's kml.
	 */
	public String robotKml(Player robot ,long remainingTime) 
	{
		String ans = "";
		long t = (this.time - remainingTime)/1000;

		ans = "<Placemark>\r\n" + 
				"      <TimeSpan>\r\n" +
				"     <begin>" + t + "</begin>\r\n" + 
				"        <end>"+(t + 1) + "</end>" +
				" </TimeSpan>\r\n" + 
				"<Style id=\"lodging\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/shapes/lodging.png\r\n" + 
				"</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>" +
				"      <Point>\r\n" + 
				"        <coordinates>" + robot.getLocation().x() + "," + robot.getLocation().y() + ",0 </coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>";
		return ans;
	}

	/**
	 * saves the kml file. 
	 * @param kml is the kml string.
	 * @throws IOException
	 */
	public void save(String kml) throws IOException 
	{
		FileWriter writer = new FileWriter(this.scenario + ".kml");  
		PrintWriter output = new PrintWriter(writer);
		output.println(kml);
		output.close(); 
		writer.close();
	}
}
