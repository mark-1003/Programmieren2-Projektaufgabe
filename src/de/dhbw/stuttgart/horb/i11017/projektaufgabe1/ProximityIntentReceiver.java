package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.widget.Toast;

public class ProximityIntentReceiver extends BroadcastReceiver 
{
	private static final int NOTIFICATION_ID = 1000;
	
	private int prevRingerMode = -1;
	
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

		if ( entering ) 
		{
			if ( myLocation.getShowMessage() == 1 )
			{
				Toast.makeText(context, myLocation.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			if ( myLocation.getMute() == 1 )
			{
				AudioManager audiomanager =	(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				prevRingerMode = audiomanager.getRingerMode();
				audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			}
		} 
		else 
		{
			if ( myLocation.getMute() == 1 )
			{
				AudioManager audiomanager =	(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				audiomanager.setRingerMode(prevRingerMode);
			}
		}		
	}

}
