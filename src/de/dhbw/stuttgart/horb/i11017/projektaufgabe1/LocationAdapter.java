package de.dhbw.stuttgart.horb.i11017.projektaufgabe1;

import java.util.ArrayList;

import de.dhbw.stuttgart.horb.i11017.projektaufgabe1.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// Adapter class for listView
public class LocationAdapter extends ArrayAdapter<MyLocation>
{
	private Activity activity; 
    private ArrayList<MyLocation> data = null;

	public LocationAdapter(Activity activity, ArrayList<MyLocation> data) 
	{
		super(activity, R.layout.list_item, data);
		this.activity = activity;
        this.data = data;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) 
	{
        View rowView = convertView;
        LocationView locationView = null;
 
        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null);
            
            // Hold the view objects in an object,
            // so they don't need to be re-fetched
            locationView = new LocationView();
            locationView.name = (TextView) rowView.findViewById(R.id.itemName);
            locationView.detail = (TextView) rowView.findViewById(R.id.itemDetail);
            
            // Cache the view objects in the tag,
            // so they can be re-accessed later
            rowView.setTag(locationView);
        }
        else
        {
        	locationView = (LocationView) rowView.getTag();
        }
 
        // Transfer the stock data from the data object
        // to the view objects
        MyLocation currentLocation = data.get(position);
        locationView.name.setText(currentLocation.getName());
        locationView.detail.setText( "Längengrad: " + String.valueOf(currentLocation.getLocation().getLongitude()) + "\n" +
        								"Breitengrad: " + String.valueOf(currentLocation.getLocation().getLatitude()) );
        
        return rowView;
    }
 
    protected static class LocationView
    {
        protected TextView name;
        protected TextView detail;
    }
}