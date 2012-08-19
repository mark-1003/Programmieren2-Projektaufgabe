package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

public class ProximityIntentReceiver extends BroadcastReceiver 
{
	private MyLocation myLocation = null;
	
	public ProximityIntentReceiver(MyLocation location)
	{
		if ( location != null )
		{
			myLocation = location;
		}
		else
		{
			myLocation = new MyLocation("", new Location(""));
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String key = LocationManager.KEY_PROXIMITY_ENTERING;

		Boolean entering = intent.getBooleanExtra(key, false);

		if (entering) 
		{
			Toast.makeText(context, myLocation.getName() + " betreten", Toast.LENGTH_LONG).show();
		} 
		else 
		{
			Toast.makeText(context, myLocation.getName() + " verlassen", Toast.LENGTH_LONG).show();
		}
		
	}
}
