package gameClient;



import utils.StdDraw;

import javax.swing.*;
import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.Icon;

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
				Thread.sleep(100);
				ArrayList<Player>  playerList = StdDraw.mgg.getPlayerArrayList();
				ArrayList<Fruit> fruitList = StdDraw.mgg.getFruitArrayList();			
				temp++;
				for (Player player: playerList) 
				{
					Placemark mark = document.createAndAddPlacemark();
					Icon icon = new Icon();
					icon.setHref("http://pngimg.com/uploads/alien/alien_PNG71.png");
					icon.setViewBoundScale(1);
					icon.setViewRefreshTime(1);
					icon.withRefreshInterval(1);
					IconStyle style = new IconStyle();
					style.setScale(1);
					style.setHeading(1);
					style.setColor("ff007db3");
					style.setIcon(icon);
					mark.createAndAddStyle().setIconStyle(style);
					mark.withDescription("Mac: " + "\nType: Robot").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(player.getLocation().x(),player.getLocation().y());
					String t1 = makeString(toMillisec(whatIsTheTime()) + temp * 1000);
					String t2 = makeString(toMillisec(whatIsTheTime()) + (temp + 1) * 1000);
					String[] splitedT1 = t1.split(" ");
					t1 = cutTheArr(splitedT1);
					String[] splitedT2 = t2.split(" ");
					t2 = cutTheArr(splitedT2);
					TimeSpan span = mark.createAndSetTimeSpan();
					span.setBegin(t1);
					span.setEnd(t2);
				}
				for(Fruit fruit :fruitList)
				{
					Placemark mark = document.createAndAddPlacemark();
					Icon icon = new Icon();
					icon.setHref("http://pngimg.com/uploads/star/star_PNG1597.png");
					icon.setViewBoundScale(1);
					icon.setViewRefreshTime(1);
					icon.withRefreshInterval(1);
					IconStyle style = new IconStyle();
					style.setScale(1);
					style.setHeading(1);
					style.setColor("ff007db3");
					style.setIcon(icon);
					mark.createAndAddStyle().setIconStyle(style);
					mark.withDescription("Mac: " + "\nType: Fruit").withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(fruit.getLocation().x(),fruit.getLocation().y());
					String t1 = makeString(toMillisec(whatIsTheTime())+temp*1000);
					String t2 = makeString(toMillisec(whatIsTheTime())+(temp+1)*1000);
					String[] splitedT1 = t1.split(" ");
					t1=cutTheArr(splitedT1);
					String[] splitedT2 = t2.split(" ");
					t2=cutTheArr(splitedT2);
					TimeSpan span = mark.createAndSetTimeSpan();
					span.setBegin(t1);
					span.setEnd(t2);
				}
			}
		}
		try{
			int makeKml = JOptionPane.showConfirmDialog(null,"do you want to make a kmlfile?","Yes/No",JOptionPane.YES_NO_OPTION);
			if(makeKml == 1) 
			{
				StdDraw.saveToKML = false;
			}
			else 
			{
				StdDraw.saveToKML = true;
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
	 * getting the time from a string.
	 * @param TimeAsString is a string that represent the time.
	 * @return the time.
	 * @throws ParseException
	 */
	private long toMillisec(String TimeAsString) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date date = sdf.parse(TimeAsString.toString());
		return date.getTime();
	}
	/**
	 * making a string from n.
	 * @param n is the time.
	 * @return the string that represent the time.
	 */
	private String makeString(Long n)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ans = sdf.format(new Date(n));
		return ans;
	}
	/**\
	 * cutting the arr to 2.
	 * @param arr is the arr that we want to cut.
	 * @return the string that represent the arr.
	 */
	private String cutTheArr(String[] arr)
	{
		return arr[0] + "T" + arr[1] + "Z";
	}
	/**
	 * check the time.
	 * @return a string that represent the time.
	 */
	private String whatIsTheTime()
	{
		String ans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		return ans;
	}
}