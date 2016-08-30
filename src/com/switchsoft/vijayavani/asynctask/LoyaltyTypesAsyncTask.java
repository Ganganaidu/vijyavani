package com.switchsoft.vijayavani.asynctask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.switchsoft.vijayavani.SplashScreenActivity;
import com.switchsoft.vijayavani.model.Loyalty_Types;
import com.switchsoft.vijayavani.util.Constants;

public class LoyaltyTypesAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

	ArrayList<String> dumyarray;
	Loyalty_Types loyaltytype;
	
	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {
		
		loyaltytype = new Loyalty_Types();
		
		//get loyalty data
		getLoyaltyTypes();
		
		return dumyarray;
	}

	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("99");
		SplashScreenActivity.splashscreen.onLoyaltyTypesComplete();
	}
	
	
	/** Downloading items from server 
	 * For loyalty type details*/
	private void getLoyaltyTypes(){

		JSONArray jArray_main;
		JSONObject jsonObject;

		try{
			URL maincatUrl = new URL(Constants.getLoyaltyTypeDetails);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {
				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_NEWICatelog_Loyality_PointsDetbyUIdResult");			
				JSONObject item = new JSONObject();

				int level_length = jArray_main.length();
				if(level_length != 0){

					SplashScreenActivity.sqlhelper.deleteLoyaltyData();

					for (int i = 0; i < level_length; i++) { //check

						item  = jArray_main.getJSONObject(i);

						loyaltytype.loyalty_id = item.getInt("Loyality_Id");						
						loyaltytype.loyalty_type = item.getString("Loyality_Type");
						loyaltytype.store_id = item.getInt("Store_Id");
						loyaltytype.user_id = item.getInt("User_Id");

						SplashScreenActivity.sqlhelper.insertLoyalty_Types(loyaltytype);
					}
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
	
	
	
}
