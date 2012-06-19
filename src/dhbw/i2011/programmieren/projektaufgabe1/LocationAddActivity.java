package dhbw.i2011.programmieren.projektaufgabe1;

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
	private TextView textName;
	private EditText editName;
	private TextView textLocation;
	private TextView textLongitude;
	private TextView textLatitude;
	private Button buttonSave;
	private Button buttonCancel;
	
	/** Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);
		
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
				// TODO return new location
				finish();
			}
		});
		buttonCancel = (Button) findViewById(R.id.buttonCancel);
		buttonCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//finish();
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
				
	 	// Daten in den Textfeldern anzeigen
	 	if (name != null && latitude != 0.0 && longitude != 0.0) 
	 	{
	 	 	editName.setText(name);
	 	 	textLongitude.setText(String.valueOf(longitude));
	 	 	textLatitude.setText(String.valueOf(latitude));
	 	 }
	}
	
	@Override
	public void finish()
	{
		// Prepare data intent 
		Intent data = new Intent();
		data.putExtra("name", editName.getText().toString());
		data.putExtra("longitude", textLongitude.getText().toString());
		data.putExtra("latitude", textLatitude.getText().toString());
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		super.finish();
	}
	
}
