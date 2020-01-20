package Tests;

import gameClient.Fruit;
import gameClient.Player;
import org.junit.Test;
import utils.Point3D;

import static org.junit.Assert.*;

public class RobotTest
{
	static String JSONSTRING = "{\"Robot\":{\"id\":0,\"src\":4,\"dest\":11,\"value\":3,\"pos\":\"9.1,3.5,0.0\"}}";

	/*
	 * checking his private variables.
	 */
	@Test
	public void setPos() {
		Player player1 = new Player();
		Player player2 = new Player();
		Point3D point1 = new Point3D(8,12);
		Point3D point2 = new Point3D(9,3);
		player1.setLocation(point1);
		player2.setLocation(point2);
		player1.setSource(10);
		player2.setSource(20);
		player1.setSpeed(10);
		player2.setSpeed(8);
		assertEquals(point2 , player2.getLocation());
		assertEquals(point1 , player1.getLocation());
		assertEquals(20 , player2.getSource());
		assertEquals(10 , player1.getSource());
		assertEquals(8 , player2.getSpeed() , 0.0001);
		assertEquals(player2.getSpeed() + 2 , player1.getSpeed() , 0.0001);
	}

/*
 * update
 */
	@Test
	public void update() {
		Player player = new Player();
		Point3D point = new Point3D(9.1 ,3.5);
		player.update(JSONSTRING);
		assertEquals(4 , player.getSource());
		assertEquals(0 , player.getId());
		assertEquals(3 , player.getSpeed(),0.001);
		assertEquals(11 , player.getDestination());
		assertEquals(point , player.getLocation());

	}
	
	/*
	 * equals and copy constructor
	 */
	@Test
    public void equals()
    {
		Player player1 = new Player();
        Point3D point1 = new Point3D(8,12);
        player1.setSpeed(10);
        player1.setSource(1);
        player1.setDestination(11);
        player1.setLocation(point1);
        Player player2 = new Player(player1);
        assertEquals(10 , player2.getSpeed() , 0.0001);
        assertEquals(player1.getLocation() , player2.getLocation());
        assertEquals(1 , player2.getSource());
        assertEquals(11 , player2.getDestination());
        assertTrue(player1.equals(player2));
        
    }


}