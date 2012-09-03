package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import java.io.FileNotFoundException;
import android.content.Context;


public class XmlHelper
{
	Context context;
	
	public XmlHelper(Context context)
	{
		this.context = context;
	}
	
	/**
	 * read data from Xml and return ArrayList<MyLocation>
	 * @param context
	 */
	public XmlDataContainer getDataFromXml()
	{
		XmlHandler xml;
		xml = new XmlHandler(context);
		
		try
		{
			return xml.read();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * save data from ArrayList<MyLocation> to Xml
	 * @param myLocations
	 */
	public void saveDataToXml(XmlDataContainer data)
	{
		XmlHandler xml;
		xml = new XmlHandler(context);
		
		xml.save(data);
	}
}

