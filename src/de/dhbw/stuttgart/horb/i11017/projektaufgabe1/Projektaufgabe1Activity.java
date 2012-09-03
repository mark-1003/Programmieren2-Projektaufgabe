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
	private static final long PROX_ALERT_EXPIRATION = -1;	
	private static final String PROX_ALERT_INTENT = "de.dhbw.stuttgart.horb.i11017.projektaufgabe1";
	
	// Request codes for intends
	private static final int REQUEST_ADD = 10;
	private static final int REQUEST_DETAIL = 11;
	private static final int REQUEST_PREFERENCES = 12;
	
	// Controls
	private Button m_addButton;
	private Button m_preferencesButton;
	private Button m_exitButton;
	private ListView m_locationList;
	
	// LocationManager
	private LocationManager m_locationManager = null;
	private MyLocationListener m_locationListener = null;
	
	private Context m_context;
	
	// ArrayList for class MyLocation
	private XmlHelper m_XmlHelper;
	//private ArrayList<MyLocation> data.m_myLocations;
	private XmlDataContainer data;
	private HashMap<Integer, Integer> m_hmpUIDtoLoc;
	private LocationAdapter m_listAdapter;
	
	private ArrayList<PendingIntent> m_pendingIntents;
	private ArrayList<ProximityIntentReceiver> m_proxIntentReceivers;
	
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
        m_XmlHelper = new XmlHelper(m_context);
        
        
        // Initialize addButton
        m_addButton = (Button) findViewById(R.id.addLocation);
        m_addButton.setOnClickListener(new OnClickListener()
        {
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
        
        // Initialize preferencesButton
        m_preferencesButton = (Button) findViewById(R.id.preferences);
        m_preferencesButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				Intent intent = new Intent(Projektaufgabe1Activity.this, PreferencesActivity.class);
				
				intent.putExtra("minTimeBetweenUpdate", data.minTimeBetweenUpdate);
				intent.putExtra("minDistancechangeForUpdate", data.minDistancechangeForUpdate);
				intent.putExtra("proxAlertRadius", data.proxAlertRadius);
				
				startActivityForResult(intent, REQUEST_PREFERENCES);
			}
        	
        });
        
     // Initialize exitButton
        m_exitButton = (Button) findViewById(R.id.exit);
        m_exitButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v)
			{
				finish();
				//System.exit(0);
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
        		
				int tmpId = m_hmpUIDtoLoc.get(tmpLoc.getId());
        		// set parameters
				intent.putExtra("id", tmpId);
				intent.putExtra("name", tmpLoc.getName());
				intent.putExtra("longitude", tmpLoc.getLocation().getLongitude());
				intent.putExtra("latitude", tmpLoc.getLocation().getLatitude());
				intent.putExtra("showMessage", tmpLoc.getShowMessage());
				intent.putExtra("message", tmpLoc.getMessage());
				intent.putExtra("mute", tmpLoc.getMute());
        		
				// start activity
				startActivityForResult(intent, REQUEST_DETAIL);
        	}
        });
        
        
        // read Xml and set ArrayList to LocationAdapter
        data = m_XmlHelper.getDataFromXml();
        if ( data == null )
        {
        	data = new XmlDataContainer();
        	data.m_myLocations = new ArrayList<MyLocation>();
        }
        m_listAdapter = new LocationAdapter(this, data.m_myLocations);
        m_locationList.setAdapter(m_listAdapter);
        
        m_pendingIntents = new ArrayList<PendingIntent>();
        m_proxIntentReceivers = new ArrayList<ProximityIntentReceiver>();
        
        m_hmpUIDtoLoc = createHmpUIDtoLoc(data.m_myLocations);
        
        startLocationListener();
     	
     	registerAllLocations(data.m_myLocations);
	}
	
	public void onStart()
	{
		super.onStart();
	}
	
	public void onStop()
	{
		super.onStop();
	}
	
	public void onDestroy()
	{
		unregisterAllLocations();
		m_locationManager.removeUpdates(m_locationListener);
		super.onDestroy();
	}
	
	private HashMap<Integer, Integer> createHmpUIDtoLoc(ArrayList<MyLocation> locations)
	{
		// create HashMap for Name to Id search
		HashMap<Integer, Integer> hmpUIDtoLoc = new HashMap<Integer, Integer>();
        for (int i = 0; i < locations.size(); i++)
    	{
    		// map locationName and index to hashMap
        	hmpUIDtoLoc.put(locations.get(i).getId(), i);
    	}
        return hmpUIDtoLoc;
	}
	
	private void startLocationListener()
	{
		// start locationListener
     	m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     	m_locationListener = new MyLocationListener();
     	m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
     			data.minTimeBetweenUpdate,
     			data.minDistancechangeForUpdate,
     			m_locationListener);
	}
	
	private void registerAllLocations(ArrayList<MyLocation> myLocations)
	{
		if ( myLocations != null )
		{
			// register all locations loaded from XML
			for(MyLocation tmp : data.m_myLocations)
	     	{
	     		registerLocation(tmp);
	     	}
		}
		
	}
	
	private void registerLocation(MyLocation myLocation)
	{
		if ( m_locationManager != null && myLocation != null )
		{
			Intent intent = new Intent(PROX_ALERT_INTENT + "." + myLocation.getId());
			intent.putExtra("id", myLocation.getId());
			
     		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, myLocation.getId(), intent, 0);
     		
     		m_locationManager.addProximityAlert(myLocation.getLocation().getLatitude(),
     											myLocation.getLocation().getLongitude(),
     											data.proxAlertRadius,
     											PROX_ALERT_EXPIRATION,
     											proximityIntent);
     		
     		m_pendingIntents.add(proximityIntent);
     		
     		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT + "." + myLocation.getId());
     		ProximityIntentReceiver tmpReceiver = new ProximityIntentReceiver(myLocation);
     		m_proxIntentReceivers.add(tmpReceiver);
     		registerReceiver(tmpReceiver, filter);
		}
		
	}
	
	private void unregisterAllLocations()
	{
		for(PendingIntent tmp : m_pendingIntents)
		{
			m_locationManager.removeProximityAlert(tmp);
		}
		
		for(ProximityIntentReceiver tmpReceiver : m_proxIntentReceivers)
		{
     		unregisterReceiver(tmpReceiver);
		}
	}

	private void updateListView()
	{
		// update listAdapter
		if ( data.m_myLocations != null )
        {
        	m_listAdapter.notifyDataSetChanged();
        }
	}
	
	/**
	 * when add, detail or preferences activity gets finished
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_ADD) )
		{
			if ( data.hasExtra("name") && data.hasExtra("longitude") && data.hasExtra("latitude") )
			{
				activityAddReturned( data.getExtras().getString("name"),
										data.getExtras().getDouble("longitude"),
										data.getExtras().getDouble("latitude") );
			}
		}
		else if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_DETAIL) )		
		{
			if ( data.hasExtra("id") && data.hasExtra("oldName") && data.hasExtra("newName")
					&& data.hasExtra("showMessage") && data.hasExtra("message") && data.hasExtra("mute") )
			{
				activityDetailReturned( data.getExtras().getInt("id"),
										data.getExtras().getString("oldName"),
										data.getExtras().getString("newName"),
										data.getExtras().getInt("showMessage"),
										data.getExtras().getString("message"),
										data.getExtras().getInt("mute") );
			}
		}
		else if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_PREFERENCES) )
		{
			if ( data.hasExtra("minTimeBetweenUpdate") && data.hasExtra("minDistancechangeForUpdate") && data.hasExtra("proxAlertRadius") )
			{
				activityPreferencesReturned( data.getExtras().getInt("minTimeBetweenUpdate"),
										data.getExtras().getInt("minDistancechangeForUpdate"),
										data.getExtras().getInt("proxAlertRadius") );
			}
		}
		else if ( resultCode == RESULT_CANCELED )
		{
			
		}
	}
	
	// when add activity gets finished
	private void activityAddReturned(String name, double longitude, double latitude)
	{
		Location newLocation = new Location("");
		newLocation.setLongitude(longitude);
		newLocation.setLatitude(latitude);
		
		// if ArrayList is null, create it
		if ( data.m_myLocations == null )
		{
			data.m_myLocations = new ArrayList<MyLocation>();
		}
		
		MyLocation tmp = new MyLocation(name, newLocation);
		data.m_myLocations.add(tmp);
		m_hmpUIDtoLoc.put(tmp.getId(), data.m_myLocations.size()-1);
		m_XmlHelper.saveDataToXml(data);
		updateListView();
			
		// notify LocationManager --> new location added
		registerLocation(tmp);
	}
	
	// when detail activity gets finished
	private void activityDetailReturned(int id, String oldName, String newName, int showMessage, String message, int mute)
	{
		// if ArrayList is null, create it
		if ( data.m_myLocations == null )
		{
			data.m_myLocations = new ArrayList<MyLocation>();
		}
		
		// change items name
		data.m_myLocations.get(id).setName(newName);
		data.m_myLocations.get(id).setShowMessage(showMessage);
		data.m_myLocations.get(id).setMessage(message);
		data.m_myLocations.get(id).setMute(mute);
		m_XmlHelper.saveDataToXml(data);
		updateListView();
	}
	
	// when preferences activity gets finished
	private void activityPreferencesReturned(int minTimeBetweenUpdate, int minDistancechangeForUpdate, int proxAlertRadius)
	{
		if ( data == null )
		{
			data = new XmlDataContainer();
		}
		
		// change preferences
		data.minTimeBetweenUpdate = minTimeBetweenUpdate;
		data.minDistancechangeForUpdate = minDistancechangeForUpdate;
		data.proxAlertRadius = proxAlertRadius;
		m_XmlHelper.saveDataToXml(data);
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
			// TODO: normally do nothing
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) 
		{
			// TODO: output message
			
		}

		public void onProviderEnabled(String provider) 
		{
			// TODO: output message
			
		}

		public void onProviderDisabled(String provider) 
		{
			// TODO: output message
			
		}
		
	}
}
