package dataStructure;

public class Edge implements edge_data
{
    private int source;
    private int destination;
    private double weight;
    private String info;
    private int tag;

    //default constructor
    public Edge()
    {
        this.source = 0;
        this.destination = 0;
        this.weight = 0;
        this.info = "";
        this.tag = 0;
    }

    //constructor without tag and info
    public Edge(int source , int destination , double weight)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.info = "";
        this.tag = 0;
    }

    //full constructor
    public Edge(int source , int destination , double weight , String info , int tag)
    {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    //copy constructor
    public Edge(Edge edge)
    {
        this.source = edge.source;
        this.destination = edge.destination;
        this.weight = edge.weight;
        this.info = edge.info;
        this.tag = edge.tag;
    }

    //getters and setters method
    /**
	 * The id of the source node of this edge.
	 * @return
	 */
    public int getSrc()
    {
        return this.source;
    }
    /**
	 * Allow setting the id of the source node of this edge.
	 * @return
	 */
    public void setSrc(int source)
    {
        this.source = source;
    }
    /**
	 * The id of the destination node of this edge
	 * @return
	 */
    public int getDest()
    {
        return this.destination;
    }
    /**
	 * Allow setting the id of the destination node of this edge
	 * @return
	 */
    public void setDest(int destination)
    {
        this.destination = destination;
    }
    /**
	 * @return the weight of this edge (positive value).
	 */
    public double getWeight()
    {
        return this.weight;
    }
    /**
	 * Allow setting the weight of this edge (positive value).
	 */
    public void setWeight(double weight)
    {
        this.weight = weight;
    }
    /**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return
	 */
    public int getTag()
    {
        return this.tag;
    }
    /** 
	 * Allow setting the "tag" value for temporal marking an edge - common 
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
    public void setTag(int tag)
    {
        this.tag = tag;
    }
    /**
	 * return the remark (meta data) associated with this edge.
	 * @return
	 */
    public String getInfo()
    {
        return this.info;
    }
    /**
	 * Allows changing the remark (meta data) associated with this edge.
	 * @param s
	 */
    public void setInfo(String info)
    {
        this.info = info;
    }

    //equals for the Junit
    public boolean equals(Edge edge)
    {
        if (this.destination == edge.destination && this.weight == edge.weight && this.info.equals(edge.info)
                && this.tag == edge.tag && this.source == edge.source)
        {
            return true;
        }
        return false;
    }

    //toString
    public String toString()
    {
        return "source = " + this.source + " , destination = " + this.destination +
        " , weight = " + this.weight + " , tag = " + this.tag + " , info = " + this.info;
    }

}