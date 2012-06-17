package dhbw.i2011.programmieren.projektaufgabe1;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class MyLocationListener implements LocationListener
{

	public void onLocationChanged(Location location) 
	{
		// TODO Auto-generated method stub
		location.getLatitude();
		location.getLongitude();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String provider) 
	{
		// TODO Auto-generated method stub
		
	}
	
}