package dhbw.i2011.programmieren.projektaufgabe1;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LocationDetailActivity extends Activity
{	
	private TextView textName;
	private EditText editName;
	private TextView textLocation;
	private TextView textLongitude;
	private TextView textLatitude;	
	private Button buttonSave;
	private Button buttonCancel;
	
	private ArrayList<MyLocation> myLocations;
	private HashMap<String, Integer> hmpLocationToId;
	
	/** Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		textName = (TextView) findViewById(R.id.textName);
		editName = (EditText) findViewById(R.id.editName);
		textLocation = (TextView) findViewById(R.id.textLocation);
		textLongitude = (TextView) findViewById(R.id.textLongitude);
		textLatitude = (TextView) findViewById(R.id.textLatitude);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				// TODO return modified location
				
			}
		});
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

	    Intent intent = getIntent();

	    // read data from intent
	 	Bundle extras = getIntent().getExtras();
	 	if (extras == null) 
	 	{
	 		return;
	 	}
	 	String name = extras.getString("name");
	 	double longitude = extras.getDouble("longitude");
	 	double latitude = extras.getDouble("latitude");
	 	//myLocations = (ArrayList<MyLocation>) extras.get("myLocations");	
	 	//hmpLocationToId = (HashMap<String, Integer>) extras.get("locationToId");
		
	 	
	 	// Daten in den Textfeldern anzeigen
		if (name != null && longitude != 0.0/*myLocations != null*/ && latitude != 0.0 /*hmpLocationToId != null*/)
	 	{
	 		editName.setText(name);
	 		textLongitude.setText( String.valueOf(longitude) );
 			textLatitude.setText( String.valueOf(latitude) );
	 		// get id from HashMap and get Longitude and Latitude from myLocations Array
	 		/*if ( hmpLocationToId.containsKey(name) )
	 		{
	 			Integer id = hmpLocationToId.get(name);
	 			//textLongitude.setText( String.valueOf(myLocations.get(id).getLocation().getLongitude()) );
	 			//textLatitude.setText( String.valueOf(myLocations.get(id).getLocation().getLatitude()) );
	 			
	 		}*/
	 	}
	}
	
}
