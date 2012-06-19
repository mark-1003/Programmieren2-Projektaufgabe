package dhbw.i2011.programmieren.projektaufgabe1;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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
	
	private LocationManager locationManager = null;
	private MyLocationListener locationListener = null;
	
	private Context context;
	
	private ArrayList<MyLocation> myLocations;
	private HashMap<String, Integer> hmpLocationToId;
	
	/**
	 * Called when the activity is first created.
	 * @param <T>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        context = this;
        
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
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
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
					Toast.makeText(context, getString(R.string.noGpsSignal), Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        // Initialize locationList
        m_locationList = (ListView) findViewById(R.id.locationList);
        m_locationList.setOnItemClickListener(new OnItemClickListener()
        {
        	/**
        	 * locationList onItemClick event
        	 */
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
        	{
        		Object o = m_locationList.getItemAtPosition(position);
        	    String selectedName = o.toString();
        	    
				Intent intent = new Intent(Projektaufgabe1Activity.this, LocationDetailActivity.class);
        		
				int id = hmpLocationToId.get(selectedName);
				MyLocation tmpLocation = myLocations.get(id);
        		// set parameters
				intent.putExtra("name", selectedName);
				intent.putExtra("longitude", tmpLocation.getLocation().getLongitude());
				intent.putExtra("latitude", tmpLocation.getLocation().getLatitude());
        		
				// start activity
				startActivityForResult(intent, REQUEST_DETAIL);
        	}
        });
        
        updateListFromXml();
        
        // start locationListener
        // TODO: register all locations loaded from XML
     	locationListener = new MyLocationListener();
     	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     	// time/distance to get new location
     	// 60000 ms --> 1min
     	// 100 meter
     	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener);
	}
	
	private void updateListFromXml()
	{
		// get data from XML and write to list
        myLocations = getDataFromXml();
        hmpLocationToId = new HashMap<String, Integer>();
        if ( myLocations != null )
        {
        	// create StringList for locationList
        	List<String> valueList = new ArrayList<String>();
        	for (int i = 0; i < myLocations.size(); i++)
        	{
        		// map locationName and index to hashMap
        		hmpLocationToId.put(myLocations.get(i).getName(), i);
        		// add location to strtingList for ListView
        		valueList.add(myLocations.get(i).getName());
        	}
        	ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, valueList);
        	// set adapter to list
        	m_locationList.setAdapter(adapter);
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
			Location newLoc = new Location("");
			// read name, longitude and latitude
			// create new MyLocation object and add to array
			if ( data.hasExtra("name")
					&& data.hasExtra("longitude")
					&& data.hasExtra("latitude") )
			{
				name = data.getExtras().getString("name");
				newLoc.setLongitude( Double.valueOf(data.getExtras().getString("longitude")) );
				newLoc.setLatitude( Double.valueOf( data.getExtras().getString("latitude")) );
				
				if ( myLocations != null )
				{
					myLocations.add(new MyLocation(name, newLoc));
				}
				else
				{
					myLocations = new ArrayList<MyLocation>();
					myLocations.add(new MyLocation(name, newLoc));
				}
				
				saveDataToXml(myLocations);
				updateListFromXml();
			}			
			
		}
		else if ( (resultCode == RESULT_OK) && (requestCode == REQUEST_DETAIL) )		
		{
			
		}
	}
	
	/**
	 * 
	 */
	private ArrayList<MyLocation> getDataFromXml()
	{
		XmlHandler xml;
		xml = new XmlHandler(getApplicationContext());
		
		/*Location tmpLoc = new Location("");
		tmpLoc.setLongitude(9.40985256806016);
		tmpLoc.setLatitude(48.40653599705547);
		
		ArrayList<MyLocation> inLocations;
		inLocations = new ArrayList<MyLocation>();
		inLocations.add(new MyLocation(0, "Arbeit", tmpLoc));
		inLocations.add(new MyLocation(1, "Zuhause", tmpLoc));
		
		xml.saveLocations(inLocations);*/
		
		//ArrayList<MyLocation> outLocations;
		//outLocations = new ArrayList<MyLocation>();
		
		// test output
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
	
	public void saveDataToXml(ArrayList<MyLocation> myLocations)
	{
		XmlHandler xml;
		xml = new XmlHandler(getApplicationContext());
		
		xml.saveLocations(myLocations);
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
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
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
