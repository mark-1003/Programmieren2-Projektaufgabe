package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import de.dhbw.stuttgart.horb.i11017.projektaufgabe1.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;


public class LocationDetailActivity extends Activity
{	
	private TextView textName;
	private EditText editName;
	private TextView textLocation;
	private TextView textLongitude;
	private TextView textLatitude;	
	private TextView textPreferences;
	private CheckBox cbShowMessage;
	private EditText editMessage;
	private CheckBox cbMute;
	private Button buttonSave;
	private Button buttonCancel;
	
	private int locationId;
	private String oldLocationName = "";
	private double longitude;
	private double latitude;
	private int showMessage;
	private String message = "";
	private int mute;
	
	private int returnValue = RESULT_CANCELED;
	
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
		textPreferences = (TextView) findViewById(R.id.textPreferences);
		cbShowMessage = (CheckBox) findViewById(R.id.cbShowMessage);
		cbShowMessage.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if ( cbShowMessage.isChecked() )
				{
					showMessage = 1;
					editMessage.setEnabled(true);
				}
				else
				{
					showMessage = 0;
					editMessage.setEnabled(false);
				}
			}			
		});
		editMessage = (EditText) findViewById(R.id.editMessage);
		cbMute = (CheckBox) findViewById(R.id.cbMute);
		cbMute.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if ( cbMute.isChecked() )
				{
					mute = 1;
				}
				else
				{
					mute = 0;
				}
			}			
		});
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
	 	locationId = extras.getInt("id");
	 	oldLocationName = extras.getString("name");
	 	longitude = extras.getDouble("longitude");
	 	latitude = extras.getDouble("latitude");
	 	int showMessage = extras.getInt("showMessage");
	 	String message = extras.getString("message");
	 	int mute = extras.getInt("mute");
		
	 	
	 	// Daten in den Textfeldern anzeigen
		if (oldLocationName != null && longitude != 0.0 && latitude != 0.0
				&& message != null )
	 	{
	 		editName.setText(oldLocationName);
	 		textLongitude.setText( getString(R.string.longitude) +" " + String.valueOf(longitude) );
 			textLatitude.setText( getString(R.string.latitude) + " " + String.valueOf(latitude) );
 			if ( showMessage == 1 )
 			{
 				cbShowMessage.setChecked(true);
 				editMessage.setEnabled(true);
 			}
 			else
 			{
 				cbShowMessage.setChecked(false);
 				editMessage.setEnabled(false);
 			}
 			editMessage.setText(message);
 			if ( mute == 1 )
 			{
 				cbMute.setChecked(true);
 			}
 			else
 			{
 				cbMute.setChecked(false);
 			}
	 	}
	}
	
	@Override
	public void finish()
	{
		// Prepare data intent 
		Intent data = new Intent();
		data.putExtra("id", locationId);
		data.putExtra("oldName", oldLocationName);
		data.putExtra("newName", editName.getText().toString());
		data.putExtra("showMessage", showMessage);
		data.putExtra("message", editMessage.getText().toString());
		data.putExtra("mute", mute);
		// Activity finished, return the data
		setResult(returnValue, data);
		super.finish();
	}	
}
