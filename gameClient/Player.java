package gameClient;

import utils.Point3D;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

public class Player
{
	private int destination;
	private Point3D location;
	private int id;
	private String picture;
	private double speed;
	private int source;

	//constuctor Player()
	public Player()
	{
		this.id = 0;
		this.location = null;
		this.picture = "";
		this.speed = 0;
		this.source = 0;
		this.destination = 0;
	}

	//constuctor Player(Player player)
	public Player(Player player)
	{
		this.id = player.id;
		this.location = new Point3D(player.location);
		this.picture = player.picture;
		this.speed = player.speed;
		this.source = player.source;
		this.destination = player.destination;

	}

	/**
	 * create a player from a json string.
	 * @param json the json string that represent the player.
	 * @param indexPicture an index for the player picture.
	 * @return the player that we created.
	 */
	public Player(String json , int indexPicture)
	{
		try
		{
			JSONObject fruit = new JSONObject(json);
			JSONObject newFruit = fruit.getJSONObject("Robot");
			this.id = newFruit.getInt("id");
			this.source = newFruit.getInt("src");
			this.destination = newFruit.getInt("dest");
			this.speed = newFruit.getInt("value");
			this.location = new Point3D(newFruit.getString("pos"));
			this.picture = "player" + indexPicture +".jpg";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * we update the player.
	 * @param str the json string of the player.
	 */
	public void update(String str)
	{
		try {
			JSONObject player = new JSONObject(str);
			JSONObject newPlayer = player.getJSONObject("Robot");
			this.id = newPlayer.getInt("id");
			this.source = newPlayer.getInt("src");
			this.destination = newPlayer.getInt("dest");
			this.location = new Point3D(newPlayer.getString("pos"));
			this.speed = newPlayer.getDouble("value");
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	//getters and setters
	public String getPicture()
	{
		return this.picture;
	}

	public double getSpeed()
	{
		return this.speed;
	}

	public void setSpeed(int speed)
	{
		this.speed = speed;
	}

	public int getDestination()
	{
		return this.destination;
	}

	public void setDestination(int destination)
	{
		this.destination = destination;
	}

	public int getSource()
	{
		return this.source;
	}

	public void setSource(int source)
	{
		this.source = source ;
	}

	public int getId()
	{
		return this.id;
	}

	public Point3D getLocation()
	{
		return this.location;
	}

	public void setLocation(Point3D point3D)
	{
		this.location = new Point3D(point3D);
	}

	/**
	 * creat arraylist of players from list of jsons.
	 * @param list is the list of jsons that represent the players.
	 * @return the arraylist that we created
	 */
	public ArrayList<Player> initToList(List<String> list)
	{
		int indexPicture = 0;
		ArrayList<Player> initArr = new ArrayList<>();
		for (String player : list)
		{
			initArr.add(new Player(player , indexPicture));
			indexPicture++;
		}
		return initArr;
	}
	/**
	 * checks if 2 players are equals.
	 * @param player is the player that we want to check is is the same. 
	 * @return true if equal, false otherwise.
	 */
	public boolean equals(Player player)
	{
		if (this.picture.equals(player.picture) && this.source == player.source && this.destination == player.destination
				&& this.location.equals(player.location) && this.speed == player.speed && this.id == player.id)
		{
			return  true;
		}
		return false;
	}
}