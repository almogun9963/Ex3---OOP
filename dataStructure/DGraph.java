package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class DGraph implements graph, Serializable
{
	//HashMap <id , Node>
	private HashMap <Integer , node_data> nodesHashMap;

	//HashMap <source , HashMap <destination , Edge>>
	private HashMap <Integer , HashMap <Integer , edge_data>> edgesHashMap;

	//counter changes in the graph
	private int mc;

	//counter the edges in the graph
	private int edgesCounter;

	//default constructor
	public DGraph()
	{
		this.nodesHashMap = new HashMap <>();
		this.edgesHashMap = new HashMap <>();
		this.mc = 0;
		this.edgesCounter = 0;
	}

	//copy constructor
	public DGraph(DGraph dGraph)
	{
		this.nodesHashMap = dGraph.nodesHashMap;
		this.edgesHashMap = dGraph.edgesHashMap;
		this.mc = dGraph.mc;
		this.edgesCounter = dGraph.edgesCounter;
	}

	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */
	public node_data getNode(int key)
	{
		if(this.nodesHashMap.containsKey(key))
		{
			return this.nodesHashMap.get(key);
		}
		return null;
	}

	/**
	 * Returns the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src represents the source node
	 * @param dest represents the destination node.
	 * @return
	 */
	public edge_data getEdge(int src , int dest)
	{
		if(this.edgesHashMap.containsKey(src) && this.edgesHashMap.get(src).containsKey(dest))
		{
			return this.edgesHashMap.get(src).get(dest);
		}
		return null;
	}

	/**
	 * Adds a new node to the graph with the given node_data.
	 * @param n represents the given node.
	 */
	public void addNode(node_data n)
	{
		//just if the key isn't exist(null) in the graph already enter the node
		if(this.nodesHashMap.get(n.getKey()) == null)
		{
			this.nodesHashMap.put(n.getKey() , n);
			this.mc++;
		}
		else
		{
			throw new ArithmeticException("The node's key already exists in the graph.");
		}

	}

	/**
	 * Connect an edge with weight w between node src to node dest.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */
	public void connect(int src, int dest, double w)
	{
		//weight positive
		if(w<=0)
		{
			throw new ArithmeticException("The weight isn't positive");
		}
		//source and destination equals
		if(src == dest)
		{
			throw new ArithmeticException("Source and destination are equals");

		}
		//source and destination exists in map
		if(this.nodesHashMap.containsKey(src) && this.nodesHashMap.containsKey(dest))
		{
			if(!this.edgesHashMap.containsKey(src))
			{
				HashMap<Integer , edge_data> tempHM = new HashMap<> ();
				this.edgesHashMap.put(src , tempHM);
			}
			edge_data tempEdge = new Edge(src , dest , w);
			this.edgesHashMap.get(src).put(dest , tempEdge);
			this.mc++;
			this.edgesCounter++;
		}
		else
		{
			throw new ArithmeticException("Source and destination are not exists in the graph");
		}
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodesHashMap in the graph.
	 * @return Collection<node_data> represents a list of the nodesHashMap in the graph.
	 */
	public Collection<node_data> getV()
	{
		return this.nodesHashMap.values();
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edgesHashMap getting out of
	 * the given node (all the edgesHashMap starting (source) at the given node).
	 * @return Collection<edge_data> represents a list of the edgesHashMap in the graph.
	 */
	public Collection<edge_data> getE(int node_id)
	{
		if(edgesHashMap.containsKey(node_id))
		{
			return this.edgesHashMap.get(node_id).values();
		}
		return null;
	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edgesHashMap which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edgesHashMap should be removed.
	 * @return the data of the removed node (null if none).
	 * @param key represents the ID of the requested node.
	 * We took the for that run only on keys
	 */
	public node_data removeNode(int key)
	{
		if(this.nodesHashMap.containsKey(key))
		{
			//o(n) - all nodes in the Hashmap
			for (Integer node : this.nodesHashMap.keySet())
			{
				if(key != node && this.edgesHashMap.containsKey(node) && this.edgesHashMap.get(node).containsKey(key))
				{
					this.mc++;
					this.edgesCounter--;
					this.edgesHashMap.get(node).remove(key);
				}
				if(key == node && edgesHashMap.containsKey(key))
				{
					this.mc += this.edgesHashMap.get(key).size();
					this.edgesCounter -= this.edgesHashMap.get(key).size();;
					this.edgesHashMap.remove(key);
				}
			}
			this.mc++;
			return this.nodesHashMap.remove(key);
		}
		return null;
	}

	/**
	 * Delete the edge from the graph,
	 * Note: this method should run in O(1) time.
	 * @param src represents the source node.
	 * @param dest represents the destination node.
	 * @return the data of the removed edge (null if none).
	 */
	public edge_data removeEdge(int src, int dest)
	{
		if(this.nodesHashMap.containsKey(src) && this.nodesHashMap.containsKey(dest))
		{
			if(this.edgesHashMap.containsKey(src) && this.edgesHashMap.get(src).containsKey(dest))
			{
				this.edgesCounter--;
				this.mc++;
				return this.edgesHashMap.get(src).remove(dest);
			}
		}
		return null;
	}

	/**
	 * Returns the number of vertices (nodesHashMap) in the graph.
	 * @return
	 */
	public int nodeSize()
	{
		return this.nodesHashMap.size();
	}

	/**
	 * Returns the number of edgesHashMap (assume directional graph).
	 * Note: this method should run in O(1) time.
	 * @return
	 */
	public int edgeSize()
	{
		return this.edgesCounter;
	}

	/**
	 * Returns the Mode Count - for testing changes in the graph.
	 * @return
	 */
	public int getMC()
	{
		return this.mc;
	}

	public HashMap<Integer, node_data> getNodes()
	{
		return this.nodesHashMap;
	}
	//init from a string
	public void init(String data)
	{
		try
		{
			JSONObject graph = new JSONObject(data);
			JSONArray node = graph.getJSONArray("Nodes");
			JSONArray edge = graph.getJSONArray("Edges");
			int id;
			String location;
			Point3D point3D;
			int destination;
			double weight;
			for(int i = 0; i < edge.length(); i++)
			{
				weight = edge.getJSONObject(i).getDouble("w");
				id = edge.getJSONObject(i).getInt("src");
				destination = edge.getJSONObject(i).getInt("dest");
				this.connect(id , destination , weight);
			}

			for(int i = 0; i < node.length(); i++)
			{
				id = node.getJSONObject(i).getInt("id");
				location = node.getJSONObject(i).getString("pos");
				weight = node.getJSONObject(i).getDouble("w");
				point3D = new Point3D(location);
				this.addNode(new Node(id , point3D , weight));
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}