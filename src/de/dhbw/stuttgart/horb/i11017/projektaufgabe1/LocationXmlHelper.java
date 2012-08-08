package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;


public class LocationXmlHelper
{
	Context context;
	
	public LocationXmlHelper(Context context)
	{
		this.context = context;
	}
	
	/**
	 * read data from Xml and return ArrayList<MyLocation>
	 * @param context
	 */
	public ArrayList<MyLocation> getDataFromXml()
	{
		XmlHandler xml;
		xml = new XmlHandler(context);
		
		try
		{
			return xml.readLocations();
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
	public void saveDataToXml(ArrayList<MyLocation> myLocations)
	{
		XmlHandler xml;
		xml = new XmlHandler(context);
		
		xml.saveLocations(myLocations);
	}
}

