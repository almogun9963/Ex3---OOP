package Tests;

import org.junit.Test;
import utils.Point3D;
import gameClient.Fruit;

import static org.junit.Assert.*;

public class FruitTest {
    static String JSONSTRING ="{\"Fruit\":{\"value\":8,\"type\":-1,\"pos\":\"9.1,3.5,0.0\"}}";

/*
 * 4 tests about his private variables.
 */
    @Test
    public void tag()
    {
        Fruit fruit1 = new Fruit();
        Fruit fruit2 = new Fruit();
        fruit1.setTag(10);
        fruit2.setTag(20);
        assertEquals(10 , fruit1.getTag());
        assertEquals(20 , fruit2.getTag());
    }
    @Test
    public void location()
    {
        Fruit fruit1 = new Fruit();
        Fruit fruit2 = new Fruit();
        Point3D point1 = new Point3D(8,12);
        Point3D point2 = new Point3D(9,3);
        fruit1.setLocation(point1);
        fruit2.setLocation(point2);
        assertEquals(point1 , fruit1.getLocation());
        assertEquals(point2 , fruit2.getLocation());
        assertFalse(point2.equals( fruit1.getLocation()));

    }
    @Test
    public void type()
    {
        Fruit fruit1 = new Fruit();
        Fruit fruit2 = new Fruit();
        fruit1.setTag(1);
        fruit2.setTag(fruit1.getTag());
        assertEquals(fruit1.getType() , fruit2.getType());
    }

    @Test
    public void value() {
        Fruit fruit1 = new Fruit();
        Fruit fruit2 = new Fruit();
        fruit1.setValue(10.4);
        fruit2.setValue(2.3);
        assertEquals(2.3,fruit2.getValue(), 0.0001);
        assertEquals(10.4,fruit1.getValue(), 0.0001);
    }

/*
 * update
 */
    @Test
    public void update() {
        Point3D point = new Point3D(9.1 , 3.5);
        Fruit fruit = new Fruit();
        fruit.update(JSONSTRING);
        assertEquals(-1 , fruit.getType());
        assertEquals(point , fruit.getLocation());

    }
    
    /*
     * equals and copy constructor
     */
    @Test
    public void equals()
    {
    	Fruit fruit1 = new Fruit();
        Point3D point1 = new Point3D(8,12);
        fruit1.setValue(10.4);
        fruit1.setTag(1);
        fruit1.setLocation(point1);
        Fruit fruit2 = new Fruit(fruit1);
        assertEquals(10.4 , fruit2.getValue() , 0.0001);
        assertEquals(fruit1.getLocation() , fruit2.getLocation());
        assertTrue(fruit1.equals(fruit2));
        
    }
}