package dhbw.i2011.programmieren.projektaufgabe1;

import android.location.Location;

public class MyLocation
{
	private String name;
	private Location location;
	
	public MyLocation(String name, Location location)
	{
		this.name = name;
		this.location = location;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
}
