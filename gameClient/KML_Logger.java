package gameClient;

import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.*;
import java.util.ArrayList;
import java.util.Calendar;
import utils.StdDraw;
import javax.swing.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class KML_Logger 
{

	/**
	 * make a kmlfile.
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public void initTheGame() throws ParseException, InterruptedException 
	{
		Kml kml = new Kml();
		Document document = kml.createAndSetDocument();
		int temp = 0;
		if( StdDraw.mgg.getGameServiceFirst() != null && StdDraw.mgg != null )
		{
			while(StdDraw.mgg.getGameServiceFirst().isRunning())
			{
				Thread.sleep(120);
				ArrayList<Player>  playerList = StdDraw.mgg.getPlayerArrayList();
				ArrayList<Fruit> fruitList = StdDraw.mgg.getFruitArrayList();			
				temp += 1;
				for (Player player: playerList) 
				{
					Placemark mark = document.createAndAddPlacemark();
					Icon icon = new Icon();
					icon.setHref("http://pngimg.com/uploads/alien/alien_PNG71.png");
					icon(icon , mark , temp);
					mark.withDescription("Mac: " + "\nType: Robot").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(player.getLocation().x(),player.getLocation().y());
					icon2(icon , mark , temp);
				}
				for(Fruit fruit :fruitList)
				{
					Placemark mark = document.createAndAddPlacemark();
					Icon icon = new Icon();
					icon.setHref("http://pngimg.com/uploads/star/star_PNG1597.png");
					icon(icon , mark , temp);
					mark.withDescription("Mac: " + "\nType: Fruit").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(fruit.getLocation().x(),fruit.getLocation().y());
					icon2(icon , mark , temp);
				}
			}
		}
		try
		{
			int makeKml = JOptionPane.showConfirmDialog(null,"Make a kmlfile? sign your wishes","Yes/No",JOptionPane.YES_NO_OPTION);
			if(makeKml != 1) 
			{
				StdDraw.saveToKML = true;
			}
			else 
			{
				StdDraw.saveToKML = false;
			}
			if(StdDraw.saveToKML)
			{
				kml.marshal(new File("kmlFile.kml"));
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * makes the first icon.
	 * @param icon is the img that we want.
	 * @param mark is the place.
	 * @param temp is the number of the player.
	 */
	private void icon(Icon icon , Placemark mark , int temp)
	{
		icon.setViewBoundScale(1);
		icon.setViewRefreshTime(1);
		icon.withRefreshInterval(1);
		IconStyle style = new IconStyle();
		style.setScale(1);
		style.setHeading(1);
		style.setIcon(icon);
		mark.createAndAddStyle().setIconStyle(style);
		
	}
	/**
	 * makes the second icon.
	 * @param icon is the img that we want.
	 * @param mark is the place.
	 * @param temp is the number of the fruit.
	 * @throws ParseException
	 */
	private void icon2(Icon icon , Placemark mark , int temp) throws ParseException
	{
		String t1 = makeString(toMillisec(whatIsTheTime()) + temp * 1000);
		String t2 = makeString(toMillisec(whatIsTheTime()) + (temp + 1) * 1000);
		String[] splitedT1 = t1.split(" ");
		t1=cutTheArr(splitedT1);
		String[] splitedT2 = t2.split(" ");
		t2=cutTheArr(splitedT2);
		TimeSpan span = mark.createAndSetTimeSpan();
		span.setBegin(t1);
		span.setEnd(t2);
	}
	

	/**
	 * getting the time from a string.
	 * @param string is a string that represent the time.
	 * @return the time.
	 * @throws ParseException
	 */
	private long toMillisec(String string) throws ParseException
	{
		SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		return simpleDateFormat.parse(string.toString()).getTime();
	}
	/**
	 * making a string from n.
	 * @param n is the time.
	 * @return the string that represent the time.
	 */
	private String makeString(Long n)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(n));
	}
	/**
	 * cutting the arr to 2.
	 * @param stringArr is the arr that we want to cut.
	 * @return the string that represent the arr.
	 */
	private String cutTheArr(String[] stringArr)
	{
		return stringArr[0] + "T" + stringArr[1] + "Z";
	}
	/**
	 * check the time.
	 * @return a string that represent the time.
	 */
	private String whatIsTheTime()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	}
}