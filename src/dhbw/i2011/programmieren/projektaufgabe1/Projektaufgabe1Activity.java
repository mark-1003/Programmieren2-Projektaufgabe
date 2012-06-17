package dhbw.i2011.programmieren.projektaufgabe1;




import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import android.util.Log;



public class Projektaufgabe1Activity<T> extends Activity
{
	private Button m_addButton;
	private ListView m_locationList;
	
	private LocationManager locationManager = null;
	private MyLocationListener locationListener = null;
	
	
	/** Called when the activity is first created. 
	 * @param <T>*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        // Initialize controls
        m_addButton = (Button) findViewById(R.id.addLocation);
        m_locationList = (ListView) findViewById(R.id.locationList);
        
        
        // create StringList for testing
        List valueList = new ArrayList<String>();        
        for (int i = 0; i < 10; i++)
        {
        	valueList.add("value"+i);
        }
        
        ListAdapter adapter = new ArrayAdapter<T>(getApplicationContext(), android.R.layout.simple_list_item_1, valueList);

        m_locationList.setAdapter(adapter);
        m_locationList.setOnItemClickListener(new OnItemClickListener()
        {
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		Intent intent = new Intent(Projektaufgabe1Activity.this,LocationDetailActivity.class);
        		intent.putExtra("selected", m_locationList.getAdapter().getItem(arg2).toString());
        		startActivity(intent);
        	}
        }
        );
        
		
		
        // start locationListener
		locationListener = new MyLocationListener();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// time/distance to get new location
		// 60000 ms --> 1min
		// 100 meter
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener);
	}
	
	public void onClick(View view) {
		// create Intent
		Intent intent = new Intent(Projektaufgabe1Activity.this, LocationAddActivity.class);

		// set parameters
		intent.putExtra("locationName", "Arbeit");
		intent.putExtra("locationValue", "1337-new");
		
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) 
		{
			// read value1
			if (data.hasExtra("returnKey1")) 
			{
				String key1 = data.getExtras().getString("returnKey1");
				Toast.makeText(this, key1, Toast.LENGTH_SHORT).show();
			}
			
			// read value2
			if (data.hasExtra("returnKey2")) 
			{
				String key2 = data.getExtras().getString("returnKey2");
				Toast.makeText(this, key2, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	/**
	 * Constant for Request-Code
	 */
	private static final int REQUEST_CODE = 10;
	
}
