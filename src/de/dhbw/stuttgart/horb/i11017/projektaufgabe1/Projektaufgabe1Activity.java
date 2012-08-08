package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import de.dhbw.stuttgart.horb.i11017.projektaufgabe1.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
	// Request codes for intends
	private static final int REQUEST_ADD = 10;
	private static final int REQUEST_DETAIL = 11;
	
	
	// Controls
	private Button m_addButton;
	private ListView m_locationList;
	
	// LocationManager
	private LocationManager m_locationManager = null;
	private MyLocationListener m_locationListener = null;
	
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
        
        
        // create HashMap for Name to Id search
        m_hmpLocationToId = new HashMap<String, Integer>();
        for (int i = 0; i < m_myLocations.size(); i++)
    	{
    		// map locationName and index to hashMap
    		m_hmpLocationToId.put(m_myLocations.get(i).getName(), i);
    	}
        
        
        // start locationListener
        // TODO: register all locations loaded from XML
     	m_locationListener = new MyLocationListener();
     	m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     	// time/distance to get new location
     	// 60000 ms --> 1min
     	// 100 meter
     	m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, m_locationListener);
     	
     	for(MyLocation tmp : m_myLocations)
     	{
     		Intent intent = new Intent("");
     		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

     		/*m_locationManager.addProximityAlert(tmp.getLocation().getLatitude(), 
     				tmp.getLocation().getLongitude(), 100, -1, intent);*/
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
			String name = "";
			Location newLocation = new Location("");
			// read name, longitude and latitude from Intent
			if ( data.hasExtra("name") && data.hasExtra("longitude") && data.hasExtra("latitude") )
			{
				name = data.getExtras().getString("name");
				newLocation.setLongitude( Double.valueOf(data.getExtras().getString("longitude")) );
				newLocation.setLatitude( Double.valueOf( data.getExtras().getString("latitude")) );
				
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
					m_myLocations.add(new MyLocation(name, newLocation));
					m_hmpLocationToId.put(name, m_myLocations.size()-1);
					m_locationXmlHelper.saveDataToXml(m_myLocations);
					updateListView();
					
					// TODO notify LocationManager --> new location added
				}
			}
		}
		else if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_DETAIL) )		
		{
			Integer id;
			String oldName = "";
			String newName = "";
			// read name, longitude and latitude from Intent
			if ( data.hasExtra("id") && data.hasExtra("oldName") && data.hasExtra("newName") )
			{
				id = data.getExtras().getInt("id");
				oldName = data.getExtras().getString("oldName");
				newName = data.getExtras().getString("newName");
				
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
					
					// TODO notify LocationManager --> items name has changed
				}
			}
		}
		else if ( resultCode == RESULT_CANCELED )
		{
			
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
