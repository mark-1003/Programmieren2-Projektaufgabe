package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PreferencesActivity extends Activity
{
	private int returnValue = RESULT_CANCELED;

	//private TextView text1;
	private EditText edit1;
	//private TextView text2;
	private EditText edit2;
	//private TextView text3;
	private EditText edit3;

	private Button buttonSave;
	private Button buttonCancel;

	/** Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		//text1 = (TextView) findViewById(R.id.textView1);
		edit1 = (EditText) findViewById(R.id.editText1);
		//text2 = (TextView) findViewById(R.id.textView2);
		edit2 = (EditText) findViewById(R.id.editText2);
		//text3 = (TextView) findViewById(R.id.textView3);
		edit3 = (EditText) findViewById(R.id.editText3);

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
		int minTimeBetweenUpdate = extras.getInt("minTimeBetweenUpdate");
		int minDistancechangeForUpdate = extras.getInt("minDistancechangeForUpdate");
		int proxAlertRadius = extras.getInt("proxAlertRadius");

		// Daten in den Textfeldern anzeigen
		if (proxAlertRadius != 0 && minDistancechangeForUpdate != 0 && proxAlertRadius != 0 )
		{
			edit1.setText( String.valueOf(minTimeBetweenUpdate) );
			edit2.setText( String.valueOf(minDistancechangeForUpdate) );
			edit3.setText( String.valueOf(proxAlertRadius) );
		}
	}

	@Override
	public void finish()
	{
		// Prepare data intent 
		Intent data = new Intent();
		data.putExtra( "minTimeBetweenUpdate", Integer.valueOf(edit1.getText().toString()) );
		data.putExtra( "minDistancechangeForUpdate", Integer.valueOf(edit2.getText().toString()) );
		data.putExtra( "proxAlertRadius", Integer.valueOf(edit3.getText().toString()) );
		// Activity finished, return the data
		setResult(returnValue, data);
		super.finish();
	}
}
