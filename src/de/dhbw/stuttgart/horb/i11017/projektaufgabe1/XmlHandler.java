package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

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
	private Context context;
	private static final String m_XmlFileName = "locations.xml";
	
	private MyLocation myLocation;
	private ArrayList<MyLocation> myLocations;
	private XmlDataContainer data;
	
	/**
	 * constructor
	 * @param cont
	 */
	public XmlHandler(Context context)
	{
	    myLocations = new ArrayList<MyLocation>();
	    data = new XmlDataContainer();
	    data.m_myLocations = myLocations;
	    this.context = context;
	}
	
	/**
	 * read all locations from XML file
	 */
	public XmlDataContainer/*ArrayList<MyLocation>*/ read() throws FileNotFoundException
	{
		// open File
		File xmlFile = new File(context.getFilesDir(), m_XmlFileName);
		
		FileInputStream fileInputStream = new FileInputStream(xmlFile);
		
		RootElement root = new RootElement("appdata");
		Element globalPreferences = root.getChild("globalPreferences");
		Element location = root.getChild("location");
		
		globalPreferences.setStartElementListener(new StartElementListener()
		{
			public void start(Attributes attributes)
			{
				data.minTimeBetweenUpdate = Integer.parseInt(attributes.getValue("minimumTimeBetweenUpdate"));
				data.minDistancechangeForUpdate = Integer.parseInt(attributes.getValue("minimumDistancechangeForUpdate"));
				data.proxAlertRadius = Integer.parseInt(attributes.getValue("proximityAlertRadius"));
			}
		});
		
		location.setStartElementListener(new StartElementListener()
		{
			public void start(Attributes attributes)
			{
				//Attribute: 0=id 1=name 2=longitude 3=latitude
				int id = Integer.parseInt(attributes.getValue("id"));
				String name = attributes.getValue("name");
				Location location = new Location("");
				location.setLongitude(Double.parseDouble(attributes.getValue("longitude")));
				location.setLatitude(Double.parseDouble(attributes.getValue("latitude")));
				int showMessage = Integer.parseInt(attributes.getValue("showMessage"));
				String message = attributes.getValue("message");
				int mute = Integer.parseInt(attributes.getValue("mute"));
				myLocation = new MyLocation(id, name, location);
				myLocation.setShowMessage(showMessage);
				myLocation.setMessage(message);
				myLocation.setMute(mute);
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
			return data;
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
	
	public void save(XmlDataContainer data/*ArrayList<MyLocation> myLocations*/)
	{
		File xmlFile = new File(context.getFilesDir(), m_XmlFileName);
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
			
			serializer.startTag(null, "globalPreferences");
			serializer.attribute(null, "minimumTimeBetweenUpdate", String.valueOf(data.minTimeBetweenUpdate));
			serializer.attribute(null, "minimumDistancechangeForUpdate", String.valueOf(data.minDistancechangeForUpdate));
			serializer.attribute(null, "proximityAlertRadius", String.valueOf(data.proxAlertRadius));
			serializer.endTag(null, "globalPreferences");
			
			for (MyLocation myLocation : data.m_myLocations)
			{
				serializer.startTag(null, "location");
				serializer.attribute(null, "id", String.valueOf(myLocation.getId()));
				serializer.attribute(null, "name", myLocation.getName());
				serializer.attribute(null, "longitude", String.valueOf(myLocation.getLocation().getLongitude()));
				serializer.attribute(null, "latitude", String.valueOf(myLocation.getLocation().getLatitude()));	
				serializer.attribute(null, "showMessage", String.valueOf(myLocation.getShowMessage()));
				serializer.attribute(null, "message", myLocation.getMessage());
				serializer.attribute(null, "mute", String.valueOf(myLocation.getMute()));
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
