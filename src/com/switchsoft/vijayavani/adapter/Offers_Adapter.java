package com.switchsoft.vijayavani.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.switchsoft.vijayavani.R;

public class Offers_Adapter extends BaseAdapter{

	//Inflating the view
	private LayoutInflater inflate;

	Activity mActivity;
	
	//Array list of all data 
	private ArrayList<HashMap<String, Object>> details;
	

	/** This will display the Patients Main List on the Main Screen*/
	public Offers_Adapter(Activity activity,ArrayList<HashMap<String, Object>>  data) {
		// Cache the LayoutInflate to avoid asking for a new one each time.
		inflate = LayoutInflater.from(activity);

		mActivity = activity;
		details = data;
		
	}

	//returns the size of the array list
	public int getCount() {
		return details.size();
	}

	public Object getItem(int arg0) {
		return details.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unnecessary calls
		// to findViewById() on each row.
		ViewHolder holder = null;
		
		// When convertView is not null, we can reuse it directly, there is no need
		// to re inflate it. We only inflate a new View when the convertView supplied
		// by ListView is null.
		if(convertView == null){
			convertView = inflate.inflate(R.layout.row_offers, null); 

			holder = new ViewHolder();
			holder.mPhoto_Imageview = (ImageView)convertView.findViewById(R.id.row_event_imageview);
			holder.mName_Textview = (TextView)convertView.findViewById(R.id.row_name_textview);
			holder.maDate_textview = (TextView)convertView.findViewById(R.id.row_date_textview);
			holder.mDescription_Textview = (TextView)convertView.findViewById(R.id.row_description_textview);
			

			convertView.setTag(holder);	
		}
		else{
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder=(ViewHolder)convertView.getTag();
		}
		

		//Displaying the data into the view
		holder.mName_Textview.setText((String) details.get(position).get("Heading"));
		holder.maDate_textview.setText("Time : "+(String) details.get(position).get("Time"));
		holder.mDescription_Textview.setText((String) details.get(position).get("Description"));
		
		//Displaying the image
		holder.mPhoto_Imageview.setImageBitmap((Bitmap)details.get(position).get("Converted_Image"));
		
		return convertView;	
	}

	//Holds the views
	private class ViewHolder{

		TextView mName_Textview, maDate_textview,	
		mDescription_Textview;

		ImageView mPhoto_Imageview;

	}

	
}

