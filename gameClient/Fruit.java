package gameClient;

import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;


public class Fruit
{
	private static final double EPSILON = 0.0025;
	private int type;
	private Point3D location;
	private double value;
	private String picture;
	private int tag;

	//constructor Fruit()
	public Fruit()
	{
		this.type = 0;
		this.location = null;
		this.value = 0;
		this.picture = "";
		this.tag = 0;
	}
	// constructor Fruit(Point3D point3D , int type , double value , String picture , int tag)
	public Fruit(Point3D point3D , int type , double value , String picture , int tag)
	{
		this.type = type;
		this.location = new Point3D(point3D);
		this.value = value;
		this.picture = picture;
		this.tag = tag;
	}

	//constructor Fruit(Fruit c)
	public Fruit(Fruit c)
	{
		this.type = c.type;
		this.location = new Point3D(c.location);
		this.value = c.value;
		this.picture = c.picture;
		this.tag = c.tag;
	}
	//getters and setters
	public int getType()
	{
		return  this.type ;
	}

	public Point3D getLocation()
	{
		return this.location;
	}

	public void setLocation(Point3D point3D)
	{
		this.location = new Point3D(point3D);
	}

	public double getValue()
	{
		return this.value;
	}

	public void setValue(double weight)
	{
		this.value = weight;
	}

	public int getTag()
	{
		return this.tag;
	}

	public void setTag(int tag)
	{
		this.tag = tag;
	}

	public String getPicture()
	{
		return this.picture;
	}

	public void setPicture(int type) 
	{
		if (type == -1) 
		{
			this.picture = "redStar.png";
		}
		else
		{
			this.picture = "yellowStar.png";
		}
	}

	/**
	 *	update a fruit.
	 * @param str the json string.
	 */
	public void update(String str)
	{
		try
		{
			JSONObject fruit = new JSONObject(str);
			JSONObject fruitt = fruit.getJSONObject("Fruit");
			this.type = fruitt.getInt("type");
			this.value = fruitt.getDouble("value");
			String pos = fruitt.getString("pos");
			this.location = new Point3D(pos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	/**
	 * creat a fruit from json string.
	 * @param json the json string.
	 * @return the fruit that we created.
	 */
	public Fruit(String json)
	{
		try
		{
			JSONObject fruit = new JSONObject(json);
			JSONObject newFruit = fruit.getJSONObject("Fruit");
			this.type = newFruit.getInt("type");
			this.value= newFruit.getDouble("value");
			this.location = new Point3D(newFruit.getString("pos"));
			setPicture(this.type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * create list of fruit.
	 * @param list is list of the strings that represent the fruits.
	 * @return the list of fruit that we created.
	 */
	public ArrayList<Fruit> initToList(List<String> list)
	{
		ArrayList<Fruit> initArr = new ArrayList<>();
		for (String fruit:list)
		{
			initArr.add(new Fruit(fruit));
		}
		return initArr;
	}

	/**
	 * chooses where the robot should move.
	 * @param graph the graph we are working on.
	 * @param fruit the fruit that we are checking.
	 * @return the node id the robot should move to.
	 */
	public edge_data getFruitEdge(graph graph, Fruit fruit)
	{
		Collection<node_data> nodesCollection = graph.getV();

		for(node_data node : nodesCollection)
		{
			Collection<edge_data> neighbors = graph.getE(node.getKey());
			if(neighbors != null)
			{
				for (edge_data edge : neighbors)
				{
					if(givenEdge(graph , edge , fruit))
					{
						return edge;
					}
				}
			}
		}
		return null;
	}

	/**
	 * check if the fruit on given edge.
	 * @param g the graph we are working on.
	 * @param edge the edge we want to check if the fruit is on it.
	 * @param f the fruit that we want to check about.
	 * @return yes if the fruit on the edge false otherwise.
	 */
	private boolean givenEdge(graph g,edge_data edge,Fruit f)
	{
		double one , two , three;
		Node destination =(Node) g.getNode(edge.getDest());
		Node source = (Node)g.getNode(edge.getSrc());
		two = Math.sqrt(Math.pow(source.getLocation().y() - f.location.y() ,2) + Math.pow(source.getLocation().x() - f.location.x(),2));
		one = Math.sqrt(Math.pow(destination.getLocation().y() - f.location.y() ,2) + Math.pow(destination.getLocation().x() - f.location.x(),2));
		three = Math.sqrt(Math.pow(source.getLocation().y() - destination.getLocation().y() ,2) + Math.pow(source.getLocation().x() - destination.getLocation().x(),2));
		if (one + two < three + EPSILON && one + two > three - EPSILON)
		{
			return true;
		}
		return false;
	}

	/**
	 * copy the given array list.
	 * @param copy the arraylist we want to copy.
	 * @return the copied arraylist.
	 */
	public ArrayList<Fruit> copy(List<Fruit> copy){
		ArrayList<Fruit> ans = new ArrayList<>(copy.size());
		for (Fruit fruit:copy) 
		{
			ans.add(new Fruit(fruit));
		}
		return ans;
	}

	/**
	 * checks if 2 fruit are equals.
	 * @param fruit is the fruit that we want to check is is the same. 
	 * @return true if equal, false otherwise.
	 */
	public boolean equals(Fruit fruit)
	{
		if (this.picture.equals(fruit.picture) && this.tag == fruit.tag && this.type == fruit.type
				&& this.location.equals(fruit.location) && this.value == fruit.value)
		{
			return  true;
		}

		return false;
	}

//	public edge_data edge(DGraph graph) 
//	{
//		
//		return null;
//	}
}