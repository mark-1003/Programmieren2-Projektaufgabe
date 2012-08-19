package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import java.util.ArrayList;
import java.util.HashMap;

import de.dhbw.stuttgart.horb.i11017.projektaufgabe1.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 
 * @author mark
 *
 * @param <T>
 */
public class Projektaufgabe1Activity<T> extends Activity
{
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 60000;	// in Milliseconds
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 45;	// in Meters
	
	private static final long POINT_RADIUS = 50;	// in Meters
	private static final long PROX_ALERT_EXPIRATION = -1;
	
	private static final String PROX_ALERT_INTENT = "de.dhbw.stuttgart.horb.i11017.projektaufgabe1";
	
	
	// Request codes for intends
	private static final int REQUEST_ADD = 10;
	private static final int REQUEST_DETAIL = 11;
	
	
	// Controls
	private Button m_addButton;
	private ListView m_locationList;
	
	// LocationManager
	private LocationManager m_locationManager = null;
	//private MyLocationListener m_locationListener = null;
	
	private Context m_context;
	
	// ArrayList for class MyLocation
	private LocationXmlHelper m_locationXmlHelper;
	private ArrayList<MyLocation> m_myLocations;
	private HashMap<String, Integer> m_hmpLocationToId;
	private LocationAdapter m_listAdapter;	
	
	/**
	 * Called when the activity is first created.
	 * @param <T>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        
        m_context = this;
        m_locationXmlHelper = new LocationXmlHelper(m_context);
        
        
        // Initialize addButton
        m_addButton = (Button) findViewById(R.id.addLocation);
        m_addButton.setOnClickListener(new OnClickListener()
        {
        	/**
        	 * on addButton click
        	 */
			public void onClick(View v)
			{
				// get last known location
				Location location = m_locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
				// check if location is available
				if ( location != null )
				{
					// create Intent
					Intent intent = new Intent(Projektaufgabe1Activity.this, LocationAddActivity.class);
					
					// set parameters
					intent.putExtra("name", getString(R.string.name));
					intent.putExtra("longitude", location.getLongitude());
					intent.putExtra("latitude", location.getLatitude());
					
					startActivityForResult(intent, REQUEST_ADD);
				}
				else
				{
					// no GPS signal found message
					Toast.makeText(m_context, getString(R.string.noGpsSignal), Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        
        // Initialize locationList
        m_locationList = (ListView) findViewById(R.id.locationList);
        m_locationList.setOnItemClickListener(new OnItemClickListener()
        {
        	//locationList onItemClick event
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
        	{
        		MyLocation tmpLoc = (MyLocation) m_locationList.getItemAtPosition(position);
        	    
				Intent intent = new Intent(Projektaufgabe1Activity.this, LocationDetailActivity.class);
        		
				int tmpId = m_hmpLocationToId.get(tmpLoc.getName());
        		// set parameters
				intent.putExtra("id", tmpId);
				intent.putExtra("name", tmpLoc.getName());
				intent.putExtra("longitude", tmpLoc.getLocation().getLongitude());
				intent.putExtra("latitude", tmpLoc.getLocation().getLatitude());
        		
				// start activity
				startActivityForResult(intent, REQUEST_DETAIL);
        	}
        });
        
        
        // read Xml and set ArrayList to LocationAdapter
        m_myLocations = m_locationXmlHelper.getDataFromXml();
        if ( m_myLocations == null )
        {
        	m_myLocations = new ArrayList<MyLocation>();
        }
        m_listAdapter = new LocationAdapter(this, m_myLocations);
        m_locationList.setAdapter(m_listAdapter);
        
        
        m_hmpLocationToId = createHmpNameToId(m_myLocations);
        
        startLocationListener();
     	
     	registerAllLocations(m_myLocations);
	}
	
	private HashMap<String, Integer> createHmpNameToId(ArrayList<MyLocation> locations)
	{
		// create HashMap for Name to Id search
		HashMap<String, Integer> hmpLocationToId = new HashMap<String, Integer>();
        for (int i = 0; i < locations.size(); i++)
    	{
    		// map locationName and index to hashMap
    		hmpLocationToId.put(locations.get(i).getName(), i);
    	}
        return hmpLocationToId;
	}
	
	private void startLocationListener()
	{
		// start locationListener
     	m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     	m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
     			MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE,
     			new MyLocationListener());
	}
	
	private void registerAllLocations(ArrayList<MyLocation> myLocations)
	{
		if ( myLocations != null )
		{
			// register all locations loaded from XML
	     	// TODO: do the same, when a new location is added or something is changed
			for(MyLocation tmp : m_myLocations)
	     	{
	     		registerLocation(tmp);
	     	}
		}
		
	}
	
	private void registerLocation(MyLocation myLocation)
	{
		if ( m_locationManager != null && myLocation != null )
		{
			Intent intent = new Intent(PROX_ALERT_INTENT + "." + myLocation.getName());
     		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
     		m_locationManager.addProximityAlert(myLocation.getLocation().getLatitude(),
     				myLocation.getLocation().getLongitude(),
     				POINT_RADIUS,
     				PROX_ALERT_EXPIRATION,
     				proximityIntent);
     		
     		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT + "." + myLocation.getName());
     		registerReceiver(new ProximityIntentReceiver(myLocation), filter);
		}
		
	}

	private void updateListView()
	{
		// update listAdapter
		if ( m_myLocations != null )
        {
        	m_listAdapter.notifyDataSetChanged();
        }
	}
	
	/**
	 * 
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_ADD) )
		{			
			// read name, longitude and latitude from Intent
			if ( data.hasExtra("name") && data.hasExtra("longitude") && data.hasExtra("latitude") )
			{
				activityAddReturned( data.getExtras().getString("name"),
										Double.valueOf(data.getExtras().getString("longitude")),
										Double.valueOf( data.getExtras().getString("latitude")) );
			}
		}
		else if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_DETAIL) )		
		{
			// read name, longitude and latitude from Intent
			if ( data.hasExtra("id") && data.hasExtra("oldName") && data.hasExtra("newName") )
			{
				activityDetailReturned( data.getExtras().getInt("id"),
										data.getExtras().getString("oldName"),
										data.getExtras().getString("newName") );
				
			}
		}
		else if ( resultCode == RESULT_CANCELED )
		{
			
		}
	}
	
	private void activityAddReturned(String name, double longitude, double latitude)
	{
		Location newLocation = new Location("");
		newLocation.setLongitude(longitude);
		newLocation.setLatitude(latitude);
		
		// if ArrayList is null, create it
		if ( m_myLocations == null )
		{
			m_myLocations = new ArrayList<MyLocation>();
		}
		
		// check if name already exists
		if ( m_hmpLocationToId.containsKey(name) )
		{
			Toast.makeText(m_context, getString(R.string.itemAlreadyExists), Toast.LENGTH_LONG).show();
			// TODO: open alert for better recognition
		}
		else
		{
			MyLocation tmp = new MyLocation(name, newLocation);
			m_myLocations.add(tmp);
			m_hmpLocationToId.put(name, m_myLocations.size()-1);
			m_locationXmlHelper.saveDataToXml(m_myLocations);
			updateListView();
			
			// notify LocationManager --> new location added
			registerLocation(tmp);
		}
	}
	
	private void activityDetailReturned(int id, String oldName, String newName)
	{
		// if ArrayList is null, create it
		if ( m_myLocations == null )
		{
			m_myLocations = new ArrayList<MyLocation>();
		}
		
		// check if new name already exists
		if ( newName.equals(oldName) )
		{
			// do nothing
		}
		// check if any item got the same name already
		else if ( m_hmpLocationToId.containsKey(newName) && (m_hmpLocationToId.get(newName) != id) )
		{
			Toast.makeText(m_context, getString(R.string.itemAlreadyExists), Toast.LENGTH_SHORT).show();
		}
		// change items name
		else
		{
			m_myLocations.get(id).setName(newName);
			m_hmpLocationToId.remove(oldName);
			m_hmpLocationToId.put(newName, id);
			m_locationXmlHelper.saveDataToXml(m_myLocations);
			updateListView();
			
			// notify LocationManager --> items name has changed
			// Done --> ProximityIntentReceiver owns a reference to myLocation
		}
	}
	
	/**
	 * 
	 * @author mark
	 *
	 */
	public class MyLocationListener implements LocationListener
	{

		public void onLocationChanged(Location location) 
		{
			// TODO Auto-generated method stub
			//double latitude = location.getLatitude();
			//double longitude = location.getLongitude();
			//Location pointLocation = retrievelocationFromPreferences();
			//float distance = location.distanceTo(pointLocation);
			//Toast.makeText(Projektaufgabe1Activity.this, "Distance from Point:"+distance, Toast.LENGTH_LONG).show();
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
}
