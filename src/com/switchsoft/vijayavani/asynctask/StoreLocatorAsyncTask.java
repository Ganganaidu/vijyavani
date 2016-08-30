package com.switchsoft.vijayavani.asynctask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.switchsoft.vijayavani.SplashScreenActivity;
import com.switchsoft.vijayavani.model.StoreLocatorModel;
import com.switchsoft.vijayavani.util.Constants;

public class StoreLocatorAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
	
	ArrayList<String> dumyarray;
	StoreLocatorModel locationmodel;
	
	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {
		
		locationmodel = new StoreLocatorModel();
		//gets the store data
		getStoreLocator();
 		return dumyarray;
	}
	
	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("90");
		SplashScreenActivity.splashscreen.onStoreLocatorComplete();
	}
	
	
	/**Downloading the background data from the server*/
	private void getStoreLocator(){

		JSONArray jArray_main;

		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.storelocator_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				SplashScreenActivity.sqlhelper.deleteStoreLocatorData();		

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_ICatelog_StoreLocator_CategoryResult");			

				JSONObject store = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{

					store  = jArray_main.getJSONObject(i);

					locationmodel.email_id = store.getString("Email_Id");
					String latitude_longitude = store.getString("Lang_Lat");

					StringTokenizer tokens = new StringTokenizer(latitude_longitude, ",");
					String latitude = tokens.nextToken();// this will contain latitude
					String longitude = tokens.nextToken();// this will contain longitude

					locationmodel.latitude = latitude;
					locationmodel.longitude = longitude;
					locationmodel.location = store.getString("Location");
					locationmodel.phone_no = store.getString("Phono_No");
					locationmodel.street_address = store.getString("Store_Address");
					locationmodel.store_id = store.getInt("Store_Id");
					locationmodel.userid = store.getInt("User_Id");
					locationmodel.store_name = store.getString("Store_Name");

					SplashScreenActivity.sqlhelper.insertStorelocatorData(locationmodel);

				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
