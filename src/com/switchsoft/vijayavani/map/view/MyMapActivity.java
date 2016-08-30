/*
 * Copyright (C) 2011 Ievgenii Nazaruk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.switchsoft.vijayavani.map.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.switchsoft.vijayavani.R;
import com.switchsoft.vijayavani.map.util.MyItemizedOverlay;
import com.switchsoft.vijayavani.sql.SQLHelper;
import com.switchsoft.vijayavani.util.Constants;

public class MyMapActivity extends MapActivity {

	private MapView mDisplay_Map;

	Bitmap converted_image;;

	@SuppressWarnings("unused")
	private MapController mapController;	
	private List<Overlay> mapOverlaysnear;	
	private MyItemizedOverlay myItemizedOverlaynear;

	HashMap<String, Object> hm;	
	ArrayList<HashMap<String, Object>> location_details;
	ArrayList<HashMap<String, Object>> stores_near_me;

	SQLHelper sqlhelper;
	Constants constant;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		//removes the top status bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
		.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.locatestore);

		mDisplay_Map = (MapView)findViewById(R.id.display_map);

		sqlhelper = new SQLHelper(this);
		constant = new Constants();

		stores_near_me = new ArrayList<HashMap<String,Object>>();
		mDisplay_Map.setBuiltInZoomControls(false);

		getLocationData();
		displayShops();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void displayShops(){

		if(location_details.size()  != 0 ){
			GeoPoint geoPoint = null;

			mapOverlaysnear = mDisplay_Map.getOverlays();
			Drawable mapPin = this.getResources().getDrawable(R.drawable.store_pin);
			myItemizedOverlaynear = new MyItemizedOverlay(mapPin, mDisplay_Map);

			for (int i = 0; i < location_details.size(); i++) {

				double latitude = Double.valueOf((String)location_details.get(i).get("latitude")) * 1000000;
				double longitude = Double.valueOf((String)location_details.get(i).get("longitude")) * 1000000;
				String storeLocation =  (String)location_details.get(i).get("location");
				String storeAddress =  (String)location_details.get(i).get("phoneno");

				geoPoint = new GeoPoint((int) (latitude), (int) (longitude));

				OverlayItem overlayitem = new OverlayItem(geoPoint, storeLocation, storeAddress);
				myItemizedOverlaynear.addOverlay(overlayitem);
			}

			mapOverlaysnear.add(myItemizedOverlaynear);

			MapController mapcontrol = mDisplay_Map.getController();

			mapcontrol.animateTo(geoPoint);

			mapcontrol.setZoom(16);



		} else {

			Toast.makeText(getApplicationContext(),
					"Data Unavailable !!", Toast.LENGTH_LONG).show();


		}

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();	

		if(sqlhelper != null){
			sqlhelper.close();
		}
	}

	/**When back key is pressed this method is called*/
	@Override
	public void onBackPressed() {		
		finish();
	}

	private void getLocationData(){

		location_details = new ArrayList<HashMap<String, Object>>();

		Cursor cursor = getEvents("STORELOCATOR");

		//while loop for fetching all the data
		while (cursor.moveToNext()) {

			hm = new HashMap<String, Object>();	

			hm.put("latitude",cursor.getString(cursor.getColumnIndex("Latitude")));
			hm.put("longitude",cursor.getString(cursor.getColumnIndex("Longitude")));
			hm.put("location",cursor.getString(cursor.getColumnIndex("Store_Name")));
			hm.put("phoneno",cursor.getString(cursor.getColumnIndex("Street_Address")));

			location_details.add(hm);

		}

	}

	/**This method will get the details based on table
	 * With the sequence as the criteria 
	 * Returns the data in ascending order of sequence*/
	private Cursor getEvents(String table) {
		SQLiteDatabase db = (sqlhelper).getReadableDatabase();
		Cursor cursor = db.query(table, null, null, null, null, null, null);

		stopManagingCursor(cursor);
		return cursor;
	}
}
