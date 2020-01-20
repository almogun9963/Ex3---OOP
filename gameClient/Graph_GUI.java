package gameClient;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class Graph_GUI extends Thread
{
	public static DGraph dGraph = new DGraph();
	public static Graph_Algo graph_algo = new Graph_Algo();
	private static Range xRange= new Range(0,0);
	private static Range yRange= new Range(0,0);

	public Graph_GUI ()
	{
		this.openWindow();
	}
	public Graph_GUI(DGraph dGraph)
	{
		StdDraw.clear();
		this.dGraph = dGraph;
		graph_algo.init(dGraph);
		this.openCanvas();
		this.start();
	}

	/**
	 * check is there is node or not.
	 */
	 public node_data findNode(double x, double y)
	 {
		 Collection<node_data> temp = dGraph.getV();
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
	  * find the x range.
	  * @return the range.
	  */
	 public Range scaleX()
	 {
		 double minX , maxX;
		 Range rangeX , otherRange;
		 if(this.dGraph.nodeSize() != 0) 
		 {
			 minX = Integer.MAX_VALUE;
			 maxX = Integer.MIN_VALUE;
			 Collection<node_data> nodes = this.dGraph.getV();
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
			 }
			 rangeX = new Range(minX, maxX);
			 xRange = rangeX;
			 return rangeX;
		 }
		 else
		 {
			 otherRange = new Range(-100,100);
			 xRange = otherRange;
			 return otherRange;
		 }
	 }
	 /**
	  * find the y range.
	  * @return the range.
	  */
	 public Range scaleY()
	 {
		 double minY , maxY;
		 Range rangeY , otherRange;
		 if(this.dGraph.nodeSize() != 0) 
		 {
			 minY = Integer.MAX_VALUE;
			 maxY = Integer.MIN_VALUE;
			 Collection<node_data> nodes = this.dGraph.getV();
			 for (node_data node : nodes) 
			 {
				 if (node.getLocation().x() > maxY)
				 {
					 maxY = node.getLocation().x();
				 }
				 if (node.getLocation().x() < minY)
				 {
					 minY = node.getLocation().x();
				 }
			 }
			 rangeY = new Range(minY, maxY);
			 yRange = rangeY;
			 return rangeY;
		 }
		 else
		 {
			 otherRange = new Range(-100,100);
			 yRange = otherRange;
			 return otherRange;
		 }
	 }


	 /**
	  * open the window of the graph printed.
	  */
	 public void openCanvas()
	 {
		 Range x , y;
		 StdDraw.setCanvasSize(1920,1080);
		 x = scaleX();
		 y = scaleY();
		 System.out.println(y.get_min() + "," + y.get_max());
		 System.out.println(x.get_min() + "," + x.get_max());
		 StdDraw.setYscale(y.get_min() , y.get_max());
		 StdDraw.setXscale(x.get_min() , x.get_max());
		 StdDraw.enableDoubleBuffering();
		 printGraph();

	 }
	 /**
	  * opens a window.
	  */
	 public void openWindow()
	 {
		 StdDraw.setCanvasSize(1920,1080);
		 StdDraw.clear(Color.CYAN);
		 StdDraw.setYscale(-100 , 100);
		 StdDraw.setXscale(-100 , 100);
	 }
	 /**
	  * this function prints the graph
	  *
	  */
	 public void printGraph()
	 {

		 double xLocation;
		 double yLocation;
		 double rightScaleX;
		 double firstX;
		 double firstY;
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
		 DGraph dGraph = this.dGraph;
		 if(!(dGraph == null))
		 {
			 fillCircled(dGraph , rightScaleX);
			 StdDraw.setPenColor(Color.BLACK);
			 firstIterator = dGraph.getV().iterator();
			 while(firstIterator.hasNext()){
				 Node temp1 = (Node)firstIterator.next();
				 if(dGraph.getE(temp1.getKey())!=null){
					 secondIterator = dGraph.getE(temp1.getKey()).iterator();
					 while(secondIterator.hasNext()){
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
		 while (it.hasNext()) {
			 Node node = (Node) it.next();
			 Point3D p = node.getLocation();
			 StdDraw.filledCircle(p.x(), p.y(),rightScaleX * 0.3);
			 StdDraw.text(p.x(), p.y() + rightScaleX * 0.3, "" + node.getKey());
		 }
	 }
	 public void setGraph(DGraph d)
	 {
		 dGraph=d;
	 }
}