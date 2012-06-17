package dhbw.i2011.programmieren.projektaufgabe1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LocationDetailActivity extends Activity
{	
	private TextView textName;
	private EditText editName;
	private TextView textLocation;
	private TextView textLocationValue;
	
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
		textLocationValue = (TextView) findViewById(R.id.textLocationValue);

	    Intent intent = getIntent();

	    // read data from intent
	 	Bundle extras = getIntent().getExtras();
	 	if (extras == null) 
	 	{
	 		return;
	 	}
	 	String value1 = extras.getString("Value1");
	 	String value2 = extras.getString("Value2");
	 				
	 	// Daten in den Textfeldern anzeigen
	 	if (value1 != null && value2 != null) 
	 	{
	 		editName.setText(value1);
	 		textLocationValue.setText(value2);
	 	}
	}
	

}
