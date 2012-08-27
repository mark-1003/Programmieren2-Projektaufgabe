package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import android.location.Location;

public class MyLocation
{
	private int id = -1;
	private String name = "unknown";
	private Location location = null;
	
	private int showMessage = 0;	// 0 = off; 1 = on
	private String message = "Benutzerdefinierte Nachricht";
	private int mute = 0;	// 0 = off; 1 = on
	
	// constructor for creating a new Location
	public MyLocation(String name, Location location)
	{
		this.id = IdGenerator.generateUniqueId();
		this.name = name;
		this.location = location;
	}
	
	// constructor for reading Location from Xml
	public MyLocation(int id, String name, Location location)
	{
		this.id = id;
		this.name = name;
		this.location = location;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
	
	public void setShowMessage(int showMessage)
	{
		this.showMessage = showMessage;
	}
	
	public int getShowMessage()
	{
		return this.showMessage;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	public void setMute(int mute)
	{
		this.mute = mute;
	}
	
	public int getMute()
	{
		return this.mute;
	}
}
