package dhbw.i2011.programmieren.projektaufgabe1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;
import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;


/**
 * 
 * @author mark
 *
 */
public class XmlHandler extends DefaultHandler 
{
	private static final String _XMLFileName = "locations.xml";
	
	private MyLocation myLocation;
	private ArrayList<MyLocation> myLocations;
	
	/**
	 * constructor
	 * @param cont
	 */
	public XmlHandler(Context context)
	{
	    myLocations = new ArrayList<MyLocation>();    
	}
	
	/**
	 * read all locations from XML file
	 */
	public ArrayList<MyLocation> readLocations() throws FileNotFoundException
	{
		// open File
		File xmlFile = new File(Environment.getExternalStorageDirectory(), _XMLFileName);
		FileInputStream fileInputStream = new FileInputStream(xmlFile);
		
		RootElement root = new RootElement("appdata");
		Element location = root.getChild("location");
		
		location.setStartElementListener(new StartElementListener()
		{
			public void start(Attributes attributes)
			{
				//Attribute: 0=name 1=longitude 2=latitude 3=accuracy
				String name = attributes.getValue("name")/*.getValue(0)*/;
				Location location = new Location("");
				location.setLongitude(Double.parseDouble(attributes.getValue("longitude")/*.getValue(1)*/));
				location.setLatitude(Double.parseDouble(attributes.getValue("latitude")/*.getValue(2)*/));				
				myLocation = new MyLocation(name, location);
			}
		});
		
		
		location.setEndElementListener(new EndElementListener()
		{
			public void end()
			{
				myLocations.add(myLocation);
			}
		});

		try
		{
			Xml.parse(fileInputStream, Xml.Encoding.UTF_8, root.getContentHandler());
			return myLocations;
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveLocations(ArrayList<MyLocation> myLocations)
	{
		File xmlFile = new File(Environment.getExternalStorageDirectory(), _XMLFileName);
		try
		{
			xmlFile.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(xmlFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		XmlSerializer serializer = Xml.newSerializer();
		try
		{
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			serializer.startTag(null, "appdata");
			for (MyLocation myLocation : myLocations)
			{
				serializer.startTag(null, "location");
				//serializer.attribute(null, "id", String.valueOf(myLocation.getId()));
				serializer.attribute(null, "name", myLocation.getName());
				serializer.attribute(null, "longitude", String.valueOf(myLocation.getLocation().getLongitude()));
				serializer.attribute(null, "latitude", String.valueOf(myLocation.getLocation().getLatitude()));				
				serializer.endTag(null, "location");
			}
			serializer.endTag(null, "appdata");
			serializer.endDocument();
			serializer.flush();
			fos.close();
			
			System.out.println("File saved!");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
