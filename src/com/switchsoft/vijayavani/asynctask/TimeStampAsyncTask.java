package com.switchsoft.vijayavani.asynctask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.AsyncTask;

import com.switchsoft.vijayavani.SplashScreenActivity;
import com.switchsoft.vijayavani.util.Constants;

public class TimeStampAsyncTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {


	ArrayList<HashMap<String, Object>> timestamp;
	private boolean inTimestamp_exception = false;

	String saved_timestamp_date = "";

	@Override
	protected ArrayList<HashMap<String, Object>> doInBackground(Void... arg0) {
		//Initializing the hash map array list 
		timestamp = new ArrayList<HashMap<String, Object>>();

		inTimestamp_exception = false;
		//Gets all the time stamps
		getTimeStamps();

		//Checking weather to get data or not
		if(inTimestamp_exception == false){

			for(int i = 0;i <= timestamp.size()-1;i++){

				String category = (String) timestamp.get(i).get("category_name");
				String date = (String) timestamp.get(i).get("update_date");

				if(category.equals(Constants.timestamp_Offers)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						SplashScreenActivity.download_offers = true;
					} 

				} else if(category.equals(Constants.timestamp_MainCategory)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						SplashScreenActivity.download_main_category = true;
					} 

				} else if(category.equals(Constants.timestamp_SubCategory)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						SplashScreenActivity.download_sub_category = true;
					} 

				} else if(category.equals(Constants.timestamp_Items)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						SplashScreenActivity.download_items = true;
					} 

				} else if(category.equals(Constants.timestamp_storelocator)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						SplashScreenActivity.download_storelocator = true;
					} 

				} 			

			}
		}

		return timestamp;
	}

	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<HashMap<String, Object>> results) {
		SplashScreenActivity.splashscreen.update_percentage("10");
		if(inTimestamp_exception == false){
			save_timestamp_data();
			SplashScreenActivity.splashscreen.onTimeStampComplete();

		} else {
			SplashScreenActivity.splashscreen.openMainActivity();				
		}

	}


	private void getTimeStamps(){

		timestamp = new ArrayList<HashMap<String, Object>>();	
		HashMap<String, Object> hm;

		JSONArray jArray_main;
		JSONObject jsonObject;


		try
		{
			URL maincatUrl = new URL(Constants.timestamp_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Update_ICatelog_UpdateResult");			

				JSONObject main_category = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					hm = new HashMap<String, Object>();	

					main_category  = jArray_main.getJSONObject(i);

					String image_link = main_category.getString("CategoryName");
					hm.put("category_name", image_link);

					String cat_name = main_category.getString("UpdateDate");
					hm.put("update_date", cat_name);

					timestamp.add(hm);

				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			inTimestamp_exception = true;
		}

	}


	private void get_timestamp_from_database(String category){
		//Query represents the position from where the data has to be fetched
		String 	qurey = "select * from "+"TIMESTAMP"+" where "+
				"TIMESTAMP"+"."+"Category_Name"+" = \"" +  category + "\"";

		Cursor cursor = SplashScreenActivity.splashscreen.getRawEvents(qurey);

		try {
			if(cursor != null){
				cursor.moveToFirst();

				saved_timestamp_date = cursor.getString(cursor.getColumnIndex("Updated_Date"));		

				cursor.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void save_timestamp_data(){

		try {
			for(int i = 0; i < timestamp.size(); ++i){

				String category = (String)timestamp.get(i).get("category_name");
				String date = (String)timestamp.get(i).get("update_date");

				SplashScreenActivity.sqlhelper.insertTimestampData(category, date);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
