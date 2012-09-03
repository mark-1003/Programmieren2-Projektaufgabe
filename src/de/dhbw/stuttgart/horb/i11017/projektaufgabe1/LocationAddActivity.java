package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import de.dhbw.stuttgart.horb.i11017.projektaufgabe1.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LocationAddActivity extends Activity 
{
	//private TextView textName;
	private EditText editName;
	//private TextView textLocation;
	private TextView textLongitude;
	private TextView textLatitude;
	private Button buttonSave;
	private Button buttonCancel;
	
	String name = "";
	double longitude = 0.0;
	double latitude = 0.0;
	
	private int returnValue = RESULT_CANCELED;
	
	/** Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		
		//textName = (TextView) findViewById(R.id.textName);
		editName = (EditText) findViewById(R.id.editName);
		//textLocation = (TextView) findViewById(R.id.textLocation);
		textLongitude = (TextView) findViewById(R.id.textLongitude);
		textLatitude = (TextView) findViewById(R.id.textLatitude);
		
		
		
		buttonSave = (Button) findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				// Save
				returnValue = RESULT_OK;
				finish();
			}
		});
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				// Cancel
				returnValue = RESULT_CANCELED;
				finish();
			}
		});
		
		// read data from intent
		Bundle extras = getIntent().getExtras();
		if (extras == null) 
		{
			return;
		}
		name = extras.getString("name");
		longitude = extras.getDouble("longitude");
		latitude = extras.getDouble("latitude");	 	
				
	 	// show data in textfields
	 	if (name != null && latitude != 0.0 && longitude != 0.0)
	 	{
	 	 	editName.setText(name);
	 	 	textLongitude.setText(getString(R.string.longitude) +" " + String.valueOf(longitude));
	 	 	textLatitude.setText(getString(R.string.latitude) +" " + String.valueOf(latitude));
	 	 }
	}
	
	@Override
	public void finish()
	{
		// Prepare data intent 
		Intent data = new Intent();
		data.putExtra("name", editName.getText().toString());
		data.putExtra("longitude", longitude);
		data.putExtra("latitude", latitude);
		// Activity finished, return the data
		setResult(returnValue, data);
		super.finish();
	}
}
